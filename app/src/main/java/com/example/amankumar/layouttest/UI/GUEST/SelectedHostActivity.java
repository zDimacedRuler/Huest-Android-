package com.example.amankumar.layouttest.UI.GUEST;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.amankumar.layouttest.Model.AccommodationModel;
import com.example.amankumar.layouttest.Model.BookmarkModel;
import com.example.amankumar.layouttest.Model.ChatListModel;
import com.example.amankumar.layouttest.Model.ChatMessageModel;
import com.example.amankumar.layouttest.Model.FoodModel;
import com.example.amankumar.layouttest.Model.GuestModel;
import com.example.amankumar.layouttest.Model.HostModel;
import com.example.amankumar.layouttest.R;
import com.example.amankumar.layouttest.UI.ChatActivity;
import com.example.amankumar.layouttest.UI.ChatDetailActivity;
import com.example.amankumar.layouttest.Util.Constants;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class SelectedHostActivity extends AppCompatActivity {
    String listId, encodedEmail,name;
    Toolbar toolbar;
    Firebase ref, hostRef, hostFoodRef, hostAccoRef, bookmarkRef, guestRef;
    TextView nameText, cityText, mobileText;
    //food
    TextView foodTypeText, foodDescText, dayText, weekText, monthText;
    //accommodation
    TextView vacantText, accoDescText, singleText, sharingText, trialText;
    SharedPreferences sp;
    Boolean isBookmarked;
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_host);
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Host");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init();
        bookmarkCheck();
        hostRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HostModel hostModel = dataSnapshot.getValue(HostModel.class);
                nameText.setText(hostModel.getFirstName() + " " + hostModel.getLastname());
                cityText.setText(hostModel.getCity());
                mobileText.setText(hostModel.getMobileno());
                String isFood = hostModel.getFood();
                String isAcco = hostModel.getAccommodation();
                if (isFood.equals("true")) {
                    LinearLayout linearLayout = (LinearLayout) findViewById(R.id.SH_food);
                    linearLayout.setVisibility(View.VISIBLE);
                    hostFoodRef = new Firebase(Constants.FIREBASE_HOSTS_FOOD_URL).child(listId);
                    hostFoodRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            FoodModel foodModel = dataSnapshot.getValue(FoodModel.class);
                            foodTypeText.setText(foodModel.getFoodType());
                            foodDescText.setText(foodModel.getDescription());
                            String dayRate = foodModel.getPer_Day();
                            if (dayRate.equals("n/a"))
                                dayText.setText("Not Applicable");
                            else
                                dayText.setText(Constants.RUPPEES + dayRate);
                            String weekRate = foodModel.getPer_Week();
                            if (weekRate.equals("n/a"))
                                weekText.setText("Not Applicable");
                            else
                                weekText.setText(Constants.RUPPEES + weekRate);
                            String monthRate = foodModel.getPer_Month();
                            if (monthRate.equals("n/a"))
                                monthText.setText("Not Applicable");
                            else
                                monthText.setText(Constants.RUPPEES + monthRate);
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
                }
                if (isAcco.equals("true")) {
                    LinearLayout linearLayout = (LinearLayout) findViewById(R.id.SH_accommodation);
                    linearLayout.setVisibility(View.VISIBLE);
                    hostAccoRef = new Firebase(Constants.FIREBASE_HOSTS_ACCOMODATION_URL).child(listId);
                    hostAccoRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            AccommodationModel accommodationModel = dataSnapshot.getValue(AccommodationModel.class);
                            int limit = Integer.parseInt(accommodationModel.getLimits());
                            if (limit == 0) {
                                vacantText.setText("No");
                            } else
                                vacantText.setText("Yes");
                            accoDescText.setText(accommodationModel.getDescriptionOfPlace());
                            String singleRate = accommodationModel.getSingle();
                            if (singleRate.equals("n/a"))
                                singleText.setText("Not Applicable");
                            else
                                singleText.setText(Constants.RUPPEES + singleRate);
                            String sharingRate = accommodationModel.getSharing();
                            if (sharingRate.equals("n/a"))
                                sharingText.setText("Not Applicable");
                            else
                                sharingText.setText(Constants.RUPPEES + sharingRate);
                            String trialRate = accommodationModel.getTrial();
                            if (trialRate.equals("n/a"))
                                trialText.setText("Not Applicable");
                            else
                                trialText.setText(Constants.RUPPEES + trialRate);
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

    }

    private void init() {
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        nameText = (TextView) findViewById(R.id.SH_name);
        cityText = (TextView) findViewById(R.id.SH_city);
        mobileText = (TextView) findViewById(R.id.SH_mobile);
        //food
        foodTypeText = (TextView) findViewById(R.id.SH_foodType);
        foodDescText = (TextView) findViewById(R.id.SH_foodDesc);
        dayText = (TextView) findViewById(R.id.SH_dayRate);
        weekText = (TextView) findViewById(R.id.SH_weekRate);
        monthText = (TextView) findViewById(R.id.SH_monthRate);
        //accommodation
        vacantText = (TextView) findViewById(R.id.SH_vacancy);
        accoDescText = (TextView) findViewById(R.id.SH_accoDesc);
        singleText = (TextView) findViewById(R.id.SH_singleRate);
        sharingText = (TextView) findViewById(R.id.SH_sharingRate);
        trialText = (TextView) findViewById(R.id.SH_trialRate);

        isBookmarked = false;
        encodedEmail = sp.getString(Constants.CURRENT_USER_ENCODED_EMAIL, null);
        listId = getIntent().getStringExtra("listId");
        name=getIntent().getStringExtra("name");
        ref = new Firebase(Constants.FIREBASE_URL);
        hostRef = ref.child(Constants.LOCATION_HOSTS).child(listId);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        this.menu = menu;
        getMenuInflater().inflate(R.menu.menu_selected_host, menu);
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
        if (id == R.id.action_bookmark) {
            if (!isBookmarked) {
                isBookmarked = true;
                createBookmarkInFirebaseHelper();
                Toast.makeText(this, "Bookmark made", Toast.LENGTH_SHORT).show();
                item.setIcon(R.drawable.ic_bookmark_white_24dp);
            } else {
                isBookmarked = false;
                bookmarkRef = new Firebase(Constants.FIREBASE_GUESTS_URL).child(encodedEmail).child(Constants.LOCATION_BOOKMARK);
                bookmarkRef.child(listId).setValue(null);
                Toast.makeText(this, "Bookmark removed", Toast.LENGTH_SHORT).show();
                item.setIcon(R.drawable.ic_bookmark_border_white_24dp);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void createBookmarkInFirebaseHelper() {
        bookmarkRef = new Firebase(Constants.FIREBASE_GUESTS_URL).child(encodedEmail).child(Constants.LOCATION_BOOKMARK);
        Firebase ref = new Firebase(Constants.FIREBASE_HOSTS_URL).child(listId);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HostModel hostModel = dataSnapshot.getValue(HostModel.class);
                String name = hostModel.getFirstName() + " " + hostModel.getLastname();
                String location = hostModel.getCity();
                BookmarkModel bookmarkModel = new BookmarkModel(name, location);
                bookmarkRef.child(listId).setValue(bookmarkModel);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public void bookmarkCheck() {
        Firebase bookRef = new Firebase(Constants.FIREBASE_GUESTS_URL).child(encodedEmail);
        bookRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(Constants.LOCATION_BOOKMARK).exists()) {
                    Firebase ref = new Firebase(Constants.FIREBASE_GUESTS_URL).child(encodedEmail).child(Constants.LOCATION_BOOKMARK);
                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.child(listId).exists()) {
                                isBookmarked = true;
                                MenuItem item = menu.findItem(R.id.action_bookmark);
                                item.setIcon(R.drawable.ic_bookmark_white_24dp);
                            }
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

    }

    public void chatButtonHandler(View view) {
        Firebase isChatRef = new Firebase(Constants.FIREBASE_GUESTS_URL).child(encodedEmail);
        isChatRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(Constants.LOCATION_CHAT + "/" + listId).exists()) {
                    Intent intent = new Intent(SelectedHostActivity.this, ChatDetailActivity.class);
                    intent.putExtra("listId",listId);
                    intent.putExtra("name",name);
                    startActivity(intent);

                } else {
                    guestRef = new Firebase(Constants.FIREBASE_GUESTS_URL).child(encodedEmail);
                    guestRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            GuestModel guestModel = dataSnapshot.getValue(GuestModel.class);
                            String name = guestModel.getFirstName() + " " + guestModel.getLastName();
                            String city = guestModel.getCity();
                            ChatListModel chatListModel = new ChatListModel(name, city);
                            Firebase ref = new Firebase(Constants.FIREBASE_HOSTS_URL).child(listId).child(Constants.LOCATION_CHAT);
                            ref.child(encodedEmail).setValue(chatListModel);
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
                    Firebase hostRef = new Firebase(Constants.FIREBASE_HOSTS_URL).child(listId);
                    hostRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            HostModel hostModel = dataSnapshot.getValue(HostModel.class);
                            String name = hostModel.getFirstName() + " " + hostModel.getLastname();
                            String city = hostModel.getCity();
                            ChatListModel chatListModel = new ChatListModel(name, city);
                            Firebase ref = new Firebase(Constants.FIREBASE_GUESTS_URL).child(encodedEmail).child(Constants.LOCATION_CHAT);
                            ref.child(listId).setValue(chatListModel);
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
                    Firebase chatRef = new Firebase(Constants.FIREBASE_URL).child(Constants.LOCATION_USERS_CHATS).child(listId + "_and_" + encodedEmail);
                    String author = encodedEmail + "_to_" + listId;
                    ChatMessageModel chatMessageModel = new ChatMessageModel(author, "Hi");
                    chatRef.push().setValue(chatMessageModel);
                    Intent intent = new Intent(SelectedHostActivity.this, ChatActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }
}
