package com.ms.music_service.controller;

import com.ms.music_service.security.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class AuthUtil {
    public CustomUserDetails getCurrentUser(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth == null || !auth.isAuthenticated() || auth.getPrincipal().equals("anonymousUser"))
            throw new IllegalStateException("User not authenticated");
        return (CustomUserDetails) auth.getPrincipal();
    }

    public UUID getCurrentUserId(){
        return getCurrentUser().getUserId();
    }

    public boolean isAuthenticated(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null &&
                auth.isAuthenticated() &&
                !((auth.getPrincipal()) instanceof String);
    }
}