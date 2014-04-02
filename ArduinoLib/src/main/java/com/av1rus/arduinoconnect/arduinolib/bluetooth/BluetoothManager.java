package com.av1rus.arduinoconnect.arduinolib.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;

import com.av1rus.arduinoconnect.arduinolib.exceptions.ArduinoLibraryException;
import com.av1rus.arduinoconnect.arduinolib.exceptions.BluetoothDeviceException;
import com.av1rus.arduinoconnect.arduinolib.listener.BluetoothListener;
import com.av1rus.arduinoconnect.arduinolib.model.ConnectionState;
import com.av1rus.arduinoconnect.arduinolib.utils.ByteUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Created by nick on 4/1/14.
 */
public class BluetoothManager implements IBluetoothManager{

    BluetoothSocket mBluetoothSocket;
    InputStream mInputStream;
    OutputStream mOutputStream;
    Scanner mScanner;
    BluetoothListener mListener;

    Pattern mEndLinePattern = Pattern.compile("[\\r\\n]+]");

    boolean mIsScanning;

    public BluetoothManager(){
        mBluetoothSocket = null;
        mInputStream = null;
        mOutputStream = null;
        mScanner = null;
        mIsScanning = false;
    }

    @Override
    public Set<BluetoothDevice> getPairedDevices(){
        return BluetoothAdapter.getDefaultAdapter().getBondedDevices();
    }

    @Override
    public BluetoothDevice getConnectedDevice(){
        return mBluetoothSocket.getRemoteDevice();
    }

    @Override
    public boolean bluetoothEnabled() {
        return BluetoothAdapter.getDefaultAdapter().isEnabled();
    }

    @Override
    public boolean isConnected(){
        return (mBluetoothSocket != null && mBluetoothSocket.isConnected());
    }

    @Override
    public void setBluetoothListener(Context context, BluetoothListener bluetoothListener) {
        mListener = bluetoothListener;
        IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        context.registerReceiver(mBluetoothStateChangedReceiver, filter);
    }

    @Override
    public void sendStringToDevice(String message){
        try {
            if (mOutputStream != null) {
                mOutputStream.write(message.getBytes());
            }
        } catch (IOException e) {
//            Log.e(TAG, "");
        }
    }

    @Override
    public void connectToDevice(BluetoothDevice device) throws BluetoothDeviceException, ArduinoLibraryException {
        if(mListener == null){
            throw new ArduinoLibraryException(ArduinoLibraryException.ExceptionCause.LISTENER_NOT_SET, "BluetoothManager: BluetoothListener must be set");
        }

        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            return;
        }
        if (!mBluetoothAdapter.isEnabled()) {
            throw new BluetoothDeviceException(BluetoothDeviceException.ExceptionCause.BLUETOOTH_NOT_ENABLED, "You must enable bluetooth");
        }

        //Make sure device is paired
        if (device.getBondState() == BluetoothDevice.BOND_BONDED)  {
            try {
                Method m = device.getClass().getMethod("createRfcommSocket", new Class[] { int.class });
                mBluetoothSocket = (BluetoothSocket) m.invoke(device, 1);
                mBluetoothSocket.connect();

                mInputStream = mBluetoothSocket.getInputStream();
                mOutputStream = mBluetoothSocket.getOutputStream();

                mListener.onConnectionStateChanged(ConnectionState.STATE_CONNECTED, device);
                //device.getName() + "\nConnected"
                startScan();

            } catch (Exception e) {
                mListener.onConnectionStateChanged(ConnectionState.STATE_ERROR_CONNECTING, device);
                //listener ERROR
                return;
            }

        } else {
            throw new BluetoothDeviceException(BluetoothDeviceException.ExceptionCause.DEVICE_NOT_PAIRED, "Device is not paired with phone");
        }
    }

    @Override
    public void disconnectDevice(){
        if(mBluetoothSocket != null && mBluetoothSocket.isConnected()){
            try {
                mBluetoothSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void startScan(){
//        mScanner = new Scanner(new InputStreamReader(mInputStream));
//        mScanner.useDelimiter(mEndLinePattern);
//
//
//        if(mScanner != null && mBluetoothSocket.isConnected() && !mIsScanning){
//            Handler handler = new Handler();
//            handler.postDelayed(mFilterScan, 1000);
//        } else {
//            mIsScanning = false;
//        }

        if(mBluetoothSocket.isConnected() && !mIsScanning){
            Handler handler = new Handler();
            handler.postDelayed(mBluetoothScan, 1000);
        } else {
            mIsScanning = false;
        }
    }

    @Override
    public void stopScan() {
        mScanner = null;
    }

//    private Runnable mFilterScan = new Runnable(){
//        @Override
//        public void run() {
//            if(mScanner == null) return;
//
//            try {
//                mIsScanning = true;
//                if(mScanner.hasNext(mEndLinePattern))
//                while(mScanner.hasNext(mEndLinePattern)){
//                    mListener.onBluetoothMessageReceived(mScanner.next(mEndLinePattern));
//                }
//            } catch (Exception e) {
//                mListener.onConnectionStateChanged(ConnectionState.CONNECTION_ERROR, mBluetoothSocket.getRemoteDevice());
//                return;
//            }
//
//            if(mBluetoothSocket.isConnected()){
//                Handler handler = new Handler();
//                handler.postDelayed(this, 3000);
//            } else {
//                mIsScanning = false;
//                mListener.onConnectionStateChanged(ConnectionState.STATE_DISCONNECTED, mBluetoothSocket.getRemoteDevice());
//
//            }
//
//        }
//    };

    private Runnable mBluetoothScan = new Runnable(){
        @Override
        public void run() {
            try {
                mIsScanning = true;
                final byte[] bytes = new byte[1024];
                if (mBluetoothSocket != null && mBluetoothSocket.isConnected() && mInputStream != null && mOutputStream != null) {
//                    String str2 = ByteUtils.hexToString(ByteUtils.byteToHex(bytes, mInputStream.read(bytes)));


//                    mListener.onBluetoothMessageReceived(str2);

//                    if (reading.length() > 6) {
//                        if(!reading.trim().equals("DT:") && !reading.trim().equals("DT") && !reading.trim().equals("D")){
//                            handleReceived(reading);
//                            reading = "";}
//                    }

                }
            } catch (Exception e) {
                mListener.onConnectionStateChanged(ConnectionState.CONNECTION_ERROR, mBluetoothSocket.getRemoteDevice());
                return;
            }


            if(mBluetoothSocket.isConnected()){
                Handler handler = new Handler();
                handler.postDelayed(this, 3000);
            } else {
                mIsScanning = false;
                mListener.onConnectionStateChanged(ConnectionState.STATE_DISCONNECTED, mBluetoothSocket.getRemoteDevice());
            }
        }
    };

    private final BroadcastReceiver mBluetoothStateChangedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if(action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)){
                int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);

                mListener.onBluetoothStateChanged(state);
            }

        }
    };
}
