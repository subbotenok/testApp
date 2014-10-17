package com.testapp.fragment;

import java.util.HashMap;

import com.androidquery.AQuery;
import com.example.testapp.R;
import com.testapp.activity.MainActivity;
import com.testapp.data.LoadWeatherData;
import com.testapp.data.WeatherDataList;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CurrentWeatherFragment extends Fragment {

	private static final String CITY_NAME = "cityName";

	public static CurrentWeatherFragment createCurrentWeatherFragment(
			String cityName) {
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
		View rootView = inflater.inflate(R.layout.current_weather_fragment,
				container, false);

		AQuery aq = new AQuery(rootView);

		mCityName = getArguments().getString(CITY_NAME, "");

		Activity activity = getActivity();

		if (activity != null && activity instanceof MainActivity) {

			MainActivity mMainActivity = (MainActivity) activity;

			WeatherDataList mDataList = mMainActivity.mDataListService
					.getWeatherDataList(mCityName);

			HashMap<String, String> map = mDataList.getSavedList().get(0);

			aq.id(R.id.cityNameTextView).text(mDataList.mCityName);
			aq.id(R.id.temperatureTextView)
					.text(String
							.format("temperature value=\"%s\" min=\"%s\" max=\"%s\"",
									map.get(LoadWeatherData.TEMPERATURE_DAY),
									map.get(LoadWeatherData.TEMPERATURE_MIN),
									map.get(LoadWeatherData.TEMPERATURE_MAX)));

			aq.id(R.id.pressureTextView)
			.text(String
					.format("pressure value=\"%1$s\"",
							map.get(LoadWeatherData.PRESSURE)));

			
			

		}

		return rootView;
	}

}
