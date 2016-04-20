package com.example.amankumar.layouttest.Model;

/**
 * Created by AmanKumar on 4/5/2016.
 */
public class FoodModel {
    String userName;
    String userCity;
    String foodType;
    String description;
    String limits;
    String per_Day;
    String per_Month;
    String per_Week;

    public FoodModel() {
    }


    public FoodModel(String userName,String userCity, String foodType, String limits, String description, String per_Day, String per_Month, String per_Week) {
        this.userName = userName;
        this.userCity=userCity;
        this.foodType = foodType;
        this.limits = limits;
        this.description = description;
        this.per_Day = per_Day;
        this.per_Month = per_Month;
        this.per_Week = per_Week;
    }

    public String getFoodType() {
        return foodType;
    }

    public String getPer_Week() {
        return per_Week;
    }

    public String getPer_Month() {
        return per_Month;
    }

    public String getPer_Day() {
        return per_Day;
    }

    public String getDescription() {
        return description;
    }

    public String getLimits() {
        return limits;
    }

    public String getUserName() {
        return userName;
    }
    public String getUserCity() {
        return userCity;
    }
}
