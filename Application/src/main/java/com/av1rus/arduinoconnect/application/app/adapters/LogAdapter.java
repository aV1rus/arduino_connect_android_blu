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

import java.util.List;

/**
 * Created by aV1rus on 4/1/14.
 */
public class LogAdapter extends BaseAdapter {
    private List<String> mItems;
    private Activity mActivity;


    public LogAdapter(Activity activity, List<String> items) {
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
        final String item = mItems.get(position);

        if (convertView == null) {
            row = (RelativeLayout) LayoutInflater.from(mActivity).inflate(R.layout.row_message, parent, false);
        }
        else {
            row = (RelativeLayout) convertView;
        }

        //Sets the name and image of the row in the SlideMenu
        TextView label = (TextView) row.findViewById(R.id.messageTV);

        label.setText(item);

        label.setTextColor(mActivity.getResources().getColor(R.color.black));

        return row;
    }
}
