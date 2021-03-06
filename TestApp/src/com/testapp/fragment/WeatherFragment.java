package com.testapp.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.androidquery.AQuery;
import com.example.testapp.R;
import com.testapp.data.WeatherDataList;
import com.testapp.utils.Utils;

public class WeatherFragment extends Fragment {

	private static final String CITY_NAME = "cityName";
	public static final String API_QUERY_FORECAST = "http://api.openweathermap.org/data/2.5/forecast?q=%1$s&mode=xml";
	public static final String API_QUERY_WEATHER = "http://api.openweathermap.org/data/2.5/weather?q=%1$s&mode=xml";
	public static final String API_QUERY_DAYLY  = "http://api.openweathermap.org/data/2.5/forecast/daily?q=%1$s&mode=xml&units=metric&cnt=7";
	CurrentWeatherFragment mCurrentWeatherFragment;
	WeatherFor7DaysFragment mWeatherFor7DaysFragment;
	boolean mShowCurrentWeatherFlag;
	private String mCityName;
	
	
	public static WeatherFragment createWeatherFragment(String cityName) {
		Bundle bundle = new Bundle();
		bundle.putString(CITY_NAME, cityName);
		
		WeatherFragment weatherFragment = new WeatherFragment();
		weatherFragment.setArguments(bundle);
		
		return weatherFragment;
	}
	
	public WeatherFragment() {
		super();
	}

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		mCityName = getArguments().getString(CITY_NAME,"");

		mCurrentWeatherFragment = CurrentWeatherFragment.createCurrentWeatherFragment(mCityName);
		mWeatherFor7DaysFragment  = WeatherFor7DaysFragment.createWeatherFor7DaysFragment(mCityName);

		
		View rootView = inflater.inflate(R.layout.weather_fragment, container,
				false);
		final AQuery aq = new AQuery(rootView);
		
		mGeneratedFrameLayoutID = Utils.generateViewId();
		
		aq.id(R.id.content_fragment).getView().setId(mGeneratedFrameLayoutID);
	
		
//		mDataList = new WeatherDataList(mCityName, getActivity());
		
		FragmentTransaction transaction = this.getFragmentManager().beginTransaction();
		transaction.replace(mGeneratedFrameLayoutID, mCurrentWeatherFragment);
		transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		transaction.commitAllowingStateLoss();
		
		mShowCurrentWeatherFlag = true;
		
		
		
		aq.id(R.id.buttonSwith).text("Show weather for week.").clicked(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if (mShowCurrentWeatherFlag)
				{
					aq.text("Show current weather.");
					
					FragmentTransaction transaction = WeatherFragment.this.getFragmentManager().beginTransaction();
					transaction.replace(mGeneratedFrameLayoutID, mWeatherFor7DaysFragment);
					transaction.commitAllowingStateLoss();
					
				}else
				{
					aq.text("Show weather for week.");
					
					FragmentTransaction transaction = WeatherFragment.this.getFragmentManager().beginTransaction();
					transaction.replace(mGeneratedFrameLayoutID, mCurrentWeatherFragment);
					transaction.commitAllowingStateLoss();
					
				}
				
				
				mShowCurrentWeatherFlag = !mShowCurrentWeatherFlag;
			}
		});
		

		return rootView;
	}
	
	
	
	private int mGeneratedFrameLayoutID;
	public WeatherDataList mDataList;
	

}
