package com.example.amankumar.layouttest.UI;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.amankumar.layouttest.Adapter.ChatListAdapter;
import com.example.amankumar.layouttest.Model.ChatListModel;
import com.example.amankumar.layouttest.R;
import com.example.amankumar.layouttest.Util.Constants;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class ChatActivity extends AppCompatActivity {
    String userType, encodedEmail, listId;
    SharedPreferences sp;
    Toolbar toolbar;
    ListView listView;
    Firebase chatRef, userRef;
    ChatListAdapter chatListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        toolbar = (Toolbar) findViewById(R.id.appBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Chats(beta)");
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        userType = sp.getString(Constants.CURRENT_USER_TYPE, "");
        encodedEmail = sp.getString(Constants.CURRENT_USER_ENCODED_EMAIL, "");
        listView = (ListView) findViewById(R.id.CA_list_view);
        userRef = new Firebase(Constants.FIREBASE_URL).child(userType).child(encodedEmail);
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(Constants.LOCATION_CHAT).exists()) {
                    chatRef = new Firebase(Constants.FIREBASE_URL).child(userType).child(encodedEmail).child(Constants.LOCATION_CHAT);
                    chatListAdapter = new ChatListAdapter(ChatActivity.this, ChatListModel.class, R.layout.list_view, chatRef);
                    listView.setAdapter(chatListAdapter);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                listId = chatListAdapter.getRef(position).getKey();
                AlertDialog.Builder builder = new AlertDialog.Builder(ChatActivity.this);
                builder.setMessage("Remove Chat?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Firebase ref = new Firebase(Constants.FIREBASE_URL).child(userType).child(encodedEmail).child(Constants.LOCATION_CHAT);
                        ref.child(listId).setValue(null);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
                return true;
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listId = chatListAdapter.getRef(position).getKey();
                ChatListModel chatListModel = chatListAdapter.getItem(position);
                Intent intent = new Intent(ChatActivity.this, ChatDetailActivity.class);
                intent.putExtra("listId", listId);
                intent.putExtra("name", chatListModel.getUsername());
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
