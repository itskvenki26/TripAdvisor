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

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.nyu.cs9033.eta.R;
import com.nyu.cs9033.eta.models.Person;
import com.nyu.cs9033.eta.models.Trip;

public class CreateTripActivity extends FragmentActivity {
	static String date;
	
	int frnd_num = 0;
	// RIds for dynamically created list
	private static final int Name_RID = 10000;
	private static final int LOCATION_RID = 20000;
	private static final int PHONE_RID = 30000;
	private static final int NONE_RID = 40000;
	private static final int LNTWOID_RID = 50000;
	private static final int Contact_Num = 1;
	private static final String TAG = "CreateTrip";
	private static final String RESPONSE_JSON_KEY = "trip_id";
	private static final boolean DEBUG = true;
	Trip t;
	//public httphelper req = new httphelper();
	private long responseId = -1;

	public static class DatePickerFragment extends DialogFragment implements
			DatePickerDialog.OnDateSetListener {
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// Use the current date as the default date in the picker
			final Calendar cal = Calendar.getInstance();
			int year = cal.get(Calendar.YEAR);
			int month = cal.get(Calendar.MONTH);
			int day = cal.get(Calendar.DAY_OF_MONTH);

			return new DatePickerDialog(getActivity(), this, year, month, day);
		}

		@Override
		public void onDateSet(DatePicker view, int year, int month, int day) {

			try {
				date = month + "-" + day + "-" + year;
			} catch (Exception e) {

			}
		}
	}

	public void showDatePickerDialog(View v) {
		DialogFragment newFragment = new DatePickerFragment();
		newFragment.show(getSupportFragmentManager(), "datePicker");
	}
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_create);
			if (getIntent().getAction() != null) {
				Intent intent = getIntent();
				String action = intent.getAction();
				if (Intent.ACTION_SEND.equals(action)
						& intent.getType().equals("text/plain")) {
					String sentText = intent.getStringExtra(Intent.EXTRA_TEXT);
					sentText = sentText.substring(0, sentText.indexOf("http"))
							.trim();
					EditText destination = (EditText) findViewById(R.id.editdestname);
					destination.setText(sentText);
				}
			}
		} catch (Exception e) {
			TextView error = (TextView) findViewById(R.id.errordev);
			error.setText(e.toString());
		}
	}

	public void onClick(View v) {
		try {
			Button b = (Button) v;
			switch (b.getId()) {
			case R.id.btncreatetrip:
				if (DEBUG)
					Log.d(TAG, "create trip");
				try {
					t = createTrip();
					
					CreateTripOnCloud ct = new CreateTripOnCloud();
					ct.execute(t);
					// t.setServerRefId(0);
					Toast.makeText(this,
							"No Network Connection - Saving locally!",
							Toast.LENGTH_LONG).show();
					//persistTrip(t);
				} catch (Exception exception) {
					if (DEBUG)
						Log.i(TAG, "Exception create trip exception");
				}

				break;
			case R.id.btnadd:
				frnd_num++;
				addFriend();
				Intent intent = new Intent(Intent.ACTION_PICK,
						ContactsContract.Contacts.CONTENT_URI);
				startActivityForResult(intent, Contact_Num);
				break;
			case R.id.btndelete:
				deletePerson();
				frnd_num--;
				break;
			}
		} catch (Exception e) {
			TextView error = (TextView) findViewById(R.id.errordev);
			error.setText(e.toString());
		}
	}

	public boolean isConnected() {
		try {
			ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo ni = cm.getActiveNetworkInfo();
			return ni != null && ni.isConnected();
		} catch (Exception e) {
			Log.i(TAG, "Exception in isConnected :" + e.toString());
			Toast.makeText(this, "Exception in isConnected : " + e.toString(),
					Toast.LENGTH_LONG).show();
		}
		return false;
	}

	public void processFinish(JSONObject output) {
		try {
			responseId = output.getLong(RESPONSE_JSON_KEY);
			t.setServerRefId(responseId);
			persistTrip(t);
		} catch (Exception e) {
			Log.i(TAG, "Exception in processFinish :" + e.toString());
			Toast.makeText(this,
					"Exception in processFinish : " + e.toString(),
					Toast.LENGTH_LONG).show();
		}
	}

	

	public int display(int dps) {
		final float scale = this.getResources().getDisplayMetrics().density;
		int pixels = (int) (dps * scale + 0.5f);
		return pixels;
	}

	public void addFriend() {
		try {
			LinearLayout parent = (LinearLayout) findViewById(R.id.friendlayout);

			LinearLayout ll = new LinearLayout(this);
			LinearLayout.LayoutParams myLayoutParams1 = new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			myLayoutParams1.topMargin = display(5);
			ll.setId(NONE_RID + frnd_num);
			ll.setOrientation(LinearLayout.HORIZONTAL);
			ll.setLayoutParams(myLayoutParams1);

			TextView label = new TextView(this);
			LayoutParams labelParams = new LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			labelParams.leftMargin = display(10);
			label.setLayoutParams(labelParams);
			label.setText("Friend " + frnd_num);
			label.setTextAppearance(this, android.R.style.TextAppearance_Small);
			label.setTypeface(Typeface.MONOSPACE);
			label.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
			label.setPadding(display(5), display(5), display(5), display(5));

			EditText name = new EditText(this);
			LayoutParams nameParams = new LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			nameParams.leftMargin = display(10);
			name.setId(Name_RID + frnd_num);
			name.setLayoutParams(nameParams);
			name.setHeight(display(10));
			name.setWidth(display(100));

			EditText loc = new EditText(this);
			LayoutParams locParams = new LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			locParams.leftMargin = display(5);
			loc.setId(LOCATION_RID + frnd_num);
			loc.setLayoutParams(nameParams);
			loc.setHeight(display(10));
			loc.setWidth(display(100));

			ll.addView(label);
			ll.addView(name);
			ll.addView(loc);

			LinearLayout ll2 = new LinearLayout(this);
			LinearLayout.LayoutParams myLayoutParams2 = new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			myLayoutParams2.topMargin = display(5);
			ll2.setId(LNTWOID_RID + frnd_num);
			ll2.setOrientation(LinearLayout.HORIZONTAL);
			ll2.setLayoutParams(myLayoutParams2);

			TextView phonelabel = new TextView(this);
			LayoutParams phonelabelParams = new LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			phonelabelParams.leftMargin = display(40);
			phonelabel.setLayoutParams(phonelabelParams);
			phonelabel.setText("Phonenum ");
			phonelabel.setTextAppearance(this,
					android.R.style.TextAppearance_Small);
			phonelabel.setTypeface(Typeface.MONOSPACE);
			phonelabel.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
			phonelabel.setPadding(display(5), display(5), display(5),
					display(5));

			EditText phone = new EditText(this);
			LayoutParams phoneParams = new LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			phoneParams.leftMargin = display(10);
			phone.setId(PHONE_RID + frnd_num);
			phone.setLayoutParams(phoneParams);
			phone.setHeight(display(10));
			phone.setWidth(display(150));

			ll2.addView(phonelabel);
			ll2.addView(phone);

			parent.addView(ll);
			parent.addView(ll2);
		} catch (Exception e) {
			Toast.makeText(this, "Exception : " + e.toString(),
					Toast.LENGTH_LONG).show();
		}
	}

	public void deletePerson() {
		try {
			LinearLayout parent = (LinearLayout) findViewById(R.id.friendlayout);

			parent.removeView(findViewById(NONE_RID + frnd_num));
			parent.removeView(findViewById(LNTWOID_RID + frnd_num));
		} catch (Exception e) {
			Toast.makeText(this, "Exception : " + e.toString(),
					Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public void onBackPressed() {
		cancelTripCreation();
	}

	/**
	 * This method should be used to instantiate a Trip model.
	 * 
	 * @return The Trip as represented by the View.
	 */
	public Trip createTrip() {
		// TODO - fill in here
		try {
			EditText tripNameW = (EditText) findViewById(R.id.edittripname);
			String tripName = tripNameW.getText().toString();
			EditText destNameW = (EditText) findViewById(R.id.editdestname);
			String destName = destNameW.getText().toString();
			// String destName =Long_Lat;
			EditText creatorW = (EditText) findViewById(R.id.editcreatorname);
			String creator = creatorW.getText().toString();
			EditText timeW = (EditText) findViewById(R.id.edittime);
			String time = timeW.getText().toString();
			// EditText timeW = (EditText) findViewById(R.id.edittime);
			// String time = timeW.getText().toString();

			ArrayList<Person> friends = new ArrayList<Person>();
			for (int i = 1; i <= frnd_num; i++) {
				EditText personName = (EditText) findViewById(Name_RID + i);
				String name = personName.getText().toString();

				EditText personLocation = (EditText) findViewById(LOCATION_RID
						+ i);
				String location = personLocation.getText().toString();

				EditText personPhone = (EditText) findViewById(PHONE_RID + i);
				String phone = personPhone.getText().toString();

				friends.add(new Person(name, location, phone));
			}
			Trip t = new Trip(0, tripName, destName, creator, time, date, 0,
					friends);

			JSONObject json = t.toJSON();

			Runnable r = new Runnable() {

				@Override
				public void run() {

				}
			};

			Thread th = new Thread(r);
			th.start();

			return t;

		} catch (Exception e) {
			TextView error = (TextView) findViewById(R.id.errordev);
			error.setText(e.toString());
			return null;
		}
	}

	private class CreateTripOnCloud extends AsyncTask<Trip, Void, String> {
		private static final String TAG = "CreateTripOnCloud";
		private Trip trip;

		@Override
		protected String doInBackground(Trip... params) {
			Log.e(TAG, "just entered");
			trip = params[0];
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
					String js = trip.toJSON().toString();
					Log.d(TAG, js);
					out.write(js);
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
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			try {
				JSONObject json = new JSONObject(result);
				Log.e(TAG, result);
				if (json.getInt("response_code") == 0) {

					trip.setTripId(json.getLong("trip_id"));
					CreateTripActivity.this.persistTrip(trip);
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	/**
	 * For HW2 you should treat this method as a way of sending the Trip data
	 * back to the main Activity
	 * 
	 * Note: If you call finish() here the Activity eventually end and pass an
	 * Intent back to the previous Activity using setResult().
	 * 
	 * @return whether the Trip was successfully persisted.
	 */
	public boolean persistTrip(Trip trip) {

		// TODO - fill in here
		try {
			/*
			 * Intent result = getIntent(); Bundle b = new Bundle();
			 * b.putParcelable("trip", trip); result.putExtras(b);
			 * setResult(Activity.RESULT_OK, result); finish(); return true;
			 */

			TripDatabaseHelper db = new TripDatabaseHelper(this);

			long trip_id = db.insertTrip(trip);

			for (Person p : trip.getFriends()) {
				db.insertPerson(trip_id, p);
			}
			Toast.makeText(this, "Trip Saved", Toast.LENGTH_LONG).show();
			finish();
			return true;
		} catch (Exception e) {
			TextView error = (TextView) findViewById(R.id.errordev);
			error.setText(e.toString());
			return false;
		}
	}

	/**
	 * This method should be used when a user wants to cancel the creation of a
	 * Trip.
	 * 
	 * Note: You most likely want to call this if your activity dies during the
	 * process of a trip creation or if a cancel/back button event occurs.
	 * Should return to the previous activity without a result using finish()
	 * and setResult().
	 */
	public void cancelTripCreation() {
		// TODO - fill in here
		try {
			setResult(RESULT_CANCELED, getIntent());
			Toast.makeText(this, "Creation of Trip Cancelled!!!",
					Toast.LENGTH_SHORT).show();
			finish();
		} catch (Exception e) {
			TextView error = (TextView) findViewById(R.id.errordev);
			error.setText(e.toString());
		}
	}

	@Override
	public void onActivityResult(int input, int output, Intent data) {
		super.onActivityResult(input, output, data);
		String name = null;
		String phone = null;
		try {
			switch (input) {
			case (Contact_Num):
				if (output == Activity.RESULT_OK) {
					Uri uridata = data.getData();
					Cursor c = getContentResolver().query(uridata, null, null,
							null, null);
					if (c.moveToFirst()) {
						String id = c
								.getString(c
										.getColumnIndexOrThrow(ContactsContract.Contacts._ID));

						String hasPhone = c
								.getString(c
										.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

						if (hasPhone.equalsIgnoreCase("1")) {
							Cursor phones = getContentResolver()
									.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
											null,
											ContactsContract.CommonDataKinds.Phone.CONTACT_ID
													+ " = " + id, null, null);
							phones.moveToFirst();
							phone = phones
									.getString(phones
											.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

							name = c.getString(c
									.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));

							String fullname[] = name.split(" ");
							EditText n = (EditText) findViewById(Name_RID
									+ frnd_num);
							n.setText(fullname[0]);

							EditText p = (EditText) findViewById(PHONE_RID
									+ frnd_num);
							p.setText(phone);
						}
					}
				}
				break;
			}
		} catch (Exception e) {
			Toast.makeText(this, "Exception : " + e.toString(),
					Toast.LENGTH_LONG).show();
		}
	}
}
