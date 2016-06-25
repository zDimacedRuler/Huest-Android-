package com.example.amankumar.layouttest.HostSignUp;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.amankumar.layouttest.R;

public class HostSignUp1 extends Fragment {
    EditText emailEdit, passwordEdit, firstEdit, lastEdit;
    String email, password, firstName, lastName;
    Button nextButton;
    private OnFragmentHostSignUp1Listener mListener;

    public static HostSignUp1 newInstance() {
        HostSignUp1 fragment = new HostSignUp1();
        return fragment;
    }

    public HostSignUp1() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_host_sign_up1, container, false);
        emailEdit = (EditText) view.findViewById(R.id.email_HostEditText);
        passwordEdit = (EditText) view.findViewById(R.id.password_HostEditText);
        firstEdit = (EditText) view.findViewById(R.id.firstName_HostEditText);
        lastEdit = (EditText) view.findViewById(R.id.lastName_HostEditText);
        nextButton = (Button) view.findViewById(R.id.hostSignUp1NextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = String.valueOf(emailEdit.getText()).toLowerCase();
                password = String.valueOf(passwordEdit.getText());
                firstName = String.valueOf(firstEdit.getText());
                lastName = String.valueOf(lastEdit.getText());
                boolean validEmail = isValidEmail(email);
                boolean validPassword = isValidPassword(password);
                boolean validFirstName = isValidFirstName(firstName);
                boolean validLastName = isValidLastName(lastName);
                if (!validEmail || !validFirstName || !validLastName || !validPassword)
                    return;
                if (mListener != null) {
                    mListener.onFragmentHostSignUp1(email, password, firstName, lastName);
                }
            }
        });
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
        if (password.length() < 6) {
            passwordEdit.setError("Password length should be greater than 6");
            return false;
        }
        return true;
    }

    private boolean isValidFirstName(String firstName) {
        if (!firstName.matches( "[A-Z][a-zA-Z]*" ) || firstName.length()<4) {
            firstEdit.setError("Incorrect First Name");
            return false;
        }
        return true;
    }

    private boolean isValidLastName(String lastName) {
        if (!lastName.matches("[A-Z][a-zA-Z]*") || lastName.length()<4) {
            lastEdit.setError("Incorrect Last Name");
            return false;
        }
        return true;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentHostSignUp1Listener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentHostSignUp2Listener");
        }
    }

    public interface OnFragmentHostSignUp1Listener {
        void onFragmentHostSignUp1(String email, String password, String firstName, String lastName);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

}
