package com.testapp.fragment;

import java.util.HashMap;

import com.androidquery.AQuery;
import com.example.testapp.R;
import com.testapp.activity.MainActivity;
import com.testapp.data.LoadWeatherData;
import com.testapp.data.WeatherDataList;
import com.testapp.sql.ArraySQLSavedList;

import android.app.Activity;
import android.content.Loader;
import android.content.Loader.OnLoadCompleteListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;

public class WeatherFor3DaysFragment extends Fragment {
	
	private static final String CITY_NAME = "cityName";
	public static WeatherFor3DaysFragment createWeatherFor3DaysFragment(String cityName) {
		Bundle bundle = new Bundle();
		bundle.putString(CITY_NAME, cityName);
		
		WeatherFor3DaysFragment weatherFragment = new WeatherFor3DaysFragment();
		weatherFragment.setArguments(bundle);
		
		return weatherFragment;
	}

	private String mCityName;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		mCityName = getArguments().getString(CITY_NAME,"");

		
		
		View rootView = inflater.inflate(R.layout.weather_for_3_days_fragment, container,
				false);
		AQuery aq = new AQuery(rootView);
		
		Activity activity = getActivity();
		
		if (activity !=null && activity instanceof MainActivity)
		{
			
			MainActivity mMainActivity = (MainActivity) activity; 
			
			WeatherDataList mDataList = mMainActivity.mDataListService.getWeatherDataList(mCityName);
			
			
			
			int [] res = new int[]{
					R.id.dateTextView,
					R.id.precipitation,
					R.id.temperature_day,
					R.id.temperature_night
					};
			
			String[] keys = new String[]
					{
					LoadWeatherData.DAY,
					LoadWeatherData.SYMBOL,
					LoadWeatherData.TEMPERATURE_DAY,
					LoadWeatherData.TEMPERATURE_NIGHT
					}; 
			
			final SimpleAdapter adapter = new SimpleAdapter(getActivity(), mDataList.getSavedList(), R.layout.weather_for_3_days_item, keys, res);
			aq.id(R.id.listView1).adapter(adapter);
			
			mDataList.setLoadCompleteListener(new OnLoadCompleteListener<ArraySQLSavedList<HashMap<String,String>>>() {

				@Override
				public void onLoadComplete(
						Loader<ArraySQLSavedList<HashMap<String, String>>> loader,
						ArraySQLSavedList<HashMap<String, String>> data) {
					
					adapter.notifyDataSetChanged();
					
				}
			});
			
		}
		
	
		return rootView;
	}

}
