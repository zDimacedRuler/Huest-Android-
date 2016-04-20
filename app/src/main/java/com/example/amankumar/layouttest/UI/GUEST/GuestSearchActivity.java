package com.example.amankumar.layouttest.UI.GUEST;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.amankumar.layouttest.Adapter.ActiveHostAccommodationAdapter;
import com.example.amankumar.layouttest.Adapter.ActiveHostFoodAdapter;
import com.example.amankumar.layouttest.Model.AccommodationModel;
import com.example.amankumar.layouttest.Model.FoodModel;
import com.example.amankumar.layouttest.R;
import com.example.amankumar.layouttest.Util.Constants;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

public class GuestSearchActivity extends AppCompatActivity {
    String location, facility;
    Toolbar toolbar;
    Firebase ref, accommodationRef, foodRef;
    Query query;
    ActiveHostFoodAdapter foodAdapter;
    ActiveHostAccommodationAdapter accommodationAdapter;
    ListView searchList;
    EditText searchEdit;
    TextView sorryText, naText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_search);
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Guest");
        init();
        facilityInit(location);
    }

    private void facilityInit(String location) {
        if (facility.equals(Constants.HOSTMODEL_FOOD)) {
            foodRef = new Firebase(Constants.FIREBASE_HOSTS_FOOD_URL);
            query = foodRef.orderByChild("userCity").equalTo(location);
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (!dataSnapshot.exists()) {
                        sorryText.setVisibility(View.VISIBLE);
                        naText.setVisibility(View.VISIBLE);
                    } else {
                        sorryText.setVisibility(View.GONE);
                        naText.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
            foodAdapter = new ActiveHostFoodAdapter(this, FoodModel.class, R.layout.food_list_view, query);
            searchList.setAdapter(foodAdapter);
            searchList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String listId = foodAdapter.getRef(position).getKey();
                    FoodModel foodModel=foodAdapter.getItem(position);
                    Intent intent = new Intent(GuestSearchActivity.this, SelectedHostActivity.class);
                    intent.putExtra("listId", listId);
                    intent.putExtra("name",foodModel.getUserName());
                    startActivity(intent);
                }
            });
        } else if (facility.equals(Constants.HOSTMODEL_ACCOMMODATION)) {
            accommodationRef = new Firebase(Constants.FIREBASE_HOSTS_ACCOMODATION_URL);
            query = accommodationRef.orderByChild("userCity").equalTo(location);
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (!dataSnapshot.exists()) {
                        sorryText.setVisibility(View.VISIBLE);
                        naText.setVisibility(View.VISIBLE);
                    } else {
                        sorryText.setVisibility(View.GONE);
                        naText.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
            accommodationAdapter = new ActiveHostAccommodationAdapter(this, AccommodationModel.class, R.layout.list_view, query);
            searchList.setAdapter(accommodationAdapter);
            searchList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String listId = accommodationAdapter.getRef(position).getKey();
                    AccommodationModel model=accommodationAdapter.getItem(position);
                    Intent intent = new Intent(GuestSearchActivity.this, SelectedHostActivity.class);
                    intent.putExtra("listId", listId);
                    intent.putExtra("name",model.getUserName());
                    startActivity(intent);
                }
            });
        }
    }

    private void init() {
        searchList = (ListView) findViewById(R.id.searchListView);
        searchEdit = (EditText) findViewById(R.id.GS_searchEdit);
        sorryText = (TextView) findViewById(R.id.GS_sorry);
        naText = (TextView) findViewById(R.id.GS_na);
        Bundle bundle = getIntent().getBundleExtra(Constants.SEARCH_BUNDLE_KEY);
        location = bundle.getString(Constants.SEARCH_BUNDLE_LOCATION);
        facility = bundle.getString(Constants.SEARCH_BUNDLE_FACILITY);
        searchEdit.setText(location);
        searchEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || event.getAction() == KeyEvent.ACTION_DOWN) {
                    String newLocation = String.valueOf(searchEdit.getText()).toLowerCase();
                    if (newLocation.equals(""))
                        return false;
                    facilityInit(newLocation);
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_guest_search, menu);
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

    @Override
    protected void onPause() {
        super.onPause();
        if (foodAdapter != null)
            foodAdapter.cleanup();
        if (accommodationAdapter != null)
            accommodationAdapter.cleanup();
    }
}
