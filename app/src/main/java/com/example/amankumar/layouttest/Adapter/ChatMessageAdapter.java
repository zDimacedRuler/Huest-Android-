package com.example.amankumar.layouttest.Adapter;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;

import com.example.amankumar.layouttest.Model.ChatMessageModel;
import com.example.amankumar.layouttest.R;
import com.example.amankumar.layouttest.Util.Constants;
import com.firebase.client.Firebase;
import com.firebase.ui.FirebaseListAdapter;

/**
 * Created by AmanKumar on 4/18/2016.
 */
public class ChatMessageAdapter extends FirebaseListAdapter<ChatMessageModel> {
    Activity context;
    SharedPreferences sp;
    String encodedEmail;
    public ChatMessageAdapter(Activity activity, Class<ChatMessageModel> modelClass, int modelLayout, Firebase ref) {
        super(activity, modelClass, modelLayout, ref);
        context=activity;
    }

    @Override
    protected void populateView(View v, ChatMessageModel model, int position) {
        sp= PreferenceManager.getDefaultSharedPreferences(context);
        encodedEmail=sp.getString(Constants.CURRENT_USER_ENCODED_EMAIL,"");
        TextView leftMessageText = (TextView) v.findViewById(R.id.leftmessageText_LV);
        TextView rightMessageText = (TextView) v.findViewById(R.id.rightmessageText_LV);
        String author=model.getAuthor();
        if(author.startsWith(encodedEmail)){
            rightMessageText.setVisibility(View.VISIBLE);
            rightMessageText.setText(model.getMessage());
            leftMessageText.setVisibility(View.GONE);
        }
        else{
            leftMessageText.setVisibility(View.VISIBLE);
            leftMessageText.setText(model.getMessage());
            rightMessageText.setVisibility(View.GONE);
        }
    }
}
