package com.av1rus.arduinoconnect.arduinolib.listener;

import android.bluetooth.BluetoothDevice;

import com.av1rus.arduinoconnect.arduinolib.model.ConnectionState;
import com.av1rus.arduinoconnect.arduinolib.model.LightColor;
import com.av1rus.arduinoconnect.arduinolib.model.LightType;

/**
 * Created by nick on 4/1/14.
 */
public interface ArduinoConnectListener {
    void onConnectionStateChanged(ConnectionState state, BluetoothDevice device);

    void onBluetoothStateChanged(int state);

    void onLightsStateChanged(LightColor color, LightType type);

    void onDeviceChanged(BluetoothDevice device);
}
