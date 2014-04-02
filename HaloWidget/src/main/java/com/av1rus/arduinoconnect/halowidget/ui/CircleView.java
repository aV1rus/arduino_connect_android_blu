package com.av1rus.arduinoconnect.halowidget.ui;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.SurfaceView;
import android.view.View;

import com.av1rus.arduinoconnect.halowidget.R;

public class CircleView extends View {

    private Paint circlePaint;
    private int mWidth = 100;
    private int mHeight = 100;

    private int mWidgetColor;

    public CircleView(Context context) {
        super(context);
        init(null);
    }

    public CircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public CircleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    public void setColor(int color){
        mWidgetColor = color;
        invalidate();
    }

    public void init(AttributeSet attrs){
        if (!isInEditMode()) {
            setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }
        if(attrs != null){
            TypedArray attrsArray = getContext().obtainStyledAttributes(attrs, R.styleable.circleview);
            mWidgetColor = attrsArray.getColor(R.styleable.circleview_cFillColor, Color.BLUE);
            attrsArray.recycle();
        } else {
            mWidgetColor = Color.BLUE;
        }

        initView();

    }

    public void initView()
    {
        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setStrokeWidth(5);
        circlePaint.setColor(mWidgetColor);
    }

    public void setParams(int height, int width){
        mHeight = height;
        mWidth = width;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawCircle(mWidth/2, mHeight/2, mHeight/2-10, circlePaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);

        //Width
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        if (widthSpecMode == MeasureSpec.AT_MOST) {
            mWidth = widthSpecSize;
        } else if (widthSpecMode == MeasureSpec.EXACTLY) {
            mWidth = widthSpecSize;
        }

        //Measure height
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
        if (heightSpecMode == MeasureSpec.AT_MOST) {
            mHeight = heightSpecSize;
        } else if (heightSpecMode == MeasureSpec.EXACTLY) {
            mHeight = heightSpecSize;
        }


        //Make sure they equal eachother
        if(mWidth != mHeight){
            if(mWidth>mHeight){
                mHeight = mWidth;
            } else {
                mWidth = mHeight;
            }
        }
    }

}