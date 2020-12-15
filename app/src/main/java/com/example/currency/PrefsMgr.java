package com.example.currency;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PrefsMgr {
    private static SharedPreferences sSharedPreferences;
    public static void setString(Context context, String key, String valueWaluteCode ){
        sSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sSharedPreferences.edit();
        editor.putString(key,valueWaluteCode);
        editor.commit();
    }

    public static String getString(Context context, String key){
        sSharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context);
        return sSharedPreferences.getString(key, null);

    }

}
