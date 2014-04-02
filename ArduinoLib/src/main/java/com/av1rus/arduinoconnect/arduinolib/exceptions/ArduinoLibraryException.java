package com.av1rus.arduinoconnect.arduinolib.exceptions;

/**
 * Created by aV1rus on 4/1/14.
 */
public class ArduinoLibraryException extends Exception {

    public static enum ExceptionCause{
        LISTENER_NOT_SET,
    }

    public ExceptionCause mCausedBy;

    public ArduinoLibraryException(ExceptionCause causedBy, String message){
        super(message);
        mCausedBy = causedBy;
    }

    public ArduinoLibraryException(ExceptionCause causedBy, String message, Throwable cause){
        super(message, cause);
        mCausedBy = causedBy;
    }

    public ExceptionCause getCausedBy(){
        return mCausedBy;
    }
}
