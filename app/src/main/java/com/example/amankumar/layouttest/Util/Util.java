package com.example.amankumar.layouttest.Util;

import android.content.Context;

/**
 * Created by AmanKumar on 4/5/2016.
 */
public class Util {
    private Context mContext = null;

    public Util(Context mContext) {
        this.mContext = mContext;
    }

    public static String encodeEmail(String userEmail) {
        return userEmail.replace(".", ",");
    }

    public static String decodeEmail(String userEmail) {
        return userEmail.replace(",", ".");
    }
}
