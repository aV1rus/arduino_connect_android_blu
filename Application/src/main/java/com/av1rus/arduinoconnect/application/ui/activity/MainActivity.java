package com.av1rus.arduinoconnect.application.ui.activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.av1rus.arduinoconnect.application.R;
import com.av1rus.arduinoconnect.application.app.ArduinoConnectApp;
import com.av1rus.arduinoconnect.application.model.FragmentPager;
import com.av1rus.arduinoconnect.application.ui.fragment.LogFragment;
import com.av1rus.arduinoconnect.application.ui.fragment.PairedDevicesFragment;
import com.av1rus.arduinoconnect.arduinolib.ArduinoConnect;
import com.av1rus.arduinoconnect.arduinolib.exceptions.ArduinoLibraryException;
import com.av1rus.arduinoconnect.arduinolib.exceptions.BluetoothDeviceException;
import com.av1rus.arduinoconnect.arduinolib.listener.ArduinoConnectListener;
import com.av1rus.arduinoconnect.arduinolib.model.ConnectionState;
import com.av1rus.arduinoconnect.arduinolib.model.LightColor;
import com.av1rus.arduinoconnect.arduinolib.model.LightType;
import com.viewpagerindicator.TitlePageIndicator;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends SlidingMenuActivity {
    private ViewPager mViewPager;
    private MyFragmentPagerAdapter mMyFragmentPagerAdapter;
    private ArduinoConnectApp mArduinoConnectApp;

    TextView mStateTV;

//    public static List<FragmentPager> fragments;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //create a new library
        mArduinoConnectApp = ArduinoConnectApp.getApp();
        mArduinoConnectApp.setArduinoLibrary(new ArduinoConnect());

        Button button = (Button) findViewById(R.id.button);
        mStateTV = (TextView) findViewById(R.id.stateTV);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

//
//        fragments = new ArrayList<FragmentPager>();
//        fragments.add(new FragmentPager("Paired Devices", new PairedDevicesFragment()));
//        fragments.add(new FragmentPager("Log", new LogFragment()));

        final float density = getResources().getDisplayMetrics().density;
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mMyFragmentPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mMyFragmentPagerAdapter);

        TitlePageIndicator indicator = (TitlePageIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(mViewPager);
        indicator.setBackgroundColor(0x00000000);
        indicator.setFooterColor(0xFFAA2222);
        indicator.setFooterLineHeight(1 * density); // 1dp
        indicator.setFooterIndicatorHeight(3 * density); // 3dp
        indicator.setFooterIndicatorStyle(TitlePageIndicator.IndicatorStyle.Underline);
        indicator.setTextColor(0xAA000000);
        indicator.setSelectedColor(Color.CYAN);
        indicator.setSelectedBold(true);

        setupArduino();

    }

    public void setupArduino(){
        final ArduinoConnect library = mArduinoConnectApp.getArduinoLibrary();
        mStateTV.setText("Connecting");
        library.setArduinoConnectListener(this, new ArduinoConnectListener() {
            @Override
            public void onConnectionStateChanged(ConnectionState state, BluetoothDevice device) {
                switch(state){
                    case STATE_CONNECTED:
                        mStateTV.setText(device.getName()+": Connected");
//                        mViewPager.setCurrentItem(1);
                        break;
                    case STATE_DISCONNECTED:
                        mStateTV.setText(device.getName()+": Disconnected");
//                        mViewPager.setCurrentItem(0);
                        break;
                    case STATE_ERROR_CONNECTING:
                        mStateTV.setText(device.getName()+": Error Connected");
                        break;
                    case CONNECTION_ERROR:
                        mStateTV.setText(device.getName()+": Connection Error");
                        break;
                    default:
                        mStateTV.setText(device.getName()+": State Change");
                        break;

                }
            }

            @Override
            public void onBluetoothStateChanged(int state) {
                switch(state){
                    case BluetoothAdapter.STATE_ON:
                        connectToDevice();
                        mStateTV.setText("Bluetooth On");
                        break;
                    case BluetoothAdapter.STATE_OFF:
                        mStateTV.setText("Bluetooth Off");
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        mStateTV.setText("Bluetooth Turning On");
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        mStateTV.setText("Bluetooth Turning Off");
                        break;
                    default:
                        mStateTV.setText("Error");
                        break;
                }

            }

            @Override
            public void onLightsStateChanged(LightColor color, LightType type) {

            }

            @Override
            public void onBluetoothMessageReceived(String message) {
                mStateTV.setText("Message: "+message);
            }

            @Override
            public void onDeviceChanged(BluetoothDevice device) {
                connectToDevice();
            }
        });

        if(library.bluetoothEnabled()){
            connectToDevice();
        } else {
            mStateTV.setText("You must enable Bluetooth");
        }
    }

    private void connectToDevice(){
        try {
            mArduinoConnectApp.getArduinoLibrary().startArduinoConnection(this);
        } catch (BluetoothDeviceException e) {
            switch(e.getCausedBy()){
                case BLUETOOTH_NOT_ENABLED:
                    mStateTV.setText("Enable Bluetooth");
                    break;
                case DEVICE_NOT_FOUND:
                    mStateTV.setText("Device Not Found");
                    break;
                case DEVICE_NOT_SET:
                    mStateTV.setText("Select a paired device before continuing");
                    break;
            }
        } catch (ArduinoLibraryException e) {
            switch(e.getCausedBy()){
                case LISTENER_NOT_SET:
                    mStateTV.setText("LISTENER NOT SET");
                    break;
            }
        }
    }

    private static class MyFragmentPagerAdapter extends FragmentPagerAdapter {

        public MyFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int index) {
            switch(index){
                case 0:
                    return PairedDevicesFragment.newInstance();
                case 1:
                    return LogFragment.newInstance();
            }

            return PairedDevicesFragment.newInstance();
        }

        @Override
        public CharSequence getPageTitle(int index) {
            switch(index){
                case 0:
                    return "Paired Devices";
                case 1:
                    return "Log";
            }
            return "";
        }

        @Override
        public int getCount() {
            return 2;
        }
    }



}
