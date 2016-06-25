package com.example.amankumar.layouttest.UI.GUEST;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.amankumar.layouttest.Account.LogInActivity;
import com.example.amankumar.layouttest.Model.GuestModel;
import com.example.amankumar.layouttest.R;
import com.example.amankumar.layouttest.UI.ChatActivity;
import com.example.amankumar.layouttest.Util.Constants;
import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class GuestMain extends AppCompatActivity {
    String encodedEmail, currentUserType;
    Toolbar toolbar;
    View headerView;
    TabLayout tabLayout;
    ViewPager viewPager;
    NavigationView navigationView;
    DrawerLayout drawerLayout;
    Firebase ref, guestRef;
    Firebase.AuthStateListener mAuthStateListener;
    SharedPreferences sp;
    SharedPreferences.Editor spe;
    TextView usernameText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Guest");
        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        ab.setDisplayHomeAsUpEnabled(true);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        ref=new Firebase(Constants.FIREBASE_URL);
        sp= PreferenceManager.getDefaultSharedPreferences(this);
        spe=sp.edit();
        encodedEmail=sp.getString(Constants.CURRENT_USER_ENCODED_EMAIL,"");
        mAuthStateListener = new Firebase.AuthStateListener() {
            @Override
            public void onAuthStateChanged(AuthData authData) {
                if (authData == null) {
                    spe.putString(Constants.CURRENT_USER_ENCODED_EMAIL, null).apply();
                    spe.putString(Constants.CURRENT_USER_TYPE, null).apply();
                    takeUserToLoginScreenOnUnAuth();
                }
            }
        };
        ref.addAuthStateListener(mAuthStateListener);
        headerView = navigationView.getHeaderView(0);
        usernameText= (TextView) headerView.findViewById(R.id.nav_username);
        guestRef=new Firebase(Constants.FIREBASE_GUESTS_URL).child(encodedEmail);
        guestRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GuestModel guestModel=dataSnapshot.getValue(GuestModel.class);
                String guestName=guestModel.getFirstName()+" "+guestModel.getLastName();
                usernameText.setText(guestName);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    private void takeUserToLoginScreenOnUnAuth() {
        Intent intent = new Intent(this, LogInActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(GuestHomeFragment.newInstance(), "Home");
        adapter.addFragment(GuestBookmarkFragment.newInstance(), "Bookmark");
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_guest_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.action_logout:
                ref.unauth();
        }
        return true;
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> fragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            fragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        drawerLayout.closeDrawers();
                        switch (menuItem.getItemId()){
                            case R.id.nav_home:
                                viewPager.setCurrentItem(0);
                                break;
                            case R.id.nav_bookmark:
                                viewPager.setCurrentItem(1);
                                break;
                            case R.id.nav_chats:
                                Intent intent=new Intent(GuestMain.this, ChatActivity.class);
                                startActivity(intent);
                        }
                        return true;
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ref.removeAuthStateListener(mAuthStateListener);
    }
}
