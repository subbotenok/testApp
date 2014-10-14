package com.testapp.adapter;

import android.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.testapp.fragment.WeatherFragment;

public class WeatherPagerAdapter extends FragmentPagerAdapter {
	
	
	
	private ActionBar mActionBar;



	public WeatherPagerAdapter(FragmentManager fm, ActionBar actionBar) {
		super(fm);
		this.mActionBar = actionBar;
	}

	@Override
	public Fragment getItem(int arg0) {
		
		Object object = mActionBar.getTabAt(arg0).getTag();
		
		if (object instanceof WeatherFragment)
		{
			WeatherFragment fragment = (WeatherFragment) object;
			return fragment;
		}
		return new Fragment();
	}

	@Override
	public int getCount() {
		return mActionBar.getTabCount();
//		return 0;
	}
	
	

	@Override
	public CharSequence getPageTitle(int position) {
		// TODO Auto-generated method stub
		return super.getPageTitle(position);
	}
}
