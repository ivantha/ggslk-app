package com.ggslk.ggslk.fragment

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ggslk.ggslk.R
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

    companion object {
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        val transaction = fragmentManager!!.beginTransaction()
        val fragment: Fragment

        when (item.itemId) {
            R.id.navigation_events -> {
                fragment = EventsFragment.newInstance()
                transaction.replace(R.id.container, fragment)
                transaction.commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_news_feed -> {
                fragment = NewsFeedFragment.newInstance()
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        navigation.selectedItemId = R.id.navigation_news_feed
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }
}
