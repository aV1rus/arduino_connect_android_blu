package com.av1rus.arduinoconnect.application.model;

import android.support.v4.app.Fragment;

/**
 * Created by aV1rus on 4/1/14.
 */
public class FragmentPager {

    private Fragment mFragment;
    private String mTitle;

    public FragmentPager(String title, Fragment fragment){
        mFragment = fragment;
        mTitle = title;
    }

    public Fragment getFragment(){
        return mFragment;
    }

    public String getTitle(){
        return mTitle;
    }

}
