package com.av1rus.arduinoconnect.arduinolib.listener;

/**
 * Created by nick on 7/9/14.
 */
public interface ConnectedThreadListener {

    void obtainMessage(int bytes, byte[] buffer);
}
