package com.example.amankumar.layouttest.UI.GUEST;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.example.amankumar.layouttest.Account.LogInActivity;
import com.example.amankumar.layouttest.R;
import com.example.amankumar.layouttest.UI.ChatActivity;
import com.example.amankumar.layouttest.Util.Constants;
import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class GuestActivity extends AppCompatActivity {
    String encodedEmail, currentUserType;
    Toolbar toolbar;
    Firebase ref,guestRef;
    Firebase.AuthStateListener mAuthStateListener;
    SharedPreferences sp;
    SharedPreferences.Editor spe;
    EditText locationEdit;
    String locationString;
    String facility;
    RadioGroup facilityGroup;
    Button viewBookmarkButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest);
        toolbar = (Toolbar) findViewById(R.id.appBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Guest");
        init();
        mAuthStateListener = new Firebase.AuthStateListener() {
            @Override
            public void onAuthStateChanged(AuthData authData) {
                if (authData == null) {
                    SharedPreferences.Editor spe = sp.edit();
                    spe.putString(Constants.CURRENT_USER_ENCODED_EMAIL, null).apply();
                    spe.putString(Constants.CURRENT_USER_TYPE, null).apply();
                    takeUserToLoginScreenOnUnAuth();
                } else {
                    facilityGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int checkedId) {
                            if(checkedId==R.id.Food_GuestRadio){
                                facility=Constants.HOSTMODEL_FOOD;
                            }
                            else if(checkedId==R.id.Acco_GuestRadio) {
                                facility=Constants.HOSTMODEL_ACCOMMODATION;
                            }
                        }
                    });
                    guestRef=new Firebase(Constants.FIREBASE_GUESTS_URL).child(encodedEmail);
                    guestRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.child(Constants.LOCATION_BOOKMARK).exists())
                                viewBookmarkButton.setVisibility(View.VISIBLE);
                            else
                                viewBookmarkButton.setVisibility(View.GONE);
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
                }
            }
        };
        ref.addAuthStateListener(mAuthStateListener);
    }

    private void init() {
        ref = new Firebase(Constants.FIREBASE_URL);
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        spe = sp.edit();
        encodedEmail = sp.getString(Constants.CURRENT_USER_ENCODED_EMAIL, null);
        currentUserType = sp.getString(Constants.CURRENT_USER_TYPE, null);
        facilityGroup= (RadioGroup) findViewById(R.id.Select_facility_guest);
        locationEdit = (EditText) findViewById(R.id.location_EditText);
        viewBookmarkButton= (Button) findViewById(R.id.G_viewBookmarkButton);
        facility=Constants.HOSTMODEL_FOOD;
    }

    private void takeUserToLoginScreenOnUnAuth() {
        Intent intent = new Intent(this, LogInActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_guest, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_logout) {
            ref.unauth();
        }
        if(id==R.id.action_chat){
            Intent intent=new Intent(this, ChatActivity.class);
            startActivity(intent);
        }
        if (id==R.id.action_main){
            Intent intent=new Intent(this, GuestMain.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ref.removeAuthStateListener(mAuthStateListener);
    }

    public void searchButtonHandler(View view) {
        locationString = String.valueOf(locationEdit.getText()).trim().toLowerCase();
        boolean validLocation = isValidLocation(locationString);
        if (!validLocation)
            return;
        Bundle bundle = new Bundle();
        bundle.putString(Constants.SEARCH_BUNDLE_LOCATION, locationString);
        bundle.putString(Constants.SEARCH_BUNDLE_FACILITY, facility);
        Intent intent = new Intent(this, GuestSearchActivity.class);
        intent.putExtra(Constants.SEARCH_BUNDLE_KEY, bundle);
        startActivity(intent);
    }

    private boolean isValidLocation(String locationString) {
        if (locationString.equals("")) {
            locationEdit.setError("Cannot be empty");
            return false;
        }
        return true;
    }

    public void viewBookmarkHandler(View view) {
        Intent intent=new Intent(this,BookmarkActivity.class);
        startActivity(intent);
    }
}
