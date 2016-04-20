package com.example.amankumar.layouttest.Adapter;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.example.amankumar.layouttest.Model.FoodModel;
import com.example.amankumar.layouttest.R;
import com.firebase.client.Query;
import com.firebase.ui.FirebaseListAdapter;

/**
 * Created by AmanKumar on 4/5/2016.
 */
public class ActiveHostFoodAdapter extends FirebaseListAdapter<FoodModel> {

    public ActiveHostFoodAdapter(Activity activity, Class<FoodModel> modelClass, int modelLayout, Query ref) {
        super(activity, modelClass, modelLayout, ref);
    }

    @Override
    protected void populateView(View v,FoodModel model) {
        TextView titleText = (TextView) v.findViewById(R.id.host_name_LV);
        TextView provideText = (TextView) v.findViewById(R.id.host_foodDescLV);
        TextView foodTypeText= (TextView) v.findViewById(R.id.host_foodTypeLV);
        titleText.setText(model.getUserName());
        provideText.setText(model.getDescription());
        foodTypeText.setText(model.getFoodType());
    }
}
