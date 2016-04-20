package com.example.amankumar.layouttest.Adapter;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.example.amankumar.layouttest.Model.AccommodationModel;
import com.example.amankumar.layouttest.R;
import com.firebase.client.Query;
import com.firebase.ui.FirebaseListAdapter;

/**
 * Created by AmanKumar on 4/13/2016.
 */
public class ActiveHostAccommodationAdapter extends FirebaseListAdapter<AccommodationModel> {
    public ActiveHostAccommodationAdapter(Activity activity, Class<AccommodationModel> modelClass, int modelLayout, Query ref) {
        super(activity, modelClass, modelLayout, ref);
    }

    @Override
    protected void populateView(View v, AccommodationModel model, int position) {
        TextView titleText = (TextView) v.findViewById(R.id.text_view_nameLV);
        TextView provideText = (TextView) v.findViewById(R.id.text_view_providesLV);
        titleText.setText(model.getUserName());
        provideText.setText(model.getDescriptionOfPlace());
    }
}
