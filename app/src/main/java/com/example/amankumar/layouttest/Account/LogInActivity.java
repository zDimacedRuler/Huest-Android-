package com.example.amankumar.layouttest.Account;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.transition.Scene;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.transition.TransitionManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.amankumar.layouttest.R;
import com.example.amankumar.layouttest.UI.GUEST.GuestActivity;
import com.example.amankumar.layouttest.UI.HOST.HostActivity;
import com.example.amankumar.layouttest.Util.Constants;
import com.example.amankumar.layouttest.Util.Util;
import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class LogInActivity extends AppCompatActivity {
    TextView huestTitle;
    EditText emailEdit, passwordEdit;
    Scene signupScene, guestOrUserScene;
    ViewGroup sceneRoot;
    Transition transition;
    Firebase ref, userRef, nameRef;
    SharedPreferences sp;
    SharedPreferences.Editor spe;
    ProgressDialog mProgressDialog;
    String email, password, encodedEmail, currentUser, usertType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        init();
        currentUser = sp.getString(Constants.CURRENT_USER_ENCODED_EMAIL, null);
        usertType = sp.getString(Constants.CURRENT_USER_TYPE, null);
        if (currentUser != null) {
            if (usertType.equals(Constants.LOCATION_HOSTS))
                takeUserToHostActivity();
            else if (usertType.equals(Constants.LOCATION_GUESTS))
                takeUserToGuestActivity();
        }

    }

    private void init() {
        huestTitle = (TextView) findViewById(R.id.huest_title);
        sceneRoot = (ViewGroup) findViewById(R.id.scene_root);
        emailEdit = (EditText) findViewById(R.id.email_EditText);
        passwordEdit = (EditText) findViewById(R.id.password_EditText);
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        spe = sp.edit();
        ref = new Firebase(Constants.FIREBASE_URL);
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle("Loading...");
        mProgressDialog.setMessage("Connecting");
        mProgressDialog.setCancelable(false);
    }

    public void signUpHandler(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            signupScene = Scene.getSceneForLayout(sceneRoot, R.layout.signup, this);
            guestOrUserScene = Scene.getSceneForLayout(sceneRoot, R.layout.host_or_user, this);
            transition = TransitionInflater.from(this).inflateTransition(R.transition.transtion);
            TransitionManager.go(guestOrUserScene, transition);
        } else {
            View host0rGuest = getLayoutInflater().inflate(R.layout.host_or_user, sceneRoot, false);
            View signup = getLayoutInflater().inflate(R.layout.signup, sceneRoot, false);
            sceneRoot.removeView(signup);
            sceneRoot.addView(host0rGuest, sceneRoot.indexOfChild(host0rGuest));
        }
    }

    public void guestSingnUpHandler(View view) {
        Intent intent = new Intent(this, GuestSignUpActivity.class);
        startActivity(intent);
    }

    public void LoginButtonHandler(View view) {
        email = emailEdit.getText().toString().toLowerCase();
        password = passwordEdit.getText().toString();
        encodedEmail = Util.encodeEmail(email);
        if (email.equals("")) {
            emailEdit.setError("Email cannot be empty");
            return;
        }
        if (password.equals("")) {
            passwordEdit.setError("Field cannot be empty");
            return;
        }
        mProgressDialog.show();
        ref.authWithPassword(email, password, new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authData) {
                mProgressDialog.dismiss();
                if (authData != null) {
                    spe.putString(Constants.CURRENT_USER_ENCODED_EMAIL, encodedEmail).apply();
                    userRef = ref.child(Constants.LOCATION_USERS).child(encodedEmail);
                    userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String userType = (String) dataSnapshot.getValue();
                            spe.putString(Constants.CURRENT_USER_TYPE, userType).apply();
                            if (userType.equals("HOSTS"))
                                takeUserToHostActivity();
                            else if (userType.equals("GUESTS"))
                                takeUserToGuestActivity();
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {
                        }
                    });
                }
            }

            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {
                mProgressDialog.dismiss();
                switch (firebaseError.getCode()) {
                    case FirebaseError.INVALID_EMAIL:
                    case FirebaseError.USER_DOES_NOT_EXIST:
                        emailEdit.setError(getString(R.string.error_message_email_issue));
                        break;
                    case FirebaseError.INVALID_PASSWORD:
                        passwordEdit.setError(firebaseError.getMessage());
                        break;
                    case FirebaseError.NETWORK_ERROR:
                        showErrorToast(getString(R.string.error_message_failed_sign_in_no_network));
                        break;
                    default:
                        showErrorToast(firebaseError.toString());
                }
            }
        });
    }

    private void takeUserToGuestActivity() {
        Intent intent = new Intent(this, GuestActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

    private void takeUserToHostActivity() {
        Intent intent = new Intent(this, HostActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

    private void showErrorToast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        String signUpEmail = sp.getString(Constants.SIGNUP_EMAIL, null);
        if (signUpEmail != null) {
            emailEdit.setText(signUpEmail);
            spe.putString(Constants.SIGNUP_EMAIL, null).apply();
        }
    }

    public void hostSingnUpHandler(View view) {
        Intent intent = new Intent(this, HostSignUpActivity.class);
        startActivity(intent);
    }
}
