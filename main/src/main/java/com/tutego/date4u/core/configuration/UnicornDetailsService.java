package com.tutego.date4u.core.configuration;

import com.tutego.date4u.core.entities.Unicorn;
import com.tutego.date4u.core.repositories.UnicornRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collections;
import java.util.Optional;

@Configuration
public class UnicornDetailsService implements UserDetailsService {
   @Autowired
   private UnicornRepository unicornRepository;
   @Override
   public UserDetails loadUserByUsername(String username ) throws UsernameNotFoundException {
      Optional<Unicorn> unicorn = unicornRepository.findUnicornByEmail( username );
      if( unicorn.isEmpty() ) {
         System.out.println("User not found " + username );
         throw new UsernameNotFoundException( "User not found " + username );
      }

      return new UnicornUser(unicorn.get().getEmail(), unicorn.get().getPassword(), Collections.emptyList(),unicorn.get().getProfile().getId(),unicorn.get().getProfile().getNickname());
   }


}