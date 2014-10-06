package com.google.gwt.picnicplanner.client;

import java.io.Serializable;
import java.util.Map;

public class LoginInfo implements Serializable {

	private boolean loggedIn = false;
	private String loginUrl;
	private String logoutUrl;
	private String emailAddress;
	private String nickname;
	private boolean loggedInFacebook = false;
	private String facebookToken;
	private String facebookName;
	private String facebookId;
	private String profilePicUrl;
	private Map<String,NameLinkPair> friends;

	public boolean isLoggedIn() {
		return loggedIn;
	}

	public void setLoggedIn(boolean loggedIn) {
		this.loggedIn = loggedIn;
	}

	public String getLoginUrl() {
		return loginUrl;
	}

	public void setLoginUrl(String loginUrl) {
		this.loginUrl = loginUrl;
	}

	public String getLogoutUrl() {
		return logoutUrl;
	}

	public void setLogoutUrl(String logoutUrl) {
		this.logoutUrl = logoutUrl;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public boolean isLoggedInFacebook() {
		return loggedInFacebook;
	}

	public void setLoggedInFacebook(boolean loggedInFacebook) {
		this.loggedInFacebook = loggedInFacebook;
	}

	public String getFacebookToken() {
		return facebookToken;
	}

	public void setFacebookToken(String facebookToken) {
		this.facebookToken = facebookToken;
	}

	public String getFacebookName() {
		return facebookName.substring(1, facebookName.length()-1);
	}

	public void setFacebookName(String facebookName) {
		this.facebookName = facebookName;
	}

	public String getFacebookId() {
		return facebookId.substring(1, facebookId.length()-1);
	}

	public void setFacebookId(String facebookId) {
		this.facebookId = facebookId;
	}

	public String getProfilePicUrl() {
		return profilePicUrl;
	}

	public void setProfilePicUrl(String profilePicUrl) {
		this.profilePicUrl = profilePicUrl;
	}

	public Map<String, NameLinkPair> getFriends() {
		return friends;
	}

	public void setFriends(Map<String, NameLinkPair> friends) {
		this.friends = friends;
	}
}