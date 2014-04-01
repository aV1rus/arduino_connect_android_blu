package com.av1rus.arduinoconnect.arduinolib.listener;

import android.bluetooth.BluetoothDevice;

import com.av1rus.arduinoconnect.arduinolib.model.ConnectionState;

/**
 * Created by nick on 4/1/14.
 */
public interface BluetoothListener {
    void onConnectionStateChanged(ConnectionState state, BluetoothDevice device);

    void onBluetoothMessageReceived(String message);

    void onBluetoothStateChanged(int state);

    void onBluetoothIsOf();
}
