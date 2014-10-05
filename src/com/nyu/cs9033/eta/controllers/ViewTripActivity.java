package com.nyu.cs9033.eta.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.nyu.cs9033.eta.R;
import com.nyu.cs9033.eta.models.Person;
import com.nyu.cs9033.eta.models.Trip;

public class ViewTripActivity extends Activity {

	private static final String TAG = "ViewTripActivity";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view);
		// TODO - fill in here
		Trip trip = getMostRecentTrip(this.getIntent());
		initView(trip);
	}

	/**
	 * Create the most recent trip that was passed to TripViewer.
	 * 
	 * @param i
	 *            The Intent that contains the most recent trip data.
	 * 
	 * @return The Trip that was most recently passed to TripViewer, or null if
	 *         there is none.
	 */
	public Trip getMostRecentTrip(Intent i) {

		// TODO - fill in here
		Trip trip = null;
		try {
			Bundle b = i.getExtras();
			trip = b.getParcelable("trip");
		} catch (Exception e) {
			TextView error = (TextView) findViewById(R.id.errordev);
			error.setText("Please Create trip properly!");
		}
		return trip;
	}

	/**
	 * Populate the View using a Trip model.
	 * 
	 * @param trip
	 *            The Trip model used to populate the View.
	 */
	public void initView(Trip trip) {

		// TODO - fill in here
		try {
		//	TextView tripname = (TextView) findViewById(R.id.viewtripname);
			//tripname.setText(trip.getTripName());

			TextView destname = (TextView) findViewById(R.id.viewdestname);
			destname.setText(trip.getLocations());

			TextView creator = (TextView) findViewById(R.id.viewcreator);
			creator.setText(trip.getCreator());

			TextView date = (TextView) findViewById(R.id.viewdate);
			date.setText(trip.getDate());

			TextView time = (TextView) findViewById(R.id.viewtime);
			time.setText(trip.getTime());

			ArrayList<Person> friends = trip.getFriends();

			//for (int i = 1; i <= friends.size(); i++) {
				//Person p = friends.get(i - 1);

				//displayfrndsView(p, i);
				viewTripOnCloud vc = new viewTripOnCloud();
				vc.execute(Long.toString(trip.getTripId()));
				
			//}
		} catch (Exception e) {
			if (trip == null) {
				Toast.makeText(this, "ERROR : Please Create a Trip First!",
						Toast.LENGTH_LONG).show();
			}
		}
	}

	public int dp(int dps) {
		final float scale = this.getResources().getDisplayMetrics().density;
		int pixels = (int) (dps * scale + 0.5f);
		return pixels;
	}

	public void displayfrndsView(Person p, int i) {
		try {
			LinearLayout parent = (LinearLayout) findViewById(R.id.friendlayout);

			LinearLayout ll = new LinearLayout(this);
			LinearLayout.LayoutParams myLayoutParams = new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			myLayoutParams.topMargin = dp(15);
			ll.setOrientation(LinearLayout.HORIZONTAL);
			ll.setLayoutParams(myLayoutParams);

			TextView label = new TextView(this);
			LayoutParams labelParams = new LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			labelParams.leftMargin = dp(10);
			label.setLayoutParams(labelParams);
			label.setText("Friend " + i);
			label.setTextAppearance(this, android.R.style.TextAppearance_Small);
			label.setTypeface(Typeface.MONOSPACE);
			label.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
			label.setPadding(dp(5), dp(5), dp(5), dp(5));

			TextView name = new TextView(this);
			LayoutParams nameParams = new LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			nameParams.leftMargin = dp(30);
			name.setText(p.getName());
			name.setLayoutParams(nameParams);
			name.setWidth(dp(80));
			name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
			name.setPadding(dp(5), dp(5), dp(5), dp(5));

			TextView loc = new TextView(this);
			LayoutParams locParams = new LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			locParams.leftMargin = dp(5);
			loc.setText(p.getLocation());
			loc.setLayoutParams(nameParams);
			loc.setWidth(dp(80));
			loc.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
			loc.setPadding(dp(5), dp(5), dp(5), dp(5));

			ll.addView(label);
			ll.addView(name);
			ll.addView(loc);

			LinearLayout ll2 = new LinearLayout(this);
			LinearLayout.LayoutParams myLayoutParams2 = new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			myLayoutParams2.topMargin = dp(5);
			ll2.setOrientation(LinearLayout.HORIZONTAL);
			ll2.setLayoutParams(myLayoutParams2);

			TextView ph = new TextView(this);
			LayoutParams phonelabelParams = new LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			phonelabelParams.leftMargin = dp(40);
			ph.setLayoutParams(phonelabelParams);
			ph.setText("Phone ");
			ph.setTextAppearance(this, android.R.style.TextAppearance_Small);
			ph.setTypeface(Typeface.MONOSPACE);
			ph.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
			ph.setPadding(dp(5), dp(5), dp(5), dp(5));

			TextView phone = new TextView(this);
			LayoutParams phoneParams = new LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			phoneParams.leftMargin = dp(30);
			phone.setText(p.getPhoneNum());
			phone.setLayoutParams(phoneParams);
			phone.setWidth(dp(150));

			ll2.addView(ph);
			ll2.addView(phone);

			parent.addView(ll);
			parent.addView(ll2);
		} catch (Exception e) {
			Toast.makeText(this, "Exception : " + e.toString(),
					Toast.LENGTH_LONG).show();
		}
	}

	// Responses Example: {"distance_left": [20.399999999999999,
	// 6.7000000000000002], "time_left": [1920, 900], "people": ["Joe Smith",
	// "John
	// Doe"]}
	public void displayfrndsView(JSONObject json) {
		JSONArray distance_left = null;
		JSONArray time_left = null;
		JSONArray people = null;
		int num = 0;
		try {
			distance_left = json.getJSONArray("distance_left");
			time_left = json.getJSONArray("time_left");
			people = json.getJSONArray("people");
			num = distance_left.length();
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			Log.d(TAG, "displayfrndsview", e1);
		}

		// JSONArray distance_left= json.getJSONArray("distance_left");

		try {

			LinearLayout parent = (LinearLayout) findViewById(R.id.friendlayout);

			LinearLayout ll = new LinearLayout(this);
			LinearLayout.LayoutParams myLayoutParams = new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			myLayoutParams.topMargin = dp(15);
			ll.setOrientation(LinearLayout.HORIZONTAL);
			ll.setLayoutParams(myLayoutParams);

			for (int i = 0; i < num; i++) {
				TextView label = new TextView(this);
				LayoutParams labelParams = new LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				labelParams.leftMargin = dp(10);
				label.setLayoutParams(labelParams);
				label.setText(people.get(i).toString());
				label.setTextAppearance(this,
						android.R.style.TextAppearance_Small);
				label.setTypeface(Typeface.MONOSPACE);
				label.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
				label.setPadding(dp(5), dp(5), dp(5), dp(5));

				TextView name = new TextView(this);
				LayoutParams nameParams = new LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				nameParams.leftMargin = dp(30);
				name.setText( Double.toString(distance_left.getDouble(i)));
				name.setLayoutParams(nameParams);
				name.setWidth(dp(80));
				name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
				name.setPadding(dp(5), dp(5), dp(5), dp(5));

				TextView loc = new TextView(this);
				LayoutParams locParams = new LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				locParams.leftMargin = dp(5);
				long t= time_left.getLong(i);
				Calendar c = Calendar.getInstance();
				c.setTimeInMillis(t);
				int hour =c.get(Calendar.HOUR_OF_DAY);
				int min=c.get(Calendar.MINUTE);
				loc.setText((hour>9?hour:"0"+hour)+"-"+(min>9?min:"0"+min));
				loc.setLayoutParams(nameParams);
				loc.setWidth(dp(80));
				loc.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
				loc.setPadding(dp(5), dp(5), dp(5), dp(5));

				ll.addView(label);
				ll.addView(name);
				ll.addView(loc);

				/*
				 * LinearLayout ll2 = new LinearLayout(this);
				 * LinearLayout.LayoutParams myLayoutParams2 = new
				 * LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
				 * LayoutParams.WRAP_CONTENT); myLayoutParams2.topMargin =
				 * dp(5); ll2.setOrientation(LinearLayout.HORIZONTAL);
				 * ll2.setLayoutParams(myLayoutParams2);
				 * 
				 * TextView ph = new TextView(this); LayoutParams
				 * phonelabelParams = new
				 * LayoutParams(LayoutParams.WRAP_CONTENT,
				 * LayoutParams.WRAP_CONTENT); phonelabelParams.leftMargin =
				 * dp(40); ph.setLayoutParams(phonelabelParams);
				 * ph.setText("Phone "); ph.setTextAppearance(this,
				 * android.R.style.TextAppearance_Small);
				 * ph.setTypeface(Typeface.MONOSPACE);
				 * ph.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
				 * ph.setPadding(dp(5), dp(5), dp(5), dp(5));
				 * 
				 * TextView phone = new TextView(this); LayoutParams phoneParams
				 * = new LayoutParams(LayoutParams.WRAP_CONTENT,
				 * LayoutParams.WRAP_CONTENT); phoneParams.leftMargin = dp(30);
				 * phone.setText(p.getPhoneNum());
				 * phone.setLayoutParams(phoneParams); phone.setWidth(dp(150));
				 * 
				 * ll2.addView(ph); ll2.addView(phone);
				 */

			}
			parent.addView(ll);
			// parent.addView(ll2);

		} catch (Exception e) {
			Toast.makeText(this, "Exception : " + e.toString(),
					Toast.LENGTH_LONG).show();
		}
	}

	public void deleteTrip(View v) {
		Trip trip = null;
		try {
			Intent i = getIntent();
			Bundle b = i.getExtras();
			trip = b.getParcelable("trip");

			TripDatabaseHelper tdh = new TripDatabaseHelper(this);
			tdh.deleteTrip(trip.getTripId());
			Toast.makeText(this, "Deleting Trip : " + trip.getTripName(),
					Toast.LENGTH_LONG).show();

			setResult(Activity.RESULT_OK);
			finish();
		} catch (Exception e) {
			Toast.makeText(this, "Exception : " + e.toString(),
					Toast.LENGTH_LONG).show();
		}
	}

	private class viewTripOnCloud extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			JSONObject json = new JSONObject();
			Log.e(TAG, "just entered");
			// trip = params[0];
			Log.e(TAG, "param is wrongr");
			ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			Log.e(TAG, "before the sever");
			NetworkInfo ni = cm.getActiveNetworkInfo();
			Log.e(TAG, "ni is null");
			if (ni.isConnected()) {
				Log.e(TAG, "into the server");
				try {
					URL url = new URL("http://cs9033-homework.appspot.com/");
					HttpURLConnection connection = (HttpURLConnection) url
							.openConnection();
					connection.setDoOutput(true);
					connection.setRequestMethod("POST");
					connection.setRequestProperty("Content-Type",
							"application/json");
					OutputStreamWriter out = new OutputStreamWriter(
							connection.getOutputStream());
					
					try {
						json.accumulate("command", "TRIP_STATUS");
						json.accumulate("trip_id", params[0]);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					String js = json.toString();
					Log.d(TAG, js);
					out.write(js);
					Log.e(TAG, js);
					out.close();
					connection.connect();

					int result = connection.getResponseCode();
					if (result == HttpURLConnection.HTTP_OK) {
						return (new BufferedReader(new InputStreamReader(
								connection.getInputStream()))).readLine()
								.toString();
					}

				} catch (MalformedURLException e) {
					Log.e(TAG, "MalformedURLException", e);
				} catch (IOException e) {
					Log.e(TAG, "IOException", e);
				}
				// return null;
			}
			return null;
		
		}
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			
			super.onPostExecute(result);
			try {
				JSONObject json =new JSONObject(result);
				ViewTripActivity.this.displayfrndsView(json);
				Log.e(TAG, json.toString());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
}
