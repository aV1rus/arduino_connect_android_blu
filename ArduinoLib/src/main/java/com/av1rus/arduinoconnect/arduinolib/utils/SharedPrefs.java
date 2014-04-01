package com.av1rus.arduinoconnect.arduinolib.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefs {

    public static void storeCacheValue(Activity activity, String name, int value){
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(name, value);
        editor.commit();
    }

    public static void storeCacheValue(Activity activity, String name, boolean value){
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(name, value);
        editor.commit();
    }

    public static void storeCacheValue(Activity activity, String name, String value){
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(name, value);
        editor.commit();
    }

    public static int getCachedInt(Activity activity, String name){
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        return sharedPref.getInt(name, 0);
    }

    public static boolean getCachedBoolean(Activity activity, String name){
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        return sharedPref.getBoolean(name, false);
    }

    public static String getCachedString(Activity activity, String name, String defaultValue){
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        return sharedPref.getString(name, defaultValue);
    }

}
