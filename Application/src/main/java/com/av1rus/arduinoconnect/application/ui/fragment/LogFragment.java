package com.av1rus.arduinoconnect.application.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.av1rus.arduinoconnect.application.R;
import com.av1rus.arduinoconnect.application.app.adapters.LogAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aV1rus on 4/1/14.
 */
public class LogFragment extends Fragment {

    Activity mActivity;
    Context mContext;
    List<String> mMessages;
    TextView mLabelTV;
    ListView mListView;
    LogAdapter mLogListAdapter;

    private static LogFragment mLogFragment;


    public static LogFragment getInstance() {
        if(mLogFragment == null){
            mLogFragment = new LogFragment();
        }
        return mLogFragment;
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
        mLabelTV = (TextView) view.findViewById(R.id.labelTV);
        mListView = (ListView) view.findViewById(R.id.listView);
        return view;
    }


    @Override
    public void onStart(){
        super.onStart();
//      LogAdapter adapter = new LogAdapter(mActivity, messages);
        mLabelTV.setText("Log: ");

    }



    public void updateLog(String message){
        if(mListView == null) return;

        if(mMessages == null){
            mMessages = new ArrayList<String>();
        }
        if(mLogListAdapter == null){
            mLogListAdapter = new LogAdapter(mActivity, mMessages);
            mListView.setAdapter(mLogListAdapter);
        }


        mMessages.add(message);

        mLogListAdapter.notifyDataSetInvalidated();
    }

}
