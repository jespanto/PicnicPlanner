package com.google.gwt.picnicplanner.client;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.cell.client.ClickableTextCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.geolocation.client.Geolocation;
import com.google.gwt.geolocation.client.Position;
import com.google.gwt.geolocation.client.Position.Coordinates;
import com.google.gwt.geolocation.client.PositionError;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.maps.client.InfoWindowContent;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.Maps;
import com.google.gwt.maps.client.control.MapTypeControl;
import com.google.gwt.maps.client.control.SmallZoomControl;
import com.google.gwt.maps.client.event.MarkerClickHandler;
import com.google.gwt.maps.client.event.MarkerMouseOutHandler;
import com.google.gwt.maps.client.event.MarkerMouseOverHandler;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.geom.Point;
import com.google.gwt.maps.client.geom.Size;
import com.google.gwt.maps.client.overlay.Icon;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.maps.client.overlay.MarkerOptions;
import com.google.gwt.media.client.Audio;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.user.client.ui.Anchor;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class PicnicPlanner implements EntryPoint {
	
	// Panels
	private VerticalPanel mainPanel = new VerticalPanel();
	private VerticalPanel filterPanel = new VerticalPanel();
	private VerticalPanel tablePanel = new VerticalPanel();
	private HorizontalPanel buttonPanel = new HorizontalPanel();
	private HorizontalPanel bottomButtonPanel = new HorizontalPanel();
	private HorizontalPanel bottomPanel = new HorizontalPanel();
	private VerticalPanel searchPanel = new VerticalPanel();
	private VerticalPanel distancePanel = new VerticalPanel();
	private VerticalPanel neighbourhoodPanel = new VerticalPanel();
	private VerticalPanel facilitiesPanel = new VerticalPanel();
	private VerticalPanel featuresPanel = new VerticalPanel();
	private VerticalPanel sizePanel = new VerticalPanel();
	private HorizontalPanel mapWeatherPanel = new HorizontalPanel();
	private DockPanel topPanel = new DockPanel();
	private HorizontalPanel linksPanel = new HorizontalPanel();
	private DockPanel parkPanel = new DockPanel();
	private SimplePanel weatherPanel = new SimplePanel();
	private PopupPanel favlistPopup = new PopupPanel(true);
	private PopupPanel historyPopup = new PopupPanel(true);
	private VerticalPanel favlistPanel = new VerticalPanel();
	private VerticalPanel historyPanel = new VerticalPanel();
	//private VerticalPanel myAccountPanel = new VerticalPanel();
	private HorizontalPanel favlistTitlePanel = new HorizontalPanel();
	private HorizontalPanel historyTitlePanel = new HorizontalPanel();
	private VerticalPanel friendsPanel = new VerticalPanel();
	
	// Labels
	private Label distanceLabel = new Label("Distance");
	private Label neighbourhoodLabel = new Label("Neighbourhood");
	private Label facilitiesLabel = new Label("Facilities");
	private Label featuresLabel = new Label("Special Features");
	private Label sizeLabel = new Label("Size");
	private Label numResultsLabel = new Label();
	private Label copyrightLabel = new Label("\u00a9 2013 Picnic Co.");
	private Label mapLoadingLabel = new Label("Loading map and local weather...");
	
	private ListBox filterListBoxNeighbourhood = new ListBox(true);
	private ListBox filterListBoxFacility = new ListBox(true);
	private ListBox filterListBoxSpecialFeatures = new ListBox(true);
	private ListBox filterBoxRadius = new ListBox();
	private ListBox filterBoxSize = new ListBox();
	private CheckBox filterCheckBoxWashroom = new CheckBox("Washrooms");
	private TextBox searchTextBox = new TextBox();
	private Button topSearchButton = new Button("SEARCH");
	private Button searchButton = new Button("SEARCH");
	private Button bottomSearchButton = new Button("SEARCH");
	private Button resetButton = new Button("RESET");
	private Button bottomResetButton = new Button("RESET");
	private String textBoxDefault = "Enter park name, neighbourhood, or street";
	private CellTable<Park> cellTable = new CellTable<Park>();
	private SimplePager pager = new SimplePager();

	// Login-related stuff
	private LoginInfo loginInfo = null;
//	private VerticalPanel loginPanel = new VerticalPanel();
//	private Label loginLabel = new Label("Please sign in to your Google Account to access the PicnicPlanner application.");
//	private Anchor signInLink = new Anchor("Sign In");
	private Anchor signOutLink = new Anchor("Sign Out");
	private String redirectUrl = "http://127.0.0.1:8888/PicnicPlanner.html?gwt.codesvr=127.0.0.1:9997";

	// Links
	private Anchor favlistLink = new Anchor("Favorites");
	private Anchor historyLink = new Anchor("History");
	private Anchor myAccountLink = new Anchor("Account");
	private Anchor homeLink = new Anchor("Home");

	//Create a data provider for celltable
	//Changes to parklist will automatically be displayed in celltable
	private ListDataProvider<Park> dataProvider = new ListDataProvider<Park>();
	private List<Park> parklist = dataProvider.getList();
	private ParkServiceAsync ParkSvc = (ParkServiceAsync) GWT.create(ParkService.class);
	private List<Park> PARKS = null;
	private Weather weather = null;
	
	//Facebook
	private String token;
	private Label greeting = new Label();
	
	//misc
	private LatLng userLocation = null;
	private Boolean justEntered = true;
	private int currentUnit = 0; // 0:C 1:F
	private List<Park> currentParklist = null;
	private int lastPage = 0; // 0: homepage 1: myaccount 2: myfavorites 3: history
	private Boolean searched = false;

	//Location tracking stuff
	private Boolean test = false;
	private Boolean firstUserLocation = true;

	private MapWidget map;
	private Marker userMarker = null;
	
	//Audio
	private Audio errorAudio;
	private Audio removeAudio;
	
	public void onModuleLoad() {
		// Check login status using login service
		// Deny access to picnicplanner app if not logged in
		
		errorAudio = Audio.createIfSupported();
		errorAudio.setSrc("/audio/error.m4r");
		
		token = Window.Location.getHash();
		LoginServiceAsync loginSvc = (LoginServiceAsync) GWT.create(LoginService.class);
		loginSvc.login(redirectUrl, new AsyncCallback<LoginInfo>() {
			public void onFailure(Throwable error) {
				handleError(error);
			}

			public void onSuccess(LoginInfo result) {
				loginInfo = result;
				if (loginInfo.isLoggedIn()) {
					if (!token.equals("") && !loginInfo.isLoggedInFacebook()) {
						loginInfo.setFacebookToken(token.substring(14,token.indexOf("&")));
						loginInfo.setLoggedInFacebook(true);
						fetchFacebookUserData();
						fetchFacebookPicData();
						fetchFacebookFriendData();
					}
					loadPicnicPlanner();
				} else {
					loadLogin();
				}
			}
		});
	}

	private void loadLogin() {
		//Assemble login panel.
		//signInLink.setHref(loginInfo.getLoginUrl());
		//loginPanel.add(loginLabel);
		//loginPanel.add(signInLink);
		//RootPanel.get().add(loginPanel);
		Window.Location.replace(loginInfo.getLoginUrl());
	}

	private void loadPicnicPlanner() {
		
		// make the RPC call to retrieve Park data
		//if (PARKS == null) {
		retrieveParks();
/*		} else {
			UIUpdate();
			
			Maps.loadMapsApi("", "2", false, new Runnable() {
				public void run() {
					final Geolocation geolocation = Geolocation.getIfSupported();
					if (geolocation == null) {
						errorAudio.play();
						Window.alert("Your browser does not support location tracking.");
					}

					//mapWeatherPanel.clear();
					displayMap();
					retrieveWeather();
					
					geolocation.watchPosition(new Callback<Position, PositionError>() {
						@Override
						public void onSuccess(Position result) {
							Coordinates co = result.getCoordinates();
							//the first userLocation is used to test the location tracking of the app
							if(firstUserLocation) {
								userLocation = LatLng.newInstance(49.249783, -123.155250);
							} else {
								userLocation = LatLng.newInstance(co.getLatitude(), co.getLongitude());
							}
							undisplayUserLocationMarker(map);
							displayUserLocationMarker(map);
						}

						@Override
						public void onFailure(PositionError reason) {
							errorAudio.play();
							Window.alert("Picnic Planner is unable to retrieve your current location.");
						}
					});
				}
			});
		}*/

		Column<Park, String> nameColumn = new Column<Park, String>(new ClickableTextCell()) {
			public String getValue(Park park) {
				return park.getName();
			}
		};
		
		nameColumn.setFieldUpdater(new FieldUpdater<Park, String>() {
			public void update(int index, Park object, String value) {
				addParkToHistory(object);
				parkInfo(object);
			}
		});

		Column<Park, String> streetNameColumn = new Column<Park, String>(new ClickableTextCell()) {
			public String getValue(Park park) {
				return park.getAddress();
			}
		};
		
		streetNameColumn.setFieldUpdater(new FieldUpdater<Park, String>() {
			public void update(int index, Park object, String value) {
				addParkToHistory(object);
				parkInfo(object);
			}
		});

		Column<Park, String> sizeColumn = new Column<Park, String>(new ClickableTextCell()) {
			public String getValue(Park park) {
				return String.valueOf(park.getSize());
			}
		};
		
		sizeColumn.setFieldUpdater(new FieldUpdater<Park, String>() {
			public void update(int index, Park object, String value) {
				addParkToHistory(object);
				parkInfo(object);
			}
		});

		Column<Park, String> neighbourhoodColumn = new Column<Park, String>(new ClickableTextCell()) {
			public String getValue(Park park) {
				return park.getNeighbourhood();
			}
		};
		
		neighbourhoodColumn.setFieldUpdater(new FieldUpdater<Park, String>() {
			public void update(int index, Park object, String value) {
				addParkToHistory(object);
				parkInfo(object);
			}
		});

		Column<Park, String> facilityColumn = new Column<Park, String>(new ClickableTextCell()) {
			public String getValue(Park park) {
				String s = "";
				int length = park.getFacilities().size();
				for (Facility a : park.getFacilities()) {
					s += a.getType();
					if (!(length - 1 <= 0)) {
						s += ", ";
						--length;
					}
				}

				return s;
			}
		};
		
		facilityColumn.setFieldUpdater(new FieldUpdater<Park, String>() {
			public void update(int index, Park object, String value) {
				addParkToHistory(object);
				parkInfo(object);
			}
		});

		Column<Park, String> specialFeatureColumn = new Column<Park, String>(new ClickableTextCell()) {
			public String getValue(Park park) {
				String s = "";
				int length = park.getSpecialFeatures().size();
				for (String a : park.getSpecialFeatures()) {
					s += a;
					if (!(length - 1 <= 0)) {
						s += ", ";
						--length;
					}
				}

				return s;
			}
		};
		
		specialFeatureColumn.setFieldUpdater(new FieldUpdater<Park, String>() {
			public void update(int index, Park object, String value) {
				addParkToHistory(object);
				parkInfo(object);
			}
		});

		Column<Park, String> washroomColumn = new Column<Park, String>(new ClickableTextCell()) {
			public String getValue(Park park) {
				return park.hasWashroom() ? "Yes" : "No";
			}
		};
		
		washroomColumn.setFieldUpdater(new FieldUpdater<Park, String>() {
			public void update(int index, Park object, String value) {
				addParkToHistory(object);
				parkInfo(object);
			}
		});
				
		// Make columns sortable
		nameColumn.setSortable(true);
		neighbourhoodColumn.setSortable(true);
		washroomColumn.setSortable(true);
		streetNameColumn.setSortable(true);
		sizeColumn.setSortable(true);
		facilityColumn.setSortable(true);
		specialFeatureColumn.setSortable(true);

		// Add the columns for park names, facility names, neighbourhood names, washrooms
		cellTable.addColumn(nameColumn, "Name");
		cellTable.addColumn(neighbourhoodColumn, "Neighbourhood");
		cellTable.addColumn(streetNameColumn, "Address");
		cellTable.addColumn(facilityColumn, "Facilities");
		cellTable.addColumn(specialFeatureColumn, "Features");
		cellTable.addColumn(washroomColumn, "Washrooms");
		cellTable.addColumn(sizeColumn, "Hectares");
		cellTable.setStyleName("pointer");
		cellTable.setWidth("880px");

		// Set width for each column
		cellTable.setColumnWidth(nameColumn, "150px");
		cellTable.setColumnWidth(neighbourhoodColumn, "100px");
		cellTable.setColumnWidth(streetNameColumn, "150px");
		cellTable.setColumnWidth(facilityColumn, "180px");
		cellTable.setColumnWidth(specialFeatureColumn, "100px");
		
		// Change cellTable style
		nameColumn.setCellStyleNames("smallFont");
		neighbourhoodColumn.setCellStyleNames("smallFont");
		washroomColumn.setCellStyleNames("smallFont");
		streetNameColumn.setCellStyleNames("smallFont");
		sizeColumn.setCellStyleNames("smallFont");
		facilityColumn.setCellStyleNames("verySmallFont");
		specialFeatureColumn.setCellStyleNames("verySmallFont");

		// Connect the table to the data provider
		dataProvider.addDataDisplay(cellTable);

		// Create paging controls
		pager.setDisplay(cellTable);

		// Construct the filter panel with listbox and checkbox widgets
		// add search and reset buttons 
		searchButton.setWidth("74px");
		resetButton.setWidth("74px");
		searchButton.setStyleName("returnButton");
		resetButton.setStyleName("returnButton");
		buttonPanel.add(searchButton);
		buttonPanel.add(resetButton);
		buttonPanel.setSpacing(4);
		searchPanel.add(buttonPanel);
		filterPanel.add(searchPanel);

		// add washroom checkbox
		filterPanel.add(filterCheckBoxWashroom);

		// add size filter
		filterBoxSize.addItem("No Limits");
		filterBoxSize.addItem("Very Small (<1ha)");
		filterBoxSize.addItem("Small (1-3ha)");
		filterBoxSize.addItem("Medium (3-5ha)");
		filterBoxSize.addItem("Large (5-10ha)");
		filterBoxSize.addItem("Very Large (>10ha)");
		filterBoxSize.setWidth("160px");
		sizeLabel.setStyleName("filterHeading");
		sizePanel.add(sizeLabel);
		sizePanel.add(filterBoxSize);
		filterPanel.add(sizePanel);

		// add distance filter
		filterBoxRadius.addItem("No limits");
		filterBoxRadius.addItem("Within 5km");
		filterBoxRadius.addItem("Within 10km");
		filterBoxRadius.addItem("Within 15km");
		filterBoxRadius.addItem("Within 25km");
		filterBoxRadius.addItem("Within 35km");
		filterBoxRadius.addItem("Within 50km");
		filterBoxRadius.setWidth("160px");
		distanceLabel.setStyleName("filterHeading");
		distancePanel.add(distanceLabel);
		distancePanel.add(filterBoxRadius);
		filterPanel.add(distancePanel);

		// add neighborhood filter
		filterListBoxNeighbourhood.setSize("160px", "200px");
		neighbourhoodLabel.setStyleName("filterHeading");
		neighbourhoodPanel.add(neighbourhoodLabel);
		neighbourhoodPanel.add(filterListBoxNeighbourhood);
		filterPanel.add(neighbourhoodPanel);

		// add facility filter
		filterListBoxFacility.setSize("160px", "200px");
		facilitiesLabel.setStyleName("filterHeading");
		facilitiesPanel.add(facilitiesLabel);
		facilitiesPanel.add(filterListBoxFacility);
		filterPanel.add(facilitiesPanel);

		// add special feature filter
		filterListBoxSpecialFeatures.setSize("160px", "160px");
		featuresLabel.setStyleName("filterHeading");
		featuresPanel.add(featuresLabel);
		featuresPanel.add(filterListBoxSpecialFeatures);
		filterPanel.add(featuresPanel);

		// add bottom search buttons
		bottomSearchButton.setWidth("74px");
		bottomResetButton.setWidth("74px");
		bottomSearchButton.setStyleName("returnButton");
		bottomResetButton.setStyleName("returnButton");
		bottomButtonPanel.add(bottomSearchButton);
		bottomButtonPanel.add(bottomResetButton);
		bottomButtonPanel.setSpacing(4);
		filterPanel.add(bottomButtonPanel);

		// add copyright
		copyrightLabel.setStyleName("smallGreyFont");
		filterPanel.add(copyrightLabel);
		filterPanel.setSpacing(15);
	
		// add map and table to tablePanel
		mapLoadingLabel.setStyleName("blinker");
		tablePanel.add(mapLoadingLabel);
		tablePanel.add(mapWeatherPanel);
		numResultsLabel.setHeight("20px");
		tablePanel.add(numResultsLabel);
		tablePanel.add(cellTable);
		tablePanel.add(pager);
		tablePanel.setSpacing(15);
		tablePanel.setWidth("100%");

		// add filters and tablePanel to bottomPanel
		bottomPanel.add(filterPanel);
		bottomPanel.add(tablePanel);

		// add logo
		final Image header = new Image();
		header.setUrl("/images/header.png");
		header.setSize("411px", "55px");
		header.setStyleName("pointer");
		header.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				resetEverything();
			}
		});

		// add textbox and large search button
		searchTextBox.setSize("400px", "30px");
		searchTextBox.setText(textBoxDefault);
		searchTextBox.setStyleName("textBoxMessage");
		searchTextBox.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (searchTextBox.getText().matches(textBoxDefault)) {
					searchTextBox.setText("");
					searchTextBox.setStyleName("textBoxInput");
				}
			}
		});
		searchTextBox.addKeyPressHandler(new KeyPressHandler() {
			@Override
			public void onKeyPress(KeyPressEvent event) {
				if (event.getNativeEvent().getKeyCode() != KeyCodes.KEY_ENTER) {
					String s = event.getCharCode() + "";
		            if(RegExp.compile("[^A-Za-z0-9-.() \t]").test(s)) {
		            	errorAudio.play();
		            	Window.alert("Please enter a valid name.");
		            	searchTextBox.cancelKey();
		            }
				}
			}
		});
		topSearchButton.setSize("120px", "35px");
		topSearchButton.setStyleName("largeSearchButton");

		
		topPanel.add(header, DockPanel.WEST);
		topPanel.add(linksPanel, DockPanel.NORTH);
		topPanel.add(searchTextBox, DockPanel.WEST);
		topPanel.add(topSearchButton, DockPanel.CENTER);
		topPanel.setWidth("100%");
		topPanel.setCellWidth(topSearchButton, "120px");
		topPanel.setCellVerticalAlignment(searchTextBox, HasVerticalAlignment.ALIGN_BOTTOM);
		topPanel.setCellHorizontalAlignment(searchTextBox, HasHorizontalAlignment.ALIGN_RIGHT);
		topPanel.setCellVerticalAlignment(topSearchButton, HasVerticalAlignment.ALIGN_BOTTOM);
		topPanel.setSpacing(15);

		
		Label favoritesLabel = new Label("Favorites");
		Label historyLabel = new Label("History");
		favoritesLabel.setStyleName("historyLabel");
		historyLabel.setStyleName("historyLabel");
		favlistTitlePanel.setWidth("100%");
		historyTitlePanel.setWidth("100%");
		favlistTitlePanel.setHeight("40px");
		historyTitlePanel.setHeight("40px");
		favlistTitlePanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		historyTitlePanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		favlistTitlePanel.add(favoritesLabel);
		historyTitlePanel.add(historyLabel);
		
		Anchor showHistoryMap = new Anchor("Show Map");
		Anchor showFavoritesMap = new Anchor("Show Map");
		Anchor clearHistory = new Anchor("Clear History");
		removeAudio = Audio.createIfSupported();
		removeAudio.setSrc("/audio/remove.m4r");
		
		showFavoritesMap.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				favlistPopup.hide();
				myFavorites();
			}
		});
		
		showHistoryMap.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				historyPopup.hide();
				myHistory();
			}
		});
		
		clearHistory.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				clearHistory();
			}
		});
		
		favlistTitlePanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		historyTitlePanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		favlistTitlePanel.add(showFavoritesMap);
		historyTitlePanel.add(clearHistory);
		historyTitlePanel.add(showHistoryMap);
		favlistTitlePanel.setSpacing(5);
		historyTitlePanel.setSpacing(5);
		
		favlistPanel.add(favlistTitlePanel);
		historyPanel.add(historyTitlePanel);
		favlistPanel.setSpacing(5);
		historyPanel.setSpacing(5);
		
		//attach the panels to house favorite list/ history entries
		favlistPopup.setWidget(favlistPanel);
		historyPopup.setWidget(historyPanel);
		favlistPopup.setGlassEnabled(true);
		historyPopup.setGlassEnabled(true);
		favlistPopup.setWidth("320px");
		historyPopup.setWidth("300px");
		favlistPopup.setAnimationEnabled(true);
		historyPopup.setAnimationEnabled(true);
		
		//if the link is clicked, display the appropriate popup-panel
		favlistLink.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				favlistPopup.center();
			}
		});
		historyLink.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				historyPopup.center();
			}
		});
		myAccountLink.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				//myAccountPopup.show();
				myAccount();
			}
		});
		homeLink.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				resetEverything();
			}
		});
		
		//Add label to display logged in user's nickname
		//Label loggedinasLabel = new Label("Logged in as, " + loginInfo.getNickname());
		if (!loginInfo.isLoggedInFacebook())
			greeting.setText("Hello, " + loginInfo.getNickname());
		linksPanel.add(greeting);
		//linksPanel.add(loggedinasLabel);
		linksPanel.add(homeLink);
		linksPanel.add(myAccountLink);
		linksPanel.add(favlistLink);
		linksPanel.add(historyLink);
		signOutLink.setHref(loginInfo.getLogoutUrl());

		linksPanel.add(signOutLink);
		linksPanel.setSpacing(15);

		mainPanel.add(linksPanel);
		mainPanel.setCellHorizontalAlignment(linksPanel, HasHorizontalAlignment.ALIGN_RIGHT);
		mainPanel.add(topPanel);
		mainPanel.add(bottomPanel);
		mainPanel.setWidth("1100px");

		RootPanel.get().add(mainPanel);

		
		// Listen for mouse events on the search and reset buttons.
		topSearchButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				justEntered = false;
				refreshParkList();
			}
		});

		searchButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				justEntered = false;
				refreshParkList();
			}
		});

		resetButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				justEntered = true;
				resetFilters();
			}
		});

		bottomSearchButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				justEntered = false;
				refreshParkList();
			}
		});

		bottomResetButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				justEntered = true;
				resetFilters();
			}
		});

		// Listen for keyboard events in the input box.
		searchTextBox.addKeyDownHandler(new KeyDownHandler() {
			public void onKeyDown(KeyDownEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					justEntered = false;
					refreshParkList();
				}
			}
		});

		// Add a ColumnSortEvent.ListHandler to support sorting in parklist
		ListHandler<Park> columnSortHandler = new ListHandler<Park>(parklist);
		columnSortHandler.setComparator(nameColumn, new Comparator<Park>() {
			public int compare(Park o1, Park o2) {
				if (o1 == o2) {
					return 0;
				}

				if (o1 != null) {
					return (o2 != null) ? o2.getName().compareTo(o1.getName()) : 1;
				}

				return -1;
			}
		});

		columnSortHandler.setComparator(neighbourhoodColumn, new Comparator<Park>() {
			public int compare(Park o1, Park o2) {
				if (o1 == o2) {
					return 0;
				}

				if (o1 != null) {
					return (o2 != null) ? o2.getNeighbourhood().compareTo(o1.getNeighbourhood()) : 1;

				}

				return -1;
			}
		});
		
		columnSortHandler.setComparator(facilityColumn, new Comparator<Park>() {
			public int compare(Park o1, Park o2) {
				Integer s1 = o1.getFacilities().size();
				Integer s2 = o2.getFacilities().size();
				if (o1 == o2) {
					return 0;
				}

				{
					return (o2 != null) ? s1.compareTo(s2) : 1;

				}
			}
		});
		
		columnSortHandler.setComparator(specialFeatureColumn, new Comparator<Park>() {
			public int compare(Park o1, Park o2) {
				Integer s1 = o1.getSpecialFeatures().size();
				Integer s2 = o2.getSpecialFeatures().size();
				if (o1 == o2) {
					return 0;
				}

				{
					return (o2 != null) ? s1.compareTo(s2) : 1;

				}
			}
		});
		
		columnSortHandler.setComparator(washroomColumn, new Comparator<Park>() {
			public int compare(Park o1, Park o2) {
				if (o1 == o2) {
					return 0;
				}

				if (o1 != null) {
					return (o2 != null) ? o1.hasWashroomInt().compareTo(o2.hasWashroomInt()) : 1;
				}

				return -1;
			}
		});

		columnSortHandler.setComparator(sizeColumn, new Comparator<Park>() {
			@SuppressWarnings("unused")
			public int compare(Park o1, Park o2) {
				Double d1 = new Double(o1.getSize());
				Double d2 = new Double(o2.getSize());
				if (o1 == o2) {
					return 0;
				}

				if (o1 != null) {
					return (o2 != null) ? d1.compareTo(d2) : 1;
				}

				return -1;
			}
		});

		columnSortHandler.setComparator(streetNameColumn, new Comparator<Park>() {
			public int compare(Park o1, Park o2) {
				if (o1 == o2) {
					return 0;
				}

				if (o1 != null) {
					return (o2 != null) ? o1.getAddress().compareTo(o2.getAddress()) : 1;
				}

				return -1;
			}
		});
		
		cellTable.addColumnSortHandler(columnSortHandler);
		
		// retrieve favorites data from datastore
	    loadFavorites();
	    //retrieve history data from datastore
	    loadHistory();
		
	}
	
	private void fetchFacebookUserData() {
		try {
			RequestBuilder rb = new RequestBuilder(RequestBuilder.GET,"https://graph.facebook.com/me?access_token=" + loginInfo.getFacebookToken());
			rb.setCallback(new RequestCallback() {
				public void onResponseReceived(Request request,Response response) {
					parseJsonUserData(response.getText());
					if (loginInfo.isLoggedInFacebook())
						greeting.setText("Hello, " + loginInfo.getFacebookName());
					//TODO:
					persistFacebookID(Long.valueOf(loginInfo.getFacebookId()));
				}
				public void onError(Request request, Throwable exception) {
					errorAudio.play();
					Window.alert("Error occurred" + exception.getMessage());
				}
			});
			rb.send();
		} catch (RequestException e) {
			errorAudio.play();
			Window.alert("Error occurred" + e.getMessage());
		}
	}
	
	private void parseJsonUserData(String json) {
		JSONValue value = JSONParser.parseLenient(json);
		JSONObject object = value.isObject();
		if (object.get("error") != null){
			loginInfo.setLoggedInFacebook(false);
		} else {
			loginInfo.setFacebookName(object.get("name").toString());
			loginInfo.setFacebookId(object.get("id").toString());
		}
	}

	private void fetchFacebookPicData() {
		try {
			RequestBuilder rb = new RequestBuilder(RequestBuilder.GET,
					"https://graph.facebook.com/me?fields=picture.width(100).height(100)&access_token=" + loginInfo.getFacebookToken());
			rb.setCallback(new RequestCallback() {
				public void onResponseReceived(Request request,Response response) {
					parseJsonPicData(response.getText());
				}
				public void onError(Request request, Throwable exception) {
					errorAudio.play();
					Window.alert("Error occurred" + exception.getMessage());
				}
			});
			rb.send();
		} catch (RequestException e) {
			errorAudio.play();
			Window.alert("Error occurred" + e.getMessage());
		}
	}

	private void parseJsonPicData(String json) {
		JSONValue value = JSONParser.parseLenient(json);
		JSONObject object = value.isObject();
		if (object.get("error") != null){
			loginInfo.setLoggedInFacebook(false);
		} else {
			JSONObject object2 = object.get("picture").isObject();
			JSONObject object3 = object2.get("data").isObject();
			loginInfo.setProfilePicUrl(object3.get("url").toString());
		}
	}

	private void fetchFacebookFriendData() {
		try {
			RequestBuilder rb = new RequestBuilder(RequestBuilder.GET,"https://graph.facebook.com/me/friends?fields=name,id,link&access_token=" + loginInfo.getFacebookToken());
			rb.setCallback(new RequestCallback() {
				public void onResponseReceived(Request request,Response response) {
					parseJsonFriendData(response.getText());
				}
				public void onError(Request request, Throwable exception) {
					errorAudio.play();
					Window.alert("Error occurred" + exception.getMessage());
				}
			});
			rb.send();
		} catch (RequestException e) {
			errorAudio.play();
			Window.alert("Error occurred" + e.getMessage());
		}
	}

	private void parseJsonFriendData(String json) {
		JSONValue value = JSONParser.parseLenient(json);
		JSONObject object = value.isObject();
		if (object.get("error") != null){
			loginInfo.setLoggedInFacebook(false);
		} else {
			JSONArray objectArray = object.get("data").isArray();

			Map<String,NameLinkPair> friends = new HashMap<String,NameLinkPair>();
			if (objectArray != null) {
				for (int i=0; i<objectArray.size(); i++){
					JSONObject friend = objectArray.get(i).isObject();
					String name = friend.get("name").toString();
					String id = friend.get("id").toString();
					String link = friend.get("link").toString();
					//remove extra quotes in strings before adding
					friends.put(id.substring(1,id.length()-1), new NameLinkPair(name.substring(1,name.length()-1),link.substring(1,link.length()-1)));
				}
			}
			loginInfo.setFriends(friends);
		}
	}
	
	private void persistFacebookID(Long facebookid) {
		ParkSvc.addFacebookID(facebookid, new AsyncCallback<Void>() {
			public void onFailure(Throwable error) {
				handleError(error);
			}
			public void onSuccess(Void ignore) {
				return;
			}
		});
	}
	
	private void loadFacebookFriends() {
		ParkSvc.getFacebookIDs(new AsyncCallback<Long[]>() {  
			public void onFailure(Throwable error) {  
				handleError(error);
			}  
			public void onSuccess(Long[] facebookids) {
				for (Long id : facebookids) {
					for (String friendid : loginInfo.getFriends().keySet()) {
						if (id.equals(Long.valueOf(friendid))) {
							displayFriend(friendid);
						}
					}
				}
			}  
		});
	}
	
	private void displayFriend(String friendid) {
		Anchor friendlink = new Anchor(loginInfo.getFriends().get(friendid).getName());
		friendlink.setHref(loginInfo.getFriends().get(friendid).getLink());
		friendsPanel.add(friendlink);
	}
	
	private void addFavorite(final String symbol) {
		ParkSvc.addFavorite(symbol, new AsyncCallback<Void>() {
			public void onFailure(Throwable error) {
				handleError(error);
			}
			public void onSuccess(Void ignore) {
				displayFavorite(symbol);
			}
		});
	}
	
	private void removeFavorite(final String symbol) {
		ParkSvc.removeFavorite(symbol, new AsyncCallback<Void>() {
			public void onFailure(Throwable error) {
				handleError(error);
			}
			public void onSuccess(Void ignore) {
				undisplayFavorite(symbol);
				removeAudio.play();
			}
		});
	}
	
	private void undisplayFavorite(String symbol) {
		int numofFavoriteListItems = favlistPanel.getWidgetCount();
		for (int i = 1; i < numofFavoriteListItems; i++) {
			if (((Label) ((HorizontalPanel) favlistPanel.
					getWidget(i)).getWidget(0)).getText().matches(symbol)) {
				favlistPanel.remove(i);
				return;
			}
		}
	}

	private void loadFavorites() {
		ParkSvc.getFavorites(new AsyncCallback<String[]>() {  
			public void onFailure(Throwable error) {  
				handleError(error);
			}  
			public void onSuccess(String[] symbols) {
				displayFavorites(symbols);
			}  
		});  
	}
	
	private void displayFavorites(String[] symbols) {
		for (String symbol : symbols) {
			displayFavorite(symbol);
		}
	}
	
	private void displayFavorite(final String symbol) {
		HorizontalPanel favoriteOptions = new HorizontalPanel();
		Label parkLabel = new Label(symbol);
		parkLabel.setWidth("230px");
		parkLabel.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				Park parkToDisplay = null;
				for (Park p : PARKS) {
					if (p.getName().matches(symbol)) {
						parkToDisplay = p;
						break;
					}	
				}
				parkInfo(parkToDisplay);
				favlistPopup.hide();
				addParkToHistory(parkToDisplay);
			}
		});
		
		final Image upButton = new Image("/images/up.png");
		upButton.setSize("20px", "20px");
		upButton.addMouseOverHandler(new MouseOverHandler() {
			public void onMouseOver(MouseOverEvent event) {
				upButton.setUrl("/images/up-hover.png");
			}
		});
		upButton.addMouseOutHandler(new MouseOutHandler() {
			public void onMouseOut(MouseOutEvent event) {
				upButton.setUrl("/images/up.png");
			}
		});
		upButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (((Label) ((HorizontalPanel) favlistPanel.
						getWidget(1)).getWidget(0)).getText().matches(symbol)) {
					return;
				}
				changeFavoritePosition(symbol, "up");
			}
		});
		
		final Image downButton = new Image("/images/down.png");
		downButton.setSize("20px", "20px");
		downButton.addMouseOverHandler(new MouseOverHandler() {
			public void onMouseOver(MouseOverEvent event) {
				downButton.setUrl("/images/downhover.png");
			}
		});
		downButton.addMouseOutHandler(new MouseOutHandler() {
			public void onMouseOut(MouseOutEvent event) {
				downButton.setUrl("/images/down.png");
			}
		});
		downButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (((Label) ((HorizontalPanel) favlistPanel.
						getWidget(favlistPanel.getWidgetCount() - 1)).getWidget(0)).getText().matches(symbol)) {
					return;
				}
				changeFavoritePosition(symbol, "down");
			}
		});
		
		final Image removeButton = new Image("/images/remove.png");
		removeButton.setSize("20px", "20px");
		removeButton.addMouseOverHandler(new MouseOverHandler() {
			public void onMouseOver(MouseOverEvent event) {
				removeButton.setUrl("/images/removehover.png");
			}
		});
		removeButton.addMouseOutHandler(new MouseOutHandler() {
			public void onMouseOut(MouseOutEvent event) {
				removeButton.setUrl("/images/remove.png");
			}
		});
		removeButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				removeFavorite(symbol);
			}
		});
		
		favoriteOptions.add(parkLabel);
		favoriteOptions.setCellVerticalAlignment(parkLabel, HasVerticalAlignment.ALIGN_MIDDLE);
		favoriteOptions.add(upButton);
		favoriteOptions.add(downButton);
		favoriteOptions.add(removeButton);
		favoriteOptions.setSpacing(4);
		favoriteOptions.setStyleName("historyLink");
		favlistPanel.add(favoriteOptions);
	}
	
	private void changeFavoritePosition(final String symbol, final String change){
		ParkSvc.changeFavorites(symbol, change, new AsyncCallback<Void>() {
			public void onFailure(Throwable error) {
				handleError(error);
			}
			public void onSuccess(Void ignore) {
				if (change.matches("up")) {
					int numofFavoriteListItems = favlistPanel.getWidgetCount();
					for (int i = 1; i < numofFavoriteListItems; i++) {
						if (((Label) ((HorizontalPanel) favlistPanel.
								getWidget(i)).getWidget(0)).getText().matches(symbol)) {
							HorizontalPanel temp = (HorizontalPanel) favlistPanel.getWidget(i - 1);
							favlistPanel.insert(favlistPanel.getWidget(i), i - 1);
							favlistPanel.insert(temp, i);
							return;
						}
					}
				} else {
					int numofFavoriteListItems = favlistPanel.getWidgetCount();
					for (int i = 1; i < numofFavoriteListItems; i++) {
						if (((Label) ((HorizontalPanel) favlistPanel.
								getWidget(i)).getWidget(0)).getText().matches(symbol)) {
							HorizontalPanel temp = (HorizontalPanel) favlistPanel.getWidget(i + 1);
							favlistPanel.insert(favlistPanel.getWidget(i), i + 1);
							favlistPanel.insert(temp, i);
							return;
						}
					}
				}
			}
		});
	}
	
	private void addHistory(final String symbol) {
		ParkSvc.addHistory(symbol, new AsyncCallback<Void>() {
			public void onFailure(Throwable error) {
				handleError(error);
			}
			public void onSuccess(Void ignore) {
				displayHistoryItem(symbol);
			}
		});
	}
	
	private void removeHistory(final String symbol) {
		ParkSvc.removeHistory(symbol, new AsyncCallback<Void>() {
			public void onFailure(Throwable error) {
				handleError(error);
			}
			public void onSuccess(Void ignore) {
				undisplayHistoryItem(symbol);
			}
		});
	}
	
	private void undisplayHistoryItem(String symbol) {
		int numofHistoryItems = historyPanel.getWidgetCount();
		for (int i = 1; i < numofHistoryItems; i++) {
			if (((Label) historyPanel.getWidget(i)).getText().matches(symbol)) {
				historyPanel.remove(i);
				return;
			}
		}
	}
	
	private void removeHistoryExisting(final String symbol) {
		ParkSvc.removeHistory(symbol, new AsyncCallback<Void>() {
			public void onFailure(Throwable error) {
				handleError(error);
			}
			public void onSuccess(Void ignore) {
				undisplayHistoryItem(symbol);
				addHistory(symbol);
			}
		});
	}
	
	private void removeHistoryFull(final String firstitem, final String symbol) {
		ParkSvc.removeHistory(firstitem, new AsyncCallback<Void>() {
			public void onFailure(Throwable error) {
				handleError(error);
			}
			public void onSuccess(Void ignore) {
				undisplayHistoryItem(firstitem);
				addHistory(symbol);
			}
		});
	}
	
	private void clearHistory() {
		int numofHistoryItems = historyPanel.getWidgetCount();
		if (numofHistoryItems > 1) {
			for (int i = 1; i < numofHistoryItems; i++) {
				removeHistory(((Label) historyPanel.getWidget(i)).getText());
			}
			removeAudio.play();
		}

	}
	
	private void loadHistory() {
		ParkSvc.getHistory(new AsyncCallback<String[]>() {  
			public void onFailure(Throwable error) {  
				handleError(error);
			}  
			public void onSuccess(String[] symbols) {  
				displayHistory(symbols);
			}  
		}); 
	}
	
	private void displayHistory(String[] symbols) {
		for (String symbol : symbols) {
			displayHistoryItem(symbol);
		}
	}
	
	private void displayHistoryItem(final String symbol) {
		Label parkLabel = new Label(symbol);
		parkLabel.setStylePrimaryName("historyLink");
		parkLabel.setWidth("290px");
		parkLabel.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				Park parkToDisplay = null;
				for (Park p : PARKS) {
					if (p.getName().matches(symbol)) {
						parkToDisplay = p;
						break;
					}	
				}
				parkInfo(parkToDisplay);
				historyPopup.hide();
				addParkToHistory(parkToDisplay);
			}
		});
		
		historyPanel.add(parkLabel);
	}

	
	//Make a RPC call to get park data from server
	private void retrieveParks() {
		// Initialize the service proxy.
		if (ParkSvc == null) {
			ParkSvc = GWT.create(ParkService.class);
		}

		// Set up the callback object.
		AsyncCallback<List<Park>> callback = new AsyncCallback<List<Park>>() {
			public void onFailure(Throwable error) {
				handleError(error);
			}

			public void onSuccess(List<Park> result) {
				PARKS = result;
				UIUpdate();
				
				Maps.loadMapsApi("", "2", false, new Runnable() {
					public void run() {
						final Geolocation geolocation = Geolocation.getIfSupported();
						if (geolocation == null) {
							errorAudio.play();
							Window.alert("Your browser does not support location tracking.");
						}

						//mapWeatherPanel.clear();
						displayMap();
						retrieveWeather();
						
						geolocation.watchPosition(new Callback<Position, PositionError>() {
							@Override
							public void onSuccess(Position result) {
								Coordinates co = result.getCoordinates();
								//the first userLocation is used to test the location tracking of the app
								if(test) {
									userLocation = LatLng.newInstance(49.249783,-123.155250);
									test = false;
								} else {
									userLocation = LatLng.newInstance(co.getLatitude(), co.getLongitude());
								}
								undisplayUserLocationMarker(map);
								displayUserLocationMarker(map);
							}

							@Override
							public void onFailure(PositionError reason) {
								errorAudio.play();
								Window.alert("Picnic Planner is unable to retrieve your current location.");
							}
						});
					}
				});
				
			}
		};

		// Make the call to the Park data service
		ParkSvc.retrieveAllParks(callback);
	}
	
	private void retrieveWeather() {
		
		if (ParkSvc == null) {
			ParkSvc = GWT.create(ParkService.class);
		}

		AsyncCallback<Weather> callback = new AsyncCallback<Weather>() {
			public void onFailure(Throwable error) {
				handleError(error);
			}
			public void onSuccess(Weather result) {
				weather = result;
				
				//weatherPanel.clear();
				displayWeatherPanel();
			    /*if (currentUnit == 0)
			    	displayWeatherInC();
			    else
			    	displayWeatherInF();
			    mapWeatherPanel.add(weatherPanel);*/
				
//				Timer refreshWeatherdetails = new Timer() {
//					@Override
//					public void run() {
//						updateWeather();
//					}
//				};
//				
//				refreshWeatherdetails.scheduleRepeating(5000);
			}
		};
		
		ParkSvc.getWeatherDetails(callback);
	}
	
/*	private void updateWeather() {
		AsyncCallback<Weather> callback = new AsyncCallback<Weather>() {
			public void onFailure(Throwable error) {
				handleError(error);
			}
			public void onSuccess(Weather result) {
				weather = result;
				
				//update current weather panel widget with new weather details,
				//change so that instead of redrawing panel, only label names, etc. are updated
				if (currentUnit == 0)
			    	displayWeatherInC();
			    else
			    	displayWeatherInF();
			}
		};
		
		ParkSvc.getWeatherDetails(callback);
	}*/
	
	private void displayWeatherPanel() {
		weatherPanel.clear();
		if (currentUnit == 0)
			displayWeatherInC();
		else
			displayWeatherInF();
		mapWeatherPanel.add(weatherPanel);
	}
	
	private void UIUpdate() {
		List<String> temp = new ArrayList<String>();

		// construct the list for the facility filterbox
		for (Park park : PARKS) {
			for (Facility s : park.getFacilities()) {
				temp.add(s.getType());
			}
		}

		RemoveDuplicateStrings(temp);

		for (String s : temp) {
			filterListBoxFacility.addItem(s);
		}

		temp.clear();

		// do the same for the neighbourhood one
		for (Park park : PARKS) {
			temp.add(park.getNeighbourhood());
		}

		RemoveDuplicateStrings(temp);

		for (String s : temp) {
			filterListBoxNeighbourhood.addItem(s);
		}

		temp.clear();

		// and special features as well
		for (Park park : PARKS) {
			for (String s : park.getSpecialFeatures()) {
				temp.add(s);
			}
		}

		RemoveDuplicateStrings(temp);

		for (String s : temp) {
			filterListBoxSpecialFeatures.addItem(s);
		}

		// Add the park data to the data provider, which automatically pushes it to the celltable
		for (Park park : PARKS) {
			parklist.add(park);
			currentParklist = parklist;
		}
	}
	
	/**
	 * Refresh the parklist according to the search.
	 * Will execute when search button is clicked or user presses the return key in the textbox.
	 */
	private void refreshParkList() {
		
		lastPage = 0;
		
	    final String searchString = searchTextBox.getText().toUpperCase().trim();
	    
	    if ((searchString.equals("") || searchString.equals(textBoxDefault.toUpperCase())) && 
	    		filterBoxSize.getSelectedIndex() == 0 &&
				filterBoxRadius.getSelectedIndex() == 0 &&
				filterListBoxNeighbourhood.getSelectedIndex() == -1 &&
				filterListBoxFacility.getSelectedIndex() == -1 &&
				filterListBoxSpecialFeatures.getSelectedIndex() == -1 &&
				!filterCheckBoxWashroom.getValue()) {
	    	errorAudio.play();
	    	Window.alert("You have not selected any filters or entered anything.");
	    	return;
		}
	    
	    if (filterBoxRadius.getSelectedIndex() != 0 && userLocation == null) {
	    	errorAudio.play();
	    	Window.alert("Picnic Planner is unable to retrieve your current location.");
	    	return;
	    }
	    
	    parklist.clear();	
	    
	    if (searchString.equals("") || searchString.equals(textBoxDefault.toUpperCase())) {
    		parklist.addAll(PARKS);
    	}
	    
	    else {
	    	for (Park park : PARKS) {
	    		if (park.getNeighbourhood().toUpperCase().contains(searchString) ||
	    				(park.getName().toUpperCase().contains(searchString)) ||
	    				(park.getAddress().toUpperCase().contains(searchString)))
	    			parklist.add(park);
	    	}
	    }
	    
	    switch (filterBoxSize.getSelectedIndex()) {
	    case 1:
	    	for (Park park : PARKS) {
	    		if (park.getSize() > 1.0)
	    			parklist.remove(park);
	    	}
	    	break;
	    case 2:
	    	for (Park park : PARKS) {
	    		if (park.getSize() <= 1.0 || park.getSize() > 3.0)
	    			parklist.remove(park);
	    	}
	    	break;
	    case 3:
	    	for (Park park : PARKS) {
	    		if (park.getSize() <= 3.0 || park.getSize() > 5.0)
	    			parklist.remove(park);
	    	}
	    	break;
	    case 4:
	    	for (Park park : PARKS) {
	    		if (park.getSize() <= 5.0 || park.getSize() > 10.0)
	    			parklist.remove(park);
	    	}
	    	break;
	    case 5:
	    	for (Park park : PARKS) {
	    		if (park.getSize() <= 10.0)
	    			parklist.remove(park);
	    	}
	    	break;
	    }
	    
	    switch (filterBoxRadius.getSelectedIndex()) {
	    case 1:
	    	for (Park park : PARKS) {
	    		if (userLocation.distanceFrom(park.getLocation()) > 5000)
	    			parklist.remove(park);
	    	}
	    	break;
	    case 2:
	    	for (Park park : PARKS) {
	    		if (userLocation.distanceFrom(park.getLocation()) > 10000)
	    			parklist.remove(park);
	    	}
	    	break;
	    case 3:
	    	for (Park park : PARKS) {
	    		if (userLocation.distanceFrom(park.getLocation()) > 15000)
	    			parklist.remove(park);
	    	}
	    	break;
	    case 4:
	    	for (Park park : PARKS) {
	    		if (userLocation.distanceFrom(park.getLocation()) > 25000)
	    			parklist.remove(park);
	    	}
	    	break;
	    case 5:
	    	for (Park park : PARKS) {
	    		if (userLocation.distanceFrom(park.getLocation()) > 35000)
	    			parklist.remove(park);
	    	}
	    	break;
	    case 6:
	    	for (Park park : PARKS) {
	    		if (userLocation.distanceFrom(park.getLocation()) > 50000)
	    			parklist.remove(park);
	    	}
	    	break;
	    }
	    	    
	    if (filterListBoxNeighbourhood.getSelectedIndex() != -1) {
	    	for (Park park : PARKS) {
	    		Boolean b = false;
	    		for (Integer i : GetSelectedItems(filterListBoxNeighbourhood)) {
	    			if (filterListBoxNeighbourhood.getItemText(i).matches(park.getNeighbourhood()))
	    				b = true;
	    		}
	    		if (!b)
	    			parklist.remove(park);
	    	}
	    }

	    if (filterListBoxFacility.getSelectedIndex() != -1) {
	    	for (Park park : PARKS) {
	    		Boolean a = true; // check if the park has all the selected facilities
	    		for (Integer i : GetSelectedItems(filterListBoxFacility)) {
	    			Boolean b = false; // check if the park has this particular facility i
	    			for (Facility s : park.getFacilities()) {
	    				if (filterListBoxFacility.getItemText(i).matches(s.getType()))
	    					b = true; // set b to true when facility i is found
	    			}
	    			if (!b) // if the park doesn't have one or more of the selected facilities
	    				a = false; 
	    		}
	    		if (!a)
	    			parklist.remove(park);
	    	}
	    }
	    
	    if (filterListBoxSpecialFeatures.getSelectedIndex() != -1) {
	    	for (Park park : PARKS) {
	    		Boolean a = true;
	    		for (Integer i : GetSelectedItems(filterListBoxSpecialFeatures)) {
	    			Boolean b = false;
	    			for (String s : park.getSpecialFeatures()) {
	    				if (filterListBoxSpecialFeatures.getItemText(i).matches(s))
	    					b = true;
	    			}
	    			if (!b)
	    				a = false;
	    		}
	    		if (!a)
	    			parklist.remove(park);
	    	}
	    }
	    
	    if (filterCheckBoxWashroom.getValue()) {
	    	for (Park park : PARKS) {
	    		if (!park.hasWashroom()) {
	    			parklist.remove(park);
	    		}
	    	}
	    }
	    
	    //Make sure no duplicate parks are displayed in table
	    RemoveDuplicates(parklist);
	    
	    //Save the parklist;
	    currentParklist = parklist;
	    
	    searched = true;
	    refreshTableAndMap();
	}
	
	private void refreshTableAndMap() {
	    //Display the map
	    mapWeatherPanel.clear();
	    if (tablePanel.getWidget(0) != mapLoadingLabel) {
			parkPanel.clear();
			tablePanel.clear();
			displayMap();
			displayWeatherPanel(); ///
			//undisplayUserLocationMarker(map);
			displayUserLocationMarker(map); ///
			tablePanel.add(mapWeatherPanel);
			tablePanel.add(numResultsLabel);
			tablePanel.add(cellTable);
			pager.setDisplay(cellTable);
			tablePanel.add(pager);
		}
	    else {
	    	displayMap();
	    	displayWeatherPanel(); ///
	    	//undisplayUserLocationMarker(map);
	    	displayUserLocationMarker(map); ///
	    }
	    
	    //Display number of parks found
	    if (parklist.isEmpty())
	    	numResultsLabel.setText("No parks found.");
	    
	    else if (parklist.size() == 1)
	    	numResultsLabel.setText("There is only 1 result.");
	    
	    else
	    	numResultsLabel.setText("There are " + parklist.size() + " results.");
	    
		cellTable.getColumnSortList().clear();

	}

	private void resetFilters() {
		filterListBoxNeighbourhood.setSelectedIndex(-1);
		filterListBoxFacility.setSelectedIndex(-1);
		filterListBoxSpecialFeatures.setSelectedIndex(-1);
		filterBoxRadius.setSelectedIndex(0);
		filterBoxSize.setSelectedIndex(0);
		filterCheckBoxWashroom.setValue(false);
	}
	
	private void resetEverything() {
		lastPage = 0;
		searched = false;
		justEntered = true;
		mapWeatherPanel.clear();
		parklist.clear();
		parklist.addAll(PARKS);
		filterListBoxNeighbourhood.setSelectedIndex(-1);
		filterListBoxFacility.setSelectedIndex(-1);
		filterListBoxSpecialFeatures.setSelectedIndex(-1);
		filterBoxRadius.setSelectedIndex(0);
		filterBoxSize.setSelectedIndex(0);
		filterCheckBoxWashroom.setValue(false);
		searchTextBox.setText(textBoxDefault);
		searchTextBox.setStyleName("textBoxMessage");
		numResultsLabel.setText("");
		cellTable.getColumnSortList().clear();
		if (tablePanel.getWidget(0) != mapLoadingLabel) {
			parkPanel.clear();
			tablePanel.clear();
			displayMap();
	    	displayWeatherPanel(); ///
	    	//undisplayUserLocationMarker(map);
	    	displayUserLocationMarker(map); ///
			tablePanel.add(mapWeatherPanel);
			tablePanel.add(numResultsLabel);
			tablePanel.add(cellTable);
			pager.setDisplay(cellTable);
			tablePanel.add(pager);
		}
		else {
			displayMap();
	    	displayWeatherPanel(); ///
	    	//undisplayUserLocationMarker(map);
	    	displayUserLocationMarker(map); ///
		}
	}


	private List<Park> RemoveDuplicates(List<Park> parks) {
		Map<String, Park> map = new LinkedHashMap<String, Park>();
		for (Park park : parks) {
			map.put(park.getName(), park);
		}
		parks.clear();
		parks.addAll(map.values());
		return parks;
	}

	public <T> List<T> RemoveDuplicateStrings(List<T> list) {
		int size = list.size();
		int out = 0;
		{
			final Set<T> encountered = new HashSet<T>();
			for (int in = 0; in < size; in++) {
				final T t = list.get(in);
				final boolean first = encountered.add(t);
				if (first) {
					list.set(out++, t);
				}
			}
		}
		while (out < size) {
			list.remove(--size);
		}

		return list;
	}

	private LinkedList<Integer> GetSelectedItems(ListBox lb) {
		LinkedList<Integer> selectedItems = new LinkedList<Integer>();
		for (int i = 0; i < lb.getItemCount(); i++) {
			if (lb.isItemSelected(i)) {
				selectedItems.add(i);
			}
		}
		return selectedItems;
	}

	// Display a map with parks and current location plotted
	private void displayMap() {
		//final MapWidget map = new MapWidget();
		map = new MapWidget();
		map.setZoomLevel(12);
		if ((justEntered && userLocation != null) || parklist.isEmpty()) {
	    	map.setCenter(userLocation);
	    }
	    
	    else if (!parklist.isEmpty()) {
	    	Park firstParkOnTheList = parklist.get(0);
	    	map.setCenter(firstParkOnTheList.getLocation());
	    }
		
	    else
	    	map.setCenter(LatLng.newInstance(49.25, -123.1));
	    map.setSize("678px", "300px");
	    map.addControl(new SmallZoomControl());
	    map.addControl(new MapTypeControl());
	    map.setScrollWheelZoomEnabled(false);
	    //displayUserLocationMarker(map);
	    map.setStyleName("greyBorder");
	    
	    for (final Park p : parklist) {
	    	displayParkMarker(map, p);
	    }
	    
	    
	    mapLoadingLabel.setText("");
	    mapWeatherPanel.add(map);
	    Label emptySpace = new Label();
	    emptySpace.setWidth("10px");
	    mapWeatherPanel.add(emptySpace);
	}
	
	// Display weather widget in Celsius
	private void displayWeatherInC() {
		weatherPanel.clear();
		currentUnit = 0;
		VerticalPanel panel = new VerticalPanel();
		panel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		panel.setSize("190px", "302px");
		HorizontalPanel unitSwitch = new HorizontalPanel();
		Label c = new Label("\u2103");
		Label s = new Label("/");
		Anchor f = new Anchor("\u2109");
		f.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event) {
				displayWeatherInF();
			}
		});
		unitSwitch.add(c);
		unitSwitch.add(s);
		unitSwitch.add(f);
		unitSwitch.setSpacing(5);
		panel.add(unitSwitch);
		panel.setCellHorizontalAlignment(unitSwitch, HasHorizontalAlignment.ALIGN_RIGHT);
		panel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		DockPanel dock = new DockPanel();
		dock.setWidth("140px");
		dock.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		Label currentTemp = new Label(Math.round((weather.getCurrentTemp()-32)/1.8) + "\u00b0");
		currentTemp.setStyleName("currentTemp");
		currentTemp.setWidth("70px");
		Image icon = new Image("http://l.yimg.com/a/i/us/we/52/" + weather.getCode() + ".gif");
		icon.setSize("40px", "40px");
		Label cond = new Label(weather.getForecast());
		cond.setStyleName("verySmallFont");
		dock.add(currentTemp, DockPanel.EAST);
		dock.setCellHorizontalAlignment(currentTemp, HasHorizontalAlignment.ALIGN_RIGHT);
		dock.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		dock.add(cond, DockPanel.SOUTH);
		dock.add(icon, DockPanel.CENTER);
		panel.add(dock);
		Label emptySpace = new Label();
		emptySpace.setHeight("20px");
		panel.add(emptySpace);
		for (WeatherForecast w : weather.getNextFiveDays()) {
			HorizontalPanel hp = new HorizontalPanel();
			hp.setWidth("150px");
			Label day = new Label(w.getDay());
			day.setStyleName("verySmallFont");
			day.setWidth("77px");
			Image icon1 = new Image("http://l.yimg.com/a/i/us/we/52/" + w.getCode() + ".gif");
			icon1.setSize("18px", "18px");
			Label hi = new Label(Math.round((w.getHigh()-32)/1.8)+"\u00b0");
			hi.setStyleName("verySmallFont");
			hi.setWidth("30px");
			Label lo = new Label(Math.round((w.getLow()-32)/1.8)+"\u00b0");
			lo.setStyleName("verySmallFont");
			lo.setWidth("25px");
			hp.add(day);
			hp.add(icon1);
			hp.add(hi);
			hp.add(lo);
			hp.setCellHorizontalAlignment(hi, HasHorizontalAlignment.ALIGN_RIGHT);
			hp.setCellHorizontalAlignment(lo, HasHorizontalAlignment.ALIGN_RIGHT);
			panel.add(hp);
		}
		Label time = new Label("Updated: " + weather.getTime());
		time.setStyleName("verySmallFont");
		panel.add(time);
		panel.setStyleName("weatherPanel");
		weatherPanel.add(panel);
	}
	
	// Display weather widget in Fahrenheit
	private void displayWeatherInF() {
		weatherPanel.clear();
		currentUnit = 1;
		VerticalPanel panel = new VerticalPanel();
		panel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		panel.setSize("190px", "302px");
		HorizontalPanel unitSwitch = new HorizontalPanel();
		Anchor c = new Anchor("\u2103");
		Label s = new Label("/");
		Label f = new Label("\u2109");
		c.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event) {
				displayWeatherInC();
			}
		});
		unitSwitch.add(c);
		unitSwitch.add(s);
		unitSwitch.add(f);
		unitSwitch.setSpacing(5);
		panel.add(unitSwitch);
		panel.setCellHorizontalAlignment(unitSwitch, HasHorizontalAlignment.ALIGN_RIGHT);
		panel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		DockPanel dock = new DockPanel();
		dock.setWidth("140px");
		dock.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		Label currentTemp = new Label(weather.getCurrentTemp() + "\u00b0");
		currentTemp.setStyleName("currentTemp");
		currentTemp.setWidth("70px");
		Image icon = new Image("http://l.yimg.com/a/i/us/we/52/" + weather.getCode() + ".gif");
		icon.setSize("40px", "40px");
		Label cond = new Label(weather.getForecast());
		cond.setStyleName("verySmallFont");
		dock.add(currentTemp, DockPanel.EAST);
		dock.setCellHorizontalAlignment(currentTemp, HasHorizontalAlignment.ALIGN_RIGHT);
		dock.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		dock.add(cond, DockPanel.SOUTH);
		dock.add(icon, DockPanel.CENTER);
		panel.add(dock);
		Label emptySpace = new Label();
		emptySpace.setHeight("20px");
		panel.add(emptySpace);
		for (WeatherForecast w : weather.getNextFiveDays()) {
			HorizontalPanel hp = new HorizontalPanel();
			hp.setWidth("150px");
			Label day = new Label(w.getDay());
			day.setStyleName("verySmallFont");
			day.setWidth("77px");
			Image icon1 = new Image("http://l.yimg.com/a/i/us/we/52/" + w.getCode() + ".gif");
			icon1.setSize("18px", "18px");
			Label hi = new Label(w.getHigh()+"\u00b0");
			hi.setStyleName("verySmallFont");
			hi.setWidth("30px");
			Label lo = new Label(w.getLow()+"\u00b0");
			lo.setStyleName("verySmallFont");
			lo.setWidth("25px");
			hp.add(day);
			hp.add(icon1);
			hp.add(hi);
			hp.add(lo);
			hp.setCellHorizontalAlignment(hi, HasHorizontalAlignment.ALIGN_RIGHT);
			hp.setCellHorizontalAlignment(lo, HasHorizontalAlignment.ALIGN_RIGHT);
			panel.add(hp);
		}
		Label time = new Label("Updated: " + weather.getTime());
		time.setStyleName("verySmallFont");
		panel.add(time);
		panel.setStyleName("weatherPanel");
		weatherPanel.add(panel);
	}
	
	// Display a marker on a given map
	private void displayParkMarker(final MapWidget map, final Park p) {
		
		// High res park marker
		Icon parkIcon = null;
		parkIcon = Icon.newInstance("/images/park-pin.png");
	    parkIcon.setIconSize(Size.newInstance(12, 30));
	    parkIcon.setIconAnchor(Point.newInstance(6, 30));
	    MarkerOptions parkMarkerOptions = MarkerOptions.newInstance();
	    parkMarkerOptions.setIcon(parkIcon);
	    
	    final Marker mar = new Marker(p.getLocation(), parkMarkerOptions);

		map.addOverlay(mar);
		mar.addMarkerMouseOverHandler(new MarkerMouseOverHandler() {
			public void onMouseOver(MarkerMouseOverEvent event) {
				mar.setImage("/images/park-pin1.png");
			}
    	});
		mar.addMarkerMouseOutHandler(new MarkerMouseOutHandler() {
			public void onMouseOut(MarkerMouseOutEvent event) {
				mar.setImage("/images/park-pin.png");
			}
    	});
		mar.addMarkerClickHandler(new MarkerClickHandler() {
			public void onClick(MarkerClickEvent event) {
				VerticalPanel panel = new VerticalPanel();
				panel.add(new HTML("<h3>" + p.getName() +"</h3>" +
						"- " + p.getAddress() +
						"<br/>- " +p.getSize() + " hectares" +
						(userLocation == null ? "" : "<br/>- About " +
						Math.round(userLocation.distanceFrom(p.getLocation()))
								+ "m away from you") +
						"<br/>- This park " +(p.hasWashroom() ?
								"provides" : "does not provide") + " washrooms"));
				Anchor l = new Anchor("- more info");
				panel.add(l);
				Label emptySpace = new Label();
				emptySpace.setHeight("15px");
				panel.add(emptySpace);
				l.addClickHandler(new ClickHandler() {
					public void onClick(ClickEvent event) {
						parkInfo(p);
					}
				});
				InfoWindowContent content = new InfoWindowContent(panel);
				map.panTo(p.getLocation());
				map.getInfoWindow().open(p.getLocation(), content);
				addParkToHistory(p);
			}
		});
		
	}
	
	// Add park to history list
	private void addParkToHistory(Park p) {
		int numofHistoryItems = historyPanel.getWidgetCount();
		for (int i = 1; i < numofHistoryItems; i++) {
			if (((Label) historyPanel.getWidget(i)).getText().matches(p.getName())) {
				removeHistoryExisting(p.getName());
				return;
			} 
		}
			
		if (numofHistoryItems >= 16) {
			String firstItem = ((Label) historyPanel.getWidget(1)).getText();
			removeHistoryFull(firstItem, p.getName());
			return;
		} else {
			addHistory(p.getName());
			return;
		}
	}
	
	private void undisplayUserLocationMarker(final MapWidget map) {
		if (userMarker != null) {
			map.removeOverlay(userMarker);
		}
	}
	
	// Display a marker for the user's current location.
	private void displayUserLocationMarker(final MapWidget map) {
		if (userLocation != null) {
			if (firstUserLocation) {
				map.setCenter(userLocation);
				firstUserLocation = false;
			}
	    	Icon userIcon = Icon.newInstance("/images/user-pin.png");
	    	userIcon.setIconSize(Size.newInstance(14, 14));
	    	userIcon.setIconAnchor(Point.newInstance(7, 7));
	    	MarkerOptions userMarkerOptions = MarkerOptions.newInstance();
	    	userMarkerOptions.setIcon(userIcon);
	    	//Marker userMarker = new Marker(userLocation, userMarkerOptions);
	    	userMarker = new Marker(userLocation, userMarkerOptions);
	    	map.addOverlay(userMarker);
	    	userMarker.addMarkerClickHandler(new MarkerClickHandler() {
		    	public void onClick(MarkerClickEvent event) {
		    		map.panTo(userLocation);
		    		map.getInfoWindow().open(userLocation, new InfoWindowContent("This is "
		    				+ "your current location."));
		    	}
		    });
	    }
	}

	// Replace cellTable with information of a given park
	private void parkInfo(final Park p) {
		
		parkPanel.clear();
		
		//button panel for Park info pages
		HorizontalPanel parkInfoButtonPanel = new HorizontalPanel();
		
		//create button to return to map and table display
		final Image goBackIcon = new Image("/images/gobackgrey.png");
		goBackIcon.setSize("75px", "25px");
		goBackIcon.setStyleName("pointer");
		goBackIcon.setTitle("Go back");
		goBackIcon.addMouseOverHandler(new MouseOverHandler() {
			public void onMouseOver(MouseOverEvent event) {
				goBackIcon.setUrl("/images/gobackorange.png");
			}
		});
		goBackIcon.addMouseOutHandler(new MouseOutHandler() {
			public void onMouseOut(MouseOutEvent event) {
				goBackIcon.setUrl("/images/gobackgrey.png");
			}
		});
		goBackIcon.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				justEntered = false;
				if (lastPage == 0) {
					if (searched) {
					    parklist = currentParklist;
					    refreshTableAndMap();
					}
					else
						resetEverything();
				}
				else if (lastPage == 1) {
					myAccount();
				}
				else if (lastPage == 2) {
					myFavorites();
				}
				else if (lastPage == 3) {
					myHistory();
				}
			}

		});
		
		//create button to add parks to favorites
		final Image favIcon = new Image("/images/favgrey.png");
		favIcon.setSize("75px", "25px");
		favIcon.setTitle("Add to favorites");
		favIcon.addMouseOverHandler(new MouseOverHandler() {
			public void onMouseOver(MouseOverEvent event) {
				favIcon.setStyleName("pointer");
				favIcon.setUrl("/images/favred.png");
			}
		});
		favIcon.addMouseOutHandler(new MouseOutHandler() {
			public void onMouseOut(MouseOutEvent event) {
				favIcon.setUrl("/images/favgrey.png");
			}
		});
		favIcon.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (!favlistPopup.isShowing())
					favlistPopup.center();
				favIcon.setStyleName("imageBlinker");
				int numofFavoriteListItems = favlistPanel.getWidgetCount();
				for (int i = 1; i < numofFavoriteListItems; i++) {
					if (((Label) ((HorizontalPanel) favlistPanel.
							getWidget(i)).getWidget(0)).getText().matches(p.getName())) {
						return;
					}
				}
				
				addFavorite(p.getName());
				
			}
		});
		
		Label parkName = new Label(p.getName());
		parkName.setStyleName("parkName");

		parkInfoButtonPanel.add(goBackIcon);
		parkInfoButtonPanel.add(favIcon);
		parkInfoButtonPanel.setSpacing(5);
		
		tablePanel.clear();
		final MapWidget map = new MapWidget(p.getLocation(), 14);
	    map.setSize("400px", "290px");
	    map.addControl(new SmallZoomControl());
	    map.addControl(new MapTypeControl());
	    map.setScrollWheelZoomEnabled(false);
	    map.setStyleName("greyBorder");
	    displayUserLocationMarker(map);
	    
	    Icon parkIcon = Icon.newInstance("/images/park-pin.png");
	    parkIcon.setIconSize(Size.newInstance(18, 45));
	    parkIcon.setIconAnchor(Point.newInstance(9, 45));
	    MarkerOptions parkMarkerOptions = MarkerOptions.newInstance();
	    parkMarkerOptions.setIcon(parkIcon);
	    Marker mar = new Marker(p.getLocation(), parkMarkerOptions);
		map.addOverlay(mar);
		
		
		//Park info
		
		VerticalPanel addressPanel = new VerticalPanel();
		Label parkAddressTitle = new Label("Address");
		parkAddressTitle.setStyleName("largeFont");
		Label parkAddress = new Label("- " + p.getAddress());
		addressPanel.add(parkAddressTitle);
		addressPanel.add(parkAddress);
		
		VerticalPanel neighbourhoodPanel = new VerticalPanel();
		Label parkNeighbourhoodTitle = new Label("Neighbourhood");
		parkNeighbourhoodTitle.setStyleName("largeFont");
		Anchor link = new Anchor("Community page");
		link.setHref(p.getNeighbourhoodUrl());
		HTML parkNeighbourhood = new HTML("- " + p.getNeighbourhood() + " (" + link + ")");
		neighbourhoodPanel.add(parkNeighbourhoodTitle);
		neighbourhoodPanel.add(parkNeighbourhood);
		
		VerticalPanel areaPanel = new VerticalPanel();
		Label parkAreaTitle = new Label("Area");
		parkAreaTitle.setStyleName("largeFont");
		Label parkArea = new Label("- " + p.getSize() + " hectares");
		areaPanel.add(parkAreaTitle);
		areaPanel.add(parkArea);
		
		VerticalPanel distancePanel = new VerticalPanel();
		Label distanceTitle = new Label("Distance");
		distanceTitle.setStyleName("largeFont");
		Label distance = new Label();
		if (userLocation != null) {
			distance.setText("- This park is approximately " +
		         Math.round(userLocation.distanceFrom(p.getLocation())) + " meters away from you.");
		}
		else {
			distance.setText("- Picnic Planner is unable to retrieve your current location.");
		}
		distancePanel.add(distanceTitle);
		distancePanel.add(distance);
				
		VerticalPanel facilitiesPanel = new VerticalPanel();
		Label parkFacilitiesTitle = new Label("Facilities");
		parkFacilitiesTitle.setStyleName("largeFont");
		String facilities = "- ";
		int numFacilities = p.getFacilities().size();
		if (numFacilities > 0) {
			for (Facility a : p.getFacilities()) {
				facilities = facilities + a.getType() + "(x" + a.getCount() + ")";
				if (!(numFacilities - 1 <= 0)) {
					facilities += ", ";
					--numFacilities;
				}
			}
		}
		else
			facilities = "- This park does not provide any facilities.";
		Label parkFacilities = new Label(facilities);
		facilitiesPanel.add(parkFacilitiesTitle);
		facilitiesPanel.add(parkFacilities);
		
		VerticalPanel featuresPanel = new VerticalPanel();
		Label parkFeaturesTitle = new Label("Special Features");
		parkFeaturesTitle.setStyleName("largeFont");
		String features = "- ";
		int numFeatures = p.getSpecialFeatures().size();
		if (numFeatures > 0) {
			for (String a : p.getSpecialFeatures()) {
				features += a;
				if (!(numFeatures - 1 <= 0)) {
					features += ", ";
					--numFeatures;
				}
			}
		}
		else
			features = "- This park does not have any special features.";
		Label parkFeatures = new Label(features);
		featuresPanel.add(parkFeaturesTitle);
		featuresPanel.add(parkFeatures);
		
		VerticalPanel washroomPanel = new VerticalPanel();
		Label washroomTitle = new Label("Washrooms");
		washroomTitle.setStyleName("largeFont");
		washroomPanel.add(washroomTitle);
		
		if (p.hasWashroom()) {
			CellTable<Washroom> washroomTable = new CellTable<Washroom>();
			
			TextColumn<Washroom> locationCol = new TextColumn<Washroom>() {
				@Override
				public String getValue(Washroom object) {
					return object.getLocation();
				}
			};
			locationCol.setCellStyleNames("verySmallFont");
			washroomTable.addColumn(locationCol, "Location");
			
			TextColumn<Washroom> notesCol = new TextColumn<Washroom>() {
				@Override
				public String getValue(Washroom object) {
					return object.getNotes();
				}
			};
			notesCol.setCellStyleNames("verySmallFont");
			washroomTable.addColumn(notesCol, "Notes");
			
			TextColumn<Washroom> summerHoursCol = new TextColumn<Washroom>() {
				@Override
				public String getValue(Washroom object) {
					return object.getSummerhours();
				}
			};
			summerHoursCol.setCellStyleNames("verySmallFont");
			washroomTable.addColumn(summerHoursCol, "Summer Hours");
			
			TextColumn<Washroom> winterHoursCol = new TextColumn<Washroom>() {
				@Override
				public String getValue(Washroom object) {
					return object.getWinterhours();
				}
			};
			winterHoursCol.setCellStyleNames("verySmallFont");
			washroomTable.addColumn(winterHoursCol, "Winter Hours");
			
			washroomTable.setRowData(p.getWashrooms());
			washroomTable.setStyleName("washroomTable");
			washroomPanel.add(washroomTable);
		}
		
		else {
			Label noWashrooms = new Label("- This park does not provide washrooms.");
			washroomPanel.add(noWashrooms);
		}
		
		
		parkPanel.add(washroomPanel, DockPanel.SOUTH);
		parkPanel.add(featuresPanel, DockPanel.SOUTH);
		parkPanel.add(facilitiesPanel, DockPanel.SOUTH);
		parkPanel.add(map, DockPanel.EAST);
		parkPanel.add(distancePanel, DockPanel.SOUTH);
		parkPanel.add(areaPanel, DockPanel.SOUTH);
		parkPanel.add(neighbourhoodPanel, DockPanel.SOUTH);
		parkPanel.add(addressPanel, DockPanel.SOUTH);
		parkPanel.add(parkName, DockPanel.SOUTH);
		parkPanel.add(parkInfoButtonPanel, DockPanel.CENTER);
		parkPanel.setCellWidth(map, "400px");
	    parkPanel.setSpacing(15);
	    parkPanel.setWidth("880px");
		tablePanel.add(parkPanel);
	}
	
	// Display a list of favorited parks
	private void myFavorites() {
		final List<Park> favlist = new ArrayList<Park>();
		lastPage = 2;
		tablePanel.clear();
		DockPanel dp = new DockPanel();
		Label title = new Label("Favorites");
		title.setStyleName("historyLabel");
		title.setHeight("40px");
				
		int numofFavoriteListItems = favlistPanel.getWidgetCount();
		
		for (int i = 1; i < numofFavoriteListItems; i++) {
			for (Park p : PARKS) {
				if (((Label) ((HorizontalPanel) favlistPanel.
						getWidget(i)).getWidget(0)).getText().matches(p.getName())) {
					favlist.add(p);
				}
			}
		}
		
		final MapWidget map = new MapWidget();
		if (favlist.size() > 0) {
			map.setCenter(favlist.get(0).getLocation());
		}
		else if (userLocation != null) {
			map.setCenter(userLocation);
		}
		else {
			map.setCenter(LatLng.newInstance(49.25, -123.1));
		}
		map.setSize("534px", "400px");
		map.setZoomLevel(11);
	    map.addControl(new SmallZoomControl());
	    map.addControl(new MapTypeControl());
	    map.setScrollWheelZoomEnabled(false);
	    displayUserLocationMarker(map);
	    map.setStyleName("greyBorder");
	    
	    final VerticalPanel vp = new VerticalPanel();
	    final Label nofav = new Label("You have not favorited any park.");
	    
	    if (favlist.isEmpty()) {
	    	vp.add(nofav);
	    }
	    for (final Park p : favlist) {
	    	
			Icon parkIcon = null;
			parkIcon = Icon.newInstance("/images/park-pin.png");
		    parkIcon.setIconSize(Size.newInstance(12, 30));
		    parkIcon.setIconAnchor(Point.newInstance(6, 30));
		    MarkerOptions parkMarkerOptions = MarkerOptions.newInstance();
		    parkMarkerOptions.setIcon(parkIcon);
		    
		    final Marker mar = new Marker(p.getLocation(), parkMarkerOptions);

			map.addOverlay(mar);
			mar.addMarkerMouseOverHandler(new MarkerMouseOverHandler() {
				public void onMouseOver(MarkerMouseOverEvent event) {
					mar.setImage("/images/park-pin1.png");
				}
	    	});
			mar.addMarkerMouseOutHandler(new MarkerMouseOutHandler() {
				public void onMouseOut(MarkerMouseOutEvent event) {
					mar.setImage("/images/park-pin.png");
				}
	    	});
			mar.addMarkerClickHandler(new MarkerClickHandler() {
				public void onClick(MarkerClickEvent event) {
					VerticalPanel panel = new VerticalPanel();
					panel.add(new HTML("<h3>" + p.getName() +"</h3>" +
							"- " + p.getAddress() +
							"<br/>- " +p.getSize() + " hectares" +
							(userLocation == null ? "" : "<br/>- About " +
							Math.round(userLocation.distanceFrom(p.getLocation()))
									+ "m away from you") +
							"<br/>- This park " +(p.hasWashroom() ?
									"provides" : "does not provide") + " washrooms"));
					Anchor l = new Anchor("- more info");
					panel.add(l);
					Label emptySpace = new Label();
					emptySpace.setHeight("15px");
					panel.add(emptySpace);
					l.addClickHandler(new ClickHandler() {
						public void onClick(ClickEvent event) {
							parkInfo(p);
						}
					});
					InfoWindowContent content = new InfoWindowContent(panel);
					map.panTo(p.getLocation());
					map.getInfoWindow().open(p.getLocation(), content);
					addParkToHistory(p);
				}
			});
	    	final HorizontalPanel hp = new HorizontalPanel();
	    	hp.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
	    	Label pname = new Label(p.getName());
	    	pname.setWidth("280px");
	    	pname.addMouseOverHandler(new MouseOverHandler() {
				public void onMouseOver(MouseOverEvent event) {
					mar.setImage("/images/park-pin1.png");
					map.panTo(p.getLocation());
				}
	    	});
	    	pname.addMouseOutHandler(new MouseOutHandler() {
				public void onMouseOut(MouseOutEvent event) {
					mar.setImage("/images/park-pin.png");
				}
	    	});
	    	pname.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					parkInfo(p);
					addParkToHistory(p);
				}
	    	});
	    	
			final Image removeButton = new Image("/images/remove.png");
			removeButton.setSize("20px", "20px");
			removeButton.addMouseOverHandler(new MouseOverHandler() {
				public void onMouseOver(MouseOverEvent event) {
					removeButton.setUrl("/images/removehover.png");
					mar.setImage("/images/park-pin1.png");
					map.panTo(p.getLocation());
				}
			});
			removeButton.addMouseOutHandler(new MouseOutHandler() {
				public void onMouseOut(MouseOutEvent event) {
					removeButton.setUrl("/images/remove.png");
					mar.setImage("/images/park-pin.png");
				}
			});
			removeButton.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					removeFavorite(p.getName());
					hp.removeFromParent();
					favlist.remove(p);
					map.removeOverlay(mar);
					if (favlist.isEmpty()) {
				    	vp.add(nofav);
				    }
				}
			});
		    
			hp.add(removeButton);
	    	hp.add(pname);
	    	hp.setSpacing(8);
	    	hp.addStyleName("historyLink");
	    	vp.add(hp);
	    }
		
		dp.add(map, DockPanel.EAST);
		dp.add(vp, DockPanel.SOUTH);
		dp.add(title, DockPanel.CENTER);
		dp.setWidth("880px");
		dp.setCellWidth(map, "534px");
		tablePanel.add(dp);
	}
	
	// Display a list of recently viewed parks
	private void myHistory() {
		List<Park> historylist = new ArrayList<Park>();
		lastPage = 3;
		tablePanel.clear();
		DockPanel dp = new DockPanel();
		Label title = new Label("History");
		title.setStyleName("historyLabel");
		title.setHeight("40px");
		
		int numofHistoryListItems = historyPanel.getWidgetCount();
		
		for (int i = 1; i < numofHistoryListItems; i++) {
			for (Park p : PARKS) {
				if (((Label) historyPanel.getWidget(i)).getText().matches(p.getName())) {
					historylist.add(p);
				}
			}
		}

		
		final MapWidget map = new MapWidget();
		if (historylist.size() > 0) {
			map.setCenter(historylist.get(0).getLocation());
		}
		else if (userLocation != null) {
			map.setCenter(userLocation);
		}
		else {
			map.setCenter(LatLng.newInstance(49.25, -123.1));
		}
		
		map.setSize("534px", "400px");
		map.setZoomLevel(11);
	    map.addControl(new SmallZoomControl());
	    map.addControl(new MapTypeControl());
	    map.setScrollWheelZoomEnabled(false);
	    displayUserLocationMarker(map);
	    map.setStyleName("greyBorder");
	    
	    final VerticalPanel vp = new VerticalPanel();
	    final Label nohistory = new Label("You have not viewed any park.");
	    
	    if (historylist.isEmpty()) {
	    	vp.add(nohistory);
	    }
	    
	    for (final Park p : historylist) {
			Icon parkIcon = null;
			parkIcon = Icon.newInstance("/images/park-pin.png");
		    parkIcon.setIconSize(Size.newInstance(12, 30));
		    parkIcon.setIconAnchor(Point.newInstance(6, 30));
		    MarkerOptions parkMarkerOptions = MarkerOptions.newInstance();
		    parkMarkerOptions.setIcon(parkIcon);
		    
		    final Marker mar = new Marker(p.getLocation(), parkMarkerOptions);

			map.addOverlay(mar);
			mar.addMarkerMouseOverHandler(new MarkerMouseOverHandler() {
				public void onMouseOver(MarkerMouseOverEvent event) {
					mar.setImage("/images/park-pin1.png");
				}
	    	});
			mar.addMarkerMouseOutHandler(new MarkerMouseOutHandler() {
				public void onMouseOut(MarkerMouseOutEvent event) {
					mar.setImage("/images/park-pin.png");
				}
	    	});
			mar.addMarkerClickHandler(new MarkerClickHandler() {
				public void onClick(MarkerClickEvent event) {
					VerticalPanel panel = new VerticalPanel();
					panel.add(new HTML("<h3>" + p.getName() +"</h3>" +
							"- " + p.getAddress() +
							"<br/>- " +p.getSize() + " hectares" +
							(userLocation == null ? "" : "<br/>- About " +
							Math.round(userLocation.distanceFrom(p.getLocation()))
									+ "m away from you") +
							"<br/>- This park " +(p.hasWashroom() ?
									"provides" : "does not provide") + " washrooms"));
					Anchor l = new Anchor("- more info");
					panel.add(l);
					Label emptySpace = new Label();
					emptySpace.setHeight("15px");
					panel.add(emptySpace);
					l.addClickHandler(new ClickHandler() {
						public void onClick(ClickEvent event) {
							parkInfo(p);
						}
					});
					InfoWindowContent content = new InfoWindowContent(panel);
					map.panTo(p.getLocation());
					map.getInfoWindow().open(p.getLocation(), content);
					addParkToHistory(p);
				}
			});
	    	Label pname = new Label("- " + p.getName());
	    	pname.setStyleName("historyLink");
	    	pname.setSize("300px", "25px");
	    	pname.addMouseOverHandler(new MouseOverHandler() {
				public void onMouseOver(MouseOverEvent event) {
					mar.setImage("/images/park-pin1.png");
					map.panTo(p.getLocation());
				}
	    	});
	    	pname.addMouseOutHandler(new MouseOutHandler() {
				public void onMouseOut(MouseOutEvent event) {
					mar.setImage("/images/park-pin.png");
				}
	    	});
	    	pname.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					parkInfo(p);
					addParkToHistory(p);
				}
	    	});
	    	vp.add(pname);
	    }
	    
	    dp.add(map, DockPanel.EAST);
	    dp.add(vp, DockPanel.SOUTH);
	    
	    if (!historylist.isEmpty()) {
		    final Anchor clearHistory = new Anchor("Clear History");
			clearHistory.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					clearHistory();
					vp.clear();
					map.clearOverlays();
				    displayUserLocationMarker(map);
				    vp.add(nohistory);
				    clearHistory.removeFromParent();
				}
			});
		    dp.add(clearHistory, DockPanel.SOUTH);
		    dp.setCellHeight(clearHistory, "30px");
	    }
		
		dp.add(title, DockPanel.CENTER);
		dp.setWidth("880px");
		dp.setCellWidth(map, "534px");
		
		tablePanel.add(dp);
	}
	
	// Display my account page
	private void myAccount() {
		lastPage = 1;
		tablePanel.clear();
		VerticalPanel vp = new VerticalPanel();
		Label title = new Label("My Account");
		title.setStyleName("historyLabel");
		title.setHeight("50px");
		vp.add(title);
		
		if (loginInfo.isLoggedInFacebook()){
			Image facebookPic = new Image("https://graph.facebook.com/" + loginInfo.getFacebookId() +
					"/picture?width=200&height=200");
			facebookPic.setSize("100px", "100px");
			facebookPic.setStyleName("greyBorder");
			vp.add(facebookPic);
			
			Label facebookName = new Label(loginInfo.getFacebookName());
			vp.add(facebookName);
			
			friendsPanel.clear();
			Label friendsHeader = new Label("Facebook friends who have used Picnic Planner:");
			friendsPanel.add(friendsHeader);
			loadFacebookFriends();
			vp.add(friendsPanel);
		} else {
			final Image connectWithFacebook = new Image("/images/fb.png");
			connectWithFacebook.setStyleName("pointer");
			connectWithFacebook.setSize("250px", "43px");
			connectWithFacebook.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					Window.Location.assign("https://www.facebook.com/dialog/oauth?client_id=535989339819685&"
							+ "redirect_uri=" + redirectUrl + "&response_type=token");
				}
			});
			connectWithFacebook.addMouseOverHandler(new MouseOverHandler() {
				public void onMouseOver(MouseOverEvent event) {
					connectWithFacebook.setUrl("/images/fb1.png");
				}
			});
			connectWithFacebook.addMouseOutHandler(new MouseOutHandler() {
				public void onMouseOut(MouseOutEvent event) {
					connectWithFacebook.setUrl("/images/fb.png");
				}
			});
			vp.add(connectWithFacebook);

			}
		tablePanel.add(vp);
	}
	
	private void handleError(Throwable error) {
		errorAudio.play();
		Window.alert(error.getMessage());
		if (error instanceof NotLoggedInException) {
			Window.Location.replace(loginInfo.getLogoutUrl());
		}
	}
}
