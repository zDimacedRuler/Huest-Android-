package com.example.amankumar.layouttest.Adapter;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.example.amankumar.layouttest.Model.ChatListModel;
import com.example.amankumar.layouttest.R;
import com.firebase.client.Firebase;
import com.firebase.ui.FirebaseListAdapter;

/**
 * Created by AmanKumar on 4/18/2016.
 */
public class ChatListAdapter extends FirebaseListAdapter<ChatListModel> {
    public ChatListAdapter(Activity activity, Class<ChatListModel> modelClass, int modelLayout, Firebase ref) {
        super(activity, modelClass, modelLayout, ref);
    }

    @Override
    protected void populateView(View v, ChatListModel model, int position) {
        TextView nameText = (TextView) v.findViewById(R.id.text_view_nameLV);
        TextView locationText = (TextView) v.findViewById(R.id.text_view_providesLV);
        nameText.setText(model.getUsername());
        locationText.setText(model.getCity());
    }
}
