package com.example.amankumar.layouttest.Account;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.amankumar.layouttest.GuestSignUp.GuestSignUp1;
import com.example.amankumar.layouttest.GuestSignUp.GuestSignUp2;
import com.example.amankumar.layouttest.GuestSignUp.GuestSignUp3;
import com.example.amankumar.layouttest.Model.GuestModel;
import com.example.amankumar.layouttest.R;
import com.example.amankumar.layouttest.Util.Constants;
import com.example.amankumar.layouttest.Util.Util;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

public class GuestSignUpActivity extends AppCompatActivity implements GuestSignUp1.OnFragmentInteractionListener, GuestSignUp2.OnFragmentInteractionListener, GuestSignUp3.OnFragmentInteractionListener {
    String email, firstName, lastName, mobileNumber, address, pinCode,state,password,gender,occupation,city;
    String encodedEmail;
    Firebase ref,guestRef,userRef;
    ProgressDialog mProgressDialog;
    SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_sign_up);
        mProgressDialog=new ProgressDialog(this);
        mProgressDialog.setTitle("Loading...");
        mProgressDialog.setMessage("Creating Account");
        mProgressDialog.setCancelable(false);
        sp= PreferenceManager.getDefaultSharedPreferences(this);
        ref=new Firebase(Constants.FIREBASE_URL);
        GuestSignUp1 frag = GuestSignUp1.newInstance();
        FragmentManager manager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.guestSignUpContainer, frag).commit();
    }
    @Override
    public void onFragmentInteraction(String email,String password) {
        this.email = email;
        this.password=password;
        GuestSignUp2 frag = GuestSignUp2.newInstance();
        FragmentManager manager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = manager.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
        transaction.replace(R.id.guestSignUpContainer, frag).commit();

    }

    @Override
    public void onFragmentInteraction1(String mobile,String firstName,String lastName,String gender,String occupation) {
        this.mobileNumber = mobile;
        this.firstName=firstName;
        this.lastName=lastName;
        this.gender=gender;
        this.occupation=occupation;
        GuestSignUp3 frag=GuestSignUp3.newInstance();
        FragmentManager manager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = manager.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
        transaction.replace(R.id.guestSignUpContainer, frag).commit();
    }

    @Override
    public void onFragmentInteraction2(String address, String pinCode, String state,String city) {
        this.address=address;
        this.pinCode=pinCode;
        this.state=state;
        this.city=city;
        registerGuest();
    }

    private void registerGuest() {
        mProgressDialog.show();
        ref.createUser(email, password, new Firebase.ResultHandler() {
            @Override
            public void onSuccess() {
                mProgressDialog.dismiss();
                createUserInFireBaseHelper();
                SharedPreferences.Editor spe = sp.edit();
                spe.putString(Constants.SIGNUP_EMAIL, email).apply();
                login();
            }

            @Override
            public void onError(FirebaseError firebaseError) {
                mProgressDialog.dismiss();
                showErrorToast(firebaseError.getMessage());
            }
        });
    }

    private void login() {
        Intent intent = new Intent(this, LogInActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void createUserInFireBaseHelper() {
        encodedEmail= Util.encodeEmail(email);
        userRef=new Firebase(Constants.FIREBASE_USERS_URL).child(encodedEmail);
        userRef.setValue(Constants.LOCATION_GUESTS);
        guestRef=new Firebase(Constants.FIREBASE_GUESTS_URL).child(encodedEmail);
        GuestModel guestModel=new GuestModel(pinCode,state,occupation,lastName,mobileNumber,gender,firstName,city,address);
        guestRef.setValue(guestModel);
    }

    private void showErrorToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

}
