package com.example.amankumar.layouttest.UI.GUEST;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RadioGroup;

import com.example.amankumar.layouttest.R;
import com.example.amankumar.layouttest.Util.Constants;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


public class GuestHomeFragment extends Fragment {
    SharedPreferences sp;
    SharedPreferences.Editor spe;
    AutoCompleteTextView locationEdit;
    String locationString;
    String facility;
    RadioGroup facilityGroup;
    Button searchButton;
    ArrayAdapter<String> adapter;
    Set<String> cities;
    ArrayList<String> recentCities;

    public static GuestHomeFragment newInstance() {
        GuestHomeFragment fragment = new GuestHomeFragment();
        return fragment;
    }

    public GuestHomeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_guest_home, container, false);
        facility = Constants.HOSTMODEL_FOOD;
        sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        spe = sp.edit();
        cities = sp.getStringSet(Constants.RECENT_CITIES, new HashSet<String>());
        recentCities = new ArrayList<>(cities);
        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_dropdown_item_1line, recentCities);
        facilityGroup = (RadioGroup) view.findViewById(R.id.Select_facility_guest);
        facilityGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.Food_GuestRadio) {
                    facility = Constants.HOSTMODEL_FOOD;
                } else if (checkedId == R.id.Acco_GuestRadio) {
                    facility = Constants.HOSTMODEL_ACCOMMODATION;
                }
            }
        });
        locationEdit = (AutoCompleteTextView) view.findViewById(R.id.location_EditText);
        locationEdit.setAdapter(adapter);
        searchButton = (Button) view.findViewById(R.id.searchHostButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locationString = String.valueOf(locationEdit.getText()).trim().toLowerCase();
                boolean validLocation = isValidLocation(locationString);
                if (!validLocation)
                    return;
                recentCities.add(locationString);
                cities.addAll(recentCities);
                spe.putStringSet(Constants.RECENT_CITIES, cities).apply();
                Bundle bundle = new Bundle();
                bundle.putString(Constants.SEARCH_BUNDLE_LOCATION, locationString);
                bundle.putString(Constants.SEARCH_BUNDLE_FACILITY, facility);
                Intent intent = new Intent(getActivity(), GuestSearchActivity.class);
                intent.putExtra(Constants.SEARCH_BUNDLE_KEY, bundle);
                startActivity(intent);
            }
        });
        return view;
    }

    private boolean isValidLocation(String locationString) {
        if (locationString.equals("")) {
            locationEdit.setError("Cannot be empty");
            return false;
        }
        return true;
    }

}
