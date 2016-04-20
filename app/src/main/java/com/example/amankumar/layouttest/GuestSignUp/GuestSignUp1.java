package com.example.amankumar.layouttest.GuestSignUp;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.amankumar.layouttest.R;


public class GuestSignUp1 extends Fragment implements View.OnClickListener {
    EditText emailEdit, passwordEdit;
    Button nextButton;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public static GuestSignUp1 newInstance() {
        GuestSignUp1 fragment = new GuestSignUp1();
        return fragment;
    }

    public GuestSignUp1() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_guest_sign_up1, container, false);
        emailEdit = (EditText) view.findViewById(R.id.email_GuestEditText);
        passwordEdit = (EditText) view.findViewById(R.id.password_GuestEditText);
        nextButton = (Button) view.findViewById(R.id.guestSignUp1NextButton);
        nextButton.setOnClickListener(this);
        return view;
    }

    private boolean isValidEmail(String email) {
        boolean isGoodEmail = (email != null && Patterns.EMAIL_ADDRESS.matcher(email).matches());
        if (!isGoodEmail) {
            emailEdit.setError("Invalid Email");
            return false;
        }
        return true;
    }

    private boolean isValidPassword(String password) {
        if (password.length()<6) {
            passwordEdit.setError("Password should not be empty and have length greater than 6");
            return false;
        }
        return true;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentHostSignUp2Listener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        String email = String.valueOf(emailEdit.getText());
        String password = String.valueOf(passwordEdit.getText());
        boolean validEmail = isValidEmail(email);
        boolean validPassword = isValidPassword(password);
        if (!validEmail || !validPassword)
            return;
        if (mListener != null) {
            mListener.onFragmentInteraction(email,password);
        }
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(String email,String password);
    }

}
