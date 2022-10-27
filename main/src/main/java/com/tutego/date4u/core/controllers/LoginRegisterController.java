package com.tutego.date4u.core.controllers;

import com.tutego.date4u.core.configuration.UnicornUser;
import com.tutego.date4u.core.entities.Profile;
import com.tutego.date4u.core.entities.Unicorn;
import com.tutego.date4u.core.formData.RegisterFormData;
import com.tutego.date4u.core.repositories.ProfileRepository;
import com.tutego.date4u.core.repositories.UnicornRepository;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginRegisterController {

    private final ProfileRepository profileRepo;
    private final UnicornRepository unicornRepo;


    public LoginRegisterController(ProfileRepository profileRepo, UnicornRepository unicornRepo) {
        this.profileRepo = profileRepo;
        this.unicornRepo = unicornRepo;

    }

    @GetMapping("/login")
    public String showLogin(Authentication a) {
        UnicornUser user = null;
        if (a != null) {
            user = (UnicornUser) a.getPrincipal();
        }
        return user != null ? "redirect:/" : "login";
    }

    @GetMapping("/register")
    public String showRegister(Model model) {
        model.addAttribute("registerFormData", new RegisterFormData());
        return "register";
    }

    @PostMapping("/register")
    public String register(@Valid RegisterFormData rfd, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "register";
        } else {
            rfd.setPassword("{noop}" + rfd.getPassword());
            Profile profile = new Profile(rfd);
            Profile savedProfile = profileRepo.save(profile);
            Unicorn unicorn = new Unicorn(rfd.getEmail(), rfd.getPassword(), savedProfile);
            unicornRepo.save(unicorn);
            return "login";
        }
    }

    @GetMapping("/logout")
    public String logout() {
        return "login";
    }

}