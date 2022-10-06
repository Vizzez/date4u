package com.tutego.date4u.interfaces.controllers;

import com.tutego.date4u.core.photo.Photo;
import com.tutego.date4u.core.photo.PhotoRepository;
import com.tutego.date4u.core.photo.PhotoService;
import com.tutego.date4u.core.profile.Profile;
import com.tutego.date4u.core.profile.ProfileRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.aspectj.weaver.tools.cache.SimpleCacheFactory.path;

@RestController
public class PhotoRestController {

    private final PhotoService photos;
    private final ProfileRepository profiles;

    private final PhotoRepository photoRep;
    private ProblemDetail notFoundProblemDetail;

    public PhotoRestController(PhotoService photos, ProfileRepository profiles, PhotoRepository photoRep) {
        this.photoRep = photoRep;
        this.photos = photos;
        this.profiles = profiles;
        notFoundProblemDetail=ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        notFoundProblemDetail.setDetail("No profile found!");
        notFoundProblemDetail.setTitle("Not Found");
    }

    @GetMapping(path = "/api/photos/{name}",
            produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<?> photo(@PathVariable("name") String name) {

        return ResponseEntity.of(photos.download(name));
    }

    @PostMapping(path = "/api/profiles/{id}/photos")
    public ResponseEntity uploadPhoto(@PathVariable("id") long id, @RequestParam("image") MultipartFile mpFile) throws IOException {
        String result = null;
        Optional<Profile> optProfile = profiles.findById(id);


        if (optProfile.isEmpty()) return ResponseEntity.of(notFoundProblemDetail).build();
        result = photos.upload(mpFile.getBytes());
        Profile profile = optProfile.get();
        Photo photo = new Photo(null, profile, result, false, LocalDateTime.now());
        profile.add(photo);
        profiles.save(profile);
//        photoRep.save(photo);
        return ResponseEntity.created( URI.create( "/api/photos/" + result ) ).build();
    }

    @GetMapping(path="/api/profiles/{id}/photos")
    public ResponseEntity<?> getPhotos(@PathVariable("id") Long id) {
        Optional<Profile> optProfile = profiles.findById(id);
        if (optProfile.isEmpty()) return ResponseEntity.of(notFoundProblemDetail).build();
        Profile profile = optProfile.get();
        List<Photo> allPhotos = profile.getPhotos();
        return ResponseEntity.ok( allPhotos.size() );
    }
}