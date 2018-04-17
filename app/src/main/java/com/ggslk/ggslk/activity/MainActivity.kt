package com.ggslk.ggslk.activity

import android.content.Intent
import android.net.Uri
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
import com.ggslk.ggslk.fragment.FavoritesFragment
import com.ggslk.ggslk.fragment.HomeFragment
import com.ggslk.ggslk.model.Article
import com.ggslk.ggslk.model.Category
import com.google.firebase.auth.FirebaseAuth
import com.onesignal.OneSignal
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private val onDrawerNavigationItemSelectedListener = object : NavigationView.OnNavigationItemSelectedListener {
        fun onBackPressed() {
            val drawer = findViewById<View>(R.id.drawerLayout) as DrawerLayout
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START)
            } else {
                onBackPressed()
            }
        }

        fun onCreateOptionsMenu(menu: Menu): Boolean {
            // Inflate the menu; this adds items to the action bar if it is present.
            menuInflater.inflate(R.menu.main, menu)
            return true
        }

        fun onOptionsItemSelected(item: MenuItem): Boolean {
            // Handle action bar item clicks here. The action bar will
            // automatically handle clicks on the Home/Up button, so long
            // as you specify a parent activity in AndroidManifest.xml.
            val id = item.itemId

            return if (id == R.id.action_settings) {
                true
            } else onOptionsItemSelected(item)
        }

        override fun onNavigationItemSelected(item: MenuItem): Boolean {
            val manager = supportFragmentManager
            val transaction = manager.beginTransaction()
            val fragment: Fragment
            when (item.itemId) {
                R.id.nav_home -> {
                    fragment = HomeFragment.newInstance()
                    transaction.replace(R.id.fragmentContainer, fragment)
                    transaction.commit()
                }
                R.id.nav_favorites -> {
                    fragment = FavoritesFragment.newInstance()
                    transaction.replace(R.id.fragmentContainer, fragment)
                    transaction.commit()
                }
                R.id.nav_blog -> {
                    val openURL = Intent(android.content.Intent.ACTION_VIEW)
                    openURL.data = Uri.parse("https://ggslk.com/")
                    startActivity(openURL)
                }
                R.id.nav_facebook -> {
                    val openURL = Intent(android.content.Intent.ACTION_VIEW)
                    openURL.data = Uri.parse("https://www.facebook.com/GGSLK.SriLanka/?ref=br_rs")
                    startActivity(openURL)
                }
                R.id.nav_profile -> {

                }
                R.id.nav_settings -> {

                }
            }

            val drawer = findViewById<View>(R.id.drawerLayout) as DrawerLayout
            drawer.closeDrawer(GravityCompat.START)
            return true
        }
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

        navView.setNavigationItemSelectedListener(onDrawerNavigationItemSelectedListener)

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
    }

    private fun loadCachedSessionData(){
        if (SaveHandler.saveExists(this@MainActivity, "categories")){
            var categories = SaveHandler.load(this@MainActivity, "categories") as ArrayList<Category>
            for (category in categories){
                Session.categories.add(category)
            }
        }

        if (SaveHandler.saveExists(this@MainActivity, "articles")){
            var articles = SaveHandler.load(this@MainActivity, "articles") as ArrayList<Article>
            for (article in articles){
                Session.articles.add(article)
            }
        }

        if (SaveHandler.saveExists(this@MainActivity, "favorites")){
            var favorites = SaveHandler.load(this@MainActivity, "favorites") as HashMap<Int, Article>
            for (entry in favorites.entries){
                Session.favorites.put(entry.key, entry.value)
            }
        }
    }
}
