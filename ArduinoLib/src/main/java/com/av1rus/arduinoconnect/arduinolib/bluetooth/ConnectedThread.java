package com.av1rus.arduinoconnect.arduinolib.bluetooth;

import android.bluetooth.BluetoothSocket;

import com.av1rus.arduinoconnect.arduinolib.listener.ConnectedThreadListener;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ConnectedThread extends Thread {
    private final BluetoothSocket mSocket;
    private final InputStream mInStream;
    private final OutputStream mOutStream;
    private final ConnectedThreadListener mListener;

    public ConnectedThread(BluetoothSocket socket, ConnectedThreadListener listener) {
        mSocket = socket;
        mListener = listener;
        InputStream tmpIn = null;
        OutputStream tmpOut = null;

        // Get the input and output streams, using temp objects because
        // member streams are final
        try {
            tmpIn = socket.getInputStream();
            tmpOut = socket.getOutputStream();
        } catch (IOException e) { }

        mInStream = tmpIn;
        mOutStream = tmpOut;
    }

    public void run() {
        byte[] buffer = new byte[1024];  // buffer store for the stream
        int bytes; // bytes returned from read()

        // Keep listening to the InputStream until an exception occurs
        while (true) {
            try {
                // Read from the InputStream
                bytes = mInStream.read(buffer);
                // Send the obtained bytes to the UI activity
//                mHandler.obtainMessage(MESSAGE_READ, bytes, -1, buffer).sendToTarget();
                mListener.obtainMessage(bytes, buffer);
            } catch (IOException e) {
                break;
            }
        }
    }

    /* Call this from the main activity to send data to the remote device */
    public void write(byte[] bytes) {
        try {
            mOutStream.write(bytes);
        } catch (IOException e) { }
    }

    /* Call this from the main activity to shutdown the connection */
    public void cancel() {
        try {
            mSocket.close();
        } catch (IOException e) { }
    }
}