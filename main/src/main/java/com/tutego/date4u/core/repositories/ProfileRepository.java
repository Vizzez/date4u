package com.tutego.date4u.core.repositories;

import com.tutego.date4u.core.entities.Profile;
import jakarta.persistence.Tuple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
    @Query( nativeQuery = true, value = """
  SELECT YEAR(lastseen) AS y, MONTH(lastseen) AS m, COUNT(*) AS count
  FROM profile
  GROUP BY YEAR(lastseen), MONTH(lastseen)""" )
    List<Tuple> findMonthlyProfileCount();

    @Query(   "SELECT p FROM Profile p WHERE p.gender=:gender AND p.attractedToGender=:attractedToGender AND p.hornlength between :minHornlength and :maxHornlength AND p.birthdate BETWEEN :startB AND :endB" )
    List<Profile> findProfilesByFilter(int gender, int attractedToGender, int minHornlength,int maxHornlength, LocalDate startB,LocalDate endB);



}
