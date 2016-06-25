package com.example.amankumar.layouttest.Account;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.amankumar.layouttest.HostSignUp.HostSignUp1;
import com.example.amankumar.layouttest.HostSignUp.HostSignUp2;
import com.example.amankumar.layouttest.Model.HostModel;
import com.example.amankumar.layouttest.R;
import com.example.amankumar.layouttest.Util.Constants;
import com.example.amankumar.layouttest.Util.Util;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

public class HostSignUpActivity extends AppCompatActivity implements HostSignUp1.OnFragmentHostSignUp1Listener, HostSignUp2.OnFragmentHostSignUp2Listener {
    String email, password, firstName, lastName;
    String state, address, pinCode, city, mobile;
    String encodedEmail;
    Firebase ref,hostRef,userRef;
    ProgressDialog mProgressDialog;
    SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_sign_up);
        mProgressDialog=new ProgressDialog(this);
        mProgressDialog.setTitle("Loading...");
        mProgressDialog.setMessage("Creating Account");
        mProgressDialog.setCancelable(false);
        sp= PreferenceManager.getDefaultSharedPreferences(this);
        ref=new Firebase(Constants.FIREBASE_URL);
        HostSignUp1 frag = HostSignUp1.newInstance();
        FragmentManager manager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.hostSignUpContainer, frag).commit();
    }

    @Override
    public void onFragmentHostSignUp1(String email, String password, String firstName, String lastName) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        HostSignUp2 frag = HostSignUp2.newInstance();
        FragmentManager manager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = manager.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
        transaction.replace(R.id.hostSignUpContainer, frag).commit();
    }

    @Override
    public void onFragmentHostSignUp2(String state, String address, String pinCode, String city, String mobile) {
        this.state = state;
        this.address = address;
        this.pinCode = pinCode;
        this.city = city.toLowerCase();
        this.mobile = mobile;
        registerHost();
    }

    private void registerHost() {
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

    private void createUserInFireBaseHelper() {
        encodedEmail= Util.encodeEmail(email);
        userRef=new Firebase(Constants.FIREBASE_USERS_URL).child(encodedEmail);
        userRef.setValue(Constants.LOCATION_HOSTS);
        hostRef=new Firebase(Constants.FIREBASE_HOSTS_URL).child(encodedEmail);
        HostModel model=new HostModel(address,city,firstName,lastName,mobile,pinCode,state);
        hostRef.setValue(model);
    }

    private void login() {
        Intent intent = new Intent(this, LogInActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
    private void showErrorToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

}
