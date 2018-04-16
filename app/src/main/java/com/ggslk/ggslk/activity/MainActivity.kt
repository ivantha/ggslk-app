package com.ggslk.ggslk.activity

import android.content.Context
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
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
import com.ggslk.ggslk.common.Session
import com.ggslk.ggslk.fragment.CategoriesFragment
import com.ggslk.ggslk.fragment.EventsFragment
import com.ggslk.ggslk.fragment.HomeFragment
import com.google.firebase.auth.FirebaseAuth
import com.onesignal.OneSignal

class MainActivity : AppCompatActivity() {

    companion object {
        // Remove thissssss>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
        var context: Context? = null
    }

    private val onDrawerNavigationItemSelectedListener = object : NavigationView.OnNavigationItemSelectedListener {
        fun onBackPressed() {
            val drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
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
            // Handle navigation view item clicks here.
            val id = item.itemId

            when (id) {
                R.id.nav_home -> {
                    // Handle the camera action
                }
                R.id.nav_favorites -> {

                }
                R.id.nav_blog -> {

                }
                R.id.nav_facebook -> {

                }
                R.id.nav_profile -> {

                }
                R.id.nav_settings -> {

                }
            }

            val drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
            drawer.closeDrawer(GravityCompat.START)
            return true
        }
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        val fragment: Fragment

        when (item.itemId) {
            R.id.navigation_events -> {
                fragment = EventsFragment.newInstance()
                transaction.replace(R.id.container, fragment)
                transaction.commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_home -> {
                fragment = HomeFragment.newInstance()
                transaction.replace(R.id.container, fragment)
                transaction.commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_categories -> {
                fragment = CategoriesFragment.newInstance()
                transaction.replace(R.id.container, fragment)
                transaction.commit()
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setContentView(R.layout.activity_main)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        val toggle = ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.addDrawerListener(toggle)
        toggle.syncState()

        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(onDrawerNavigationItemSelectedListener)

        val navigation = findViewById<BottomNavigationView>(R.id.navigation)
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        navigation.selectedItemId = R.id.navigation_home

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

        context = this
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
}
