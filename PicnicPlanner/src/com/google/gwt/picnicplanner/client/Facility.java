package com.google.gwt.picnicplanner.client;
import java.io.Serializable;

public class Facility implements Serializable {
	private int count;
	private String type;
	
	public Facility(){};
	
	public Facility(int count, String type) {
		super();
		this.count = count;
		this.type = type;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}
