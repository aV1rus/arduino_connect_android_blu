package av1rus.arduinoconnect.MainFragments;


import av1rus.arduinoconnect.R;
import av1rus.arduinoconnect.background.BluetoothService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;  
import android.support.v4.app.Fragment;  
import android.view.LayoutInflater;  
import android.view.View;  
import android.view.ViewGroup;  
import android.widget.TextView;  
  
public class AutoDataFragment extends Fragment{  
	private Intent bIntent;
	TextView RPM_TV;
     public static AutoDataFragment newInstance(int index) {
 
    	 AutoDataFragment pageFragment = new AutoDataFragment();
         Bundle bundle = new Bundle();
         bundle.putInt("index", index);
         pageFragment.setArguments(bundle);
         return pageFragment;
     }
       
     @Override  
     public void onCreate(Bundle savedInstanceState) {  
         super.onCreate(savedInstanceState);  
         bIntent = new Intent(getActivity(), BluetoothService.class);
     }  
       
     @Override  
     public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) { 
    	int index = getArguments().getInt("index");
    	View view = inflater.inflate(R.layout.frag, container,	false);
    	RPM_TV = (TextView) view.findViewById(R.id.textView1);
    	RPM_TV.setText(BluetoothService.getBluetoothState());
 		
 		return view;
     } 
     @Override
     public void onResume(){
    	 super.onResume();
 		//getActivity().startService(bIntent);
 		getActivity().registerReceiver(broadcastReceiver, new IntentFilter(BluetoothService.BROADCAST_ACTION));
     }
     public void onPause(){
 		super.onPause();
 		getActivity().unregisterReceiver(broadcastReceiver);
 		//stopService(bIntent); 
 	}
     private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
	        @Override
	        public void onReceive(Context context, Intent intent) {
	        	updateUI(intent); 
	        }
	    }; 
	 private void updateUI(Intent intent) {
	    	int tag = intent.getIntExtra("tag", BluetoothService.LOG_CHANGE);	
	    	String data = intent.getStringExtra("data");	    	
	    	if(tag == BluetoothService.BLUETOOTH_STATE_CHANGE){
	    		RPM_TV.setText(data);
	    	}else if(tag == BluetoothService.LOG_CHANGE){
	    	}else if(tag == BluetoothService.RPM_CHANGE){
	    		RPM_TV.setText(data);
	    	}else if(tag == BluetoothService.RUNTIME_CHANGE){
	    		
	    	}else if(tag == BluetoothService.SPEED_CHANGE){
	    		
	    	}
	    }
}  