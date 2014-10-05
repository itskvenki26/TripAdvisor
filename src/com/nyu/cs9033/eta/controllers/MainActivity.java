package com.nyu.cs9033.eta.controllers;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nyu.cs9033.eta.models.Trip;
import com.nyu.cs9033.eta.R;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;
import android.view.View;

public class MainActivity extends Activity
{
	private static final int STATUSLAYOUTID = 151500;
	Trip t;
	private static final String TAG = "MainActivity";
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		try
		{
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_main);
			Log.i(TAG, "onCreate");
			updateLocation.InitService(this);
		}
		catch(Exception e)
		{
			TextView error = (TextView) findViewById(R.id.errordev);
			error.setText(e.toString());
		}
	}
	
	public void onClick(View v)
	{
		Button b = (Button)v;
		switch(b.getId())
		{
		case R.id.btncreate:
			startTripCreator();
			break;
		case R.id.btnview:
			startTripHistoryViewer();
			break;
		case R.id.btnexit:
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_HOME);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			break;
		}
	}
	
	/**
	 * Receive result from CreateTripActivity here.
	 * Can be used to save instance of Trip object
	 * which can be viewed in the ViewTripActivity.
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		// TODO - fill in here
		try
		{
			super.onActivityResult(requestCode, resultCode, data);
			Bundle b = data.getExtras();
			if(b!=null)
			{
				Trip trip = b.getParcelable("trip");
				onReceivedTrip(trip);
			}
		}
		catch(Exception e)
		{
			TextView error = (TextView) findViewById(R.id.errordev);
			error.setText(e.toString());
		}
	}
	
	/**
	 * This method should start the
	 * Activity responsible for creating
	 * a Trip.
	 */
	public void startTripCreator() 
	{
		try
		{
			Intent intent = new Intent(this, CreateTripActivity.class);
			startActivityForResult(intent, 1);
		}
		catch(Exception e)
		{
			TextView error = (TextView) findViewById(R.id.errordev);
			error.setText(e.toString());
		}
	}
	
	/**
	 * This method should start the
	 * Activity responsible for viewing
	 * a Trip.
	 */
	public void startTripViewer() {
		
		// TODO - fill in here
		try
		{
			Intent intent = new Intent(this, ViewTripActivity.class);
			Bundle b = new Bundle();
			b.putParcelable("trip", t);
			intent.putExtras(b);
	        startActivity(intent);
		}
		catch(Exception e)
		{
			TextView error = (TextView) findViewById(R.id.errordev);
			error.setText(e.toString());
		}
	}
	
	public void startTripHistoryViewer() 
	{
		try
		{
			Intent intentTH = new Intent(this, TripHistoryActivity.class);
			startActivity(intentTH); 
		}
		catch(Exception e)
		{
			Toast.makeText(this, "Exception : "+e.toString(),Toast.LENGTH_LONG).show();
		}
	}

	/**
	 * This method should be called when
	 * a Trip is returned to the main
	 * activity. Remember that the Trip
	 * will not be returned as a Trip object;
	 * it will be in some persisted form. The
	 * method that calls onReceivedTrip should
	 * extract a Trip object from however it
	 * receives it, and pass that object to 
	 * onReceivedTrip.
	 * 
	 * @param trip The Trip that
	 * has been created in the trip creator
	 * Activity.
	 */
	public void onReceivedTrip(Trip trip) {
		
		// TODO - fill in here
		t=trip;
	}		
	

	private int dp(int i) {
		// TODO Auto-generated method stub
		final float scale = this.getResources().getDisplayMetrics().density;
		int pixels = (int) (i * scale + 0.5f);
		return pixels;
		
	}
}
