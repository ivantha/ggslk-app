package com.ggslk.ggslk.activity

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.android.volley.toolbox.Volley
import com.ggslk.ggslk.R
import com.ggslk.ggslk.common.SaveHandler
import com.ggslk.ggslk.common.Session
import com.ggslk.ggslk.fragment.ContactUsFragment
import com.ggslk.ggslk.fragment.FavoritesFragment
import com.ggslk.ggslk.fragment.HomeFragment
import com.ggslk.ggslk.fragment.ReportFragment
import com.ggslk.ggslk.model.Article
import com.ggslk.ggslk.model.Category
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.onesignal.OneSignal
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    override fun onBackPressed() {
        val drawer = findViewById<View>(R.id.drawerLayout) as DrawerLayout
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            onBackPressed()
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
        val transaction = supportFragmentManager.beginTransaction()
        val fragment: Fragment
        return when (item.itemId) {
            R.id.action_report -> {
                fragment = ReportFragment.newInstance()
                transaction.replace(R.id.fragmentContainer, fragment)
                transaction.commit()
                true
            }
            R.id.action_contact_us -> {
                fragment = ContactUsFragment.newInstance()
                transaction.replace(R.id.fragmentContainer, fragment)
                transaction.commit()
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
            R.id.nav_profile -> {

            }
            R.id.nav_settings -> {

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

        // Call syncHashedEmail anywhere in your app if you have the user's email.
        // This improves the effectiveness of OneSignal's "best-time" notification scheduling feature.
        // OneSignal.syncHashedEmail(userEmail);

        loadCachedSessionData()

        // Load HomeFragment
        val transaction = supportFragmentManager.beginTransaction()
        val fragment = HomeFragment.newInstance()
        transaction.replace(R.id.fragmentContainer, fragment)
        transaction.commit()
    }

    override fun onStart() {
        super.onStart()

        // Firebase Auth
        val mAuth = FirebaseAuth.getInstance()

        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = mAuth.currentUser
        if (currentUser == null) {
            mAuth.signInAnonymously().addOnCompleteListener(this) { task ->
                if (!task.isSuccessful) {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(this@MainActivity, "Authentication failed.",
                            Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        SaveHandler.save(this@MainActivity, "categories", Session.categories)
        SaveHandler.save(this@MainActivity, "articles", Session.articles)
        SaveHandler.save(this@MainActivity, "favorites", Session.favorites)
        SaveHandler.save(this@MainActivity, "liked", Session.liked)
    }

    private fun loadCachedSessionData() {
        if (SaveHandler.saveExists(this@MainActivity, "categories")) {
            var categories = SaveHandler.load(this@MainActivity, "categories") as ArrayList<Category>
            Session.categories.clear()
            for (category in categories) {
                Session.categories.add(category)
            }
        }

        if (SaveHandler.saveExists(this@MainActivity, "articles")) {
            var articles = SaveHandler.load(this@MainActivity, "articles") as ArrayList<Article>
            Session.articles.clear()
            for (article in articles) {
                Session.articles.add(article)
            }
        }

        if (SaveHandler.saveExists(this@MainActivity, "favorites")) {
            var favorites = SaveHandler.load(this@MainActivity, "favorites") as HashMap<Int, Article>
            Session.favorites.clear()
            for (entry in favorites.entries) {
                Session.favorites[entry.key] = entry.value
            }
        }

        if (SaveHandler.saveExists(this@MainActivity, "liked")) {
            var liked = SaveHandler.load(this@MainActivity, "liked") as HashMap<Int, Article>
            Session.liked.clear()
            for (entry in liked.entries) {
                Session.liked[entry.key] = entry.value
            }
        }
    }
}
