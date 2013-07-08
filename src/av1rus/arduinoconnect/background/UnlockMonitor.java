package av1rus.arduinoconnect.background;

import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import av1rus.arduinoconnect.utils.Utils;

public class UnlockMonitor extends BroadcastReceiver {
	private final String TAG = "UNLOCKMONITOR";
	private boolean screenOff;
	Utils utils;

	public static Boolean BluetoothConnected = false;
	public static BluetoothSocket socket = null;
	public UnlockMonitor() {
		screenOff = true;
	}

	public void onReceive(Context context, Intent intent) {
    	Log.d(TAG, "Screen State change received");

		
		if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
			screenOff = true;
		}
		if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
			screenOff = true;
		}
		if (intent.getAction().equals(Intent.ACTION_USER_PRESENT)) {
			screenOff = false;
		}

		utils = (Utils) context.getApplicationContext();
		if(utils.getBluetoothDevice()!=null){
			Intent i = new Intent(context, BluetoothService.class);
			if(!screenOff){
				i.putExtra("screen_state", screenOff);
				context.startService(i);
			}else if(!Utils.activityActive){
				context.stopService(i);
			}
		}
	}
}
