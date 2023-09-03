package com.applications.divarapp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import java.util.HashMap;
import java.util.Map;

// Shared Preference of application
public class SPreferences {

    public static Map<String,Object> hasKey(String key, Context context){
        Map result = new HashMap();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        // Check this key is exist
        result.put("1", preferences.contains(key));
        // Return assigned value for this key. If not exist key returning null
        result.put("2",preferences.getAll().get(key));
        return result;
    }
    // Set key and value to shared preferences
    public static void setDefaults(String key, String value, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply(); // or editor.commit() in case you want to write data instantly
    }
    // Get value from key in shared preferences
    public static String getDefaults(String key, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, null);
    }
    // Get value from key in shared preferences
    public static void logoutUser(String key, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(key);
        editor.apply(); // or editor.commit() in case you want to write data instantly
    }
}