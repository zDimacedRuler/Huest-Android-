package com.example.amankumar.layouttest.Model;

/**
 * Created by AmanKumar on 4/5/2016.
 */
public class AccommodationModel {
    String userName;
    String userCity;
    String descriptionOfPlace;
    String limits;
    String sharing;
    String single;
    String trial;

    public AccommodationModel() {
    }

    public AccommodationModel(String userName, String userCity, String descriptionOfPlace, String limits, String sharing, String single, String trial) {
        this.userName = userName;
        this.userCity=userCity;
        this.descriptionOfPlace = descriptionOfPlace;
        this.limits = limits;
        this.sharing = sharing;
        this.single = single;
        this.trial = trial;
    }

    public String getUserCity() {
        return userCity;
    }

    public String getDescriptionOfPlace() {
        return descriptionOfPlace;
    }

    public String getTrial() {
        return trial;
    }

    public String getSingle() {
        return single;
    }

    public String getSharing() {
        return sharing;
    }

    public String getLimits() {
        return limits;
    }

    public String getUserName() {
        return userName;
    }
}
