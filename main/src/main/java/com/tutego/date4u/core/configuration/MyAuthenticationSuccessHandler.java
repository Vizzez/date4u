package com.tutego.date4u.core.configuration;

import com.tutego.date4u.core.entities.Profile;
import com.tutego.date4u.core.repositories.ProfileRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component("myAuthenticationSuccessHandler")
public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    ActiveUserStore activeUserStore;
    @Autowired
    ProfileRepository profileRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        Profile profile = profileRepository.findById(((UnicornUser) authentication.getPrincipal()).getpId()).get();
        profile.setLastseen(LocalDateTime.now());
        profileRepository.save(profile);
        HttpSession session = request.getSession(false);

        if (session != null) {
            LoggedUser user = new LoggedUser(authentication.getName(), activeUserStore);
            session.setAttribute("user", user);
        }
        response.sendRedirect("/");

    }
}