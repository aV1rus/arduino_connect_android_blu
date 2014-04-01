package com.av1rus.arduinoconnect.arduinolib.exceptions;

/**
 * Created by nick on 4/1/14.
 */
public class BluetoothDeviceException extends Exception {

    public static enum ExceptionCause{
        DEVICE_NOT_SET,
        DEVICE_NOT_FOUND,
        BLUETOOTH_NOT_ENABLED,
    }

    public ExceptionCause mCausedBy;

    public BluetoothDeviceException(ExceptionCause causedBy, String message){
        super(message);
        mCausedBy = causedBy;
    }

    public BluetoothDeviceException(ExceptionCause causedBy, String message, Throwable cause){
        super(message, cause);
        mCausedBy = causedBy;
    }

    public ExceptionCause getCausedBy(){
        return mCausedBy;
    }
}
