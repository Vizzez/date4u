package com.tutego.date4u.core.formData;

import com.tutego.date4u.core.entities.Photo;
import com.tutego.date4u.core.entities.Profile;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import org.ocpsoft.prettytime.PrettyTime;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

public class ProfileFormData {

    private long id;
    @NotNull(message = "Nickname kann nicht null sein!")
    @NotBlank(message = "Nickname kann nicht leer sein!")
    private String nickname;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthdate;
    @NotNull(message = "Hornlength kann nicht null sein!")
    @Min(value = 0,message = "Hornlänge darf nicht kleiner 0 sein!")
    @Max(value = 99,message = "Hornlänge darf nicht größer 99 sein!")
    private Integer hornlength;

    @NotNull(message = "Geschlecht kann nicht null sein!")
    @Min(value = 1,message = "Hör auf zu hacken!")
    @Max(value = 2,message = "Hör auf zu hacken!")
    private Integer gender;
    @NotNull(message = "Geschlecht kann nicht null sein!")
    @Min(value = 1,message = "Hör auf zu hacken!")
    @Max(value = 2,message = "Hör auf zu hacken!")
    private Integer attractedToGender;
    @NotNull(message = "Beschreibung kann nicht null sein!")
    private String description;
    private String lastseen;
    private List<String> allPhotos;

    private int age;

    public ProfileFormData() {
    }

    public ProfileFormData(long id, String nickname, LocalDate birthdate, int hornlength, int gender,
                           Integer attractedToGender, String description, LocalDateTime lastseen) {
        PrettyTime pt= new PrettyTime();
        this.id = id;
        this.nickname = nickname;
        this.birthdate = birthdate;
        this.hornlength = hornlength;
        this.gender = gender;
        this.attractedToGender = attractedToGender;
        this.description = description;
        this.lastseen = pt.format(lastseen);
        age = Period.between(birthdate, LocalDate.now())
                .getYears();
    }

    public ProfileFormData(Profile p) {
        PrettyTime pt= new PrettyTime();
        this.id = p.getId();
        this.nickname = p.getNickname();
        this.birthdate = p.getBirthdate();
        this.hornlength = p.getHornlength();
        this.gender = p.getGender();
        this.attractedToGender = p.getAttractedToGender();
        this.description = p.getDescription();
        this.lastseen = pt.format(p.getLastseen());
        allPhotos = new ArrayList<>();
        for (Photo photo : p.getPhotos()) {
            this.allPhotos.add(photo.getName());
        }
        age = Period.between(birthdate, LocalDate.now())
                .getYears();
    }

    @Override
    public String toString() {
        return "ProfileFormData{" + "id=" + id + ", nickname='" + nickname + '\'' + ", birthdate=" + birthdate + ", " + "hornlength=" + hornlength + ", gender=" + gender + ", attractedToGender=" + attractedToGender + ", " + "description='" + description + '\'' + ", lastseen=" + lastseen + ", allPhotos=" + allPhotos + ", " + "age=" + age + '}';
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    public int getHornlength() {
        return hornlength;
    }

    public void setHornlength(int hornlength) {
        this.hornlength = hornlength;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public Integer getAttractedToGender() {
        return attractedToGender;
    }

    public void setAttractedToGender(Integer attractedToGender) {
        this.attractedToGender = attractedToGender;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLastseen() {
        return lastseen;
    }

    public void setLastseen(LocalDateTime lastseen) {
        PrettyTime pt=new PrettyTime();
        this.lastseen = pt.format(lastseen);
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public List<String> getAllPhotos() {
        return allPhotos;
    }

    public void setAllPhotos(List<String> allPhotos) {
        this.allPhotos = allPhotos;
    }

    public void updateDetails(Profile p) {
        PrettyTime pt= new PrettyTime();
        id=p.getId();
        allPhotos= new ArrayList<>();
        for (int i = 0; i < p.getPhotos()
                .size(); i++) {
            this.allPhotos.add(p.getPhotos()
                    .get(i)
                    .getName());
        }
        age = Period.between(birthdate, LocalDate.now())
                .getYears();
        lastseen=pt.format(p.getLastseen());
    }
}