package com.av1rus.arduinoconnect.arduinolib;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

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


    public ArduinoConnect(){
        if(blueManager == null){
            blueManager = new BluetoothManager();
        }
    }

    @Override
    public Set<BluetoothDevice> getNearbyDevices() {
        return blueManager.getNearbyDevices();
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
    public void setBluetoothDevice(Activity activity, BluetoothDevice device) {
        SharedPrefs.storeCacheValue(activity, "defaultBluetooth", device.getAddress());
    }

    @Override
    public void startArduinoConnection(Activity activity, ArduinoConnectListener listener) throws BluetoothDeviceException{
        mListener = listener;
        BluetoothDevice device = getSavedBluetoothDevice(activity);

        if(device == null){
            throw new BluetoothDeviceException(BluetoothDeviceException.ExceptionCause.DEVICE_NOT_SET, "BluetoothDevice was never set... Method setBluetoothDevice must be called at any time before this.");
        }

        if(!bluetoothEnabled()){
            throw new BluetoothDeviceException(BluetoothDeviceException.ExceptionCause.BLUETOOTH_NOT_ENABLED, "BluetoothDevice was never set... Method setBluetoothDevice must be called at any time before this.");
        }

        blueManager.connectToDevice(device, mBlueListener);
    }

    @Override
    public void stopArduinoConnection() {

    }

    @Override
    public void sendMessage(String message) {
        blueManager.sendStringToDevice(message);
    }

    private BluetoothDevice getSavedBluetoothDevice(Activity activity){
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

    private void processReceivedMessage(String message){
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
