package com.testapp.data;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.Loader.OnLoadCanceledListener;
import android.content.Loader.OnLoadCompleteListener;
import android.os.AsyncTask;
import android.os.IBinder;

import android.util.Log;

import com.testapp.sql.ArraySQLSavedList;
import com.testapp.utils.Utils;

public class WeatherDataList {

	public String mCityName;

	private Pattern mPattern = Pattern.compile("\\w");

	// private static final String API_QUERY_FORECAST =
	// "http://api.openweathermap.org/data/2.5/forecast?q=%1$s&mode=xml";
	// private static final String APPID =
	// "APPID=13e2a519c7b8825ac4fc881dc706d707";

	// private static final String API_QUERY_WEATHER =
	// "http://api.openweathermap.org/data/2.5/weather?q=%1$s&mode=xml";
	private static final String API_QUERY_DAYLY = "http://api.openweathermap.org/data/2.5/forecast/daily?q=%1$s&mode=xml&units=metric&cnt=7&APPID=13e2a519c7b8825ac4fc881dc706d707";

	private ArraySQLSavedList<HashMap<String, String>> mSavedList;
	private OnLoadCompleteListener<ArraySQLSavedList<HashMap<String, String>>> loadCompleteListener = null;

	LoadWeatherData data = new LoadWeatherData() {
		protected void onPostExecute(
				java.util.ArrayList<java.util.HashMap<String, String>> result) {
			
			if (result == null || result.isEmpty())
				return;

			Log.e("", "Add!!!!");

			mSavedList.clear();
			mSavedList.addAll(result);

			if (loadCompleteListener != null) {
				loadCompleteListener.onLoadComplete(null, mSavedList);
			}
		};
	};

	public WeatherDataList(String cityName, Context context) {
		this.mCityName = cityName;
		String TableName = Utils.toSQL(cityName);

		Log.e("", "TableName = "+ TableName);

		mSavedList = new ArraySQLSavedList<HashMap<String, String>>(TableName,
				context);

		data.execute(Utils.toHTML(mCityName));

	}

	public ArraySQLSavedList<HashMap<String, String>> getSavedList() {
		return mSavedList;
	}

	public void setLoadCompleteListener(
			OnLoadCompleteListener<ArraySQLSavedList<HashMap<String, String>>> loadCompleteListener) {
		this.loadCompleteListener = loadCompleteListener;
	}

}
