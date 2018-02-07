package com.ggslk.ggslk;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements EventsFragment.OnFragmentInteractionListener, HomeFragment.OnFragmentInteractionListener,
ArticlesFragment.OnFragmentInteractionListener {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            Fragment fragment;

            switch (item.getItemId()) {
                case R.id.navigation_events:
                    fragment = EventsFragment.newInstance("p1", "p2");
                    transaction.replace(R.id.container, fragment);
                    transaction.commit();
                    return true;
                case R.id.navigation_home:
                    fragment = HomeFragment.newInstance("p1", "p2");
                    transaction.replace(R.id.container, fragment);
                    transaction.commit();
                    return true;
                case R.id.navigation_articles:
                    fragment = ArticlesFragment.newInstance("p1", "p2");
                    transaction.replace(R.id.container, fragment);
                    transaction.commit();
                    return true;
            }

            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    @Override
    public void onHomeFragmentInteraction(Uri uri) {

    }

    @Override
    public void onEventsFragmentInteraction(Uri uri) {

    }

    @Override
    public void onArticlesFragmentInteraction(Uri uri) {

    }
}
