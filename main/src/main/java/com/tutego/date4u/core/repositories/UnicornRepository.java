package com.tutego.date4u.core.repositories;

import com.tutego.date4u.core.entities.Unicorn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UnicornRepository extends JpaRepository<Unicorn, Long> {

  @Query( "SELECT u FROM Unicorn u WHERE u.email = :emailAddress" )
  Optional<Unicorn> findUnicornByEmail( String emailAddress );
}
