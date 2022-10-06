package com.tutego.date4u.interfaces.controllers;

import com.tutego.date4u.core.profile.Profile;
import com.tutego.date4u.core.profile.ProfileFormData;
import com.tutego.date4u.core.profile.ProfileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
public class Date4uWebController {
  private final ProfileRepository profiles;

  public Date4uWebController(ProfileRepository profiles) {
    this.profiles = profiles;

  }

    @RequestMapping("/")
    public String indexPage(Model model) {
      long count = profiles.count();
      model.addAttribute("count", count);
      return "index";
    }




    @RequestMapping("/profile/{id}")
    public String profilePage(@PathVariable Long id, Model model) {
      Optional<Profile> optProfile = profiles.findById(id);
      if (optProfile.isEmpty()) return "redirect:/";
      Profile profile = optProfile.get();
      model.addAttribute( "profile",
              new ProfileFormData(
                      profile.getId(), profile.getNickname(),profile.getBirthdate(),
                      profile.getHornlength(), profile.getGender(),
                      profile.getAttractedToGender(), profile.getDescription(),
                      profile.getLastseen()
              ) );
      return "profile";
    }





    @RequestMapping("/search")
    public String searchPage(Model model) {
      List<Profile> allProfiles = profiles.findAll();
      model.addAttribute("profiles", allProfiles);
        return "search";
    }

  private final Logger log = LoggerFactory.getLogger( getClass() );

  @PostMapping( "/save" )
  public String saveProfile( @ModelAttribute ProfileFormData profile ) {
    log.info( profile.toString() );
    return "redirect:/profile/" + profile.getId();
  }


}