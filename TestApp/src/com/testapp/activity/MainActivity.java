package com.testapp.activity;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.animation.BounceInterpolator;

import com.example.testapp.R;
import com.testapp.adapter.WeatherPagerAdapter;
import com.testapp.fragment.WeatherFragment;

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
				getSupportFragmentManager(), actionBar);
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

		actionBar.addTab(actionBar.newTab().setText("Москва")
				.setTag(WeatherFragment.createWeatherFragment("Moscow,ru"))
				.setTabListener(listener));
		actionBar.addTab(actionBar.newTab().setText("Питер")
				.setTag(WeatherFragment.createWeatherFragment("Saint%20Petersburg,ru"))
				.setTabListener(listener));
		mWeatherPagerAdapter.notifyDataSetChanged();
	}


}
