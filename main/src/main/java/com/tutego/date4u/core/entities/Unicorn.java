package com.tutego.date4u.core.entities;

import com.tutego.date4u.core.entities.Profile;
import jakarta.persistence.*;
import org.hibernate.envers.Audited;

@Entity
@Audited
@Access( AccessType.FIELD )
public class Unicorn {

  @Id @GeneratedValue( strategy = GenerationType.IDENTITY )
  private Long id;

  private String email;

  private String password;

  @OneToOne
  @JoinColumn( name = "profile_fk" )
  private Profile profile;

  protected Unicorn() {}

  public Unicorn( String email, String password, Profile profile ) {
    this.email = email;
    this.password = password;
    this.profile = profile;
  }

  public Long getId() {
    return id;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail( String email ) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword( String password ) {
    this.password = password;
  }

  public Profile getProfile() {
    return profile;
  }

  public void setProfile( Profile profile ) {
    this.profile = profile;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String toJson(){
    String json="\n{\n" +
            "\"email\":\"" + email + "\",\n"+
            "\"password\":\"" + password + "\",\n"+
            "\"profile_fk\":\"1\"\n}";
    return json;
  }
  @Override public String toString() {
    return "Unicorn[" + "id=" + id + ']';
  }
}