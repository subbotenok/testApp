package com.testapp.activity;

import com.testapp.fragment.SettingsFragment;
import com.testapp.service.WeatherDataListService;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;

public class SettingsActivity extends Activity {
	
	   @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);

	        getFragmentManager().beginTransaction()
	                .replace(android.R.id.content, new SettingsFragment())
	                .commit();
	        
	        doBindService();
	    }
	   
		public WeatherDataListService mDataListService = null;
		
		private ServiceConnection mConnection = new ServiceConnection() {
		    public void onServiceConnected(ComponentName className, IBinder service) {
		        mDataListService = ((WeatherDataListService.ServiceBinder)service).getService();
		    }

		    public void onServiceDisconnected(ComponentName className) {
		        mDataListService = null;
		    }
		};
		private boolean mIsBound = false;
		
		
		
		void doBindService() {
			bindService(new Intent(this, 
		    		WeatherDataListService.class), mConnection, Context.BIND_AUTO_CREATE);
		    mIsBound = true;
		}
		
		void doUnbindService() {
		    if (mIsBound) {
		        // Detach our existing connection.
		    	unbindService(mConnection);
		        mIsBound = false;
		    }
		}
		
		

		@Override
		protected void onDestroy() {
		    super.onDestroy();
		    doUnbindService();
		}

}
