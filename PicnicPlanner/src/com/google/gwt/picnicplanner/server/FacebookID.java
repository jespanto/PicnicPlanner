package com.google.gwt.picnicplanner.server;

import java.util.Date;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import com.google.appengine.api.users.User;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class FacebookID {

  @PrimaryKey
  @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
  private Long id;
  @Persistent
  private Date createDate;
  @Persistent
  private Long facebookid;

  public FacebookID() {
    this.createDate = new Date();
  }

  public FacebookID(Long facebookid) {
    this();
    this.facebookid = facebookid;
  }

  public Long getId() {
    return this.id;
  }

  public Date getCreateDate() {
    return this.createDate;
  }

  public Long getFacebookID() {
	  return this.facebookid;
  }
  
}