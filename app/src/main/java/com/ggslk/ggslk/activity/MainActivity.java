package com.ggslk.ggslk.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import com.ggslk.ggslk.fragment.ArticlesFragment;
import com.ggslk.ggslk.fragment.EventsFragment;
import com.ggslk.ggslk.fragment.HomeFragment;
import com.ggslk.ggslk.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.onesignal.OneSignal;

public class MainActivity extends AppCompatActivity implements EventsFragment.OnFragmentInteractionListener, HomeFragment.OnFragmentInteractionListener,
        ArticlesFragment.OnFragmentInteractionListener {

    private FirebaseAuth mAuth;
    FirebaseUser currentUser;

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
                    fragment = HomeFragment.newInstance();
                    transaction.replace(R.id.container, fragment);
                    transaction.commit();
                    return true;
                case R.id.navigation_articles:
                    fragment = ArticlesFragment.newInstance();
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
        navigation.setSelectedItemId(R.id.navigation_home);

        // Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // OneSignal service initialized
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();

        // Call syncHashedEmail anywhere in your app if you have the user's email.
        // This improves the effectiveness of OneSignal's "best-time" notification scheduling feature.
        // OneSignal.syncHashedEmail(userEmail);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Check if user is signed in (non-null) and update UI accordingly.
        currentUser = mAuth.getCurrentUser();
        if(currentUser == null){
            mAuth.signInAnonymously().addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        currentUser = mAuth.getCurrentUser();
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(MainActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
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
