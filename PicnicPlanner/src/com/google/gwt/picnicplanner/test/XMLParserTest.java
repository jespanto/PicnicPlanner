package com.google.gwt.picnicplanner.test;

import static org.junit.Assert.*;

import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import com.google.gwt.picnicplanner.client.Facility;
import com.google.gwt.picnicplanner.client.Park;
import com.google.gwt.picnicplanner.client.ParkList;
import com.google.gwt.picnicplanner.client.Washroom;
import com.google.gwt.picnicplanner.server.XMLParser;

public class XMLParserTest {

	@Test
	public void test() {
		try {
			XMLReader reader =XMLReaderFactory.createXMLReader();
			XMLParser handler = new XMLParser();
			reader.setContentHandler(handler);
			reader.parse("./files/parks_facilities.xml");
			ParkList parks=new ParkList(handler.parks);
			
			Park park = parks.getById(124);
			List<Facility> facilities = new ArrayList<Facility>();
			facilities.add(new Facility(1, "Playground"));
			assertEquals(park,new Park(124,"Marpole Park","1410 W 72nd Avenue",49.205507,-123.138307,0.27,
					"Marpole","http://vancouver.ca/community_profiles/marpole/index.htm",facilities,null,null));
			
			park = parks.getById(207);
			List<String> features = new ArrayList<String>();
			features.add("Seawall");
			List<Washroom> washrooms = new ArrayList<Washroom>();
			washrooms.add(new Washroom("Concession","Caretaker on site","7:00 am - Dusk","Dawn to Dusk"));
			facilities.clear();
			facilities.add(new Facility(1,"Field Houses"));
			facilities.add(new Facility(1,"Outdoor Roller Hockey Rinks"));
			facilities.add(new Facility(1,"Beaches"));
			facilities.add(new Facility(1,"Dogs Off-Leash Areas"));
			facilities.add(new Facility(1,"Food Concessions"));
			facilities.add(new Facility(1,"Ball Hockey"));
			assertEquals(park,new Park(207, "Sunset Beach Park","1204 Beach Avenue",49.281211,-123.139681,3.61,
					"West End","http://vancouver.ca/community_profiles/west_end/index.htm",facilities,features,washrooms));

		} catch (SAXException se) {
			se.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
}