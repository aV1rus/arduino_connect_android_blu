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


    public static PairedDevicesFragment newInstance() {
        return new PairedDevicesFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mActivity = getActivity();
        this.mContext = this.mActivity.getApplicationContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_paired_devices, container, false);
    }


    @Override
    public void onStart(){
        super.onStart();

        devices = new ArrayList<BluetoothDevice>();
        devices.addAll(ArduinoConnectApp.getApp().getArduinoLibrary().getPairedDevices());
        final PairedDevicesAdapter adapter = new PairedDevicesAdapter(mActivity, devices);

        final TextView textView = (TextView) mActivity.findViewById(R.id.labelTV);
        textView.setText("Set a new device:");
        ListView listView = (ListView) mActivity.findViewById(R.id.listView);
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