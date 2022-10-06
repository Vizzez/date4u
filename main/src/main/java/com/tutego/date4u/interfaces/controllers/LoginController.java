package com.tutego.date4u.interfaces.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @GetMapping("/login")
    public String showLogin() {
        return "login";
    }
    @GetMapping("/login-error")
    public String showLoginError(Model model) {
        model.addAttribute("loginError", true);
        return "login";
    }

}