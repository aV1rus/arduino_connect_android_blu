package com.av1rus.arduinoconnect.arduinolib;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Context;

import com.av1rus.arduinoconnect.arduinolib.exceptions.ArduinoLibraryException;
import com.av1rus.arduinoconnect.arduinolib.exceptions.BluetoothDeviceException;
import com.av1rus.arduinoconnect.arduinolib.listener.ArduinoConnectListener;
import com.av1rus.datacontract.model.LightColor;
import com.av1rus.datacontract.model.LightType;

import java.util.Set;

/**
 * Created by nick on 4/1/14.
 */
public interface IArduinoConnect {

    Set<BluetoothDevice> getPairedDevices();

    BluetoothDevice getConnectedDevice();

    BluetoothDevice getSavedBluetoothDevice(Context context);

    boolean bluetoothEnabled();

    void setArduinoConnectListener(Context context, ArduinoConnectListener listener);

    void setBluetoothDevice(Context context, BluetoothDevice device);

    void startArduinoConnection(Context context) throws BluetoothDeviceException, ArduinoLibraryException;

    void stopArduinoConnection();

    void disconnectAll(Context context);

    void sendMessage(String message);

    void setLightState(LightType type, LightColor color);

}
