package com.av1rus.arduinoconnect.arduinolib;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;

import com.av1rus.arduinoconnect.arduinolib.exceptions.ArduinoLibraryException;
import com.av1rus.arduinoconnect.arduinolib.exceptions.BluetoothDeviceException;
import com.av1rus.arduinoconnect.arduinolib.listener.ArduinoConnectListener;

import java.util.Set;

/**
 * Created by nick on 4/1/14.
 */
public interface IArduinoConnect {

    Set<BluetoothDevice> getPairedDevices();

    BluetoothDevice getConnectedDevice();

    boolean bluetoothEnabled();

    void setArduinoConnectListener(ArduinoConnectListener listener);

    void setBluetoothDevice(Activity activity, BluetoothDevice device);

    BluetoothDevice getSavedBluetoothDevice(Activity activity);

    void startArduinoConnection(Activity activity) throws BluetoothDeviceException, ArduinoLibraryException;

    void stopArduinoConnection();

    void sendMessage(String message);

}
