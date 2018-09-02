package com.ggslk.ggslk.activity

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.preference.PreferenceManager
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import com.android.volley.toolbox.Volley
import com.ggslk.ggslk.R
import com.ggslk.ggslk.common.SaveHandler
import com.ggslk.ggslk.common.Session
import com.ggslk.ggslk.fragment.AboutUsFragment
import com.ggslk.ggslk.fragment.FavoritesFragment
import com.ggslk.ggslk.fragment.HomeFragment
import com.ggslk.ggslk.model.Article
import com.ggslk.ggslk.model.Category
import com.ggslk.ggslk.model.Report
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.FirebaseDatabase
import com.onesignal.OneSignal
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.nav_header_main.*
import org.jetbrains.anko.*
import org.jetbrains.anko.design.textInputLayout

private const val TAG = "GGSLKGoogleSignIn"
private const val REQUEST_CODE_SIGN_IN = 1234
private const val WEB_CLIENT_ID = "714868264744-j6jnk2rombdbs7ukd1jfaqqi3g8gce6t.apps.googleusercontent.com"

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.OnConnectionFailedListener {

    private var mGoogleApiClient: GoogleApiClient? = null
    private var mAuth: FirebaseAuth? = null

    override fun onBackPressed() {
        val drawer = findViewById<View>(R.id.drawerLayout) as DrawerLayout
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            finish()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.app_bar_main_menu, menu)
        return true
    }

    /**
     * Handle action bar item clicks here.
     * The action bar will automatically handle clicks on the Home/Up button,
     * so long as you specify a parent activity in AndroidManifest.xml.
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_report -> {
                showReportAlert()
                true
            }
            else -> {
                onOptionsItemSelected(item)
            }
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val transaction = supportFragmentManager.beginTransaction()
        val fragment: Fragment
        when (item.itemId) {
            R.id.nav_home -> {
                fragment = HomeFragment.newInstance()
                transaction.replace(R.id.fragmentContainer, fragment)
            }
            R.id.nav_favorites -> {
                fragment = FavoritesFragment.newInstance()
                transaction.replace(R.id.fragmentContainer, fragment)
            }
//            R.id.nav_profile -> {
//
//            }
//            R.id.nav_settings -> {
//                fragment = SettingsFragment.newInstance()
//                transaction.replace(R.id.fragmentContainer, fragment)
//            }
            R.id.nav_about_us -> {
                fragment = AboutUsFragment.newInstance()
                transaction.replace(R.id.fragmentContainer, fragment)
            }
        }
        transaction.commit()

        val drawer = findViewById<View>(R.id.drawerLayout) as DrawerLayout
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setContentView(R.layout.activity_main)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener(this)

        // Obtain the FirebaseAnalytics instance.
        Session.mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)

        // RequestQueue initialized
        Session.mRequestQueue = Volley.newRequestQueue(this@MainActivity)

        // OneSignal service initialized
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init()

        // Initialize Google sign in
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(WEB_CLIENT_ID)
                .requestEmail()
                .build()

        mGoogleApiClient = GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build()

        mAuth = FirebaseAuth.getInstance()

        // Initiate preferences
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(baseContext)
        Session.englishEnabled = sharedPreferences.getBoolean("settingsEnglish", true)
        Session.sinhalaEnabled = sharedPreferences.getBoolean("settingsSinhala", true)
        Session.tamilEnabled = sharedPreferences.getBoolean("settingsTamil", true)

        loadCachedSessionData()

        // Load HomeFragment
        val transaction = supportFragmentManager.beginTransaction()
        val fragment = HomeFragment.newInstance()
        transaction.replace(R.id.fragmentContainer, fragment)
        transaction.commit()

        signIn()
    }

    override fun onDestroy() {
        super.onDestroy()

        SaveHandler.save(this@MainActivity, "categories", Session.categories)
        SaveHandler.save(this@MainActivity, "articles", Session.articles)
        SaveHandler.save(this@MainActivity, "favorites", Session.favorites)
        SaveHandler.save(this@MainActivity, "liked", Session.liked)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent();
        if (requestCode == REQUEST_CODE_SIGN_IN) {
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            if (result.isSuccess) {
                // successful -> authenticate with Firebase
                val account = result.signInAccount
                firebaseAuthWithGoogle(account!!)
            } else {
                // failed -> update UI
                updateUI(null)
//                Toast.makeText(applicationContext, "SignIn: failed!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {
        Log.e(TAG, "onConnectionFailed():$connectionResult")
        Toast.makeText(applicationContext, "Google Play Services error.", Toast.LENGTH_SHORT).show()
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        var credential: AuthCredential = GoogleAuthProvider.getCredential(acct.idToken, null)
        mAuth!!.signInWithCredential(credential)
                .addOnCompleteListener(this@MainActivity, {
                    if (it.isSuccessful) {
                        // Sign in success
                        Log.e(TAG, "signInWithCredential: Success!")
                        var user: FirebaseUser = mAuth!!.currentUser!!
                        updateUI(user)

                        // Set OneSignal user email for better notifications
                        OneSignal.setEmail(user.email!!)
                    } else {
                        // Sign in fails
                        Log.w(TAG, "signInWithCredential: Failed!", it.exception)
                        Toast.makeText(applicationContext, "Authentication failed!", Toast.LENGTH_SHORT).show()
                        updateUI(null)
                    }
                })
    }

    private fun signIn() {
//        val intent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient)
//        startActivityForResult(intent, REQUEST_CODE_SIGN_IN)

        // Sign in anonymously
        // TODO - Remove Anonymous sign in part to sign in normally
        val currentUser = mAuth!!.currentUser
        if (currentUser != null) {
            Log.e(TAG, "signInWithCredential: Success!")
            var user: FirebaseUser = mAuth!!.currentUser!!
            updateUI(user)

            // Set OneSignal user email for better notifications
            OneSignal.setEmail(user.email!!)
        } else {
            mAuth!!.signInAnonymously()
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val user = mAuth!!.currentUser
                            user?.let {
                                Log.e(TAG, "signInWithCredential: Success!")
                                var user: FirebaseUser = it
                                updateUI(user)

                                // Set OneSignal user email for better notifications
                                OneSignal.setEmail(user.email!!)
                            }
                        } else {
                            Toast.makeText(this@MainActivity,"Authentication failed.",Toast.LENGTH_SHORT).show()
                        }
                    }
        }
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
//            Picasso.get().load(user.photoUrl).fit().centerCrop().into(navHeaderProfileImageView)
//            navHeaderNameTextView.text = user.displayName
//            navHeaderEmailTextView.text = user.email
        }
    }

    private fun loadCachedSessionData() {
        if (SaveHandler.saveExists(this@MainActivity, "categories")) {
            val categories = SaveHandler.load(this@MainActivity, "categories") as ArrayList<Category>
            Session.categories.clear()
            for (category in categories) {
                Session.categories.add(category)
            }
        }

        if (SaveHandler.saveExists(this@MainActivity, "articles")) {
            val articles = SaveHandler.load(this@MainActivity, "articles") as ArrayList<Article>
            Session.articles.clear()
            for (article in articles) {
                Session.articles.add(article)
            }
        }

        if (SaveHandler.saveExists(this@MainActivity, "favorites")) {
            val favorites = SaveHandler.load(this@MainActivity, "favorites") as HashMap<Int, Article>
            Session.favorites.clear()
            for (entry in favorites.entries) {
                Session.favorites[entry.key] = entry.value
            }
        }

        if (SaveHandler.saveExists(this@MainActivity, "liked")) {
            val liked = SaveHandler.load(this@MainActivity, "liked") as HashMap<Int, Article>
            Session.liked.clear()
            for (entry in liked.entries) {
                Session.liked[entry.key] = entry.value
            }
        }
    }

    private fun showReportAlert() {
        var reportDialogTitleEditText: EditText? = null
        var reportDialogContentEditText: EditText? = null
        alert {
            title = "Report"
            customView {
                linearLayout {
                    textInputLayout {
                        reportDialogTitleEditText = editText {
                            hint = "Title"
                        }
                    }.lparams(width = matchParent, height = wrapContent) {
                    }
                    textInputLayout {
                        reportDialogContentEditText = editText {
                            hint = "Description"
                        }
                    }.lparams(width = matchParent, height = matchParent) {
                        topMargin = dip(8)
                    }
                    checkBox {
                        text = "Send a screenshot of the current view"
                    }.lparams {
                        topMargin = dip(8)
                    }
                    padding = dip(16)
                    orientation = LinearLayout.VERTICAL
                }
                positiveButton("Send") {
                    var report = Report(reportDialogTitleEditText!!.editableText.toString(), reportDialogContentEditText!!.editableText.toString())
                    FirebaseDatabase.getInstance().reference.child("reports/" + FirebaseAuth.getInstance().currentUser!!.uid).push().setValue(report)
                }
            }
        }.show()
    }
}
