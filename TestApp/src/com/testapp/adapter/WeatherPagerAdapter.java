package com.testapp.adapter;

import java.util.ArrayList;

import android.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.testapp.fragment.WeatherFragment;

public class WeatherPagerAdapter extends FragmentStatePagerAdapter {
	
	
	
	private ActionBar mActionBar;
	private ArrayList<WeatherFragment> fragments;



	public WeatherPagerAdapter(FragmentManager fm, ActionBar actionBar, ArrayList<WeatherFragment> fragments) {
		super(fm);
		this.mActionBar = actionBar;
		this.fragments = fragments;
	}

	@Override
	public Fragment getItem(int arg0) {
		
		if (fragments.size()>arg0)
			return fragments.get(arg0);
		return new Fragment();
	}

	@Override
	public int getCount() {
		return fragments.size();

	}
}
