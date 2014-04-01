package com.av1rus.arduinoconnect.application.model;

public class BaseRow {

    public boolean Enabled;

    /**
     * Gets the enabled state of this row on the SlideMenu.
     *
     * @return	 A boolean representing whether the row is enabled
     */
    public boolean isEnabled() {
        return Enabled;
    }

    /**
     * Sets the enabled state of this row on the SlideMenu.
     *
     * @param enabled	A boolean that determines whether this row is enabled
     */
    public void setEnabled(boolean enabled) {
        this.Enabled = enabled;
    }


}
