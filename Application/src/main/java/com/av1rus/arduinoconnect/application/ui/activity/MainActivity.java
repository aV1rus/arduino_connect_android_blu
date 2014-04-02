package com.av1rus.arduinoconnect.application.ui.activity;

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
import com.av1rus.arduinoconnect.application.ui.fragment.PairedDevicesFragment;
import com.av1rus.arduinoconnect.arduinolib.ArduinoConnect;
import com.av1rus.arduinoconnect.arduinolib.exceptions.ArduinoLibraryException;
import com.av1rus.arduinoconnect.arduinolib.exceptions.BluetoothDeviceException;
import com.av1rus.arduinoconnect.arduinolib.listener.ArduinoConnectListener;
import com.av1rus.arduinoconnect.arduinolib.model.ConnectionState;
import com.av1rus.arduinoconnect.arduinolib.model.LightColor;
import com.av1rus.arduinoconnect.arduinolib.model.LightType;
import com.viewpagerindicator.TitlePageIndicator;

public class MainActivity extends SlidingMenuActivity {
    private ViewPager mViewPager;
    private MyFragmentPagerAdapter mMyFragmentPagerAdapter;
    private ArduinoConnectApp mArduinoConnectApp;

    TextView mStateTV;

    public static FragmentPager[] fragments = {
            new FragmentPager("Paired Devices", PairedDevicesFragment.newInstance()),
            new FragmentPager("Paired Devices 2", PairedDevicesFragment.newInstance())
    };
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
        ArduinoConnect library = mArduinoConnectApp.getArduinoLibrary();
        library.setArduinoConnectListener(new ArduinoConnectListener() {
            @Override
            public void onConnectionStateChanged(ConnectionState state, BluetoothDevice device) {
                switch(state){
                    case STATE_CONNECTED:
                        mStateTV.setText(device.getName()+": Connected");
                        mViewPager.setCurrentItem(1);
                        break;
                }
            }

            @Override
            public void onBluetoothStateChanged(int state) {

            }

            @Override
            public void onLightsStateChanged(LightColor color, LightType type) {

            }

            @Override
            public void onDeviceChanged(BluetoothDevice device) {

            }
        });

        if(library.bluetoothEnabled()){
            try {
                library.startArduinoConnection(this);
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
        } else {
            mStateTV.setText("You must enable Bluetooth");
        }
    }


    private static class MyFragmentPagerAdapter extends FragmentPagerAdapter {

        public MyFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int index) {
                return fragments[index].getFragment();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragments[position].getTitle();
        }

        @Override
        public int getCount() {
            return fragments.length;
        }
    }



}
