package com.av1rus.arduinoconnect.application.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.IBinder;
import android.util.Log;

import com.av1rus.arduinoconnect.application.R;
import com.av1rus.arduinoconnect.application.ui.activity.MainActivity;
import com.av1rus.arduinoconnect.arduinolib.ArduinoConnect;
import com.av1rus.arduinoconnect.arduinolib.utils.SharedPrefs;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by nick on 4/14/14.
 */
public class MonitorService extends Service {
    //@Override
    //public void onStart(Intent intent, int startId) {
    //}

    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }
    private static final Class<?>[] mSetForegroundSignature = new Class[] {
            boolean.class};
    private static final Class<?>[] mStartForegroundSignature = new Class[] {
            int.class, Notification.class};
    private static final Class<?>[] mStopForegroundSignature = new Class[] {
            boolean.class};


    private final String TAG = "MONITORSERVICE";
    BroadcastReceiver mReceiver = null;
    BroadcastReceiver mReceiver2 = null;

    private NotificationManager mNM;
    private Method mSetForeground;
    private Method mStartForeground;
    private Method mStopForeground;
    private Object[] mSetForegroundArgs = new Object[1];
    private Object[] mStartForegroundArgs = new Object[2];
    private Object[] mStopForegroundArgs = new Object[1];


    void invokeMethod(Method method, Object[] args) {
        try {
            method.invoke(this, args);
        } catch (InvocationTargetException e) {
            // Should not happen.
            Log.w("ApiDemos", "Unable to invoke method", e);
        } catch (IllegalAccessException e) {
            // Should not happen.
            Log.w("ApiDemos", "Unable to invoke method", e);
        }
    }

    /**
     * This is a wrapper around the new startForeground method, using the older
     * APIs if it is not available.
     */
    void startForegroundCompat(int id, Notification notification) {
        // If we have the new startForeground API, then use it.
        if (mStartForeground != null) {
            mStartForegroundArgs[0] = Integer.valueOf(id);
            mStartForegroundArgs[1] = notification;
            invokeMethod(mStartForeground, mStartForegroundArgs);
            return;
        }


        // Fall back on the old API.
        mSetForegroundArgs[0] = Boolean.TRUE;
        invokeMethod(mSetForeground, mSetForegroundArgs);
        mNM.notify(id, notification);
    }

    /**
     * This is a wrapper around the new stopForeground method, using the older
     * APIs if it is not available.
     */
    void stopForegroundCompat(int id) {
        // If we have the new stopForeground API, then use it.
        if (mStopForeground != null) {
            mStopForegroundArgs[0] = Boolean.TRUE;
            invokeMethod(mStopForeground, mStopForegroundArgs);
            return;
        }

        // Fall back on the old API.  Note to cancel BEFORE changing the
        // foreground state, since we could be killed at that point.
        mNM.cancel(id);
        mSetForegroundArgs[0] = Boolean.FALSE;
        invokeMethod(mSetForeground, mSetForegroundArgs);
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "Starting monitor service");
        ArduinoConnect connect = new ArduinoConnect();

        BluetoothDevice bd = connect.getSavedBluetoothDevice(this);
        //Toast.makeText(this, "Service Created", Toast.LENGTH_LONG).show();
        if(bd != null){
            if(mReceiver == null){
                IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
                filter.addAction(Intent.ACTION_SCREEN_OFF);
                filter.addAction(Intent.ACTION_USER_PRESENT);
                mReceiver = new UnlockMonitor();
                registerReceiver(mReceiver, filter);
            }
//            if(mReceiver2 == null){
//                IntentFilter filter = new IntentFilter();
//                filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
//                filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED);
//                filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
//
//                mReceiver2 = new BluetoothReceiver();
//                registerReceiver(mReceiver2, filter);
//            }

            mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

            if(!SharedPrefs.getCachedBoolean(this, ""))
                startForeground(100, getNotification());

        }
    }
    @Override
    public void onDestroy() {
        // Make sure our notification is gone.
        //mNM.cancelAll();
        //Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
        unregisterReceiver(mReceiver);
        unregisterReceiver(mReceiver2);
        //stopForegroundCompat(Utils.NOTIFICATION_ID);
    }

    public Notification getNotification(){
        Context ctx = getApplicationContext();
        Intent notificationIntent = new Intent(ctx, MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent contentIntent = PendingIntent.getActivity(ctx, 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        Resources res = ctx.getResources();
        String title = res.getString(R.string.notif_title);
        String ticker = res.getString(R.string.notif_ticker);
        String content = res.getString(R.string.notif_content);
        Notification notification = new Notification(R.drawable.ic_launcher, ticker, System.currentTimeMillis());
        //notification.defaults = Notification.DEFAULT_SOUND;
        notification.setLatestEventInfo(this, title, content, contentIntent);
        notification.flags |= Notification.FLAG_ONLY_ALERT_ONCE | Notification.FLAG_AUTO_CANCEL;

        return notification;
    }

    @Override
    public void onStart(Intent intent, int startId) {

        super.onStart(intent, startId);

        //Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();

    }



}
