package com.av1rus.arduinoconnect.application.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.av1rus.arduinoconnect.application.R;
import com.av1rus.arduinoconnect.application.model.SlideMenuRow;

import java.util.List;

/**
 * This class is an adapter for SlideMenuRow. It extends BaseAdapter.
 * It has a List<SlideMenuRow> of items and a Context.
 * 
 * @version 1.0
 */
public class SlideMenuAdapter extends BaseAdapter {
	private List<SlideMenuRow> mItems;
	private Context mContext;

	/**
	 * A default constructor for the SlideMenuAdapter object which sets the context and pages to those passed in.
	 * 
	 * @param c			The Context the view is running in, through which it can access the current theme, resources, etc.
	 * @param pages		A List<SlideMenuRow> of the items to be shown on the SlideMenu
	 */
	public SlideMenuAdapter(Context c, List<SlideMenuRow> pages) {
		this.mContext = c;
		this.mItems = pages;
	}
	
	/**
	 * Gets the size of the List<SlideMenuRow>
	 * 
	 * @return	The number of items/rows on the SlideMenu
	 */
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
        final SlideMenuRow item = mItems.get(position);

        if (convertView == null) {
            row = (RelativeLayout) LayoutInflater.from(mContext).inflate(R.layout.row_slide_menu, parent, false);
        }
        else {
            row = (RelativeLayout) convertView;
        }

        //Sets the name and image of the row in the SlideMenu
        TextView label = (TextView) row.findViewById(R.id.menu_label);

        label.setText(item.getName());

        if (item.isEnabled()) {
            label.setTextColor(mContext.getResources().getColor(R.color.slidemenu_foreground_color));
        }
        else {
            label.setTextColor(mContext.getResources().getColor(R.color.slidemenu_disabled_foreground_color));
        }

        ((ImageView) row.findViewById(R.id.menu_icon)).setImageDrawable(item.getIcon());

        return row;
    }

    @Override
    public boolean isEnabled(int position) {
        return mItems.get(position).isEnabled();
    }

}
