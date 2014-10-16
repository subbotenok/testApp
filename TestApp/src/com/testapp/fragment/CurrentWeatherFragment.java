package com.testapp.fragment;

import com.example.testapp.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CurrentWeatherFragment extends Fragment {
	
	private static final String CITY_NAME = "cityName";
	public static CurrentWeatherFragment createCurrentWeatherFragment(String cityName) {
		Bundle bundle = new Bundle();
		bundle.putString(CITY_NAME, cityName);
		
		CurrentWeatherFragment weatherFragment = new CurrentWeatherFragment();
		weatherFragment.setArguments(bundle);
		
		return weatherFragment;
	}

	private String mCityName;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.current_weather_fragment, container,
				false);
		
		mCityName = getArguments().getString(CITY_NAME,"");


		return rootView;
	}

}
