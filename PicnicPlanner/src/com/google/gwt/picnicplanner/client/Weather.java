package com.google.gwt.picnicplanner.client;

import java.io.Serializable;
import java.util.List;

public class Weather implements Serializable{

	private String time;
	private int currentTemp;
	private String forecast;
	private int code;
	private List<WeatherForecast> nextFiveDays;
	

	public Weather(){
		
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
	
	public int getCurrentTemp() {
		return currentTemp;
	}
	
	public void setCurrentTemp(int temp) {
		this.currentTemp = temp;
	}

	public String getForecast() {
		return forecast;
	}

	public void setForecast(String forecast) {
		this.forecast = forecast;
	}
	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
	
	public List<WeatherForecast> getNextFiveDays() {
		return this.nextFiveDays;
	}
	
	public void setNextFiveDays(List<WeatherForecast> list) {
		this.nextFiveDays = list;
	}
	
}

