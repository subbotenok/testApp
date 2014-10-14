package com.testapp.fragment;

import com.example.testapp.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class WeatherFragment extends Fragment {

	private static final String CITY_NAME = "cityName";
	public static final String API_QUERY = "object";
	private String mCityName;
	
	
	public static WeatherFragment createWeatherFragment(String cityName) {
		Bundle bundle = new Bundle();
		bundle.putString(CITY_NAME, cityName);
		
		WeatherFragment weatherFragment = new WeatherFragment();
		weatherFragment.setArguments(bundle);
		
		return weatherFragment;
	}

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.weather_fragment, container,
				false);
		
		getArguments();
		
		

		return rootView;
	}

}
