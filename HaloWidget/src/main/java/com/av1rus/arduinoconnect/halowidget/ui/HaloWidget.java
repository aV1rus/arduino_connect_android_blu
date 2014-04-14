package com.av1rus.arduinoconnect.halowidget.ui;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.av1rus.arduinoconnect.halowidget.R;
import com.av1rus.datacontract.model.LightColor;
import com.av1rus.datacontract.model.LightType;

/**
 * Created by nick on 4/2/14.
 */
public class HaloWidget extends LinearLayout {

    private static final String TAG = "HaloWidget";
    GradientDrawable mLeftHeadHalo;
    GradientDrawable mRightHeadHalo;
    GradientDrawable mLeftFogHalo;
    GradientDrawable mRightFogHalo;

    private int mStrokeSize = 8;
    private String mWidgetTitle = null;

    public static int HALO_RED = R.color.halo_red;
    public static int HALO_WHITE = R.color.halo_white;
    public static int HALO_BOTH = R.color.halo_both;
    public static int HALO_OFF = R.color.halo_off;

    public HaloWidget(Context context){
        super(context);
        init();
    }

    public HaloWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HaloWidget(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public void init(){
        if (!isInEditMode()) {
            setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }
    }

    public void setTitle(String title){
        try {
            TextView tv = (TextView) findViewById(R.id.haloLabelTV);
            Typeface tf = Typeface.createFromAsset(getResources().getAssets(), "fonts/Amarante-Regular.ttf");
            tv.setTextColor(getColor(R.color.halo_title));
            tv.setTypeface(tf);
            tv.setText(title);
        }catch (NullPointerException e){
            mWidgetTitle = title;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setupView();
    }

    private void setupView(){
        inflate(getContext(), R.layout.widget_halo_lights, this);
        setBackgroundResource(R.drawable.bg_rounded);

        mRightHeadHalo = (GradientDrawable)findViewById(R.id.rightHeadCV).getBackground();
        mRightFogHalo = (GradientDrawable)findViewById(R.id.rightFogCV).getBackground();
        mLeftHeadHalo = (GradientDrawable)findViewById(R.id.leftHeadCV).getBackground();
        mLeftFogHalo = (GradientDrawable)findViewById(R.id.leftFogCV).getBackground();

//        setHeadHaloColor(HALO_WHITE);
//        setFogHaloColor(HALO_WHITE);

        if(mWidgetTitle != null){
            setTitle(mWidgetTitle);
        }
    }

    public void setHeadHaloColor(int color){
        int c = getColor(color);
        mRightHeadHalo.setStroke(mStrokeSize, c);
        mLeftHeadHalo.setStroke(mStrokeSize, c);
    }

    public void setFogHaloColor(int color){
        int c = getColor(color);
        mRightFogHalo.setStroke(mStrokeSize, c);
        mLeftFogHalo.setStroke(mStrokeSize, c);
    }

    public void setLightsState(LightType type, LightColor color){
        if(type == LightType.HeadHalo){
            if(color == LightColor.Red) setHeadHaloColor(HALO_RED);
            else if(color == LightColor.White) setHeadHaloColor(HALO_WHITE);
            else if(color == LightColor.Both) setHeadHaloColor(HALO_BOTH);
            else setHeadHaloColor(HALO_OFF);
        } else if(type == LightType.FogHalo){
            if(color == LightColor.Red) setFogHaloColor(HALO_RED);
            else if(color == LightColor.White) setFogHaloColor(HALO_WHITE);
            else if(color == LightColor.Both) setFogHaloColor(HALO_BOTH);
            else setFogHaloColor(HALO_OFF);
        } else {
            Log.e(TAG, "setLightsState :: No type can be mapped for color change");
        }

    }

    private int getColor(int color){
        if(color == HALO_RED || color == HALO_WHITE || color == HALO_BOTH){
            return getResources().getColor(color);
        }
        return color;
    }
}
