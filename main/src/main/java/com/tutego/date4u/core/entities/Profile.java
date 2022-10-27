package com.tutego.date4u.core.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.tutego.date4u.core.formData.RegisterFormData;
import jakarta.persistence.*;
import org.hibernate.envers.Audited;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Audited
@Access(AccessType.FIELD)
public class Profile {


    @OneToMany(mappedBy = "profile", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonManagedReference
    private final List<Photo> photos = new ArrayList<>();
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "Likes", joinColumns = @JoinColumn(name = "liker_fk"), inverseJoinColumns = @JoinColumn(name =
            "likee_fk"))
    private final Set<Profile> profilesThatILike = new HashSet<>();
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "Likes", joinColumns = @JoinColumn(name = "likee_fk"), inverseJoinColumns = @JoinColumn(name =
            "liker_fk"))
    private final Set<Profile> profilesThatLikeMe = new HashSet<>();
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nickname;
    private LocalDate birthdate;
    private short hornlength;
    private byte gender;
    @Column(name = "attracted_to_gender")
    private Byte attractedToGender;
    @Column(length = 2048)

    private String description;
    @Column(name = "lastseen", columnDefinition = "TIMESTAMP")
    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")

    private LocalDateTime lastseen;
    @OneToOne(mappedBy = "profile", fetch = FetchType.LAZY)
    private Unicorn unicorn;

    protected Profile() {
    }


    public Profile(String nickname, LocalDate birthdate, int hornlength, int gender, Integer attractedToGender,
                   String description, LocalDateTime lastseen) {
        setNickname(nickname);
        setBirthdate(birthdate);
        setHornlength(hornlength);
        setGender(gender);
        setAttractedToGender(attractedToGender);
        setDescription(description);
        setLastseen(lastseen);
    }

    public Profile(RegisterFormData rfd) {
        setNickname(rfd.getNickname());
        setBirthdate(rfd.getBirthdate());
        setHornlength(rfd.getHornlength());
        setGender(rfd.getGender());
        setAttractedToGender(rfd.getAttractedToGender());
        setDescription("");
        setLastseen(LocalDateTime.now());
    }

    public Long getId() {
        return id;
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
        this.hornlength = (short) hornlength;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = (byte) gender;
    }

    public @Nullable Integer getAttractedToGender() {
        return attractedToGender == null ? null : attractedToGender.intValue();
    }

    public void setAttractedToGender(@Nullable Integer attractedToGender) {
        this.attractedToGender = attractedToGender == null ? null : attractedToGender.byteValue();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getLastseen() {
        return lastseen;
    }

    public void setLastseen(LocalDateTime lastseen) {
        this.lastseen = lastseen;
    }

    public Unicorn getUnicorn() {
        return unicorn;
    }

    public void setUnicorn(Unicorn unicorn) {
        this.unicorn = unicorn;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public Profile add(Photo photo) {
        photos.add(photo);
        return this;
    }

    public Set<Profile> getProfilesThatILike() {
        return profilesThatILike;
    }

    public Set<Profile> getProfilesThatLikeMe() {
        return profilesThatLikeMe;
    }

    public String getProfilePhotoName() {
        for (Photo photo : photos) {
            if (photo.isProfilePhoto()) {
                return photo.getName();
            }
        }
        return "NoProfilePhotoFound";
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Profile profile && Objects.equals(nickname, profile.nickname);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(nickname);
    }

    @Override
    public String toString() {
        return "Profile[id=%d]".formatted(id);
    }

}