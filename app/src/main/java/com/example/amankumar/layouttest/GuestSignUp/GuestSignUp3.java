package com.example.amankumar.layouttest.GuestSignUp;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.amankumar.layouttest.R;


public class GuestSignUp3 extends Fragment {
    EditText stateEdit, addressEdit, pinCodeEdit,cityEdit;
    Button registerButton;
    private OnFragmentInteractionListener mListener;

    public static GuestSignUp3 newInstance() {
        GuestSignUp3 fragment = new GuestSignUp3();
        return fragment;
    }

    public GuestSignUp3() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_guest_sign_up3, container, false);
        addressEdit = (EditText) view.findViewById(R.id.address_GuestEditText);
        pinCodeEdit = (EditText) view.findViewById(R.id.pinCode_GuestEditText);
        stateEdit= (EditText) view.findViewById(R.id.state_GuestEditText);
        cityEdit= (EditText) view.findViewById(R.id.city_GuestEditText);
        registerButton= (Button) view.findViewById(R.id.guestSignUp3NextButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String state = String.valueOf(stateEdit.getText());
                String address = String.valueOf(addressEdit.getText());
                String pinCode = String.valueOf(pinCodeEdit.getText());
                String city = String.valueOf(cityEdit.getText());
                boolean validState = isValidState(state);
                boolean validAddress = isValidAddress(address);
                boolean validPinCode = isValidPinCode(pinCode);
                boolean validCity = isValidCity(city);
                if (!validState || !validAddress || !validPinCode || !validCity)
                    return;
                if (mListener != null) {
                    mListener.onFragmentInteraction2(address, pinCode, state,city);
                }
            }
        });
        return view;
    }

    private boolean isValidCity(String city) {
        if (city.equals("")) {
            cityEdit.setError("Cannot be empty");
            return false;
        }
        return true;
    }

    private boolean isValidState(String state) {
        if (state.equals("")) {
            stateEdit.setError("Cannot be empty");
            return false;
        }
        return true;
    }

    private boolean isValidAddress(String address) {
        if (address.equals("")) {
            addressEdit.setError("Cannot be empty");
            return false;
        }
        return true;

    }

    private boolean isValidPinCode(String pinCode) {
        if (pinCode.equals("")||pinCode.length()<6) {
            pinCodeEdit.setError("Cannot be empty");
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

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction2(String address,String pinCode,String state,String city);
    }

}
