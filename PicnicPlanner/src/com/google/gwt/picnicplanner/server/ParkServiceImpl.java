package com.google.gwt.picnicplanner.server;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.net.MalformedURLException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import com.google.gwt.picnicplanner.client.NotLoggedInException;
import com.google.gwt.picnicplanner.client.Park;
import com.google.gwt.picnicplanner.client.ParkService;
import com.google.gwt.picnicplanner.client.Weather;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class ParkServiceImpl extends RemoteServiceServlet implements ParkService {
	
	private static final Logger LOG = Logger.getLogger(ParkServiceImpl.class.getName());
	private static final PersistenceManagerFactory PMF = JDOHelper.getPersistenceManagerFactory("transactions-optional");

	public void addFacebookID(Long facebookid) throws NotLoggedInException {
	    checkLoggedIn();
	    PersistenceManager pm = getPersistenceManager();
	    try {
	    	Query q = pm.newQuery(FacebookID.class);
	    	q.setOrdering("createDate");
	    	List<FacebookID> FacebookIDs = (List<FacebookID>) q.execute();
	    	for (FacebookID f : FacebookIDs) {
	    		if (facebookid == f.getFacebookID()) {
					return;
				}
			}
	      pm.makePersistent(new FacebookID(facebookid));
	    } finally {
	      pm.close();
	    }
	 }
	
	public Long[] getFacebookIDs() throws NotLoggedInException {
		checkLoggedIn();
		PersistenceManager pm = getPersistenceManager();
		List<Long> ids = new ArrayList<Long>();
		try {
			Query q = pm.newQuery(FacebookID.class);
			q.setOrdering("createDate");
			List<FacebookID> FacebookIDs = (List<FacebookID>) q.execute();
			for (FacebookID f : FacebookIDs) {
				if (!ids.contains(f.getFacebookID()))
					ids.add(f.getFacebookID());
			}
		} finally {
			pm.close();
		}
		return (Long[]) ids.toArray(new Long[0]);
	}
	
	public Weather getWeatherDetails() throws NotLoggedInException{
		checkLoggedIn();
		WeatherParser parser = null;
		try {
			parser = new WeatherParser();
			XMLReader reader = XMLReaderFactory.createXMLReader();
			reader.setContentHandler(parser);
		    URL url = new URL("http://weather.yahooapis.com/forecastrss?w=9807");
		    reader.parse(new InputSource(url.openStream()));
		} 
			catch (Exception e) {
			e.printStackTrace();
		} 
		return parser.weather;
	}
	
	public void addHistory(String symbol) throws NotLoggedInException {
	    checkLoggedIn();
	    PersistenceManager pm = getPersistenceManager();
	    try {
	      pm.makePersistent(new History(getUser(), symbol));
	    } finally {
	      pm.close();
	    }
	 }
	
	public void removeHistory(String symbol) throws NotLoggedInException {
		checkLoggedIn();
		PersistenceManager pm = getPersistenceManager();
		try {
			long deleteCount = 0;
			Query q = pm.newQuery(History.class, "user == u");
			q.declareParameters("com.google.appengine.api.users.User u");
			List<History> history = (List<History>) q.execute(getUser());
			for (History h : history) {
				if (symbol.equals(h.getSymbol())) {
					deleteCount++;
					pm.deletePersistent(h);
				}
			}
			if (deleteCount != 1) {
				LOG.log(Level.WARNING, "removeStock deleted "+deleteCount+" Stocks");
			}
		} finally {
			pm.close();
		}
	}
	
	public String[] getHistory() throws NotLoggedInException {
		checkLoggedIn();
		PersistenceManager pm = getPersistenceManager();
		List<String> symbols = new ArrayList<String>();
		try {
			Query q = pm.newQuery(History.class, "user == u");
			q.declareParameters("com.google.appengine.api.users.User u");
			q.setOrdering("createDate");
			List<History> history = (List<History>) q.execute(getUser());
			for (History h : history) {
				symbols.add(h.getSymbol());
			}
		} finally {
			pm.close();
		}
		return (String[]) symbols.toArray(new String[0]);
	}

	@Override
	public void addFavorite(String symbol) throws NotLoggedInException {
	    checkLoggedIn();
	    PersistenceManager pm = getPersistenceManager();
	    try {
		  Query q = pm.newQuery(Favorite.class, "user == u");
		  q.declareParameters("com.google.appengine.api.users.User u");
		  List<Favorite> favorites = (List<Favorite>) q.execute(getUser());
		  int favoritesCount = favorites.size();
		  favoritesCount++;
	      pm.makePersistent(new Favorite(getUser(), symbol, favoritesCount));
	    } finally {
	      pm.close();
	    }
	}

	@Override
	public void removeFavorite(String symbol) throws NotLoggedInException {
	    checkLoggedIn();
	    PersistenceManager pm = getPersistenceManager();
	    try {
	      long deleteCount = 0;
	      Query q = pm.newQuery(Favorite.class, "user == u");
	      q.declareParameters("com.google.appengine.api.users.User u");
	      q.setOrdering("position asc");
	      List<Favorite> favorites = (List<Favorite>) q.execute(getUser());
	      int position = 0;
	      for (Favorite f : favorites) {
	        if (symbol.equals(f.getSymbol())) {
	          deleteCount++;
	          position = f.getPosition();
	          for (Favorite a: favorites) {
					if (a.getPosition() ==  (position + 1) && !a.equals(f)) {
						a.setPosition(position);
						position++;
					}
	          }
	          pm.deletePersistent(f);
	        }
	      }
/*	      position++;
	      for (Favorite f : favorites) {
	    	if (f.getPosition() == position) {
	    		f.setPosition(position - 1);
	    		position++;
	    	}
	      }*/
	      if (deleteCount != 1) {
	        LOG.log(Level.WARNING, "removeStock deleted "+deleteCount+" Stocks");
	      }
	    } finally {
	      pm.close();
	    }
	}

	@Override
	public String[] getFavorites() throws NotLoggedInException {
	    checkLoggedIn();
	    PersistenceManager pm = getPersistenceManager();
	    List<String> symbols = new ArrayList<String>();
	    try {
	      Query q = pm.newQuery(Favorite.class, "user == u");
	      q.declareParameters("com.google.appengine.api.users.User u");
	      q.setOrdering("position asc");
	      List<Favorite> favorites = (List<Favorite>) q.execute(getUser());
	      for (Favorite f : favorites) {
	        symbols.add(f.getSymbol());
	      }
	    } finally {
	      pm.close();
	    }
	    return (String[]) symbols.toArray(new String[0]);
	}
	
	
	@Override
	public void changeFavorites(String symbol, String change) throws NotLoggedInException {
		checkLoggedIn();
		PersistenceManager pm = getPersistenceManager();
		try {
			Query q = pm.newQuery(Favorite.class, "user == u");
			q.declareParameters("com.google.appengine.api.users.User u");
			List<Favorite> favorites = (List<Favorite>) q.execute(getUser());
			for (Favorite f : favorites) {
				if (symbol.equals(f.getSymbol())) {
					if (change.matches("up")) {
						f.setPosition(f.getPosition() - 1);
						for (Favorite a: favorites) {
							if (a.getPosition() ==  f.getPosition() && !a.equals(f)) {
								a.setPosition(f.getPosition() + 1);
							}
						}
					} else {
						f.setPosition(f.getPosition() + 1);
						for (Favorite a: favorites) {
							if (a.getPosition() ==  f.getPosition() && !a.equals(f)) {
								a.setPosition(f.getPosition() - 1);
							}
						}
					}
				}
			}
		} finally {
			pm.close();
		}
	}
	
	
	/*	@Override
	public List<Park> getParks() throws NotLoggedInException {
		
	}*/
	
	public List<Park> retrieveAllParks() throws NotLoggedInException {
		checkLoggedIn();
		//PersistenceManager pm = getPersistenceManager();
		XMLParser handler = null;
		try {
			handler = new XMLParser();
			XMLReader reader = XMLReaderFactory.createXMLReader();
			reader.setContentHandler(handler);
			URL HTTP_URL = new URL("http://www.ugrad.cs.ubc.ca/~c5h8/parks_facilities.xml");
			reader.parse(new InputSource(HTTP_URL.openStream()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
/*		for (Park p : handler.parks) {
			pm.makePersistent(p);
		} */
		
		//pm.close();
		return handler.parks;
	}
	
	private void checkLoggedIn() throws NotLoggedInException {
		if (getUser() == null) {
			throw new NotLoggedInException("Not logged in.");
		}
	}
	
	private User getUser() {
		UserService userService = UserServiceFactory.getUserService();
		return userService.getCurrentUser();
	}

	private PersistenceManager getPersistenceManager() {
		return PMF.getPersistenceManager();
	}
	
}
