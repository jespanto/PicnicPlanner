package com.google.gwt.picnicplanner.client;

import java.util.List;

public class ParkList {
	List<Park> list;

	public ParkList(List<Park> list) {
		this.list = list;
	}

	public Park getById(int id) {
		for (Park park : list) {
			if (park.getId() == id)
				return park;
		}
		return null;
	}

	public List<Park> getParks() {
		return list;
	}

}
