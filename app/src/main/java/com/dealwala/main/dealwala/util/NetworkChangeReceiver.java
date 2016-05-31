package com.dealwala.main.dealwala.util;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import com.afollestad.materialdialogs.MaterialDialog;
import com.dealwala.main.dealwala.R;
import com.dealwala.main.dealwala.login.LoginActivity;

public class NetworkChangeReceiver extends BroadcastReceiver {
	
	  @Override
	  public void onReceive(final Context context, final Intent intent) {
		  
		  Log.v("Notification", "Receiver Network changed");
		  
		  	if(intent.getExtras() != null)
		  	{		  
		  		/*final ConnectivityManager connMgr = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		  					    
				final NetworkInfo info = connMgr.getActiveNetworkInfo();
			
				    if (info != null && info.isConnected()) {				    	
				    	
				    	boolean wifiConnected = info.getType() == ConnectivityManager.TYPE_WIFI;
        	  			boolean mobileDataConnected = info.getType() == ConnectivityManager.TYPE_MOBILE;
				    	
        	  			Log.v("Notification", "Wifi connected : " + wifiConnected + " Mobile data connected: " + mobileDataConnected);
        	  			
        	  			if(wifiConnected || mobileDataConnected)
        	  			{
        	  				ModuleClass.isInternetOn = true;
        	  			}else{
        	  				ModuleClass.isInternetOn = false;
        	  			}
				    }else{
				    	ModuleClass.isInternetOn = false;
				    }*/

				new CheckInternetConnectionTask(context).execute();
		  	}
	  
	}

	public void checkInternetInLollipop(final Context context) {
		final ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

		Log.v("Notification", "Checking internet lollipop");

		Log.v("Notification", (connectivity != null) ? "Connectivity NOT null" : "Connectivity NULL");

		if (connectivity != null) {

			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

				NetworkRequest.Builder builder = new NetworkRequest.Builder();

				builder.addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);

				connectivity.requestNetwork(builder.build(), new ConnectivityManager.NetworkCallback() {
					@Override
					public void onAvailable(Network network) {
						Log.d("Notification", "Internet connected");

						ModuleClass.isInternetOn = true;

						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
							connectivity.unregisterNetworkCallback(this);
						}
					}

					@Override
					public void onLost(Network network) {
						Log.v("Notification", "LOST connection");
						ModuleClass.isInternetOn = false;
					}

					@Override
					public void onLosing(Network network, int maxMsToLive) {
						super.onLosing(network, maxMsToLive);
						Log.v("Notification", "LOOSING connection");
					}
				});
			}

		} else {
			ModuleClass.isInternetOn = false;
		}
	}

	class CheckInternetConnectionTask extends AsyncTask<String, String, String> {

		Context mContext;
		CheckInternetConnectionTask(Context context){
			this.mContext = context;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... params) {

			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

				checkInternetInLollipop(mContext);

				Runnable timeoutThread = new Runnable() {
					@Override
					public void run() {
						try {
							Thread.sleep(5000);

							if (!ModuleClass.isInternetOn) {

							}

						} catch (InterruptedException ie) {
							ie.printStackTrace();
						}
					}
				};

				timeoutThread.run();

			} else {
				ModuleClass.isInternetOn = new ConnectionDetector(mContext).isConnectingToInternet();
			}

			return null;
		}

		@Override
		protected void onPostExecute(String s) {
			super.onPostExecute(s);

			if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
				if (ModuleClass.isInternetOn) {
					Log.v("Notification","Internet is connected");

				} else {

				}
			}
		}
	}
	  
}