package com.av1rus.arduinoconnect.arduinolib.bluetooth;

import android.bluetooth.BluetoothDevice;

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

    void sendStringToDevice(String message);

    void connectToDevice(BluetoothDevice device, BluetoothListener bluetoothListener);

    void disconnectDevice();

    void startScan();

    void stopScan();

}
