package com.example.amankumar.layouttest.Model;

import java.util.HashMap;

/**
 * Created by AmanKumar on 4/5/2016.
 */
public class HostModel {
    String food;
    String accommodation;
    String address;
    String city;
    String firstName;
    String lastname;
    String mobileno;
    String pincode;
    String state;
    HashMap<String,Object> chats;
    public HostModel() {
    }

    public HostModel(String address, String city, String firstName, String lastname, String mobileno, String pincode, String state) {

        this.address = address;
        this.city = city;
        this.firstName = firstName;
        this.lastname = lastname;
        this.mobileno = mobileno;
        this.pincode = pincode;
        this.state = state;
        food="false";
        accommodation="false";
    }


    public String getState() {
        return state;
    }

    public String getPincode() {
        return pincode;
    }

    public String getMobileno() {
        return mobileno;
    }


    public String getLastname() {
        return lastname;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getCity() {
        return city;
    }

    public String getAddress() {
        return address;
    }

    public String getAccommodation() {
        return accommodation;
    }

    public String getFood() {
        return food;
    }

    public HashMap<String, Object> getChats() {
        return chats;
    }
}
