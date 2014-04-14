package com.av1rus.arduinoconnect.arduinolib.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.content.Context;

import com.av1rus.arduinoconnect.arduinolib.exceptions.ArduinoLibraryException;
import com.av1rus.arduinoconnect.arduinolib.exceptions.BluetoothDeviceException;
import com.av1rus.arduinoconnect.arduinolib.listener.BluetoothListener;

import java.util.Set;

/**
 * Created by nick on 4/1/14.
 */
public interface IBluetoothManager {

    Set<BluetoothDevice> getPairedDevices();

    BluetoothDevice getConnectedDevice();

    boolean bluetoothEnabled();

    boolean isConnected();

    void setBluetoothListener(Context context, BluetoothListener bluetoothListener);

    void sendStringToDevice(String message);

    void connectToDevice(BluetoothDevice device) throws BluetoothDeviceException, ArduinoLibraryException;

    void disconnectDevice();

    void disconnectAll(Context context);

    void startScan();

    void stopScan();

}
