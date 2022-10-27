package com.tutego.date4u.core.repositories;

import com.tutego.date4u.core.entities.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PhotoRepository extends JpaRepository<Photo,Long> {
    @Query("SELECT p FROM Photo p WHERE p.profile.id = :id AND p.isProfilePhoto=true")
    Optional<Photo> findProfilePhotoById(long id);
    @Query("SELECT p FROM Photo p WHERE p.profile.id = :id AND p.name=:name")
    Optional<Photo> findPhotoByIdAndName(long id,String name);
}
