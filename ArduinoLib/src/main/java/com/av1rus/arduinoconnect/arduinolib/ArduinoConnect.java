package com.av1rus.arduinoconnect.arduinolib;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;

import com.av1rus.arduinoconnect.arduinolib.exceptions.ArduinoLibraryException;
import com.av1rus.arduinoconnect.arduinolib.exceptions.UnknownLightTypeException;
import com.av1rus.arduinoconnect.arduinolib.listener.ArduinoConnectListener;
import com.av1rus.arduinoconnect.arduinolib.listener.BluetoothListener;
import com.av1rus.arduinoconnect.arduinolib.bluetooth.BluetoothManager;
import com.av1rus.arduinoconnect.arduinolib.bluetooth.IBluetoothManager;
import com.av1rus.arduinoconnect.arduinolib.exceptions.BluetoothDeviceException;
import com.av1rus.arduinoconnect.arduinolib.model.ConnectionState;
import com.av1rus.arduinoconnect.arduinolib.utils.LightCommand;
import com.av1rus.arduinoconnect.arduinolib.utils.SharedPrefs;
import com.av1rus.datacontract.model.LightColor;
import com.av1rus.datacontract.model.LightType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by nick on 4/1/14.
 */
public class ArduinoConnect implements IArduinoConnect{

    IBluetoothManager blueManager;

    ArduinoConnectListener mListener;
    BluetoothDevice mBluetoothDevice;

    Map<LightType, LightColor> currentLightState;

    public ArduinoConnect(){
        if(blueManager == null){
            blueManager = new BluetoothManager();
            currentLightState = new HashMap<LightType, LightColor>();
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
    public BluetoothDevice getSavedBluetoothDevice(Context context){
        String s = SharedPrefs.getCachedString(context, "defaultBluetooth", null);
        if(s != null){
            try{
                return BluetoothAdapter.getDefaultAdapter().getRemoteDevice(s);
            }catch(Exception e){
                setBluetoothDevice(context, null);
            }
        }

        return null;
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
    public void setBluetoothDevice(Context context, BluetoothDevice device) {
        mBluetoothDevice = device;
        SharedPrefs.storeCacheValue(context, "defaultBluetooth", device.getAddress());
    }

    @Override
    public void stopArduinoConnection() {
        blueManager.disconnectDevice();
    }

    @Override
    public void disconnectAll(Context context) {
        blueManager.disconnectAll(context);
        mListener = null;
    }

    @Override
    public void sendMessage(String message) {
        blueManager.sendStringToDevice(message);
    }

    @Override
    public void setLightState(LightType type, LightColor color) {

        if(blueManager.isConnected()){

            if(currentLightState.get(type) != color){
                try {
                    List<String> commands = getCommands(type, color);

                    for(String command : commands){
//                        sendMessage(command);
                        mListener.onBluetoothMessageReceived("Sending: "+command);
                    }

                    currentLightState.put(type, color);
                    mListener.onLightsStateChanged(type, color);
                } catch(UnknownLightTypeException e){
                    mListener.onBluetoothMessageReceived("Error Passing command");
                    //TODO :: How should we handle this?
                }

            }

        }else{
            mListener.onBluetoothError("Bluetooth is not connected");
        }
    }

//    private void sendServiceCommand(Context context, int command){
//        Intent intent = new Intent(context, BluetoothService.class);
//        intent.putExtra("message", command);
//        context.startService(intent);
//    }

    @Override
    public void startArduinoConnection(Context context) throws BluetoothDeviceException, ArduinoLibraryException {
        mBluetoothDevice = getSavedBluetoothDevice(context);

        if(!bluetoothEnabled()){
            throw new BluetoothDeviceException(BluetoothDeviceException.ExceptionCause.BLUETOOTH_NOT_ENABLED, "BluetoothDevice was never set... Method setBluetoothDevice must be called at any time before this.");
        }

        if(mBluetoothDevice == null){
            throw new BluetoothDeviceException(BluetoothDeviceException.ExceptionCause.DEVICE_NOT_SET, "You must set the bluetooth");
        }

        if(mListener == null){
            throw new ArduinoLibraryException(ArduinoLibraryException.ExceptionCause.LISTENER_NOT_SET, "You must set the listener before starting connection");
        }

//        if(mService != null) {
//            mService.disconnectDevice();
//            mService.connectToDevice(mBluetoothDevice);
//        }
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

//    private ServiceConnection mServiceConn = new ServiceConnection(){
//        public void onServiceConnected(ComponentName name, IBinder service) {
//            mService = ((BluetoothService.MyBinder)service).getService();
//            mListener.onServiceStateChange(ConnectionState.STATE_CONNECTED);
//        }
//
//        public void onServiceDisconnected(ComponentName name) {
//            mService = null;
//            mListener.onServiceStateChange(ConnectionState.STATE_DISCONNECTED);
//        }
//    };

    private List<String> getCommands(LightType type, LightColor color) throws UnknownLightTypeException {

        List<String> commands = new ArrayList<String>();
        LightColor currentColor = currentLightState.get(type);

        //NOTE:: At this point we are under the assumption that current state != new state

        if(color == LightColor.Off || currentColor == null || currentColor == LightColor.Off){
            //If the current command is to turn off light the light is currently off than command is straight forward
            commands.add(getLightIdentifier(type, color));
        } else if(currentColor == LightColor.Both){
            //If both lights are currently on than we only need to turn off the opposite
            if(color == LightColor.Red){
                commands.add(getLightIdentifier(type, LightColor.White, false));
            } else if(color == LightColor.White){
                commands.add(getLightIdentifier(type, LightColor.Red, false));
            }

        } else {
            //We have either the red or white light on so when one is called make sure to turn off the other
            if(color == LightColor.Red){
                commands.add(getLightIdentifier(type, LightColor.White, false));
            } else {
                commands.add(getLightIdentifier(type, LightColor.Red, false));
            }
            commands.add(getLightIdentifier(type, color));
        }

        return commands;
    }

    /**
     * Use this method if you want to force a turn on or turn off version of that light command
     * @param type
     * @param color
     * @param turnOn
     * @return
     * @throws UnknownLightTypeException
     */
    private String getLightIdentifier(LightType type, LightColor color, boolean turnOn) throws UnknownLightTypeException{
        return turnOn ? getLightIdentifier(type, color).toUpperCase() : getLightIdentifier(type, color).toLowerCase();
    }

    /**
     * use this method to get command directly from color
     * @param type
     * @param color
     * @return
     * @throws UnknownLightTypeException
     */
    private String getLightIdentifier(LightType type, LightColor color) throws UnknownLightTypeException {
        if(type == LightType.HeadHalo){
            if(color == LightColor.Red) return LightCommand.HEADS_RED;
            if(color == LightColor.White) return LightCommand.HEADS_WHITE;
            if(color == LightColor.Both) return LightCommand.HEADS_BOTH;
            if(color == LightColor.Off) return LightCommand.HEADS_BOTH.toLowerCase();
        } else if(type == LightType.FogHalo){
            if(color == LightColor.Red) return LightCommand.FOGS_RED;
            if(color == LightColor.White) return LightCommand.FOGS_WHITE;
            if(color == LightColor.Both) return LightCommand.FOGS_BOTH;
            if(color == LightColor.Off) return LightCommand.FOGS_BOTH.toLowerCase();
        }

        throw new UnknownLightTypeException("Light type is unknown OR color cannot be mapped to type");
    }

}
