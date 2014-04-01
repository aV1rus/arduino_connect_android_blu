package com.av1rus.arduinoconnect.application.model;

import android.graphics.drawable.Drawable;

/**
 * This class represents a row on the SlideMenu (menu that slides out from the left side of the screen).
 * Each SlideMenuRow has a String (name) and a Drawable (icon) associated with it.
 * 
 * @version 1.0
 */
public class SlideMenuRow extends BaseRow {
	private String Name;
	private Drawable Icon;
	
	/**
	 * A constructor of the SlideMenuRow object that sets the name and icon of the object to those passed in.
	 * 
	 * @param name	A String representing the name of this row in the SlideMenu
	 * @param icon	A Drawable that should be associated with this row in the SlideMenu and displayed next to the name
	 */
	public SlideMenuRow(String name, Drawable icon) {
		this(name, icon, true);
	}

    /**
     * A constructor of the SlideMenuRow object that sets the name, icon, and enabled state of the object to those passed in.
     *
     * @param name	A String representing the name of this row in the SlideMenu
     * @param icon	A Drawable that should be associated with this row in the SlideMenu and displayed next to the name
     * @param enabled	A boolean that determines whether this row is enabled
     */
    public SlideMenuRow(String name, Drawable icon, boolean enabled) {
        this.Name = name;
        this.Icon = icon;
        this.Enabled = enabled;
    }
	
	/**
	 * Gets the name of this row on the SlideMenu.
	 * 
	 * @return	A String representing the name of this row in the SlideMenu
	 */
	public String getName() {
		return Name;
	}
	
	/**
	 * Gets the icon of this row on the SlideMenu.
	 * 
	 * @return	 A Drawable object representing the icon that is displayed next to the name of this row in the SlideMenu
	 */
	public Drawable getIcon() {
		return Icon;
	}
}
