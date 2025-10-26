package com.kakaotechbootcamp.community.utils.security;

import java.security.Principal;
import lombok.Getter;

@Getter
public class AuthPrincipal implements Principal {

    private final long userId;

    public AuthPrincipal(long userId) {
        this.userId = userId;
    }
   
    @Override
    public String getName() {
        return "";
    }
}