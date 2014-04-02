package com.av1rus.arduinoconnect.application.app;

import android.app.Application;

import com.av1rus.arduinoconnect.arduinolib.ArduinoConnect;

/**
 * Created by aV1rus on 4/1/14.
 */
public class ArduinoConnectApp extends Application {

    private static ArduinoConnectApp mAppInstance;
    private static ArduinoConnect mArduinoConnectLibrary;

    public ArduinoConnectApp(){
        mAppInstance = this;
    }

    @Override
    public void onCreate(){
        super.onCreate();
    }

    public static ArduinoConnectApp getApp(){
        if(mAppInstance == null){
            mAppInstance = new ArduinoConnectApp();
        }

        return mAppInstance;
    }

    public void setArduinoLibrary(ArduinoConnect lib){
        mArduinoConnectLibrary = lib;
    }

    public ArduinoConnect getArduinoLibrary(){
        if(mArduinoConnectLibrary == null){
            setArduinoLibrary(new ArduinoConnect());
        }

        return mArduinoConnectLibrary;
    }
}
