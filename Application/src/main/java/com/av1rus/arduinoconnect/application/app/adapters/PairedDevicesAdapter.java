package com.av1rus.arduinoconnect.application.app.adapters;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.av1rus.arduinoconnect.application.R;
import com.av1rus.arduinoconnect.application.app.ArduinoConnectApp;
import com.av1rus.arduinoconnect.arduinolib.service.ArduinoConnectService;
import com.av1rus.arduinoconnect.arduinolib.service.BluetoothService;

import java.util.List;

/**
 * Created by aV1rus on 4/1/14.
 */
public class PairedDevicesAdapter extends BaseAdapter {
    private List<BluetoothDevice> mItems;
    private Activity mActivity;


    public PairedDevicesAdapter(Activity activity, List<BluetoothDevice> items) {
        this.mActivity = activity;
        this.mItems = items;
    }

    public int getCount() {
        return mItems.size();
    }

    /**
     * Gets the SlideMenuRow at the position passed in
     *
     * @param position	An int that is the position of the SlideMenuRow to get
     *
     * @return	The SlideMenuRow at the position passed in
     */
    public Object getItem(int position) {
        return mItems.get(position);
    }

    /**
     * Gets the ID of the SlideMenuRow at the position passed in which is just the position itself
     *
     * @param position	An int that is the position of the SlideMenuRow
     *
     * @return	The ID of the SlideMenuRow at the position passed in which is just the position itself
     */
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        RelativeLayout row;
        final BluetoothDevice item = mItems.get(position);

        if (convertView == null) {
            row = (RelativeLayout) LayoutInflater.from(mActivity).inflate(R.layout.row_slide_menu, parent, false);
        }
        else {
            row = (RelativeLayout) convertView;
        }

        //Sets the name and image of the row in the SlideMenu
        TextView label = (TextView) row.findViewById(R.id.menu_label);

        label.setText(item.getName());

        if( (ArduinoConnectService.getSavedBluetoothDevice(mActivity) != null)
            && item.getAddress().equals(ArduinoConnectService.getSavedBluetoothDevice(mActivity).getAddress() )) {

                ((ImageView) row.findViewById(R.id.menu_icon)).setImageDrawable(mActivity.getResources().getDrawable(R.drawable.checkbox_checked));

        }else {
            ((ImageView) row.findViewById(R.id.menu_icon)).setImageDrawable(mActivity.getResources().getDrawable(R.drawable.checkbox_unchecked));
        }

        return row;
    }
}
