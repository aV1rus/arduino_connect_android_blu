package com.av1rus.arduinoconnect.arduinolib.exceptions;

/**
 * Created by aV1rus on 4/12/14.
 */
public class UnknownLightTypeException extends Exception{
    public UnknownLightTypeException(String message){
        super(message);
    }

    public UnknownLightTypeException(String message, Throwable cause){
        super(message, cause);
    }
}
