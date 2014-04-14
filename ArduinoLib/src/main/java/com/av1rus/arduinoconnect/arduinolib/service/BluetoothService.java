package com.av1rus.arduinoconnect.arduinolib.service;

import android.app.Service;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.av1rus.arduinoconnect.arduinolib.bluetooth.BluetoothManager;
import com.av1rus.arduinoconnect.arduinolib.bluetooth.IBluetoothManager;
import com.av1rus.arduinoconnect.arduinolib.exceptions.ArduinoLibraryException;
import com.av1rus.arduinoconnect.arduinolib.exceptions.BluetoothDeviceException;
import com.av1rus.arduinoconnect.arduinolib.listener.BluetoothListener;
import com.av1rus.arduinoconnect.arduinolib.model.ConnectionState;

import java.lang.ref.WeakReference;
import java.util.Set;

/**
 * Created by aV1rus on 4/12/14.
 */
public class BluetoothService extends Service{
    IBluetoothManager blueManager;

    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder(this);
    }

    public class MyBinder extends Binder {
        private WeakReference<BluetoothService> mService;

        public MyBinder(BluetoothService service){
            mService = new WeakReference<BluetoothService>(service);
        }

        public BluetoothService getService(){
            return mService.get();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        blueManager = new BluetoothManager();

//        String stopservice = intent.getStringExtra("stopservice");
//        if (stopservice != null && stopservice.length() > 0) {
////            stop();
//        }
        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        blueManager.disconnectAll(this);
        stopSelf();
    }

    public synchronized Set<BluetoothDevice> getPairedDevices(){
        return blueManager.getPairedDevices();
    }

    public synchronized BluetoothDevice getConnectedDevice(){
        return blueManager.getConnectedDevice();
    }

    public synchronized boolean bluetoothEnabled(){
        return blueManager.bluetoothEnabled();
    }

    public synchronized boolean isConnected(){
        return blueManager.isConnected();
    }

    public synchronized void setBluetoothListener(BluetoothListener bluetoothListener){
        blueManager.setBluetoothListener(this, bluetoothListener);
    }

    public synchronized void sendStringToDevice(String message){
        blueManager.sendStringToDevice(message);
    }

    public synchronized void connectToDevice(BluetoothDevice device) throws BluetoothDeviceException, ArduinoLibraryException
    {
        blueManager.connectToDevice(device);
    }

    public synchronized void disconnectDevice(){
        blueManager.disconnectDevice();
    }

    public synchronized void disconnectAll(){
        blueManager.disconnectAll(this);
    }

    public synchronized void startScan(){
        blueManager.startScan();
    }

    public synchronized void stopScan(){
        blueManager.stopScan();
    }


    //Listeners
    BluetoothListener mBlueListener = new BluetoothListener() {
        @Override
        public void onConnectionStateChanged(ConnectionState state, BluetoothDevice device) {

        }

        @Override
        public void onBluetoothMessageReceived(String message) {

        }

        @Override
        public void onBluetoothStateChanged(int state) {

        }

        @Override
        public void onBluetoothIsOf() {

        }
    };
}
