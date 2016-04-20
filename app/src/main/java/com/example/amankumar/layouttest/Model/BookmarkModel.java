package com.example.amankumar.layouttest.Model;

/**
 * Created by AmanKumar on 4/17/2016.
 */
public class BookmarkModel {
    String userName;
    String location;

    public BookmarkModel() {
    }

    public BookmarkModel(String userName, String location) {
        this.userName = userName;
        this.location = location;
    }

    public String getUserName() {
        return userName;
    }

    public String getLocation() {
        return location;
    }
}
