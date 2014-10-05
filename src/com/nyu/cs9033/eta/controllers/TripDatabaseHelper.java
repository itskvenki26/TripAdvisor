package com.nyu.cs9033.eta.controllers;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.nyu.cs9033.eta.models.Person;
import com.nyu.cs9033.eta.models.Trip;
import com.nyu.cs9033.eta.R;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class TripDatabaseHelper extends SQLiteOpenHelper {
	public static final boolean DEBUG = true;
	// private static final String TAG = "DBAdapter";
	private static final int DATABASE_VERSION = 6;
	private static final String DATABASE_NAME = "trips";

	private static final String TABLE_TRIP = "trip";
	private static final String TRIP_ID = "_id";
	private static final String TRIP_NAME = "trip_name";
	private static final String TRIP_DESTINATION = "trip_destination";
	private static final String TRIP_CREATOR = "trip_creator";
	private static final String TRIP_DATE = "trip_date";
	private static final String TRIP_TIME = "trip_time";
	private static final String C_SERVER_REF_ID = "server_ref_id";

	private static final String TABLE_PERSON = "person";
	private static final String PERSON_ID = "_id";
	private static final String PERSON_NAME = "person_name";
	private static final String PERSON_LOCATION = "person_location";
	private static final String PERSON_PHONE = "person_phone";
	private static final String PERSON_TRIP_ID = "person_trip_id";

	private String columnTrip[] = { TRIP_ID, TRIP_NAME, TRIP_DESTINATION,
			TRIP_CREATOR, TRIP_TIME, TRIP_DATE, };
	private String columnPerson[] = { PERSON_ID, PERSON_NAME, PERSON_LOCATION,
			PERSON_TRIP_ID };
	private String columnTripHistoryList[] = { TRIP_ID, TRIP_NAME };

	public static final String TAG = "TripDatabaseHelper";

	// private static final String TABLE_TRIP=

	public String[] getColsTrip() {
		return columnTrip;
	}

	public String[] getColsPerson() {
		return columnPerson;
	}

	public String[] getColsTripHistoryList() {
		return columnTripHistoryList;
	}

	public TripDatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		if (DEBUG)
			Log.d(TAG, "new DB create");
		try {
			db.execSQL("CREATE TABLE " + TABLE_TRIP + "(" + TRIP_ID
					+ " INTEGER PRIMARY KEY AUTOINCREMENT, " +  C_SERVER_REF_ID + " INTEGER,"+TRIP_NAME
					+ " VARCHAR(100), " + TRIP_DESTINATION + " VARCHAR(100), "
					+ TRIP_CREATOR + " VARCHAR(20), " + TRIP_TIME
					+ " VARCHAR(10), " + TRIP_DATE + " VARCHAR(10))");

			/*Log.d(TAG, "CREATE TABLE " + TABLE_PERSON + "(" + PERSON_ID
					+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + PERSON_NAME
					+ " VARCHAR(20), " + PERSON_LOCATION + " VARCHAR(100), "
					+ PERSON_PHONE + " VARCHAR(20), " + PERSON_TRIP_ID
					+ " INTEGER))");*/
			db.execSQL("CREATE TABLE " + TABLE_PERSON + "(" + PERSON_ID
					+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + PERSON_NAME
					+ " VARCHAR(20), " + PERSON_LOCATION + " VARCHAR(100), "
					+ PERSON_PHONE + " VARCHAR(20), " + PERSON_TRIP_ID
					+ " INTEGER)");
		} catch (Exception exception) {
			if (DEBUG)
				Log.i(TAG, "Exception onCreate() exception");
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRIP);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PERSON);

		onCreate(db);
	}

	public long insertTrip(Trip trip) {
		ContentValues cv = new ContentValues();
		if(trip.getTripId()!=0L)
		{
			cv.put(TRIP_ID, trip.getTripId());
		}
		
		cv.put(TRIP_NAME, trip.getTripName());
		cv.put(C_SERVER_REF_ID, trip.getServerRefId());
		cv.put(TRIP_DESTINATION, trip.getLocations());
		cv.put(TRIP_CREATOR, trip.getCreator());
		cv.put(TRIP_TIME, trip.getTime());
		cv.put(TRIP_DATE, trip.getDate());
		long id = getWritableDatabase().insert(TABLE_TRIP, null, cv);
		// Log.i(TAG, "InsertTrip");
		return id;
	}

	public long insertPerson(long trip_id, Person person) {
		ContentValues cv = new ContentValues();
		cv.put(PERSON_NAME, person.getName());
		cv.put(PERSON_LOCATION, person.getLocation());
		cv.put(PERSON_PHONE, person.getPhoneNum());
		cv.put(PERSON_TRIP_ID, trip_id);
		// cv.put();

		return getWritableDatabase().insert(TABLE_PERSON, null, cv);
	}

	public Cursor getAllTrips() {
		Cursor c = getReadableDatabase().query(TripDatabaseHelper.TABLE_TRIP,
				columnTrip, null, null, null, null, null, null);
		return c;
	}

	public ArrayList<Person> getTripPersons(long trip_id) {
		Cursor c = getReadableDatabase().rawQuery(
				"SELECT * FROM " + TABLE_PERSON + " WHERE " + PERSON_TRIP_ID
						+ " = " + trip_id, null);
		c.moveToFirst();

		ArrayList<Person> alp = new ArrayList<Person>();
		while (!c.isAfterLast()) {
			Person p = new Person(c.getString(1), c.getString(2),
					c.getString(3));

			alp.add(p);
			c.moveToNext();
		}
		c.close();

		return alp;
	}

	public Trip getTrip(long trip_id) {
		Cursor c = getReadableDatabase().rawQuery(
				"SELECT * FROM " + TABLE_TRIP + " WHERE " + TRIP_ID + " = "
						+ trip_id, null);
		c.moveToFirst();

		Trip t = new Trip();

		t.setTripId(c.getLong(0));
		t.setTripName(c.getString(1));
		t.setLocations(c.getString(2));
		t.setCreator(c.getString(3));
		t.setTime(c.getString(4));
		t.setDate(c.getString(5));
		t.setServerRefId(c.getLong(6));
		t.setFriends(null);

		c.close();

		return t;
	}

	public void deleteTrip(long trip_id) {
		getWritableDatabase().delete(TABLE_TRIP, TRIP_ID + "=" + trip_id, null);
		getWritableDatabase().delete(TABLE_PERSON,
				PERSON_TRIP_ID + "=" + trip_id, null);
	}
}