package com.av1rus.arduinoconnect.arduinolib.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefs {

    private static final String PREFS_NAME = "com.av1rus.arduinoconnect.sharedprefs";

    public static void storeCacheValue(Context context, String name, int value){
        SharedPreferences sharedPref = context.getSharedPreferences(PREFS_NAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(name, value);
        editor.commit();
    }

    public static void storeCacheValue(Context context, String name, boolean value){
        SharedPreferences sharedPref = context.getSharedPreferences(PREFS_NAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(name, value);
        editor.commit();
    }

    public static void storeCacheValue(Context context, String name, String value){
        SharedPreferences sharedPref = context.getSharedPreferences(PREFS_NAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(name, value);
        editor.commit();
    }

    public static int getCachedInt(Context context, String name){
        SharedPreferences sharedPref = context.getSharedPreferences(PREFS_NAME, Activity.MODE_PRIVATE);
        return sharedPref.getInt(name, 0);
    }

    public static boolean getCachedBoolean(Context context, String name){
        SharedPreferences sharedPref = context.getSharedPreferences(PREFS_NAME, Activity.MODE_PRIVATE);
        return sharedPref.getBoolean(name, false);
    }

    public static String getCachedString(Context context, String name, String defaultValue){
        SharedPreferences sharedPref = context.getSharedPreferences(PREFS_NAME, Activity.MODE_PRIVATE);
        return sharedPref.getString(name, defaultValue);
    }

}
