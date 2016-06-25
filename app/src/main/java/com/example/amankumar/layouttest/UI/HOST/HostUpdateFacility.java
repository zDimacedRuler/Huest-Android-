package com.example.amankumar.layouttest.UI.HOST;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.amankumar.layouttest.Model.AccommodationModel;
import com.example.amankumar.layouttest.Model.FoodModel;
import com.example.amankumar.layouttest.Model.HostModel;
import com.example.amankumar.layouttest.R;
import com.example.amankumar.layouttest.Util.Constants;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import static com.example.amankumar.layouttest.R.id.host_accoDescEditText;

public class HostUpdateFacility extends Fragment {
    View view;
    Boolean food, accommodation;
    CheckBox foodCheckBox, accoCheckBox;
    ViewGroup foodRoot, accoRoot;
    View foodFacility, accoFacility;
    Button registerButton;
    String encodedEmail, userType, userName, userCity;
    //Accommodation Facility
    CheckBox singleAccoCheck, sharingAccoCheck, trialAccoCheck;
    EditText singleRateEdit, sharingRateEdit, trialRateEdit, accoLimitEdit, accoDescEdit;
    String singleRate, sharingRate, trialRate, accoLimit, accoDesc;
    //Food Facility
    RadioGroup foodTypeGroup;
    EditText foodLimitEdit, foodDescEdit, dayRateEdit, weekRateEdit, monthRateEdit;
    String foodLimit, foodDesc, dayRate, weekRate, monthRate;
    String foodType;
    //Firebase
    Firebase ref, hostRef, hostFoodRef, hostAccoRef, hostModelFoodRef, hostModelAccoRef;

    SharedPreferences sp;
    SharedPreferences.Editor spe;

    public static HostUpdateFacility newInstance() {
        HostUpdateFacility fragment = new HostUpdateFacility();
        return fragment;
    }

    public HostUpdateFacility() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_host_update_facility, container, false);
        init(view, inflater);
        userName = sp.getString(Constants.CURRENT_USER_NAME, "");
        userCity = sp.getString(Constants.CURRENT_USER_PLACE, "");
        hostRef = ref.child(Constants.LOCATION_HOSTS).child(encodedEmail);
        hostRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HostModel model = dataSnapshot.getValue(HostModel.class);
                String isFood = model.getFood();
                String isAcco = model.getAccommodation();
                if (isFood.equals("true")) {
                    foodCheckBox.setChecked(true);
                    hostFoodRef = ref.child(Constants.LOCATION_HOST_FOOD).child(encodedEmail);
                    hostFoodRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            FoodModel model = dataSnapshot.getValue(FoodModel.class);
                            String type = model.getFoodType();
                            String desc = model.getDescription();
                            String day = model.getPer_Day();
                            String week = model.getPer_Week();
                            String month = model.getPer_Month();
                            String limit = model.getLimits();
                            if (type.equals("veg"))
                                foodTypeGroup.check(R.id.veg_HostRadio);
                            else
                                foodTypeGroup.check(R.id.Non_veg_GuestRadio);
                            foodLimitEdit.setText(limit);
                            foodDescEdit.setText(desc);
                            dayRateEdit.setText(day);
                            weekRateEdit.setText(week);
                            monthRateEdit.setText(month);
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
                }
                if (isAcco.equals("true")) {
                    accoCheckBox.setChecked(true);
                    hostAccoRef = ref.child(Constants.LOCATION_HOST_ACCOMMODATION).child(encodedEmail);
                    hostAccoRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            AccommodationModel accommodationModel = dataSnapshot.getValue(AccommodationModel.class);
                            String single = accommodationModel.getSingle();
                            String sharing = accommodationModel.getSharing();
                            String trial = accommodationModel.getTrial();
                            String limit = accommodationModel.getLimits();
                            String desc = accommodationModel.getDescriptionOfPlace();
                            if (!single.equals("n/a")) {
                                singleAccoCheck.setChecked(true);
                                singleRateEdit.setText(single);
                            }
                            if (!sharing.equals("n/a")) {
                                sharingAccoCheck.setChecked(true);
                                sharingRateEdit.setText(sharing);
                            }
                            if (!trial.equals("n/a")) {
                                trialAccoCheck.setChecked(true);
                                trialRateEdit.setText(trial);
                            }
                            accoLimitEdit.setText(limit);
                            accoDescEdit.setText(desc);
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        foodCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    foodRoot.addView(foodFacility, foodRoot.indexOfChild(foodFacility));
                    foodFacilityInit(foodFacility);
                    food = true;
                } else {
                    foodRoot.removeView(foodFacility);
                    food = false;
                }
            }
        });
        accoCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    accoRoot.addView(accoFacility, foodRoot.indexOfChild(accoFacility));
                    accoFacilityInit(accoFacility);
                    accommodation = true;
                } else {
                    accoRoot.removeView(accoFacility);
                    accommodation = false;
                }
            }
        });
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Confirm changes?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        updateFacility();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        return view;
    }

    public void updateFacility() {
        if (food) {
            foodLimit = String.valueOf(foodLimitEdit.getText());
            foodDesc = String.valueOf(foodDescEdit.getText());
            dayRate = String.valueOf(dayRateEdit.getText());
            weekRate = String.valueOf(weekRateEdit.getText());
            monthRate = String.valueOf(monthRateEdit.getText());
            boolean validFoodlimit = isValidFoodLimit(foodLimit);
            boolean validFoodDesc = isValidFoodDesc(foodDesc);
            boolean validDayRate = isValidDayRate(dayRate);
            boolean validWeekRate = isValidWeekRate(weekRate);
            boolean validMonthRate = isValidMonthRate(monthRate);
            if (!validFoodlimit || !validFoodDesc || !validDayRate || !validWeekRate || !validMonthRate)
                return;
            FoodModel model = new FoodModel(userName, userCity, foodType, foodLimit, foodDesc, dayRate, monthRate, weekRate);
            hostFoodRef = ref.child(Constants.LOCATION_HOST_FOOD).child(encodedEmail);
            hostFoodRef.setValue(model);
            hostModelFoodRef = new Firebase(Constants.FIREBASE_HOSTS_URL).child(encodedEmail).child(Constants.HOSTMODEL_FOOD);
            hostModelFoodRef.setValue("true");
        }
        if (accommodation) {
            if (singleAccoCheck.isChecked())
                singleRate = String.valueOf(singleRateEdit.getText());
            else
                singleRate = "n/a";
            if (sharingAccoCheck.isChecked())
                sharingRate = String.valueOf(sharingRateEdit.getText());
            else
                sharingRate = "n/a";
            if (trialAccoCheck.isChecked())
                trialRate = String.valueOf(trialRateEdit.getText());
            else
                trialRate = "n/a";
            accoLimit = String.valueOf(accoLimitEdit.getText());
            accoDesc = String.valueOf(accoDescEdit.getText());
            boolean validSingleRate = isValidSingleRate(singleRate);
            boolean validSharingRate = isValidSharingRate(sharingRate);
            boolean validTrialRate = isValidTrialRate(trialRate);
            boolean validAccoLimit = isValidAccoLimit(accoLimit);
            boolean validAccoDesc = isValidAccoDesc(accoDesc);
            if (!singleAccoCheck.isChecked() && !sharingAccoCheck.isChecked() && !trialAccoCheck.isChecked()) {
                Toast.makeText(getActivity(), "No Rate Provided", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!validSingleRate || !validSharingRate || !validTrialRate || !validAccoLimit || !validAccoDesc) {
                Toast.makeText(getActivity(), "Return", Toast.LENGTH_SHORT).show();
                return;
            }
            hostAccoRef = ref.child(Constants.LOCATION_HOST_ACCOMMODATION).child(encodedEmail);
            AccommodationModel model = new AccommodationModel(userName, userCity, accoDesc, accoLimit, sharingRate, singleRate, trialRate);
            hostAccoRef.setValue(model);
            hostModelAccoRef = new Firebase(Constants.FIREBASE_HOSTS_URL).child(encodedEmail).child(Constants.HOSTMODEL_ACCOMMODATION);
            hostModelAccoRef.setValue("true");
        }
        if (!food) {
            hostModelFoodRef = new Firebase(Constants.FIREBASE_HOSTS_URL).child(encodedEmail).child(Constants.HOSTMODEL_FOOD);
            hostModelFoodRef.setValue("false");
            hostFoodRef = ref.child(Constants.LOCATION_HOST_FOOD).child(encodedEmail);
            hostFoodRef.setValue(null);
        }
        if (!accommodation) {
            hostModelAccoRef = new Firebase(Constants.FIREBASE_HOSTS_URL).child(encodedEmail).child(Constants.HOSTMODEL_ACCOMMODATION);
            hostModelAccoRef.setValue("false");
            hostAccoRef = ref.child(Constants.LOCATION_HOST_ACCOMMODATION).child(encodedEmail);
            hostAccoRef.setValue(null);
        }
    }

    private boolean isValidAccoDesc(String accoDesc) {
        if (accoDesc.equals("")) {
            accoDescEdit.setError("Cannot be empty");
            return false;
        }
        return true;
    }

    private boolean isValidAccoLimit(String accoLimit) {
        if (accoLimit.equals("")) {
            accoLimitEdit.setError("Cannot be empty");
            return false;
        }
        return true;
    }

    private boolean isValidTrialRate(String trialRate) {
        if (trialRate.equals("")) {
            trialRateEdit.setError("Cannot be empty");
            return false;
        }
        return true;
    }

    private boolean isValidSharingRate(String sharingRate) {
        if (sharingRate.equals("")) {
            sharingRateEdit.setError("Cannot be empty");
            return false;
        }
        return true;
    }

    private boolean isValidSingleRate(String singleRate) {
        if (singleRate.equals("")) {
            singleRateEdit.setError("Cannot be empty");
            return false;
        }
        return true;
    }

    private boolean isValidMonthRate(String monthRate) {
        if (monthRate.equals("")) {
            monthRateEdit.setError("Cannot be empty");
            return false;
        }
        return true;
    }

    private boolean isValidWeekRate(String weekRate) {
        if (weekRate.equals("")) {
            weekRateEdit.setError("Cannot be empty");
            return false;
        }
        return true;
    }

    private boolean isValidDayRate(String dayRate) {
        if (dayRate.equals("")) {
            dayRateEdit.setError("Cannot be empty");
            return false;
        }
        return true;
    }

    private boolean isValidFoodDesc(String foodDesc) {
        if (foodDesc.equals("")) {
            foodDescEdit.setError("Invalid Description");
            return false;
        }
        return true;
    }

    private boolean isValidFoodLimit(String foodLimit) {
        if (foodLimit.equals("")) {
            foodLimitEdit.setError("Invalid Limit");
            return false;
        }
        return true;
    }

    private void init(View view, LayoutInflater inflater) {
        food = false;
        accommodation = false;
        ref = new Firebase(Constants.FIREBASE_URL);
        foodRoot = (ViewGroup) view.findViewById(R.id.food_check_box_root);
        accoRoot = (ViewGroup) view.findViewById(R.id.accommodation_check_box_root);
        foodCheckBox = (CheckBox) view.findViewById(R.id.host_food_CB);
        accoCheckBox = (CheckBox) view.findViewById(R.id.host_accommodation_CB);
        foodFacility = inflater.inflate(R.layout.host_food_facility, foodRoot, false);
        accoFacility = inflater.inflate(R.layout.host_accommodation_facility, accoRoot, false);
        registerButton = (Button) view.findViewById(R.id.hostSignUp3RegisrterButton);
        sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        spe = sp.edit();
        encodedEmail = sp.getString(Constants.CURRENT_USER_ENCODED_EMAIL, "help");
        userType = sp.getString(Constants.CURRENT_USER_TYPE, null);
    }

    private void foodFacilityInit(View view) {
        foodType = "veg";
        foodTypeGroup = (RadioGroup) view.findViewById(R.id.foodType_group_Host);
        foodLimitEdit = (EditText) view.findViewById(R.id.host_foodLimitEditText);
        foodDescEdit = (EditText) view.findViewById(R.id.host_foodDescEditText);
        dayRateEdit = (EditText) view.findViewById(R.id.host_dayEditText);
        weekRateEdit = (EditText) view.findViewById(R.id.host_weekEditText);
        monthRateEdit = (EditText) view.findViewById(R.id.host_monthEditText);
        foodTypeGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.veg_HostRadio)
                    foodType = "veg";
                else if (checkedId == R.id.Non_veg_GuestRadio)
                    foodType = "non-veg";
            }
        });
    }

    private void accoFacilityInit(View view) {
        singleAccoCheck = (CheckBox) view.findViewById(R.id.host_single_accommodation);
        singleRateEdit = (EditText) view.findViewById(R.id.host_single_accommodationEditText);
        sharingAccoCheck = (CheckBox) view.findViewById(R.id.host_sharing_accommodation);
        sharingRateEdit = (EditText) view.findViewById(R.id.host_sharing_accommodationEditText);
        trialAccoCheck = (CheckBox) view.findViewById(R.id.host_trial_accommodation);
        trialRateEdit = (EditText) view.findViewById(R.id.host_trial_accommodationEditText);
        accoLimitEdit = (EditText) view.findViewById(R.id.host_accolimitEditText);
        accoDescEdit = (EditText) view.findViewById(host_accoDescEditText);
        singleAccoCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    singleRateEdit.setVisibility(View.VISIBLE);
                } else
                    singleRateEdit.setVisibility(View.GONE);
            }
        });
        sharingAccoCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    sharingRateEdit.setVisibility(View.VISIBLE);
                } else
                    sharingRateEdit.setVisibility(View.GONE);
            }
        });
        trialAccoCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    trialRateEdit.setVisibility(View.VISIBLE);
                } else
                    trialRateEdit.setVisibility(View.GONE);
            }
        });
    }
}
