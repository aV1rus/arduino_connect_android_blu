package com.av1rus.arduinoconnect.arduinolib.service;

import android.app.Service;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.av1rus.arduinoconnect.arduinolib.ArduinoConnect;
import com.av1rus.arduinoconnect.arduinolib.IArduinoConnect;
import com.av1rus.arduinoconnect.arduinolib.exceptions.ArduinoLibraryException;
import com.av1rus.arduinoconnect.arduinolib.exceptions.BluetoothDeviceException;
import com.av1rus.arduinoconnect.arduinolib.listener.ArduinoConnectListener;
import com.av1rus.arduinoconnect.arduinolib.utils.MyDialog;
import com.av1rus.datacontract.model.LightColor;
import com.av1rus.datacontract.model.LightType;
import java.util.Set;

/**
 * Created by aV1rus on 4/13/14.
 * This service can be used to make calls to ArduinoConnect with better thread handling
 */
public class ArduinoConnectService extends Service{
    private static IArduinoConnect arduinoLib;

    @Override
    public IBinder onBind(Intent intent) {
        MyDialog.showNotification(this, 1001, "title", "BOUND", "message");
        return mBinder;
    }

    private final IBinder mBinder = new LocalBinder();
    public class LocalBinder extends Binder {
        public ArduinoConnectService getService() {
            return ArduinoConnectService.this;
        }
    }

    private static void checkLib(){
        if(arduinoLib == null){
            arduinoLib = new ArduinoConnect();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        checkLib();
        MyDialog.showNotification(this, 1000, "title", "ticker", "message");
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MyDialog.removeNotification(this, 1000);
        MyDialog.removeNotification(this, 1001);
        arduinoLib.disconnectAll(this);
        stopSelf();
    }

    public static Set<BluetoothDevice> getPairedDevices(){
        checkLib();
        return arduinoLib.getPairedDevices();
    }

    public static BluetoothDevice getConnectedDevice(){
        checkLib();
        return arduinoLib.getConnectedDevice();
    }

    public static boolean bluetoothEnabled(){
        checkLib();
        return arduinoLib.bluetoothEnabled();
    }

    public void setArduinoConnectListener(ArduinoConnectListener listener){
        arduinoLib.setArduinoConnectListener(this, listener);
    }

    public static void setBluetoothDevice(Context context, BluetoothDevice device){
        checkLib();
        arduinoLib.setBluetoothDevice(context, device);
    }

    public static BluetoothDevice getSavedBluetoothDevice(Context context){
        checkLib();
        return arduinoLib.getSavedBluetoothDevice(context);
    }

    public void startArduinoConnection() throws BluetoothDeviceException, ArduinoLibraryException{
        arduinoLib.startArduinoConnection(this);
    }

    public void stopArduinoConnection(){
        arduinoLib.stopArduinoConnection();
    }

    public static void sendMessage(String message){
        checkLib();
        arduinoLib.sendMessage(message);
    }

    public static void setLightState(LightType type, LightColor color){
        checkLib();
        arduinoLib.setLightState(type, color);
    }
}
