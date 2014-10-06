package com.google.gwt.picnicplanner.client;

import java.util.List;
import java.io.Serializable;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

//import com.google.appengine.api.users.User;
import com.google.gwt.maps.client.geom.LatLng;

//@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Park implements Serializable {

	//private static final long serialVersionUID = 7554205327839083788L;
	//@PrimaryKey
	//@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	//private Long identifier;
	//@Persistent
	//private User user;
	//@Persistent
	private int id;
	//@Persistent
	private String name;
	//@Persistent
	private String address;
	// private LatLng location;
	//@Persistent
	private double Lat;
	//@Persistent
	private double Long;
	//@Persistent
	private double size;
	//@Persistent
	private String neighbourhood;
	//@Persistent
	private String neighbourhoodurl;
	//@Persistent
	private List<Facility> facilities;
	//@Persistent
	private List<String> specialfeatures;
	//@Persistent
	private List<Washroom> washrooms;

	public Park() {
	}

	public Park(int id) {
		this.id = id;
		this.washrooms = null;
	}

	public Park(//User user, 
			int id,
			String name,
			String address,
			double lat, double longitude, double size, String neighbourhood, String neighbourhoodurl,
			List<Facility> facilities, List<String> specialfeatures, List<Washroom> washrooms) {
		//this.user = user;
		this.id = id;
		this.name = name;
		this.address = address;
		this.Lat = lat;
		this.Long = longitude;
		this.size = size;
		this.neighbourhood = neighbourhood;
		this.neighbourhoodurl = neighbourhoodurl;
		this.facilities = facilities;
		this.specialfeatures = specialfeatures;
		this.washrooms = washrooms;
	}

	
	
/*	public Long getIdentifier() {
		return this.identifier;
	}
	
	public User getUser() {
		return this.user;
	}


	public void setUser(User user) {
		this.user = user;
	}*/

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(Lat);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(Long);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((address == null) ? 0 : address.hashCode());
		result = prime * result + id;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((neighbourhood == null) ? 0 : neighbourhood.hashCode());
		temp = Double.doubleToLongBits(size);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Park other = (Park) obj;
		if (Double.doubleToLongBits(Lat) != Double.doubleToLongBits(other.Lat))
			return false;
		if (Double.doubleToLongBits(Long) != Double
				.doubleToLongBits(other.Long))
			return false;
		if (address == null) {
			if (other.address != null)
				return false;
		} else if (!address.equals(other.address))
			return false;
		if (id != other.id)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (neighbourhood == null) {
			if (other.neighbourhood != null)
				return false;
		} else if (!neighbourhood.equals(other.neighbourhood))
			return false;
		if (Double.doubleToLongBits(size) != Double
				.doubleToLongBits(other.size))
			return false;
		return true;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public double getLat() {
		return Lat;
	}

	public void setLat(double lat) {
		this.Lat = lat;
	}

	public double getLong() {
		return Long;
	}

	public void setLong(double longitude) {
		this.Long = longitude;
	}

	public double getSize() {
		return size;
	}

	public void setSize(double size) {
		this.size = size;
	}

	public String getNeighbourhood() {
		return neighbourhood;
	}

	public void setNeighbourhood(String neighbourhood) {
		this.neighbourhood = neighbourhood;
	}
	
	public String getNeighbourhoodUrl() {
		return neighbourhoodurl;
	}

	public void setNeighbourhoodUrl(String neighbourhoodurl) {
		this.neighbourhoodurl = neighbourhoodurl;
	}

	public List<Facility> getFacilities() {
		return facilities;
	}

	public void setFacilities(List<Facility> facilities) {
		this.facilities = facilities;
	}

	public List<String> getSpecialFeatures() {
		return specialfeatures;
	}

	public void setSpecialFeatures(List<String> specialfeatures) {
		this.specialfeatures = specialfeatures;
	}

	public void setWashrooms(List<Washroom> washrooms) {
		this.washrooms = washrooms;
	}
	
	public List<Washroom> getWashrooms() {
		return washrooms;
	}

	public boolean hasWashroom() {
		return !(washrooms.get(0).getLocation() == null);
	}

	public Integer hasWashroomInt() {
		if (!(washrooms.get(0).getLocation() == null)) {
			return 1;
		} else {
			return 0;
		}
	}
	
	public LatLng getLocation() {
		LatLng l = LatLng.newInstance(this.Lat ,this.Long);
		return l;
	}
}
