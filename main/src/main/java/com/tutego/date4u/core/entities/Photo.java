package com.tutego.date4u.core.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import org.hibernate.envers.Audited;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;


@Entity
@Audited
@Access( AccessType.FIELD )
public class Photo {

  @Id @GeneratedValue( strategy = GenerationType.IDENTITY )
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn( name = "profile_fk" )
//  @JsonBackReference
  private Profile profile;

  @NotNull
  @Pattern( regexp = "\\w{1,200}" )
  private String name;

  @Column( name = "is_profile_photo" )
  private boolean isProfilePhoto;

  @NotNull @Past
  private LocalDateTime created;

  protected Photo() {
  }

  public Photo(Long id, Profile profile, String name, boolean isProfilePhoto) {
    this.id = id;
    this.profile = profile;
    this.name = name;
    this.isProfilePhoto = isProfilePhoto;
    this.created =  LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
  }

  public Long getId() {
    return id;
  }

  public Profile getProfile() {
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

  public void setProfile(Profile profile) {
    this.profile = profile;
  }

  @Override public String toString() {
    return "Photo[" + id + "]";
  }
}
