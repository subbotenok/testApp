package com.testapp.data;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.os.AsyncTask;
import android.util.Log;

public class LoadWeatherData extends AsyncTask<String , Integer, ArrayList<HashMap<String, String>>>
{
	
public static final String PRESSURE = "pressure";
public static final String TEMPERATURE_NIGHT = "temperature_night";
public static final String TEMPERATURE_MAX = "temperature_max";
public static final String TEMPERATURE_MIN = "temperature_min";
public static final String TEMPERATURE_DAY = "temperature_day";
public static final String SYMBOL = "symbol";
public static final String DAY = "day";

//	private static final String API_QUERY_FORECAST = "http://api.openweathermap.org/data/2.5/forecast?q=%1$s&mode=xml";
//	private static final String APPID = "APPID=13e2a519c7b8825ac4fc881dc706d707";

	//		private static final String API_QUERY_WEATHER = "http://api.openweathermap.org/data/2.5/weather?q=%1$s&mode=xml";
	private static final String API_QUERY_DAYLY  = "http://api.openweathermap.org/data/2.5/forecast/daily?q=%1$s&mode=xml&units=metric&cnt=7&APPID=13e2a519c7b8825ac4fc881dc706d707";


	@Override
	protected ArrayList<HashMap<String, String>> doInBackground(String... params) {
		
		ArrayList<HashMap<String, String>> arrayList = new ArrayList<HashMap<String,String>>();
		 String uri = String.format(Locale.ENGLISH, API_QUERY_DAYLY, params);
		 Log.e("", uri);
		 
		 HttpClient httpclient = new DefaultHttpClient();
		try{

			HttpGet httpget = new HttpGet(uri.toString());
			

			DocumentBuilder db = DocumentBuilderFactory
					.newInstance().newDocumentBuilder();
			
			

			InputStream content = httpclient.execute(httpget)
					.getEntity().getContent();
			
			
			Document doc = db.parse(content);
			
			
			

			NodeList weatherdata = doc.getFirstChild()
					.getChildNodes();
			
			NodeList location = getNodeByName(weatherdata, "location").getChildNodes();

			
			NodeList forecast = getNodeByName(weatherdata, "forecast").getChildNodes();
			
			
			for (int fori = 0; fori < forecast.getLength(); fori++)
			{
				Node item = forecast.item(fori);
				if (item.getNodeName().equals("time"))
					arrayList.add(ParceTimeNode(item));
			}
			
			
			
			
			
			
			
			
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		
		
		return arrayList;
	} 
	
	private HashMap<String, String> ParceTimeNode(Node item) {
//		<time day="2014-10-15">
//			<symbol number="501" name="moderate rain" var="10d"/>
//			<precipitation value="10" type="rain"/>
//			<windDirection deg="126" code="SE" name="SouthEast"/>
//			<windSpeed mps="4.06" name="Gentle Breeze"/>
//			<temperature day="13.74" min="13.74" max="14.86" night="14.86" eve="13.74" morn="13.74"/>
//			<pressure unit="hPa" value="1004.13"/>
//			<humidity value="96" unit="%"/>
//			<clouds value="overcast clouds" all="92" unit="%"/>
//		</time>

		Log.e("", item.getAttributes().getNamedItem(DAY).getNodeValue());
		
		HashMap<String, String> weatherData = new HashMap<String, String>();
		weatherData.put(DAY, item.getAttributes().getNamedItem(DAY).getNodeValue());
		NodeList itemList = item.getChildNodes();
		
		weatherData.put(SYMBOL, getNodeByName(itemList, SYMBOL).getAttributes().getNamedItem("name").getNodeValue());
		NamedNodeMap temperatureAttr = getNodeByName(itemList, "temperature").getAttributes();
		weatherData.put(TEMPERATURE_DAY,temperatureAttr.getNamedItem(DAY).getNodeValue());
		weatherData.put(TEMPERATURE_MIN,temperatureAttr.getNamedItem("min").getNodeValue());
		weatherData.put(TEMPERATURE_MAX,temperatureAttr.getNamedItem("max").getNodeValue());
		weatherData.put(TEMPERATURE_NIGHT,temperatureAttr.getNamedItem("night").getNodeValue());
		
		weatherData.put(PRESSURE, getNodeByName(itemList, PRESSURE).getAttributes().getNamedItem("value").getNodeValue());
		
		return weatherData;

	
		
		
	}

	HashMap<String, Integer> NodeMap = new HashMap<String, Integer>();
	private Node getNodeByName(NodeList nodeList, String name) {
		Node result = null;
		
		if (NodeMap.containsKey(name))
		{
			result = nodeList.item(NodeMap.get(name));
			if (result.getNodeName().equalsIgnoreCase(name))
				return result;
		}
		
		
		int index = 0;
		while ((result = nodeList.item(index)) != null)
		{
			if (result.getNodeName().equalsIgnoreCase(name))
			{
				NodeMap.put(name, index);
				return result;
			}
			index++;
		}
		
		return null;
	}
	
	private String getNodeValueByName(NodeList nodeList, String name) 
	{
		return getNodeByName(nodeList, name)
				.getFirstChild().getNodeValue().trim();
		
	}
	

}