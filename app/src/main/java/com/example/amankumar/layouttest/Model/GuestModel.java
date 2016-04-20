package com.example.amankumar.layouttest.Model;

import java.util.HashMap;

/**
 * Created by AmanKumar on 4/8/2016.
 */
public class GuestModel {
    String address;
    String city;
    String firstName;
    String gender;
    String lastName;
    String mobileno;
    String occupation;
    String pincode;
    String state;
    HashMap<String,Object> bookmark;
    HashMap<String,Object> chats;

    public HashMap<String, Object> getChats() {
        return chats;
    }

    public HashMap<String, Object> getBookmark() {
        return bookmark;
    }

    public GuestModel() {
    }

    public GuestModel(String pincode, String state, String occupation, String lastName, String mobileno, String gender, String firstName, String city, String address) {
        this.pincode = pincode;
        this.state = state;
        this.occupation = occupation;
        this.lastName = lastName;
        this.mobileno = mobileno;
        this.gender = gender;
        this.firstName = firstName;
        this.city = city;
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public String getState() {
        return state;
    }

    public String getPincode() {
        return pincode;
    }

    public String getOccupation() {
        return occupation;
    }

    public String getMobileno() {
        return mobileno;
    }

    public String getLastName() {
        return lastName;
    }

    public String getGender() {
        return gender;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getCity() {
        return city;
    }
}
