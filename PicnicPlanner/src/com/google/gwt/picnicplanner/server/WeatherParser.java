package com.google.gwt.picnicplanner.server;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.google.gwt.picnicplanner.client.Weather;
import com.google.gwt.picnicplanner.client.WeatherForecast;



public class WeatherParser extends DefaultHandler {

	private StringBuffer buffer;
	public Weather weather;
	private List<WeatherForecast> nextFiveDays;
	private WeatherForecast forecast;
	@Override
	public void characters(char[] chars, int start, int length) throws SAXException {
		buffer.append(chars, start, length);
	}
	@Override
	public void endDocument() throws SAXException {
		super.endDocument();
	}
	@Override
	public void endElement(String arg0, String arg1, String arg2)
			throws SAXException {
		if(arg2.toLowerCase().equals("pubdate")){
			weather.setTime(buffer.substring(17));
		}
	}
	@Override
	public void startDocument() throws SAXException {
		buffer = new StringBuffer();
		weather = new Weather();
		nextFiveDays = new ArrayList<WeatherForecast>();
		forecast = new WeatherForecast();
	}
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes atts) throws SAXException {
		if(qName.toLowerCase().equals("yweather:condition")){
			weather.setCurrentTemp(Integer.valueOf(atts.getValue("temp")));
			weather.setCode(Integer.valueOf(atts.getValue("code")));
			weather.setForecast(atts.getValue("text"));
		}
		if(qName.toLowerCase().equals("yweather:forecast")){
			if (atts.getValue("day").equals("Mon")) forecast.setDay("Monday");
			else if (atts.getValue("day").equals("Tue")) forecast.setDay("Tuesday");
			else if (atts.getValue("day").equals("Wed")) forecast.setDay("Wednesday");
			else if (atts.getValue("day").equals("Thu")) forecast.setDay("Thursday");
			else if (atts.getValue("day").equals("Fri")) forecast.setDay("Friday");
			else if (atts.getValue("day").equals("Sat")) forecast.setDay("Saturday");
			else if (atts.getValue("day").equals("Sun")) forecast.setDay("Sunday");
			forecast.setLow(Integer.valueOf(atts.getValue("low")));
			forecast.setHigh(Integer.valueOf(atts.getValue("high")));
			forecast.setCode(Integer.valueOf(atts.getValue("code")));
			nextFiveDays.add(forecast);
			forecast = new WeatherForecast();
		}
		if(qName.toLowerCase().equals("guid")){
			weather.setNextFiveDays(nextFiveDays);
		}
		buffer.setLength(0);
	}
	
	
}
