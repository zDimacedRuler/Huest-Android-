package com.example.amankumar.layouttest.UI.GUEST;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.amankumar.layouttest.R;


public class GuestBookmarkFragment extends Fragment {

    public static GuestBookmarkFragment newInstance() {
        GuestBookmarkFragment fragment = new GuestBookmarkFragment();
        return fragment;
    }

    public GuestBookmarkFragment() {
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
        return inflater.inflate(R.layout.fragment_guest_bookmark, container, false);
    }


}
