package com.av1rus.arduinoconnect.arduinolib;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;

import com.av1rus.arduinoconnect.arduinolib.exceptions.ArduinoLibraryException;
import com.av1rus.arduinoconnect.arduinolib.listener.ArduinoConnectListener;
import com.av1rus.arduinoconnect.arduinolib.listener.BluetoothListener;
import com.av1rus.arduinoconnect.arduinolib.bluetooth.BluetoothManager;
import com.av1rus.arduinoconnect.arduinolib.bluetooth.IBluetoothManager;
import com.av1rus.arduinoconnect.arduinolib.exceptions.BluetoothDeviceException;
import com.av1rus.arduinoconnect.arduinolib.model.ConnectionState;
import com.av1rus.arduinoconnect.arduinolib.utils.SharedPrefs;

import java.util.Set;

/**
 * Created by nick on 4/1/14.
 */
public class ArduinoConnect implements IArduinoConnect{

    IBluetoothManager blueManager;
    ArduinoConnectListener mListener;
    BluetoothDevice mBluetoothDevice;

    public ArduinoConnect(){
        if(blueManager == null){
            blueManager = new BluetoothManager();
        }
    }



    @Override
    public Set<BluetoothDevice> getPairedDevices() {
        return blueManager.getPairedDevices();
    }

    @Override
    public BluetoothDevice getConnectedDevice() {
        return blueManager.getConnectedDevice();
    }

    @Override
    public boolean bluetoothEnabled() {
        return blueManager.bluetoothEnabled();
    }

    @Override
    public void setArduinoConnectListener(Context context, ArduinoConnectListener listener) {
        mListener = listener;
        blueManager.setBluetoothListener(context, mBlueListener);
    }

    @Override
    public void setBluetoothDevice(Activity activity, BluetoothDevice device) {
        mBluetoothDevice = device;
        SharedPrefs.storeCacheValue(activity, "defaultBluetooth", device.getAddress());
    }

    @Override
    public void stopArduinoConnection() {

    }

    @Override
    public void sendMessage(String message) {
        blueManager.sendStringToDevice(message);
    }

    @Override
    public BluetoothDevice getSavedBluetoothDevice(Activity activity){
        String s = SharedPrefs.getCachedString(activity, "defaultBluetooth", null);
        if(s != null){
            try{
                return BluetoothAdapter.getDefaultAdapter().getRemoteDevice(s);
            }catch(Exception e){
                setBluetoothDevice(activity, null);
            }
        }

        return null;
    }

    @Override
    public void startArduinoConnection(Activity activity) throws BluetoothDeviceException, ArduinoLibraryException {
        mBluetoothDevice = getSavedBluetoothDevice(activity);

        if(!bluetoothEnabled()){
            throw new BluetoothDeviceException(BluetoothDeviceException.ExceptionCause.BLUETOOTH_NOT_ENABLED, "BluetoothDevice was never set... Method setBluetoothDevice must be called at any time before this.");
        }

        if(mBluetoothDevice == null){
            throw new BluetoothDeviceException(BluetoothDeviceException.ExceptionCause.DEVICE_NOT_SET, "You must set the bluetooth");
        }

        if(mListener == null){
            throw new ArduinoLibraryException(ArduinoLibraryException.ExceptionCause.LISTENER_NOT_SET, "You must set the listener before starting connection");
        }

        blueManager.disconnectDevice();
        blueManager.connectToDevice(mBluetoothDevice);
    }

    private void processReceivedMessage(String message){
        mListener.onBluetoothMessageReceived(message);
    }

    //LISTENER
    BluetoothListener mBlueListener = new BluetoothListener() {
        @Override
        public void onConnectionStateChanged(ConnectionState state, BluetoothDevice device) {
            mListener.onConnectionStateChanged(state, device);
        }

        @Override
        public void onBluetoothMessageReceived(String message) {
            processReceivedMessage(message);
        }

        @Override
        public void onBluetoothStateChanged(int state) {
            mListener.onBluetoothStateChanged(state);
        }

        @Override
        public void onBluetoothIsOf() {
        }
    };

}
