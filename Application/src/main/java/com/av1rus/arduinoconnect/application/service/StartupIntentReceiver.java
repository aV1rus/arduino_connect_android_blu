package com.av1rus.arduinoconnect.application.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by nick on 4/14/14.
 */

public class StartupIntentReceiver extends BroadcastReceiver {
    final String TAG = "Startup Receiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "Boot request received");

//        Intent service = new Intent(context, MonitorService.class);
//        context.startService(service);

    }
}
