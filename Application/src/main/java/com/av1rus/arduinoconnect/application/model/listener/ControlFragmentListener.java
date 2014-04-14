package com.av1rus.arduinoconnect.application.model.listener;


import com.av1rus.datacontract.model.LightColor;
import com.av1rus.datacontract.model.LightType;

/**
 * Created by aV1rus on 4/12/14.
 */
public interface ControlFragmentListener {
    void onLightSwitchChanged(LightType type, LightColor color);

}
