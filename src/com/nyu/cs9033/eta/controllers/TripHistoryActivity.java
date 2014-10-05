package com.nyu.cs9033.eta.controllers;
import java.util.ArrayList;


import com.nyu.cs9033.eta.models.Person;
import com.nyu.cs9033.eta.models.Trip;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.nyu.cs9033.eta.R;

public class TripHistoryActivity extends ListActivity
{
	TripDatabaseHelper helper = new TripDatabaseHelper(this);
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		try
		{
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_history);
			
			String history[] = helper.getColsTripHistoryList();
			int tripdisplay[] = new int[]{R.id.tripid, R.id.tripname};
			
			
			Cursor c = helper.getAllTrips();
			
			SimpleCursorAdapter sca = new SimpleCursorAdapter(this, R.layout.trip_each_row, c, history, tripdisplay);
			setListAdapter(sca);
		}
		catch(Exception e)
		{
			Toast.makeText(this, "Exception : "+e.toString(),Toast.LENGTH_LONG).show();
		}
	}
	
	@Override
    public void onListItemClick(ListView l, View v, int position, long id) 
	{
		try
		{
			Intent intent = new Intent(this, ViewTripActivity.class);
			
			long trip_id = Long.parseLong(((TextView)v.findViewById(R.id.tripid)).getText().toString());
			Trip t = helper.getTrip(trip_id);
			t.setTripId(trip_id);
			
			ArrayList<Person> p = new ArrayList<Person>();
			p = helper.getTripPersons(trip_id);
			t.setFriends(p);
			
			Bundle b = new Bundle();
			b.putParcelable("trip", t);
			intent.putExtras(b);
			
	        startActivityForResult(intent, 1);
		}
		catch(Exception e)
		{
			Toast.makeText(this, "Exception : "+e.toString(),Toast.LENGTH_LONG).show();
		}
    }
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		try
		{
			super.onActivityResult(requestCode, resultCode, data);
			if(resultCode==RESULT_OK)
			{
			   Intent refresh = new Intent(this, TripHistoryActivity.class);
			   startActivity(refresh);
			   this.finish();
			}
		}
		catch(Exception e)
		{
			Toast.makeText(this, "Exception : "+e.toString(),Toast.LENGTH_LONG).show();
		}
    }
}
