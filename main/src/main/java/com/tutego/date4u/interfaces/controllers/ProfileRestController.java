package com.tutego.date4u.interfaces.controllers;

import com.tutego.date4u.core.photo.Photo;
import com.tutego.date4u.core.profile.Profile;
import com.tutego.date4u.core.profile.ProfileFormData;
import com.tutego.date4u.core.profile.ProfileRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class ProfileRestController {
    private final ProfileRepository profiles;

    public ProfileRestController(ProfileRepository profiles) {
        this.profiles = profiles;

    }
    @GetMapping( "/{id}" )
    public ResponseEntity<?> profilePage(@PathVariable Long id ) {
        Optional<Profile> optProfile = profiles.findById(id);
        if (optProfile.isEmpty()) return ResponseEntity.noContent().build();
        Profile profile = optProfile.get();
        return ResponseEntity.ok(new ProfileFormData(
                profile.getId(), profile.getNickname(), profile.getBirthdate(),
                profile.getHornlength(), profile.getGender(),
                profile.getAttractedToGender(), profile.getDescription(),
                profile.getLastseen()));
    }

}
