package com.example.amankumar.layouttest.Model;

/**
 * Created by AmanKumar on 4/18/2016.
 */
public class ChatListModel {
    String username;
    String city;

    public ChatListModel() {
    }

    public ChatListModel(String username, String city) {
        this.username = username;
        this.city = city;
    }

    public String getUsername() {
        return username;
    }

    public String getCity() {
        return city;
    }
}
