package com.nyu.cs9033.eta.models;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;


public class Trip implements Parcelable 
{
	protected String tripName;
	protected String locations;
	//protected String friends;
	protected String date;
	protected String time;	
	protected long tripId;
	protected ArrayList<Person> friends = new ArrayList<Person>();
	protected String creator;
	protected long serverRefId;
	
	public ArrayList<Person> getFriends() {
		return friends;
	}
	public void setFriends(ArrayList<Person> friends) {
		this.friends = friends;
	}
	
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getTripName() {
		return tripName;
	}
	public void setTripName(String tripName) {
		this.tripName = tripName;
	}
	public String getLocations() {
		return locations;
	}
	
	public void setTime(String time) {
		this.time = time;
	}
	public String getTime() {
		return time;
	}
	
	public void setLocations(String locations) {
		this.locations = locations;
	}
	public long getTripId() {
		return tripId;
	}
	public void setTripId(long tripId) {
		this.tripId = tripId;
	}
	
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public long getServerRefId() {
		return serverRefId;
	}
	public void setServerRefId(long serverRefId) {
		this.serverRefId = serverRefId;
	}

	
	//Date date;
	public static final Parcelable.Creator<Trip> CREATOR = new Parcelable.Creator<Trip>() {
		@Override
		public Trip createFromParcel(Parcel p) {
			return new Trip(p);
		}

		@Override
		public Trip[] newArray(int size) {
			return new Trip[size];
		}
	};
	private static final String TAG = "Trip";
	
	public Trip()
	{
		this.serverRefId = 0;
	}
	
	/**
	 * Create a Trip model object from a Parcel
	 * 
	 * @param p The Parcel used to populate the
	 * Model fields.
	 */
	public Trip(Parcel p) 
	{		
		// TODO - fill in here
		tripId = p.readLong();
		tripName = p.readString();
		locations = p.readString();
		p.readTypedList(friends, Person.CREATOR);
		date = p.readString();
		time = p.readString();
		creator = p.readString();
		
	}
	
	/**
	 * Create a Trip model object from arguments
	 * @param date 
	 * 
	 * @param args Add arbitrary number of arguments to
	 * instantiate trip class.
	 */
	public Trip(long tripId, String tripName, String locations,String creator, String time, String date, long serverRefId, ArrayList<Person> friends)
	{	
		// TODO - fill in here, please note you must have more arguments here
		this.tripId = tripId;
		this.tripName = tripName;
		this.locations = locations;
		this.friends = friends;		
		this.time= time;
		this.date = date;
		this.creator = creator;
		this.serverRefId=serverRefId;
		//this.friends = friends;
	}

	@Override
	public void writeToParcel(Parcel out, int arg1) {
		
		// TODO - fill in here
		out.writeLong(tripId);
		out.writeString(tripName);
		out.writeString(locations);
		out.writeTypedList(friends);
		out.writeString(date);
		out.writeString(time);
		out.writeString(creator);
		out.writeLong(serverRefId);
	}
	
	/**
	 * Feel free to add additional functions as necessary below.
	 */
	
	
	/**
	 * Do not implement
	 */
	@Override
	public int describeContents() {
		// Do not implement!
		return 0;
	}
	public JSONObject toJSON()
	{
		JSONObject json = new JSONObject();
		try 
		{
			json.put("command", "CREATE_TRIP");
			json.put("location", locations);
			json.put("datetime", System.currentTimeMillis()/1000);
			//json.put("time", time);
			
			ArrayList<String> people = new ArrayList<String>();
			for(Person p : friends)
			{
				people.add(p.getName());
			}
			json.put("people", new JSONArray (people));
		}
		catch (Exception e) 
		{
			Log.i(TAG, "Exception in toJSON :" + e.toString());
		}
		return json;
	}
}
