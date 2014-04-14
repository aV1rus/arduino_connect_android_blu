package com.av1rus.arduinoconnect.application.ui.activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.av1rus.arduinoconnect.application.R;
import com.av1rus.arduinoconnect.application.app.ArduinoConnectApp;
import com.av1rus.arduinoconnect.application.model.listener.ControlFragmentListener;
import com.av1rus.arduinoconnect.application.ui.fragment.ControlFragment;
import com.av1rus.arduinoconnect.application.ui.fragment.LogFragment;
import com.av1rus.arduinoconnect.application.ui.fragment.PairedDevicesFragment;
import com.av1rus.arduinoconnect.arduinolib.ArduinoConnect;
import com.av1rus.arduinoconnect.arduinolib.exceptions.ArduinoLibraryException;
import com.av1rus.arduinoconnect.arduinolib.exceptions.BluetoothDeviceException;
import com.av1rus.arduinoconnect.arduinolib.listener.ArduinoConnectListener;
import com.av1rus.arduinoconnect.arduinolib.model.ConnectionState;
import com.av1rus.arduinoconnect.arduinolib.service.ArduinoConnectService;
import com.av1rus.arduinoconnect.arduinolib.utils.MyDialog;
import com.av1rus.arduinoconnect.halowidget.ui.HaloWidget;
import com.av1rus.datacontract.model.LightColor;
import com.av1rus.datacontract.model.LightType;
import com.viewpagerindicator.TitlePageIndicator;

public class MainActivity extends SlidingMenuActivity {
    private ViewPager mViewPager;
    private MyFragmentPagerAdapter mMyFragmentPagerAdapter;
    private ArduinoConnectApp mArduinoConnectApp;
    ArduinoConnectService mArduinoService = null;


    TextView mStateTV;
    public HaloWidget mLightsView;


    @Override
    public void onStop(){
        super.onStop();
        unbindService(mServiceConn);

    }

    @Override
    public void onResume(){
        super.onResume();

        //create a new library
        mArduinoConnectApp = ArduinoConnectApp.getApp();
//        mArduinoConnectApp.setArduinoLibrary(new ArduinoConnect());
        bindService(new Intent(this, ArduinoConnectService.class), mServiceConn, BIND_AUTO_CREATE);
//        PairedDevicesFragment.getInstance();
//        ControlFragment.getInstance(mControlFragmentListener);
//        LogFragment.getInstance();

//        setupArduino();

    }


    //    public static List<FragmentPager> fragments;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mStateTV = (TextView) findViewById(R.id.stateTV);
        mLightsView = (HaloWidget) findViewById(R.id.haloWidget);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);

        mLightsView.setTitle("Shak1ra");


//
//        fragments = new ArrayList<FragmentPager>();
//        fragments.add(new FragmentPager("Paired Devices", new PairedDevicesFragment()));
//        fragments.add(new FragmentPager("Log", new LogFragment()));

        final float density = getResources().getDisplayMetrics().density;
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
        indicator.setSelectedColor(getResources().getColor(R.color.primary_color));
        indicator.setSelectedBold(true);

    }

    public void setupArduino(){
        mStateTV.setText("Connecting");
        mArduinoService.setArduinoConnectListener(new ArduinoConnectListener() {
//            @Override
//            public void onServiceStateChange(ConnectionState state) {
//                switch(state){
//                    case STATE_CONNECTED:
//                        mStateTV.setText("Service Connected");
////                        if(library.bluetoothEnabled()){
////                            connectToDevice();
////                        } else {
////                            mStateTV.setText("You must enable Bluetooth");
////                        }
//                        break;
//                    case STATE_DISCONNECTED:
//                        mStateTV.setText("Service Disconnected");
//                        break;
//                }
//            }

            @Override
            public void onConnectionStateChanged(ConnectionState state, BluetoothDevice device) {
                switch(state){
                    case STATE_CONNECTED:
                        mStateTV.setText(device.getName()+": Connected");
                        mViewPager.setCurrentItem(1);
                        break;
                    case STATE_DISCONNECTED:
                        mStateTV.setText(device.getName()+": Disconnected");
                        mViewPager.setCurrentItem(0);
                        break;
                    case STATE_ERROR_CONNECTING:
                        mStateTV.setText(device.getName()+": Error Connecting");
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
            public void onLightsStateChanged(LightType type, LightColor color) {
                mLightsView.setLightsState(type, color);
            }

            @Override
            public void onBluetoothMessageReceived(String message) {
                LogFragment.getInstance().updateLog(message);
                mStateTV.setText("Message: "+message);
            }

            @Override
            public void onBluetoothError(String error) {
                MyDialog.notifyUserDialog(MainActivity.this, error, false);
            }

            @Override
            public void onDeviceChanged(BluetoothDevice device) {
                connectToDevice();
            }
        });

//        library.startService(this);
        if(mArduinoService.bluetoothEnabled()){
            connectToDevice();
        } else {
            mStateTV.setText("You must enable Bluetooth");
        }
    }

    private void connectToDevice(){
        try {
            mArduinoService.startArduinoConnection();
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId() == R.id.action_voice_command){
            mLightsView.setHeadHaloColor(HaloWidget.HALO_RED);
            mLightsView.setFogHaloColor(HaloWidget.HALO_RED);
            Toast.makeText(this, "Voice Command", Toast.LENGTH_LONG).show();
        }

        return super.onOptionsItemSelected(item);
    }

    private class MyFragmentPagerAdapter extends FragmentPagerAdapter {

        String titles[] = {"Paired Devices", "Controls", "Log"};

        public MyFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int index) {
            switch(index){
                case 0:
                    return PairedDevicesFragment.getInstance();
                case 1:
                    return ControlFragment.getInstance(mControlFragmentListener);
                case 2:
                    return LogFragment.getInstance();
            }

            return PairedDevicesFragment.getInstance();
        }

        @Override
        public CharSequence getPageTitle(int index) {
            return titles[index];
        }

        @Override
        public int getCount() {
            return titles.length;
        }
    }


    private ControlFragmentListener mControlFragmentListener = new ControlFragmentListener() {
        @Override
        public void onLightSwitchChanged(LightType type, LightColor color) {
            if(mArduinoService != null){
                mArduinoService.setLightState(type, color);
            }
        }
    };



    private ServiceConnection mServiceConn = new ServiceConnection(){
        public void onServiceConnected(ComponentName name, IBinder service) {
            mArduinoService = ((ArduinoConnectService.LocalBinder)service).getService();
            if(mArduinoService != null)
                setupArduino();
        }

        public void onServiceDisconnected(ComponentName name) {
            mArduinoService = null;
        }
    };
}
