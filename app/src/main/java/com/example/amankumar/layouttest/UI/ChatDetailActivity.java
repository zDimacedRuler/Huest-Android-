package com.example.amankumar.layouttest.UI;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.example.amankumar.layouttest.Adapter.ChatMessageAdapter;
import com.example.amankumar.layouttest.Model.ChatMessageModel;
import com.example.amankumar.layouttest.R;
import com.example.amankumar.layouttest.Util.Constants;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

public class ChatDetailActivity extends AppCompatActivity {
    Toolbar toolbar;
    Boolean newItems;
    SharedPreferences sp;
    Firebase ref, userRef;
    ListView chatDetailLV;
    String listId, encodedEmail, chat, userType, name;
    ChatMessageAdapter chatMessageAdapter;
    EditText chatMessageEdit;
    NotificationCompat.Builder notification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_detail);
        toolbar = (Toolbar) findViewById(R.id.appBar);
        setSupportActionBar(toolbar);
        /*getSupportActionBar().setTitle("CHAT");*/
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        notification = new NotificationCompat.Builder(this);
        encodedEmail = sp.getString(Constants.CURRENT_USER_ENCODED_EMAIL, "");
        userType = sp.getString(Constants.CURRENT_USER_TYPE, "");
        listId = getIntent().getStringExtra("listId");
        name = getIntent().getStringExtra("name");
        getSupportActionBar().setTitle(name);
        chatDetailLV = (ListView) findViewById(R.id.CD_list_view);
        userRef = new Firebase(Constants.FIREBASE_URL).child(userType).child(encodedEmail);
        chatMessageEdit = (EditText) findViewById(R.id.chatMessage_editText);
        if (userType.equals(Constants.LOCATION_GUESTS))
            chat = listId + Constants.AND + encodedEmail;
        else
            chat = encodedEmail + Constants.AND + listId;
        newItems = false;
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(Constants.LOCATION_CHAT).exists()) {
                    Firebase ref = new Firebase(Constants.FIREBASE_URL).child(Constants.LOCATION_USERS_CHATS).child(chat);
                    Query query = ref.limitToLast(1);
                    query.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            notification.setSmallIcon(R.drawable.app);
                            notification.setContentTitle("New Message");
                            notification.setContentText(name + " messaged you");
                            notification.setOngoing(false);
                            Intent intent = new Intent(ChatDetailActivity.this, ChatActivity.class);
                            PendingIntent pendingIntent = PendingIntent.getActivity(ChatDetailActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                            notification.setContentIntent(pendingIntent);
                            Notification notice = notification.build();
                            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                            if (!hasWindowFocus())
                                notificationManager.notify(1001, notice);
                        }

                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

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
        ref = new Firebase(Constants.FIREBASE_URL).child(Constants.LOCATION_USERS_CHATS).child(chat);
        chatMessageAdapter = new ChatMessageAdapter(this, ChatMessageModel.class, R.layout.message_list_view, ref);
        chatDetailLV.setAdapter(chatMessageAdapter);
        chatDetailLV.setDivider(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chat_detail, menu);
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


    public void sendMessageHandler(View view) {
        String message = chatMessageEdit.getText().toString();
        if (message.equals(""))
            return;
        Firebase messageRef = new Firebase(Constants.FIREBASE_URL).child(Constants.LOCATION_USERS_CHATS).child(chat);
        String author = encodedEmail + Constants.TO + listId;
        ChatMessageModel chatMessageModel = new ChatMessageModel(author, message);
        messageRef.push().setValue(chatMessageModel);
        chatMessageEdit.setText("");
    }
}
