package com.example.amankumar.layouttest.UI.GUEST;

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

import com.example.amankumar.layouttest.Adapter.BookmarkAdapter;
import com.example.amankumar.layouttest.Model.BookmarkModel;
import com.example.amankumar.layouttest.R;
import com.example.amankumar.layouttest.Util.Constants;
import com.firebase.client.Firebase;

public class BookmarkActivity extends AppCompatActivity {
    Toolbar toolbar;
    String encodedEmail;
    SharedPreferences sp;
    ListView bookmarkedLV;
    Firebase ref;
    BookmarkAdapter bookmarkAdapter;
    String listId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Bookmarked");
        init();
        bookmarkAdapter = new BookmarkAdapter(this, BookmarkModel.class, R.layout.list_view, ref);
        bookmarkedLV.setAdapter(bookmarkAdapter);
        bookmarkedLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listId = bookmarkAdapter.getRef(position).getKey();
                BookmarkModel bookmarkModel=bookmarkAdapter.getItem(position);
                Intent intent = new Intent(BookmarkActivity.this, SelectedHostActivity.class);
                intent.putExtra("listId", listId);
                intent.putExtra("name",bookmarkModel.getUserName());
                startActivity(intent);
            }
        });
        bookmarkedLV.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                listId = bookmarkAdapter.getRef(position).getKey();
                AlertDialog.Builder builder = new AlertDialog.Builder(BookmarkActivity.this);
                builder.setMessage("Remove bookmark?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Firebase ref=new Firebase(Constants.FIREBASE_GUESTS_URL).child(encodedEmail).child(Constants.LOCATION_BOOKMARK);
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
    }

    private void init() {
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        encodedEmail = sp.getString(Constants.CURRENT_USER_ENCODED_EMAIL, "");
        bookmarkedLV = (ListView) findViewById(R.id.bookmarkListView);
        ref = new Firebase(Constants.FIREBASE_GUESTS_URL).child(encodedEmail).child(Constants.LOCATION_BOOKMARK);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_bookmark, menu);
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
