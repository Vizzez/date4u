package com.tutego.date4u.core;

import com.tutego.date4u.core.FileSystem;
import com.tutego.date4u.core.entities.Photo;
import com.tutego.date4u.core.entities.Profile;
import com.tutego.date4u.core.repositories.ProfileRepository;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.io.UncheckedIOException;
import java.util.Optional;
import java.util.UUID;

@Service
@Validated
public class PhotoService {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final FileSystem fs;


    private final ProfileRepository pr;

    public PhotoService(FileSystem fs, ProfileRepository pr) {
        this.pr = pr;
        this.fs = fs;

    }

    @Cacheable("date4u.filesystem.file")
    public Optional<byte[]> download(String name) {

        try {

            return Optional.of(fs.load(name + ".jpg"));
        } catch (UncheckedIOException e) {

            return Optional.empty();
        }
    }

    //    @Cacheable("date4u.filesystem.file")
    public Optional<byte[]> downloadById(long id) {
        Optional<Profile> profile = pr.findById(id);
        try {
            if (profile.isPresent()) {
                return Optional.of(fs.load(profile.get()
                        .getProfilePhotoName() + ".jpg"));
            }
        } catch (UncheckedIOException e) {
            System.out.println("IOException: " + e.getMessage());
        }
        return Optional.of(fs.load("unknown.png"));
    }

    @Cacheable(cacheNames = "date4u.filesystem.file", key = "#photo.name")
    public Optional<byte[]> download(@Valid Photo photo) {
        return download(photo.getName());
    }

    public String upload(byte[] imageBytes) {

        String imageName = UUID.randomUUID()
                .toString()
                .replace("-", "");

        // First: store original image
        fs.store(imageName + ".jpg", imageBytes);


        return imageName;
    }


}
