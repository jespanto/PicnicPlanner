package com.google.gwt.picnicplanner.client;
import java.io.Serializable;

public class Washroom implements Serializable {
	
	private String location;
	private String notes;
	private String summerhours;
	private String winterhours;
	
	public Washroom(){};
	public Washroom(String location, String notes, String summerhours,
			String winterhours) {
		super();
		this.location = location;
		this.notes = notes;
		this.summerhours = summerhours;
		this.winterhours = winterhours;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getSummerhours() {
		return summerhours;
	}

	public void setSummerhours(String summerhours) {
		this.summerhours = summerhours;
	}

	public String getWinterhours() {
		return winterhours;
	}

	public void setWinterhours(String winterhours) {
		this.winterhours = winterhours;
	}
}
