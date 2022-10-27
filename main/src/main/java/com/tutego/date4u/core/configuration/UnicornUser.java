package com.tutego.date4u.core.configuration;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class UnicornUser extends User {
    Long pId;
    String name;

    public UnicornUser(String username, String password, Collection<? extends GrantedAuthority> authorities, Long id,
                       String name) {
        super(username, password, authorities);
        this.pId = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Long getpId() {
        return pId;
    }

}