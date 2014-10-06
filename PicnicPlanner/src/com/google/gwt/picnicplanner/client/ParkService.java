package com.google.gwt.picnicplanner.client;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("Parks")
public interface ParkService extends RemoteService {

	public void addFacebookID(Long facebookid) throws NotLoggedInException;
	public Long[] getFacebookIDs() throws NotLoggedInException;
	
	public Weather getWeatherDetails() throws NotLoggedInException;
	
	public void addHistory(String symbol) throws NotLoggedInException;
	public void removeHistory(String symbol) throws NotLoggedInException;
	public String[] getHistory() throws NotLoggedInException;
	
	public void addFavorite(String symbol) throws NotLoggedInException;
	public void removeFavorite(String symbol) throws NotLoggedInException;
	public String[] getFavorites() throws NotLoggedInException;
	public void changeFavorites(String symbol, String change) throws NotLoggedInException;
	
	//public List<Park> getParks() throws NotLoggedInException;
	public List<Park> retrieveAllParks() throws NotLoggedInException;
	
	
}
