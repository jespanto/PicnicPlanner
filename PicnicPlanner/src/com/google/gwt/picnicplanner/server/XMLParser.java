package com.google.gwt.picnicplanner.server;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.picnicplanner.client.Park;
import com.google.gwt.picnicplanner.client.Facility;
import com.google.gwt.picnicplanner.client.Washroom;

//import com.google.gwt.maps.client.geom.LatLng;

public class XMLParser extends DefaultHandler {

	// Store the information contained in an element in the buffer.
	private StringBuffer buffer;
	// Address is contained in different objects, so start storing info in the
	// address buffer.
	private StringBuffer address;
	private Facility facility;
	private List<Facility> facilities;
	private List<String> specialfeatures;
	private Washroom washroom;
	private List<Washroom> washrooms;
	private double lat;
	private double lng;
	private Park currentPark;
	public List<Park> parks;

	@Override
	public void startDocument() throws SAXException {
		// Initialize the buffers and field objects
		buffer = new StringBuffer();
		address = new StringBuffer();
		address.setLength(0);
		parks = new ArrayList<Park>();
		facility = new Facility();
		facilities = new ArrayList<Facility>();
		specialfeatures = new ArrayList<String>();
		washroom = new Washroom();
		washrooms = new ArrayList<Washroom>();
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
		if (qName.toLowerCase().equals("park"))
			currentPark = new Park(Integer.valueOf(atts.getValue("ID")));
		buffer.setLength(0);
	}

	@Override
	public void characters(char[] chars, int start, int length) throws SAXException {
		// add the information contained in the element.
		buffer.append(chars, start, length);
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if (qName.toLowerCase().equals("park")) {
			currentPark.setAddress(address.toString());
			// TODO include class definition
			// currentPark.setLocation(LatLng.newInstance(lat, lng));
			currentPark.setFacilities(facilities);
			currentPark.setSpecialFeatures(specialfeatures);
			currentPark.setWashrooms(washrooms);
			parks.add(currentPark);
			// reset the accumulator
			address.setLength(0);
			// reset the lists of facilities and special features.
			facilities = new ArrayList<Facility>();
			specialfeatures = new ArrayList<String>();
			washrooms = new ArrayList<Washroom>();
		}
		if (qName.toLowerCase().equals("name"))
			currentPark.setName(noBrackets(buffer));
		if (qName.toLowerCase().equals("streetnumber"))
			address.append(buffer);
		if (qName.toLowerCase().equals("streetname")) {
			address.append(" ");
			address.append(buffer);
		}
		if (qName.toLowerCase().equals("googlemapdest")) {
			// lat contains all numbers before the comma
			lat = Double.valueOf(buffer.substring(0, buffer.indexOf(",")));
			currentPark.setLat(lat);
			// lng contains all numbers after the comma
			lng = Double.valueOf(buffer.substring(1 + buffer.indexOf(",")));
			currentPark.setLong(lng);
		}
		if (qName.toLowerCase().equals("hectare"))
			currentPark.setSize(Double.valueOf(buffer.toString()));
		if (qName.toLowerCase().equals("neighbourhoodname"))
			currentPark.setNeighbourhood(buffer.toString());
		if (qName.toLowerCase().equals("neighbourhoodurl"))
			currentPark.setNeighbourhoodUrl(buffer.toString());
		if (qName.toLowerCase().equals("facilitytype"))
			facility.setType(buffer.toString());
		if (qName.toLowerCase().equals("facilitycount"))
			facility.setCount(Integer.valueOf(buffer.toString()));
		if (qName.toLowerCase().equals("facility")){
			facilities.add(facility);
			facility = new Facility();
		}
		if (qName.toLowerCase().equals("specialfeature"))
			specialfeatures.add(noBrackets(buffer).trim());
		if (qName.toLowerCase().equals("location")) {
			washroom = new Washroom();
			washroom.setLocation(buffer.toString());
		}
		if (qName.toLowerCase().equals("notes"))
			washroom.setNotes(buffer.toString());
		if (qName.toLowerCase().equals("summerhours"))
			washroom.setSummerhours(buffer.toString());
		if (qName.toLowerCase().equals("winterhours"))
			washroom.setWinterhours(buffer.toString());
		if (qName.toLowerCase().equals("washroom")){
			washrooms.add(washroom);
			washroom = new Washroom();
		}
		
		//currentPark.setUser(getUser());
	}

	@Override
	public void endDocument() throws SAXException {
		super.endDocument();
	}

	public String noBrackets(StringBuffer sb) {
		if (sb.toString().contains("(") && sb.toString().contains(")"))
			return (buffer.substring(0, buffer.indexOf("(") - 1) + buffer.substring(1 + buffer.indexOf(")")));
		else
			return sb.toString();
	}
	
/*	private User getUser() {
		UserService userService = UserServiceFactory.getUserService();
		return userService.getCurrentUser();
	}*/

}