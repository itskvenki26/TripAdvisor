package com.nyu.cs9033.eta.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

public class updateLocation extends IntentService {

	private static final String TAG = "updateLocation";

	public updateLocation(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	// Request Example: {"command": "UPDATE_LOCATION", "latitude": 0,
	// "longitude": 0, "datetime": 1382591009}
	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub
		Log.d(TAG,"started onhandle srvice");
		JSONObject json = new JSONObject();
		LocationManager l = (LocationManager) getSystemService(this.LOCATION_SERVICE);
		Location lc = l.getLastKnownLocation(l.GPS_PROVIDER);
		try {
			json.accumulate("command", "UPDATE_LOCATION");
			json.accumulate("latitude", lc.getLatitude());
			json.accumulate("longitude", lc.getLongitude());
			json.accumulate("datetime", System.currentTimeMillis());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			Log.d(TAG,"onHandleIntent",e);
			e.printStackTrace();
		}
		
		
			URL url;
			try {
				url = new URL("http://cs9033-homework.appspot.com/");
			
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setDoOutput(true);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type",
					"application/json");
			OutputStreamWriter out = new OutputStreamWriter(
					connection.getOutputStream());
			
		

			String js = json.toString();
			Log.d(TAG, js);
			out.write(js);
			Log.e(TAG, js);
			out.close();
			connection.connect();

			int result = connection.getResponseCode();
			if (result == HttpURLConnection.HTTP_OK) {
				String respondse= (new BufferedReader(new InputStreamReader(
						connection.getInputStream()))).readLine()
						.toString();
				Log.d(TAG, respondse);
			}
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.d(TAG,"onHandleIntent",e);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.d(TAG,"onHandleIntent",e);
			}
		
		
	}

	public updateLocation() {
		super(null);
	}

	public static void InitService(Context context) {
		Log.d(TAG,"Init started");
		AlarmManager m = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		Log.d(TAG,"Got alarm");
		Intent i = new Intent(context, updateLocation.class);
		
		PendingIntent pi = PendingIntent.getService(context, 0, i, 0);
		m.setRepeating(AlarmManager.RTC, 0, 5000, pi);
		Log.d(TAG,"set alarm");

	}

}
