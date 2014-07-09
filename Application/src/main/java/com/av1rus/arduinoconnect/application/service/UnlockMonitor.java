package com.av1rus.arduinoconnect.application.service;

import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.av1rus.arduinoconnect.arduinolib.ArduinoConnect;

/**
 * Created by nick on 4/14/14.
 */
public class UnlockMonitor extends BroadcastReceiver {
    private final String TAG = "Unlock Monitor";
    private boolean screenOff;

    public UnlockMonitor() {
        screenOff = true;
    }

    public void onReceive(Context context, Intent intent) {
//        Log.d(TAG, "Screen State change received");
//
//        if (intent.getAction().equals(Intent.ACTION_SCREEN_ON) || intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
//            screenOff = true;
//        }
//        if (intent.getAction().equals(Intent.ACTION_USER_PRESENT) || intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED) ) {
//            screenOff = false;
//        }
//
//        if(new ArduinoConnect().getSavedBluetoothDevice(context) != null){
//            Intent i = new Intent(context, ArduinoConnectService.class);
//            if(!screenOff){
//                i.putExtra("screen_state", screenOff);
//                context.startService(i);
//            }else {
//                context.stopService(i);
//            }
//        }
    }
}
