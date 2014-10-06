package com.google.gwt.picnicplanner.server;

import java.util.Date;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import com.google.appengine.api.users.User;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Favorite {

  @PrimaryKey
  @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
  private Long id;
  @Persistent
  private User user;
  @Persistent
  private String symbol;
  @Persistent
  private Date createDate;
  @Persistent
  private int position;

  public Favorite() {
    this.createDate = new Date();
  }

  public Favorite(User user, String symbol, int position) {
    this();
    this.user = user;
    this.symbol = symbol;
    this.position = position;
  }

  public Long getId() {
    return this.id;
  }

  public User getUser() {
    return this.user;
  }

  public String getSymbol() {
    return this.symbol;
  }

  public Date getCreateDate() {
    return this.createDate;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public void setSymbol(String symbol) {
    this.symbol = symbol;
  }
  
  public void setPosition(int position) {
	  this.position = position;
  }

  public int getPosition() {
	  return this.position;
  }
  
}
