package com.tutego.date4u.core.formData;

import jakarta.validation.constraints.*;
import org.springframework.cglib.core.Local;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class RegisterFormData {
    @NotNull(message = "Email darf nicht null sein!")
    @NotBlank(message = "Email darf nicht leer sein!")
    @Email(message = "Ungültige E-Mail!")
    private String email;
    @NotNull(message = "Passwort darf nicht null sein!")
    @NotBlank(message = "Passwort darf nicht leer sein!")
    @Size(min = 8, max = 40)
    private String password;

    @NotNull(message = "Nickname darf nicht null sein!")
    @NotBlank(message = "Nickname darf nicht leer sein!")
    private String nickname;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "Geburtsdatum darf nicht null sein")
    private LocalDate birthdate;
    @NotNull(message = "Hornlänge darf nicht null sein!")
    @Min(value = 0,message = "Hornlänge darf nicht kleiner 0 sein!")
    @Max(value = 99,message = "Hornlänge darf nicht größer 99 sein!")
    private Integer hornlength;

    @NotNull(message = "Geschlecht darf nicht null sein!")
    @Min(value = 1,message = "Hackerman is that you?")
    @Max(value = 2,message = "Hackerman is that you?")
    private Integer gender;
    @NotNull(message = "Geschlecht darf nicht null sein!")
    @Min(value = 1,message = "Hackerman is that you?")
    @Max(value = 2,message = "Hackerman is that you?")
    private Integer attractedToGender;

    public RegisterFormData() {
        birthdate= LocalDate.of(1980,1,1);
    }

    public RegisterFormData(String email, String password, String nickname, LocalDate birthdate, Integer hornlength,
                            Integer gender, Integer attractedToGender) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.birthdate = birthdate;
        this.hornlength = hornlength;
        this.gender = gender;
        this.attractedToGender = attractedToGender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public Integer getHornlength() {
        return hornlength;
    }

    public void setHornlength(Integer hornlength) {
        this.hornlength = hornlength;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public Integer getAttractedToGender() {
        return attractedToGender;
    }

    public void setAttractedToGender(Integer attractedToGender) {
        this.attractedToGender = attractedToGender;
    }

    @Override
    public String toString() {
        return "RegisterFormData{" + "email='" + email + '\'' + ", password='" + password + '\'' + ", nickname='" + nickname + '\'' + ", birthdate=" + birthdate + ", hornlength=" + hornlength + ", gender=" + gender + ", attractedToGender=" + attractedToGender + '}';
    }
}
