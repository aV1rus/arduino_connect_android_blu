package com.av1rus.arduinoconnect.application.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.av1rus.arduinoconnect.application.R;

/**
 * Created by nick on 4/11/14.
 */
public class ControlFragment extends Fragment {

    private static ControlFragment mFragment;
    Activity mActivity;
    Context mContext;

    CheckBox mRedHeadHalo;
    CheckBox mWhiteHeadHalo;
    CheckBox mBothHeadHalo;

    CheckBox mRedFogHalo;
    CheckBox mWhiteFogHalo;
    CheckBox mBothFogHalo;


    public static ControlFragment getInstance() {
        if(mFragment == null){
            mFragment = new ControlFragment();
        }
        return mFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mActivity = getActivity();
        this.mContext = this.mActivity.getApplicationContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_control, container, false);
        mRedHeadHalo = (CheckBox) view.findViewById(R.id.checkHeadRed);
        mWhiteHeadHalo = (CheckBox) view.findViewById(R.id.checkFogWhite);
        mBothHeadHalo = (CheckBox) view.findViewById(R.id.checkHeadBoth);

        mRedFogHalo = (CheckBox) view.findViewById(R.id.checkFogRed);
        mWhiteFogHalo = (CheckBox) view.findViewById(R.id.checkFogWhite);
        mBothFogHalo = (CheckBox) view.findViewById(R.id.checkFogBoth);

        return view;
    }


    @Override
    public void onStart(){
        super.onStart();


    }


    public CheckBox.OnCheckedChangeListener onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            if(isChecked) { //ON
                setHeadChecked((CheckBox)buttonView);

                switch (buttonView.getId()) {
                    case R.id.checkHeadRed:
                        break;
                    case R.id.checkHeadWhite:
                        break;
                    case R.id.checkHeadBoth:
                        break;
                    case R.id.checkFogRed:
                        break;
                    case R.id.checkFogWhite:
                        break;
                    case R.id.checkFogBoth:
                        break;
                }
            } else { //OFF

                switch (buttonView.getId()) {
                    case R.id.checkHeadRed:
                        break;
                    case R.id.checkHeadWhite:
                        break;
                    case R.id.checkHeadBoth:
                        break;
                    case R.id.checkFogRed:
                        break;
                    case R.id.checkFogWhite:
                        break;
                    case R.id.checkFogBoth:
                      break;
                }
            }
        }
    };

    private void setHeadChecked(CheckBox headView){
        if(headView != null){
            mRedHeadHalo.setChecked(headView != mRedHeadHalo);
            mWhiteHeadHalo.setChecked(headView != mWhiteHeadHalo);
            mBothHeadHalo.setChecked(headView != mBothHeadHalo);
        } else {
            mRedHeadHalo.setChecked(false);
            mWhiteHeadHalo.setChecked(false);
            mBothHeadHalo.setChecked(false);
        }

    }

    private void setFogChecked(CheckBox fogView){
        if(fogView != null){
            mRedFogHalo.setChecked(fogView != mRedFogHalo);
            mWhiteFogHalo.setChecked(fogView != mWhiteFogHalo);
            mBothFogHalo.setChecked(fogView != mBothFogHalo);
        } else {
            mRedFogHalo.setChecked(false);
            mWhiteFogHalo.setChecked(false);
            mBothFogHalo.setChecked(false);
        }
    }
}
