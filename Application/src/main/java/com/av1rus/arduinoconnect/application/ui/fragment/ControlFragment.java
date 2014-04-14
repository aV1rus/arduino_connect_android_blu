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
import android.widget.TextView;

import com.av1rus.arduinoconnect.application.R;
import com.av1rus.arduinoconnect.application.model.listener.ControlFragmentListener;
import com.av1rus.datacontract.model.LightColor;
import com.av1rus.datacontract.model.LightType;

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

    TextView mTVRed;
    TextView mTVWhite;
    TextView mTVBoth;

    private static ControlFragmentListener mListener;

    public static ControlFragment getInstance(ControlFragmentListener listener) {
        if(mFragment == null){
            mFragment = new ControlFragment();
            mListener = listener;
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
        mWhiteHeadHalo = (CheckBox) view.findViewById(R.id.checkHeadWhite);
        mBothHeadHalo = (CheckBox) view.findViewById(R.id.checkHeadBoth);

        mRedFogHalo = (CheckBox) view.findViewById(R.id.checkFogRed);
        mWhiteFogHalo = (CheckBox) view.findViewById(R.id.checkFogWhite);
        mBothFogHalo = (CheckBox) view.findViewById(R.id.checkFogBoth);

        mTVRed = (TextView) view.findViewById(R.id.redTV);
        mTVWhite = (TextView) view.findViewById(R.id.whiteTV);
        mTVBoth = (TextView) view.findViewById(R.id.bothTV);

        return view;
    }


    @Override
    public void onStart(){
        super.onStart();

        //Setup the Listeners

        mRedHeadHalo.setOnCheckedChangeListener(onCheckedChangeListener);
        mWhiteHeadHalo.setOnCheckedChangeListener(onCheckedChangeListener);
        mBothHeadHalo.setOnCheckedChangeListener(onCheckedChangeListener);

        mRedFogHalo.setOnCheckedChangeListener(onCheckedChangeListener);
        mWhiteFogHalo.setOnCheckedChangeListener(onCheckedChangeListener);
        mBothFogHalo.setOnCheckedChangeListener(onCheckedChangeListener);

        mTVRed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mRedHeadHalo.isChecked() || !mRedFogHalo.isChecked()){
                    mRedHeadHalo.setChecked(true);
                    mRedFogHalo.setChecked(true);
                }
            }
        });
        mTVWhite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mWhiteHeadHalo.isChecked() || !mWhiteFogHalo.isChecked()){
                    mWhiteHeadHalo.setChecked(true);
                    mWhiteFogHalo.setChecked(true);
                }
            }
        });
        mTVBoth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mBothHeadHalo.isChecked() || !mBothFogHalo.isChecked()){
                    mBothHeadHalo.setChecked(true);
                    mBothFogHalo.setChecked(true);
                }
            }
        });
    }

    public CheckBox.OnCheckedChangeListener onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            int buttonViewId = buttonView.getId();

            if(isChecked) { //ON


                setCheckBoxEnabled((CheckBox)buttonView);

                setLightEnabled(buttonViewId);

            } else { //OFF

                setLightDisabled(buttonViewId);
            }
        }
    };

    private void setLightEnabled(int id){
        if(mListener == null) return;

        switch(id){
            case R.id.checkHeadRed:
                mListener.onLightSwitchChanged(LightType.HeadHalo, LightColor.Red);
                break;
            case R.id.checkHeadWhite:
                mListener.onLightSwitchChanged(LightType.HeadHalo, LightColor.White);
                break;
            case R.id.checkHeadBoth:
                mListener.onLightSwitchChanged(LightType.HeadHalo, LightColor.Both);
                break;
            case R.id.checkFogRed:
                mListener.onLightSwitchChanged(LightType.FogHalo, LightColor.Red);
                break;
            case R.id.checkFogWhite:
                mListener.onLightSwitchChanged(LightType.FogHalo, LightColor.White);
                break;
            case R.id.checkFogBoth:
                mListener.onLightSwitchChanged(LightType.FogHalo, LightColor.Both);
                break;
        }
    }

    private void setLightDisabled(int id){
        if(mListener == null) return;

        if(lightSwitchStype(id) == LightType.HeadHalo){
            //Check to see if ALL switches are disabled
            if(!mWhiteHeadHalo.isChecked() && !mRedHeadHalo.isChecked() && !mBothHeadHalo.isChecked()){
                //Send the off command
                mListener.onLightSwitchChanged(LightType.HeadHalo, LightColor.Off);
            }
        } else if(lightSwitchStype(id) == LightType.FogHalo){
            //Check to see if ALL switches are disabled
            if(!mWhiteFogHalo.isChecked() && !mRedFogHalo.isChecked() && !mBothFogHalo.isChecked()){
                //Send the off command
                mListener.onLightSwitchChanged(LightType.FogHalo, LightColor.Off);
            }
        }
    }

    private void setCheckBoxEnabled(CheckBox headView){

        switch(headView.getId()){
            case R.id.checkHeadRed:
                mWhiteHeadHalo.setChecked(false);
                mBothHeadHalo.setChecked(false);
                break;
            case R.id.checkHeadWhite:
                mRedHeadHalo.setChecked(false);
                mBothHeadHalo.setChecked(false);
                break;
            case R.id.checkHeadBoth:
                mRedHeadHalo.setChecked(false);
                mWhiteHeadHalo.setChecked(false);
                break;
            case R.id.checkFogRed:
                mWhiteFogHalo.setChecked(false);
                mBothFogHalo.setChecked(false);
                break;
            case R.id.checkFogWhite:
                mRedFogHalo.setChecked(false);
                mBothFogHalo.setChecked(false);
                break;
            case R.id.checkFogBoth:
                mRedFogHalo.setChecked(false);
                mWhiteFogHalo.setChecked(false);
                break;
        }

//        if(headView != null){
//            mRedHeadHalo.setChecked(headView != mRedHeadHalo);
//            mWhiteHeadHalo.setChecked(headView != mWhiteHeadHalo);
//            mBothHeadHalo.setChecked(headView != mBothHeadHalo);
//        } else {
//            mRedHeadHalo.setChecked(false);
//            mWhiteHeadHalo.setChecked(false);
//            mBothHeadHalo.setChecked(false);
//        }

    }

//    private void setFogChecked(CheckBox fogView){
//        if(fogView != null){
//            mRedFogHalo.setChecked(fogView != mRedFogHalo);
//            mWhiteFogHalo.setChecked(fogView != mWhiteFogHalo);
//            mBothFogHalo.setChecked(fogView != mBothFogHalo);
//        } else {
//            mRedFogHalo.setChecked(false);
//            mWhiteFogHalo.setChecked(false);
//            mBothFogHalo.setChecked(false);
//        }
//    }

    private LightType lightSwitchStype(int id){
        switch(id){
            case R.id.checkHeadBoth:
            case R.id.checkHeadRed:
            case R.id.checkHeadWhite:
                return LightType.HeadHalo;
            case R.id.checkFogBoth:
            case R.id.checkFogRed:
            case R.id.checkFogWhite:
                return LightType.FogHalo;
            default:
                return LightType.Unknown;
        }
    }
}
