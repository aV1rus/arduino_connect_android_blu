package com.av1rus.arduinoconnect.application.ui.activity;

import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.*;
import android.widget.AdapterView;
import android.widget.ListView;
import com.av1rus.arduinoconnect.application.R;
import com.av1rus.arduinoconnect.application.app.adapters.SlideMenuAdapter;
import com.av1rus.arduinoconnect.application.model.SlideMenuRow;

import java.util.ArrayList;

/**
 * Created by nick on 4/1/14.
 */
public class SlidingMenuActivity extends ActionBarActivity {

    String mTitle = "Arduino Connect";
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        getActionBar().setTitle(mTitle);
        getActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_main));



        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.drawer_list);
        mDrawerToggle = new ActionBarDrawerToggle(this,
                mDrawerLayout,
                R.drawable.ic_drawer,
                R.string.open,
                R.string.closed){
            public void onDrawerClosed(View view){
                  invalidateOptionsMenu();
            }
            public void onDrawerOpened(View view){
                invalidateOptionsMenu();
            }

        };

//        refreshDrawer();
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    protected void onPostCreate(Bundle bundle){
        super.onPostCreate(bundle);

        mDrawerToggle.syncState();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if (mDrawerToggle.onOptionsItemSelected(item)){
            return true;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void invalidateOptionsMenu(){
        super.invalidateOptionsMenu();

        refreshDrawer();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode == KeyEvent.KEYCODE_MENU){
            if(mDrawerLayout.isDrawerOpen(mDrawerList)){
                mDrawerLayout.closeDrawers();
            }else{
                mDrawerLayout.openDrawer(mDrawerList);
            }

            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    /**
     * Called whenever we call invalidateOptionsMenu
     * @param menu
     * @return
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu){
//        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
         return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.action_bar, menu);

        return super.onCreateOptionsMenu(menu);
    }

    private void refreshDrawer(){
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        ArrayList<SlideMenuRow> rows = new ArrayList<SlideMenuRow>();
        rows.add(new SlideMenuRow("Test", getResources().getDrawable(R.drawable.ic_launcher), true));

        mDrawerList.setAdapter(new SlideMenuAdapter(getApplicationContext(), rows));

        //Enabled Up Navigation
        getActionBar().setDisplayHomeAsUpEnabled(true);

        getActionBar().setIcon(R.drawable.ic_launcher);


        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                switch(position){
                    case 0 :
                        break;
                    default:
                        break;
                }

                mDrawerLayout.closeDrawer(mDrawerList);
            }
        });
    }


}
