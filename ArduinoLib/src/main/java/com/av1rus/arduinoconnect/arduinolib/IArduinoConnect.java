package com.av1rus.arduinoconnect.arduinolib;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;

import com.av1rus.arduinoconnect.arduinolib.exceptions.BluetoothDeviceException;
import com.av1rus.arduinoconnect.arduinolib.listener.ArduinoConnectListener;

import java.util.Set;

/**
 * Created by nick on 4/1/14.
 */
public interface IArduinoConnect {

    Set<BluetoothDevice> getNearbyDevices();

    BluetoothDevice getConnectedDevice();

    boolean bluetoothEnabled();

    void setBluetoothDevice(Activity activity, BluetoothDevice device);

    void startArduinoConnection(Activity activity, ArduinoConnectListener listener) throws BluetoothDeviceException;

    void stopArduinoConnection();

    void sendMessage(String message);

}
