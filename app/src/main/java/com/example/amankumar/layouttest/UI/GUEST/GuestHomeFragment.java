package com.example.amankumar.layouttest.UI.GUEST;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.amankumar.layouttest.R;


public class GuestHomeFragment extends Fragment {


    public static GuestHomeFragment newInstance() {
        GuestHomeFragment fragment = new GuestHomeFragment();
        return fragment;
    }

    public GuestHomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_guest_home, container, false);
    }
}
