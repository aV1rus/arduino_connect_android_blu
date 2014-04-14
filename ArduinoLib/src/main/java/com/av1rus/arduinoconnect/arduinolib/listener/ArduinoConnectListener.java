package com.av1rus.arduinoconnect.arduinolib.listener;

import android.bluetooth.BluetoothDevice;

import com.av1rus.arduinoconnect.arduinolib.model.ConnectionState;
import com.av1rus.datacontract.model.LightColor;
import com.av1rus.datacontract.model.LightType;

/**
 * Created by nick on 4/1/14.
 */
public interface ArduinoConnectListener {

//    void onServiceStateChange(ConnectionState state);

    void onConnectionStateChanged(ConnectionState state, BluetoothDevice device);

    void onBluetoothStateChanged(int state);

    void onLightsStateChanged(LightType type, LightColor color);

    void onBluetoothMessageReceived(String message);

    void onBluetoothError(String error);

    void onDeviceChanged(BluetoothDevice device);
}
