package com.example.amankumar.layouttest.Util;

/**
 * Created by AmanKumar on 4/5/2016.
 */
public class Constants {
    //url
    public static final String FIREBASE_URL="https://huest.firebaseio.com/";
    //nodes
    public static final String LOCATION_HOSTS="HOSTS";
    public static final String LOCATION_GUESTS="GUESTS";
    public static final String LOCATION_USERS="USERS";
    public static final String LOCATION_FOOD="Food";
    public static final String LOCATION_ACCOMMODATION="Accommodation";
    public static final String LOCATION_HOST_FOOD="HOST_FOOD";
    public static final String LOCATION_HOST_ACCOMMODATION="HOST_ACCOMMODATION";
    public static final String LOCATION_BOOKMARK="bookmark";
    public static final String LOCATION_CHAT="chats";
    public static final String LOCATION_USERS_CHATS="USER_CHATS";
    //node url
    public static final String FIREBASE_HOSTS_URL=FIREBASE_URL+LOCATION_HOSTS;
    public static final String FIREBASE_USERS_URL=FIREBASE_URL+LOCATION_USERS;
    public static final String FIREBASE_GUESTS_URL=FIREBASE_URL+LOCATION_GUESTS;
    public static final String FIREBASE_FOOD_URL=FIREBASE_URL+LOCATION_FOOD;
    public static final String FIREBASE_ACCOMMODATION_URL=FIREBASE_URL+LOCATION_ACCOMMODATION;
    public static final String FIREBASE_HOSTS_FOOD_URL=FIREBASE_URL+LOCATION_HOST_FOOD;
    public static final String FIREBASE_HOSTS_ACCOMODATION_URL=FIREBASE_URL+LOCATION_HOST_ACCOMMODATION;
    public static final String FIREBASE_CHATS_URL=FIREBASE_URL+LOCATION_CHAT;
    //Current User
    public static final String CURRENT_USER_ENCODED_EMAIL="currentUser";
    public static final String CURRENT_USER_TYPE="userType";
    public static final String CURRENT_USER_NAME="username";
    public static final String CURRENT_USER_PLACE="city";
    //Bundle
    public static final String SEARCH_BUNDLE_KEY="bundleKey";
    public static final String SEARCH_BUNDLE_LOCATION=SEARCH_BUNDLE_KEY+"location";
    public static final String SEARCH_BUNDLE_FACILITY=SEARCH_BUNDLE_KEY+"facility";
    //search food and accommodation
    public static final String SEARCHED_FOOD="storedFood";
    public static final String SEARCHED_ACCOMMODATION="storedAccommodation";

    public static final String SIGNUP_EMAIL="signupEmail";

    public static final String HOSTMODEL_FOOD="food";
    public static final String HOSTMODEL_ACCOMMODATION="accommodation";

    public static final String RUPPEES="INR ";

    public static final String AND="_and_";

    public static final String TO="_to_";


}
