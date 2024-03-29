package com.nyu.cs9033.eta.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Person implements Parcelable 
{
	protected String name;
	protected String location;
	protected String phonenum;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getPhoneNum() {
		return phonenum;
	}
	public void setPhoneNum(String phone) {
		this.phonenum = phone;
	}

	
	
	public static final Parcelable.Creator<Person> CREATOR = new Parcelable.Creator<Person>() {
		@Override
		public Person createFromParcel(Parcel p) {
			return new Person(p);
		}

		@Override
		public Person[] newArray(int size) {
			return new Person[size];
		}
	};
	
	/**
	 * Create a Person model object from a Parcel
	 * 
	 * @param p The Parcel used to populate the
	 * Model fields.
	 */
	public Person(Parcel p) {
		
		// TODO - fill in here
		name = p.readString();
		location = p.readString();
		phonenum = p.readString();
	}
	
	/**
	 * Create a Person model object from arguments
	 * 
	 * @param args Add arbitrary number of arguments to
	 * instantiate trip class.
	 */
	public Person(String name, String location, String phone) {
		
		// TODO - fill in here, please note you must have more arguments here
		this.name = name;
		this.location = location;
		this.phonenum=phone;
	}

	@Override
	public void writeToParcel(Parcel out, int arg1) {
		
		// TODO - fill in here 
		out.writeString(name);
		out.writeString(location);
		out.writeString(phonenum);
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
}
