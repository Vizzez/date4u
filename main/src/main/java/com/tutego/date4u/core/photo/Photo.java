package com.tutego.date4u.core.photo;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

public class Photo {

  @Min( 1 )
  private Long id;

  private Long profile;

  @NotNull @Pattern( regexp = "\\w{1,200}" )
  private String name;

  private boolean isProfilePhoto;

  @NotNull @Past
  private LocalDateTime created;

  protected Photo() {
  }

  public Photo( Long id, long profile, String name, boolean isProfilePhoto, LocalDateTime created ) {
    this.id = id;
    this.profile = profile;
    this.name = name;
    this.isProfilePhoto = isProfilePhoto;
    this.created = created;
  }

  public Long getId() {
    return id;
  }

  public long getProfile() {
    return profile;
  }

  public String getName() {
    return name;
  }

  public void setName( String name ) {
    this.name = name;
  }

  public boolean isProfilePhoto() {
    return isProfilePhoto;
  }

  public void setProfilePhoto( boolean profilePhoto ) {
    isProfilePhoto = profilePhoto;
  }

  public LocalDateTime getCreated() {
    return created;
  }

  public void setCreated( LocalDateTime created ) {
    this.created = created;
  }

  @Override public String toString() {
    return "Photo[" + id + "]";
  }
}
