package com.example.amankumar.layouttest.HostSignUp;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.amankumar.layouttest.R;

public class HostSignUp2 extends Fragment {
    EditText stateEdit, addressEdit, pinCodeEdit, cityEdit, mobileEdit;
    Button nextButton;
    private OnFragmentHostSignUp2Listener mListener;

    public static HostSignUp2 newInstance() {
        HostSignUp2 fragment = new HostSignUp2();
        return fragment;
    }

    public HostSignUp2() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_host_sign_up2, container, false);
        stateEdit = (EditText) view.findViewById(R.id.state_HostEditText);
        addressEdit = (EditText) view.findViewById(R.id.address_HostEditText);
        pinCodeEdit = (EditText) view.findViewById(R.id.pinCode_HostEditText);
        cityEdit = (EditText) view.findViewById(R.id.city_HostEditText);
        mobileEdit = (EditText) view.findViewById(R.id.mobile_HostEditText);
        nextButton = (Button) view.findViewById(R.id.hostSignUp2NextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String state = String.valueOf(stateEdit.getText());
                String address = String.valueOf(addressEdit.getText());
                String pinCode = String.valueOf(pinCodeEdit.getText());
                String city = String.valueOf(cityEdit.getText());
                String mobile = String.valueOf(mobileEdit.getText());
                boolean validState = isValidState(state);
                boolean validAddress = isValidAddress(address);
                boolean validPinCode = isValidPinCode(pinCode);
                boolean validCity = isValidCity(city);
                boolean validMobile = isValidMobile(mobile);
                if (!validState || !validAddress || !validPinCode || !validCity || !validMobile)
                    return;
                if (mListener != null) {
                    mListener.onFragmentHostSignUp2(state,address,pinCode,city,mobile);
                }
            }
        });
        return view;
    }
    private boolean isValidCity(String city) {
        if (!city.matches( "([a-zA-Z]+|[a-zA-Z]+\\s[a-zA-Z]+)" )) {
            cityEdit.setError("Invalid City");
            return false;
        }
        return true;
    }

    private boolean isValidState(String state) {
        if (!state.matches("([a-zA-Z]+|[a-zA-Z]+\\s[a-zA-Z]+)" )) {
            stateEdit.setError("Invalid State");
            return false;
        }
        return true;
    }

    private boolean isValidAddress(String address) {
        if (address.equals("")) {
            addressEdit.setError("Invalid Address");
            return false;
        }
        return true;

    }

    private boolean isValidPinCode(String pinCode) {
        if (!pinCode.matches("\\d{6}")) {
            pinCodeEdit.setError("Invalid PinCode");
            return false;
        }
        return true;
    }
    private boolean isValidMobile(String mobile) {
        if (!mobile.matches("^(\\+\\d{1,3}[- ]?)?\\d{10}$")) {
            mobileEdit.setError("Incorrect Mobile Number");
            return false;
        }
        return true;
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentHostSignUp2Listener) activity;
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

    public interface OnFragmentHostSignUp2Listener {
        void onFragmentHostSignUp2(String state,String address,String pinCode,String city,String mobile);
    }

}
