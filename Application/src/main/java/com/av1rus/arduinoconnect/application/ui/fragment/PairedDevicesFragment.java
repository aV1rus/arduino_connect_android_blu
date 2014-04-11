package com.av1rus.arduinoconnect.application.ui.fragment;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.av1rus.arduinoconnect.application.R;
import com.av1rus.arduinoconnect.application.app.ArduinoConnectApp;
import com.av1rus.arduinoconnect.application.app.adapters.PairedDevicesAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aV1rus on 4/1/14.
 */
public class PairedDevicesFragment extends Fragment {

    Activity mActivity;
    Context mContext;
    List<BluetoothDevice> devices;
    TextView textView;
    ListView listView;

    private static PairedDevicesFragment mFragment;


    public static PairedDevicesFragment getInstance() {
        if(mFragment == null){
            mFragment = new PairedDevicesFragment();
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
        View view = inflater.inflate(R.layout.frag_paired_devices, container, false);
        textView = (TextView) view.findViewById(R.id.labelTV);
        listView = (ListView) view.findViewById(R.id.listView);
        return view;
    }


    @Override
    public void onStart(){
        super.onStart();

        devices = new ArrayList<BluetoothDevice>();
        devices.addAll(ArduinoConnectApp.getApp().getArduinoLibrary().getPairedDevices());
        final PairedDevicesAdapter adapter = new PairedDevicesAdapter(mActivity, devices);

        textView.setText("Set a new device:");
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ArduinoConnectApp.getApp().getArduinoLibrary().setBluetoothDevice(mActivity, devices.get(position));
                adapter.notifyDataSetInvalidated();
            }
        });
    }
}