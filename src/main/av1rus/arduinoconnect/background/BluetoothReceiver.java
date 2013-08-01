package av1rus.arduinoconnect.background;
/*
 * Created by Nick Maiello (aV1rus)
 * January 2, 2013
 * 
 */
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import av1rus.arduinoconnect.utils.Utils;

public class BluetoothReceiver extends BroadcastReceiver{
	Utils utils;
	public void onCreate() {
    	Log.d("BluetoothReceiver","onCreate");
	}
	@Override
	public void onReceive(Context context, Intent intent) {
		utils = (Utils) context.getApplicationContext();
		String action = intent.getAction();
		try{
	    	BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
	    	Log.d("BluetoothReceiver","onReceive");
	    	if(utils.getBluetoothDevice()!= null && device == utils.getBluetoothDevice()){ //Make sure handling only device needed
	
	        	Intent i = new Intent(context, BluetoothService.class);
	    	
		        if (BluetoothDevice.ACTION_FOUND.equals(action)) {
			           //Device found
		        	Log.d("BluetoothReceiver","actionFound");
					context.startService(i);
			    }else if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
			            //Device is now connected
			    }else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
			            //Done searching
					context.startService(i);
			    }else if (BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED.equals(action)) {
			            //Device is about to disconnect
					context.stopService(i);
			    }else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
		            //Device has disconnected
					context.stopService(i);
		        }  
	    	}
		}catch(Exception e){
			
		}
	}
	/*
	//The BroadcastReceiver that listens for bluetooth broadcasts
	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
	    @Override
	    public void onReceive(Context context, Intent intent) {
	        String action = intent.getAction();
	        BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

	        if (BluetoothDevice.ACTION_FOUND.equals(action)) {
	           //Device found
	        }
	        else if (device.ACTION_ACL_CONNECTED.equals(action)) {
	            //Device is now connected
	        }
	        else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
	            //Done searching
	        }
	        else if (device.ACTION_ACL_DISCONNECT_REQUESTED.equals(action)) {
	            //Device is about to disconnect
	        }
	        else if (device.ACTION_ACL_DISCONNECTED.equals(action)) {
	            //Device has disconnected
	        }           
	    }
	};
	@Override
	public void onReceive(Context context, Intent intent) {
		IntentFilter filter1 = new IntentFilter(BluetoothDevice.ACTION_ACL_CONNECTED);
	    IntentFilter filter2 = new IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED);
	    IntentFilter filter3 = new IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECTED);
	    context.registerReceiver(mReceiver, filter1);
	    context.registerReceiver(mReceiver, filter2);
	    context.registerReceiver(mReceiver, filter3);
		
	}
	*/
}
