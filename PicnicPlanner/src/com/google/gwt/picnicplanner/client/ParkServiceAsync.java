package com.google.gwt.picnicplanner.client;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ParkServiceAsync {
	
	 public void addFacebookID(Long facebookid, AsyncCallback<Void> async);
	 public void getFacebookIDs(AsyncCallback<Long[]> async);
	
	 public void getWeatherDetails(AsyncCallback<Weather> callback);
	
	 public void addHistory(String symbol, AsyncCallback<Void> async);
	 public void removeHistory(String symbol, AsyncCallback<Void> async);
	 public void getHistory(AsyncCallback<String[]> async);

	 public void addFavorite(String symbol, AsyncCallback<Void> async);
	 public void removeFavorite(String symbol, AsyncCallback<Void> async);
	 public void getFavorites(AsyncCallback<String[]> async);
	 public void changeFavorites(String symbol, String change, AsyncCallback<Void> async);
	 
	//public void getParks(AsyncCallback<List<Park>> callback);
	public void retrieveAllParks(AsyncCallback<List<Park>> callback);

}
