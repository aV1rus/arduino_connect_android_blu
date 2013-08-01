package av1rus.arduinoconnect.background;
/*
 * Created by Nick Maiello (aV1rus)
 * January 2, 2013
 * 
 */
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import av1rus.arduinoconnect.MActivity;
import av1rus.arduinoconnect.SettingsActivity;
import av1rus.arduinoconnect.utils.SamplesUtils;
import av1rus.arduinoconnect.utils.Utils;

public class BluetoothService extends Service {
	private final static String TAG = "BluetoothService";
	private final IBinder mBinder = new MyBinder();
	private static ArrayList<String> list = new ArrayList<String>();
	private static String currentBluetoothState = "Thinking";

	public static Boolean isConnected = false;
	private static Handler _handler = new Handler();
	private Object obj1 = new Object();
	private static Object obj2 = new Object();

	private int MAX = 250;
	Utils utils;

	private int RPM = 0;
	private int RunTime = 0;
	private int Speed = 0;

	public static final String BROADCAST_ACTION = "av1rus.arduinoconnect";
	private final Handler handler = new Handler();

	public static final int BLUETOOTH_STATE_CHANGE = 0;
	public static final int LOG_CHANGE = 1;
	public static final int RPM_CHANGE = 2;
	public static final int RUNTIME_CHANGE = 3;
	public static final int SPEED_CHANGE = 4;

	public static final int HEAD_HALO_CHANGE = 5;
	public static final int FOG_HALO_CHANGE = 6;
	public static final int FOG_CHANGE = 7;
	public static final int INTERIOR_CHANGE = 8;

	public static final int BRIGHTNESS_HALO_CHANGE = 9;
	public static final int BRIGHTNESS_INTERIOR_CHANGE = 10;
	public static final int BRIGHTNESS_FOG_CHANGE = 11;

	public static boolean Head_Halo_Red_On = false;
	public static boolean Head_Halo_White_On = true;
	public static boolean Head_Halo_Both_On = false;
	public static boolean Fog_Halo_Red_On = false;
	public static boolean Fog_Halo_White_On = true;
	public static boolean Fog_Halo_Both_On = false;
	public static boolean Fog_On = false;
	public static boolean Interior_On = false;

	public static int Halo_Brightness = 255;
	public static int Interior_Brightness = 255;
	public static int Fog_Brightness = 255;
	
	public static boolean vehicleisOn = false;

	Intent intent;

	@Override
	public void onCreate() {
		super.onCreate();
		//Log.d(TAG, "onCreate");
		intent = new Intent(BROADCAST_ACTION);
		utils = (Utils) this.getApplicationContext();
		// start();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		//Log.d(TAG, "onStartCommand");
		// handler.removeCallbacks(sendUpdatesToUI);
		// handler.postDelayed(sendUpdatesToUI, 1000); // 1 second
		start();
		return Service.START_STICKY;
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return mBinder;
	}

	public class MyBinder extends Binder {
		public BluetoothService getService() {
			return BluetoothService.this;
		}
	}

	public static List<String> getWordList() {
		return list;
	}

	public static String getBluetoothState() {
		return currentBluetoothState;
	}

	protected void connectToBluetooth(BluetoothDevice device) {
		isConnected = true;
		//Log.d(TAG, "Connecting to: " + device.getName());
		BluetoothAdapter mBluetoothAdapter = BluetoothAdapter
				.getDefaultAdapter();
		if (mBluetoothAdapter == null) {
			return;
		}
		if (!mBluetoothAdapter.isEnabled()) {
			currentBluetoothState = "DISABLED";
			DisplayLoggingInfo(BLUETOOTH_STATE_CHANGE);
			return;
		}

		if (device.getBondState() == BluetoothDevice.BOND_BONDED) {
			//Log.d(TAG, "Devices are paired");
			try {
				//Log.d(TAG, "Connecting to bluetooth");
				// Create a Socket connection: need the server's UUID number of
				// registered
				if (Utils.socket == null) {
					Method m = device.getClass().getMethod(
							"createRfcommSocket", new Class[] { int.class });
					Utils.socket = (BluetoothSocket) m.invoke(device, 1);
					Utils.socket.connect();
					//Log.d(TAG, ">>Client connected");
				}
				isConnected = true;
				currentBluetoothState = utils.getBluetoothDevice().getName()
						+ "\nConnected";
				/*
				 * if(!Utils.activityActive){ Log.d(TAG,
				 * "MActivity not running"); Intent myIntent = new
				 * Intent(getApplicationContext(),SettingsActivity.class);
				 * startActivity(myIntent); }
				 */
				//sendToBluetooth("!");
				DisplayLoggingInfo(BLUETOOTH_STATE_CHANGE);

			} catch (Exception e) {
				//Log.i(TAG, ">> Error connecting", e);
				isConnected = false;
				currentBluetoothState = utils.getBluetoothDevice().getName() + "\nError.";
				DisplayLoggingInfo(BLUETOOTH_STATE_CHANGE);
				return;
			}
			//Log.d(TAG, "Current State: " + currentBluetoothState);
			if (isConnected)
				try {
					Utils.inputStream = Utils.socket.getInputStream();
					Utils.outputStream = Utils.socket.getOutputStream();
					int read = -1;
					final byte[] bytes = new byte[1024];
					String reading = "";
					while (isConnected) {
						synchronized (obj1) {
							/*
							new Thread() {
				    			public void run() {
				    						try {
				    							Thread.sleep(300);
				    						} catch (InterruptedException e) {
				    							e.printStackTrace();
				    						}
				    			}
				    		}.start();*/
							// Log.d(TAG, "Maintaining Connection");
							read = Utils.inputStream.read(bytes);
							
							// Log.d(TAG, "Read Bytes");
							if (read > 15) {
								final int count = read;
								String str = SamplesUtils.byteToHex(bytes, count);
								String str2 = SamplesUtils.hexToString(str);
								reading += str2;
								if (reading.length() > 6) {
									if(!reading.trim().equals("DT:") && !reading.trim().equals("DT") && !reading.trim().equals("D")){
									handleReceived(reading);
									reading = "";}
								}
							} else if (read > 0) {
								final int count = read;
								String str = SamplesUtils.byteToHex(bytes,
										count);
								String str2 = SamplesUtils.hexToString(str);
								reading += str2;
								if (reading.length() > 6) {
									handleReceived(reading);
									reading = "";
								}
							}
							// Log.d(TAG, "Done Reading");
						}
					}

				} catch (Exception e) {
					//Log.i(TAG, ">> Error Bluetooth", e);
					isConnected = false;
					currentBluetoothState = utils.getBluetoothDevice()
							.getName() + "\nError..";
					DisplayLoggingInfo(BLUETOOTH_STATE_CHANGE);

					return;
				} finally {
					if (Utils.socket != null) {
						try {
							//Log.d(TAG, ">>Client Socket Close");
							Utils.socket.close();
							isConnected = false;
							DisplayLoggingInfo(BLUETOOTH_STATE_CHANGE);
							Utils.socket = null;
							// this.finish();
							return;
						} catch (IOException e) {
							isConnected = false;
							//Log.e(TAG, ">>", e);
						}
					}
				}
			isConnected = false;

		} else {
			//Log.d(TAG, "Devices are not paired anymore");
		}
	}

	public static void sendToBluetooth(final String s) {
		String editText = s;
		byte bytes[] = editText.getBytes();
		try {
			if (Utils.outputStream != null) {
				synchronized (obj2) {
					Utils.outputStream.write(bytes);
				}
			} else {
				list.add("**ERROR**" + s);
			}
		} catch (IOException e) {
			//Log.e(TAG, ">>", e);
			e.printStackTrace();
		}
		list.add("-->" + s);

	}

	protected boolean started = false;

	public void start() {
		new Thread() {
			public void run() {
				if (!started) {
					started = true;
					while (true) {
						if (!isConnected && utils.getBluetoothDevice() != null) {
							connectToBluetooth(utils.getBluetoothDevice());
							list.add("Looping again SOON to verify connected");
						} else {
							// list.add("**All seems good.");

						}

						try {
							int time = 5 * 60 * 1000;
							Thread.sleep(8000);
							if (isConnected)
								Thread.sleep(time);
							//Log.d(TAG, "Looping again to make sure connected");
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}.start();
	}

	private void DisplayLoggingInfo(int bTAG) {
		// Log.d(TAG, "entered DisplayLoggingInfo");
		intent.putExtra("tag", bTAG);
		String data = null;

		if (bTAG == BLUETOOTH_STATE_CHANGE) {
			data = currentBluetoothState;
		} else if (bTAG == LOG_CHANGE) {
			for (int i = 0; i < list.size(); i++) {
				data += list.get(i) + "\n";
			}
		} else if (bTAG == RPM_CHANGE) {
			data = RPM+"";
		} else if (bTAG == RUNTIME_CHANGE) {
			data = RunTime + " sec";
		} else if (bTAG == SPEED_CHANGE) {
			data = Speed + " K/H";
		}

		intent.putExtra("data", data);
		sendBroadcast(intent);
	}

	private void handleReceived(String s) {
		String regex = "([ \\t\\r]*\\n[ \\t\\r]*)+"; // Only this line is
														// changed.
		String str[] = s.split(regex);
		for (int i = 0; i < str.length; i++) {
			String workingWith = str[i];
			try {
				if (str[i].trim().equals("R") && i < str.length - 1) {
					workingWith += str[i + 1];
				}
				if (str[i].trim().equals("R") && workingWith.equals("R"))
					return;
				final String split[] = workingWith.split(":");
				if (split.length > 1) {
					if (!split[0].trim().equals("PM"))
						if (split[0].trim().equals("RPM")) {
							if (Integer.parseInt(split[1]) != RPM) {
								RPM = Integer.parseInt(split[1]);
								DisplayLoggingInfo(RPM_CHANGE);
							}

						} else if (split[0].trim().equals("RunTime")) {
							if (Integer.parseInt(split[1]) != RunTime) {
								RunTime = Integer.parseInt(split[1]);
								DisplayLoggingInfo(RUNTIME_CHANGE);
							}

						} else if (split[0].trim().equals("Speed")) {
							if (Integer.parseInt(split[1]) != Speed) {
								Speed = Integer.parseInt(split[1]);
								DisplayLoggingInfo(SPEED_CHANGE);
							}

						} else {
							if (list.size() > MAX)
								list.remove(0);
							list.add("<--" + workingWith);
							DisplayLoggingInfo(LOG_CHANGE);
							if (split[0].trim().equals("DT")) {
								_handler.post(new Runnable() {
									public void run() {
										try {
											//list.add("^^Light data");
											//Log.d(TAG, "Handling Light data");
											handleLightData(split);
										} catch (Exception e) {
											//Log.d(TAG,"Error handling light data" + e);
										}
									}
								});
							}
						}
				} else {
					if (list.size() > MAX)
						list.remove(0);
					list.add("<--" + workingWith);
					if(workingWith.toLowerCase().equals("off")) vehicleisOn = false;
					else if(workingWith.toLowerCase().equals("on")) vehicleisOn = true;
					DisplayLoggingInfo(LOG_CHANGE);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private void handleLightData(String[] data) {
		if (data.length >= 4 && data[0].trim().equals("DT")) {
			boolean areON = false;
			int Brightness = 0;
			if (data[1].trim().equals("S")) { // / 'S' for Switch
				try {
					if (1 == Integer.parseInt(data[3].trim())) {
						areON = true;
					}
					if (data[2].trim().equals("HB")) { // Handling Heads Both
						if (Head_Halo_Both_On != areON) { // Current Data is
															// different
							Head_Halo_Both_On = areON;
							Head_Halo_White_On = !areON;
							Head_Halo_Red_On = !areON;
							DisplayLoggingInfo(HEAD_HALO_CHANGE);
						}
					} else if (data[2].trim().equals("HW")) {// Handling Heads
																// White
						if (Head_Halo_White_On != areON) { // Current Data is
															// different
							Head_Halo_White_On = areON;
							Head_Halo_Red_On = !areON;
							Head_Halo_Both_On = false;
							DisplayLoggingInfo(HEAD_HALO_CHANGE);
						}
					} else if (data[2].trim().equals("HR")) {// Handling Heads
																// Red
						if (Head_Halo_Red_On != areON) { // Current Data is
															// different
							Head_Halo_Red_On = areON;
							Head_Halo_White_On = !areON;
							Head_Halo_Both_On = false;
							DisplayLoggingInfo(HEAD_HALO_CHANGE);
						}
					}
					if (data[2].trim().equals("FB")) { // Handling Fogs Both
						if (Fog_Halo_Both_On != areON) { // Current Data is
															// different
							Fog_Halo_Both_On = areON;
							Fog_Halo_White_On = !areON;
							Fog_Halo_Red_On = !areON;
							DisplayLoggingInfo(FOG_HALO_CHANGE);
						}
					} else if (data[2].trim().equals("FW")) {// Handling Fogs
																// White
						if (Fog_Halo_White_On != areON) { // Current Data is
															// different
							Fog_Halo_White_On = areON;
							Fog_Halo_Both_On = false;
							Fog_Halo_Red_On = !areON;
							DisplayLoggingInfo(FOG_HALO_CHANGE);
						}
					} else if (data[2].trim().equals("FR")) {// Handling Fogs
																// Red
						if (Fog_Halo_Red_On != areON) { // Current Data is
														// different
							Fog_Halo_Red_On = areON;
							Fog_Halo_White_On = !areON;
							Fog_Halo_Both_On = false;
							DisplayLoggingInfo(FOG_HALO_CHANGE);
						}
					}
					if (data[2].trim().equals("IN")) {// Handling Fogs Red
						if (Interior_On != areON) { // Current Data is different
							Interior_On = areON;
							DisplayLoggingInfo(INTERIOR_CHANGE);
						}
					}
					if (data[2].trim().equals("FS")) {// Handling Fogs Red
						if (Fog_On != areON) { // Current Data is different
							Fog_On = areON;
							DisplayLoggingInfo(FOG_CHANGE);
						}
					}
				} catch (Exception e) {
				}
			} else if (data[1].trim().equals("B")) { // / 'B' for Brightness
				try {
					Brightness = Integer.parseInt(data[3].trim());
					if (Brightness >= 0 || Brightness <= 255) {
						if (data[2].trim().equals("HB")) {
							if (Halo_Brightness != Brightness) {
								Halo_Brightness = Brightness;
								DisplayLoggingInfo(BRIGHTNESS_HALO_CHANGE);
							}
						} else if (data[2].trim().equals("IB")) {
							if (Interior_Brightness != Brightness) {
								Interior_Brightness = Brightness;
								DisplayLoggingInfo(BRIGHTNESS_INTERIOR_CHANGE);
							}
						} else if (data[2].trim().equals("FB")) {
							if (Fog_Brightness != Brightness) {
								Fog_Brightness = Brightness;
								DisplayLoggingInfo(BRIGHTNESS_FOG_CHANGE);
							}
						}
					}
				} catch (Exception e) {
				}
			}
		}
	}

	@Override
	public void onDestroy() {
		// handler.removeCallbacks(sendUpdatesToUI);
		super.onDestroy();
	}
}
