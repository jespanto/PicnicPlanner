package com.google.gwt.picnicplanner.client;

import java.io.Serializable;

public class WeatherForecast implements Serializable {
	
	private String day;
	private int low;
	private int high;
	private int code;
	
	public WeatherForecast(){
		
	}
	
	public String getDay() {
		return day;
	}
	
	public void setDay(String day) {
		this.day = day;
	}
	
	public int getLow() {
		return low;
	}

	public void setLow(int low) {
		this.low = low;
	}

	public int getHigh() {
		return high;
	}

	public void setHigh(int high) {
		this.high = high;
	}
	
	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

}
