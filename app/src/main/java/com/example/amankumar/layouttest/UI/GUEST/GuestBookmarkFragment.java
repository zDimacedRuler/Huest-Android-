package com.example.amankumar.layouttest.UI.GUEST;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.amankumar.layouttest.Adapter.BookmarkAdapter;
import com.example.amankumar.layouttest.Model.BookmarkModel;
import com.example.amankumar.layouttest.R;
import com.example.amankumar.layouttest.Util.Constants;
import com.firebase.client.Firebase;


public class GuestBookmarkFragment extends Fragment {
    Firebase ref;
    String encodedEmail;
    SharedPreferences sp;
    ListView bookmarkedLV;
    BookmarkAdapter bookmarkAdapter;
    String listId;
    public static GuestBookmarkFragment newInstance() {
        GuestBookmarkFragment fragment = new GuestBookmarkFragment();
        return fragment;
    }
    public GuestBookmarkFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_guest_bookmark, container, false);
        sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        encodedEmail = sp.getString(Constants.CURRENT_USER_ENCODED_EMAIL, "");
        ref = new Firebase(Constants.FIREBASE_GUESTS_URL).child(encodedEmail).child(Constants.LOCATION_BOOKMARK);
        bookmarkedLV = (ListView) view.findViewById(R.id.bookmarkListView);
        bookmarkAdapter = new BookmarkAdapter(getActivity(), BookmarkModel.class, R.layout.list_view, ref);
        bookmarkedLV.setAdapter(bookmarkAdapter);
        ViewCompat.setNestedScrollingEnabled(bookmarkedLV,true);
        bookmarkedLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listId = bookmarkAdapter.getRef(position).getKey();
                BookmarkModel bookmarkModel=bookmarkAdapter.getItem(position);
                Intent intent = new Intent(getActivity(), SelectedHostActivity.class);
                intent.putExtra("listId", listId);
                intent.putExtra("name",bookmarkModel.getUserName());
                startActivity(intent);
            }
        });
        bookmarkedLV.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                listId = bookmarkAdapter.getRef(position).getKey();
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
        return view;
    }
}
