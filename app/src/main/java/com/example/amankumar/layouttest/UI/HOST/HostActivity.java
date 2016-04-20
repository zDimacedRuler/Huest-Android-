package com.example.amankumar.layouttest.UI.HOST;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.amankumar.layouttest.Account.LogInActivity;
import com.example.amankumar.layouttest.Model.HostModel;
import com.example.amankumar.layouttest.R;
import com.example.amankumar.layouttest.UI.ChatActivity;
import com.example.amankumar.layouttest.Util.Constants;
import com.example.amankumar.layouttest.Util.ThreeTwoImageView;
import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.io.IOException;

public class HostActivity extends AppCompatActivity {
    String encodedEmail, currentUserType, userName, place;
    Toolbar toolbar;
    Firebase ref, nameRef;
    TextView userNameText;
    Firebase.AuthStateListener mAuthStateListener;
    SharedPreferences sp;
    SharedPreferences.Editor spe;
    private int PICK_IMAGE_REQUEST = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host);
        toolbar = (Toolbar) findViewById(R.id.appBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Host");
        init();
        nameRef = ref.child(currentUserType).child(encodedEmail);
        nameRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HostModel model = dataSnapshot.getValue(HostModel.class);
                String userName = model.getFirstName() + " " + model.getLastname();
                String place = model.getCity();
                spe.putString(Constants.CURRENT_USER_NAME, userName).apply();
                spe.putString(Constants.CURRENT_USER_PLACE, place).apply();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        userName = sp.getString(Constants.CURRENT_USER_NAME, "");
        userNameText.setText(userName);
        mAuthStateListener = new Firebase.AuthStateListener() {
            @Override
            public void onAuthStateChanged(AuthData authData) {
                if (authData == null) {
                    SharedPreferences.Editor spe = sp.edit();
                    spe.putString(Constants.CURRENT_USER_ENCODED_EMAIL, null).apply();
                    spe.putString(Constants.CURRENT_USER_TYPE, null).apply();
                    spe.putString(Constants.CURRENT_USER_NAME, "").apply();
                    spe.putString(Constants.CURRENT_USER_PLACE, "").apply();
                    takeUserToLoginScreenOnUnAuth();
                } else {

                }
            }
        };
        ref.addAuthStateListener(mAuthStateListener);

    }

    private void init() {
        ref = new Firebase(Constants.FIREBASE_URL);
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        encodedEmail = sp.getString(Constants.CURRENT_USER_ENCODED_EMAIL, null);
        currentUserType = sp.getString(Constants.CURRENT_USER_TYPE, null);
        spe = sp.edit();
        userNameText = (TextView) findViewById(R.id.host_userNameText);
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
        getMenuInflater().inflate(R.menu.menu_host, menu);
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
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ref.removeAuthStateListener(mAuthStateListener);
    }

    public void manageFacilityHandler(View view) {
        Intent intent = new Intent(this, HostFacilityActivity.class);
        startActivity(intent);
    }

    public void addImageHostHandler(View view) {
        Intent intent = new Intent();
        // Show only images, no videos or anything else
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        // Always show the chooser (if there are multiple options available)
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                ThreeTwoImageView imageView = (ThreeTwoImageView) findViewById(R.id.hostImageView);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
