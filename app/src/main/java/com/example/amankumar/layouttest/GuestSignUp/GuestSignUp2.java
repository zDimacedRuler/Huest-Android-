package com.example.amankumar.layouttest.GuestSignUp;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.example.amankumar.layouttest.R;

public class GuestSignUp2 extends Fragment {
    EditText mobileEdit, firstNameEdit, lastNameEdit;
    Button nextButton;
    RadioGroup genderGroup;
    Spinner occupationSpinner;
    ArrayAdapter<CharSequence> adapter;
    String gender,occupation;
    private OnFragmentInteractionListener mListener;

    public static GuestSignUp2 newInstance() {
        GuestSignUp2 fragment = new GuestSignUp2();
        return fragment;
    }

    public GuestSignUp2() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_guest_sign_up2, container, false);
//        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        gender="male";
        mobileEdit = (EditText) view.findViewById(R.id.mobile_GuestEditText);
        firstNameEdit = (EditText) view.findViewById(R.id.firstName_GuestEditText);
        lastNameEdit = (EditText) view.findViewById(R.id.lastName_GuestEditText);
        genderGroup= (RadioGroup) view.findViewById(R.id.gender_group_Guest);
        nextButton = (Button) view.findViewById(R.id.guestSignUp2NextButton);
        occupationSpinner= (Spinner) view.findViewById(R.id.OccupationSpinner_Guest);
        adapter=ArrayAdapter.createFromResource(getActivity(),R.array.occupation_array,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        occupationSpinner.setAdapter(adapter);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mobile = String.valueOf(mobileEdit.getText());
                String firstName = String.valueOf(firstNameEdit.getText());
                String lastName = String.valueOf(lastNameEdit.getText());
                boolean validFirstName = isValidFirstName(firstName);
                boolean validLastName = isValidLastName(lastName);
                boolean validMobile = isValidMobile(mobile);
                if (!validMobile || !validFirstName || !validLastName)
                    return;
                if (mListener != null) {
                    mListener.onFragmentInteraction1(mobile, firstName,lastName,gender,occupation);
                }
            }
        });
        genderGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.male_GuestRadio) {
                    gender="male";
                } else if (checkedId == R.id.female_GuestRadio) {
                    gender="female";
                }
            }
        });
        occupationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                occupation= (String) adapter.getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return view;
    }
    private boolean isValidMobile(String mobile) {
        if (!mobile.matches("^(\\+\\d{1,3}[- ]?)?\\d{10}$")) {
            mobileEdit.setError("Incorrect Mobile Number");
            return false;
        }
        return true;
    }
    private boolean isValidLastName(String lastName) {
        if (!lastName.matches("[A-Z][a-zA-Z]*") || lastName.length()<4) {
            lastNameEdit.setError("Incorrect Last Name");
            return false;
        }
        return true;
    }

    private boolean isValidFirstName(String firstName) {
        if (!firstName.matches( "[A-Z][a-zA-Z]*" ) || firstName.length()<4) {
            firstNameEdit.setError("Incorrect First Name");
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
        void onFragmentInteraction1(String mobile,String firstName,String lastName,String gender,String occupation);
    }

}
