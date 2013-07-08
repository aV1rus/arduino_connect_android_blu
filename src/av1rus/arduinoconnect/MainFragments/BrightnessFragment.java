package av1rus.arduinoconnect.MainFragments;


import av1rus.arduinoconnect.MActivity;
import av1rus.arduinoconnect.R;
import av1rus.arduinoconnect.background.BluetoothService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;  
import android.support.v4.app.Fragment;  
import android.util.DisplayMetrics;
import android.view.LayoutInflater;  
import android.view.View;  
import android.view.ViewGroup;  
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;  
import android.widget.TableRow.LayoutParams;
import static av1rus.arduinoconnect.utils.Uses.*;
  
public class BrightnessFragment extends Fragment { 
	private Intent bIntent; 
    TextView HaloBright;
    TextView IntBright;
    TextView FogBright;
     public static BrightnessFragment newInstance(int index) {
 
    	 BrightnessFragment pageFragment = new BrightnessFragment();
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
    	View view = inflater.inflate(R.layout.frag_brightness, container,false);
    	
    	DisplayMetrics dm = new DisplayMetrics();
	    getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
	    int screenWidth = dm.widthPixels;
	    int screenHeight = dm.heightPixels;
 		TextView HaloText = (TextView) view.findViewById(R.id.halotextView);
 		TextView InteriorText = (TextView) view.findViewById(R.id.interiortextView);
 		TextView FogsText = (TextView) view.findViewById(R.id.fogtextView);
 		HaloBright = (TextView) view.findViewById(R.id.halobright);
 		IntBright = (TextView) view.findViewById(R.id.interiobright);
 		FogBright = (TextView) view.findViewById(R.id.fogbright);
 		
 		int textviewWidth = (screenWidth-70)/3;
 		HaloText.setLayoutParams(new LayoutParams(textviewWidth,LayoutParams.WRAP_CONTENT));
 		FogsText.setLayoutParams(new LayoutParams(textviewWidth,LayoutParams.WRAP_CONTENT));
 		InteriorText.setLayoutParams(new LayoutParams(textviewWidth,LayoutParams.WRAP_CONTENT));
 		HaloText.setPadding(10, 0, 10, 5);
 		InteriorText.setPadding(10, 0, 10, 5);
 		FogsText.setPadding(10, 0, 10, 5);
 		
    	ImageButton HaloBrightUP = (ImageButton) view.findViewById(R.id.haloBrightUp);
		ImageButton HaloBrightDown = (ImageButton) view.findViewById(R.id.haloBrightDown);
		ImageButton InteriorBrightUP = (ImageButton) view.findViewById(R.id.interiorBrightUp);
		ImageButton InteriorBrightDown = (ImageButton) view.findViewById(R.id.interiorBrightDown);
		ImageButton FogBrightUP = (ImageButton) view.findViewById(R.id.fogBrightUp);
		ImageButton FogBrightDown = (ImageButton) view.findViewById(R.id.fogBrightDown);
		HaloBrightUP.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				updateHaloBright(BluetoothService.Halo_Brightness+10);
				MActivity.send("+"+B_HALOS);
			}
		});
		HaloBrightDown.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				updateHaloBright(BluetoothService.Halo_Brightness-10);
				MActivity.send("-"+B_HALOS);
			}
		});
		
		//INTERIOR
		InteriorBrightUP.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				updateIntBright(BluetoothService.Interior_Brightness+10);
				MActivity.send("+"+B_INTERIOR);
			}
		});
		InteriorBrightDown.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				updateIntBright(BluetoothService.Interior_Brightness-10);
				MActivity.send("-"+B_INTERIOR);
			}
		});
		
		//FOG
		FogBrightUP.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				updateFogBright(BluetoothService.Fog_Brightness+10);
				MActivity.send("+"+B_FOGS);
			}
		});
		FogBrightDown.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				updateFogBright(BluetoothService.Fog_Brightness-10);
				MActivity.send("-"+B_FOGS);
			}
		});
    	
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
     public void updateHaloBright(int i){
		 BluetoothService.Halo_Brightness = i;
 		HaloBright.setText(""+BluetoothService.Halo_Brightness);
     }
	 public void updateIntBright(int i){
		 BluetoothService.Interior_Brightness = i;
 		IntBright.setText(""+BluetoothService.Interior_Brightness);
	 }
	 public void updateFogBright(int i){
		 BluetoothService.Fog_Brightness = i;
 		FogBright.setText(""+BluetoothService.Fog_Brightness);
	}
     private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
	        @Override
	        public void onReceive(Context context, Intent intent) {
	        	updateUI(intent); 
	        }
	    }; 
	    private void updateUI(Intent intent) {
	    	int tag = intent.getIntExtra("tag", BluetoothService.LOG_CHANGE);	
	    	if(tag == BluetoothService.BRIGHTNESS_HALO_CHANGE){
	    		HaloBright.setText(""+BluetoothService.Halo_Brightness);
	    	}else if(tag == BluetoothService.BRIGHTNESS_FOG_CHANGE){
	    		FogBright.setText(""+BluetoothService.Fog_Brightness);
	    	}else if(tag == BluetoothService.BRIGHTNESS_INTERIOR_CHANGE){
	    		IntBright.setText(""+BluetoothService.Interior_Brightness);
	    	}
	    }
}  