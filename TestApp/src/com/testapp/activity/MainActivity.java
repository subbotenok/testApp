package com.testapp.activity;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;

import com.example.testapp.R;
import com.testapp.adapter.WeatherPagerAdapter;
import com.testapp.data.WeatherDataList;
import com.testapp.fragment.WeatherFragment;
import com.testapp.service.WeatherDataListService;
import com.testapp.service.WeatherDataListService.OnNewCityListener;

public class MainActivity extends FragmentActivity {

	WeatherPagerAdapter mWeatherPagerAdapter;
	ViewPager mViewPager;
	private ActionBar.TabListener listener = new ActionBar.TabListener() {
		public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
			mViewPager.setCurrentItem(tab.getPosition());
		}

		public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
		}

		public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
		}
	};

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		
		
		final ActionBar actionBar = getActionBar();
		mWeatherPagerAdapter = new WeatherPagerAdapter(
				getSupportFragmentManager(), actionBar,fragments);
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mWeatherPagerAdapter);

		mViewPager.setOnPageChangeListener(
	            new ViewPager.SimpleOnPageChangeListener() {
	                @Override
	                public void onPageSelected(int position) {
	                	actionBar.setSelectedNavigationItem(position);
	                }
	            }); 
		
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		
		doBindService();
	}
	
	ArrayList<WeatherFragment> fragments = new ArrayList<WeatherFragment>();
	
	private void addTab(String CityName)
	{
		final ActionBar actionBar = getActionBar();
		
		actionBar.addTab(actionBar.newTab().setText(CityName)
				.setTabListener(listener));
		
		fragments.add(WeatherFragment.createWeatherFragment(CityName));
		mWeatherPagerAdapter.notifyDataSetChanged();
	}

	
	public WeatherDataListService mDataListService = null;
	
	private ServiceConnection mConnection = new ServiceConnection() {
	    public void onServiceConnected(ComponentName className, IBinder service) {
	        mDataListService = ((WeatherDataListService.ServiceBinder)service).getService();
	        mDataListService.setNewCityListener(new OnNewCityListener() {
				@Override
				public void OnNewCity(String CityName, WeatherDataList data) {
					addTab(CityName);
				}
			});
	        mDataListService.bindAllCityes();
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
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main, menu);
	    return true;
	}
	

}
