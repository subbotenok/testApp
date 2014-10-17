package com.testapp.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import com.testapp.data.WeatherDataList;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class WeatherDataListService extends Service{
	
	HashMap<String, WeatherDataList> mWeatherMap = new HashMap<String, WeatherDataList>(); 
	
	public interface OnNewCityListener
	{
		public void OnNewCity(String CityName, WeatherDataList data);
	}
	
	private OnNewCityListener newCityListener = null;

	
	public final class ServiceBinder extends Binder {
		public WeatherDataListService getService() {
            return WeatherDataListService.this;
        }
	}
	
	@Override
	public void onCreate() {
		NewCity("Moscow");
		NewCity("Saint Petersburg,ru");
		
		super.onCreate();
	}

	private final IBinder mBinder = new ServiceBinder();
    
	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}
	
	
	public void NewCity(String CityName)
	{
		WeatherDataList value = new WeatherDataList(CityName, this.getApplicationContext());
		mWeatherMap.put(CityName, value);
		if (newCityListener != null)
			newCityListener.OnNewCity(CityName, value);
	}
	
	public void bindAllCityes()
	{
		for (WeatherDataList value : mWeatherMap.values()) {
			if (newCityListener != null)
				newCityListener.OnNewCity(value.mCityName, value);
		}
	}
	
	public Collection<WeatherDataList>  getAllCityes()
	{
		return  mWeatherMap.values();
	}
	
	
	public void setNewCityListener(OnNewCityListener newCityListener) {
		this.newCityListener = newCityListener;
	}
	
	public WeatherDataList getWeatherDataList(String CityName)
	{
		return mWeatherMap.get(CityName);
	}

}
