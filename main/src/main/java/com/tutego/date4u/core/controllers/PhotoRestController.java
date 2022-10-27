package com.tutego.date4u.core.controllers;

import com.tutego.date4u.core.PhotoService;
import com.tutego.date4u.core.entities.Photo;
import com.tutego.date4u.core.entities.Profile;
import com.tutego.date4u.core.repositories.ProfileRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
public class PhotoRestController {

    private final PhotoService photos;
    private final ProfileRepository profiles;

    private final ProblemDetail notFoundProblemDetail;


    public PhotoRestController(PhotoService photos, ProfileRepository profiles) {

        this.photos = photos;
        this.profiles = profiles;

        notFoundProblemDetail = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        notFoundProblemDetail.setDetail("No profile found!");
        notFoundProblemDetail.setTitle("Not Found");
    }

    @GetMapping(path = "/api/photos/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<?> photo(@PathVariable("id") long id) {

        return ResponseEntity.of(photos.downloadById(id));
    }

    @GetMapping(path = "/api/photo/{name}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<?> photoByName(@PathVariable("name") String name) {

        return ResponseEntity.of(photos.download(name));
    }

    @GetMapping(path = "/public/{name}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<?> publicPhoto(@PathVariable("name") String name) {

        return ResponseEntity.of(photos.download(name));
    }


    @PostMapping(path = "/api/profiles/{id}/photos")
    public ResponseEntity uploadPhoto(@PathVariable("id") long id, @RequestParam("image") MultipartFile mpFile) throws IOException {

        String result ;
        Optional<Profile> optProfile = profiles.findById(id);
        if (optProfile.isEmpty())
            return ResponseEntity.of(notFoundProblemDetail)
                    .build();
        result = photos.upload(mpFile.getBytes());
        Profile profile = optProfile.get();
        Photo photo = new Photo(null, profile, result, false);
        profile.add(photo);
        profiles.save(profile);
        return ResponseEntity.created(URI.create("/api/photos/" + result))
                .build();
    }

    @GetMapping(path = "/api/profiles/{id}/photos")
    public ResponseEntity<?> getPhotos(@PathVariable("id") Long id) {
        Optional<Profile> optProfile = profiles.findById(id);
        if (optProfile.isEmpty())
            return ResponseEntity.of(notFoundProblemDetail)
                    .build();
        Profile profile = optProfile.get();
        List<Photo> allPhotos = profile.getPhotos();
        return ResponseEntity.ok(allPhotos.size());
    }
}