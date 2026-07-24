package com.cognizant.jwtauth.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * SecuredController – Exercise 3
 *
 * Demonstrates endpoints that are protected by JWT authentication.
 *
 * The {@link com.cognizant.jwtauth.security.JwtTokenFilter} populates
 * the {@link org.springframework.security.core.context.SecurityContext}
 * before these methods are reached. If the filter did not set an
 * authentication (i.e., no valid JWT was provided), Spring Security
 * returns HTTP 401 before the controller is invoked.
 */
@RestController
@RequestMapping("/api")
public class SecuredController {

    /**
     * Basic secured endpoint accessible to any authenticated user.
     *
     * Endpoint: GET /api/secure
     * Requires: valid JWT Bearer token
     */
    @GetMapping("/secure")
    public Map<String, String> secure() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return Map.of(
            "message",  "This is a secure endpoint",
            "username", auth.getName()
        );
    }

    /**
     * Returns the current user's profile information extracted from the JWT.
     *
     * Endpoint: GET /api/profile
     * Requires: valid JWT Bearer token
     */
    @GetMapping("/profile")
    public Map<String, Object> profile() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return Map.of(
            "username",   auth.getName(),
            "roles",      auth.getAuthorities()
                             .stream()
                             .map(Object::toString)
                             .toList(),
            "authenticated", auth.isAuthenticated()
        );
    }

    /**
     * Admin-only endpoint protected by role-based access control.
     *
     * @PreAuthorize verifies the "ROLE_ADMIN" authority present in the JWT claims.
     *
     * Endpoint: GET /api/admin
     * Requires: valid JWT Bearer token with ADMIN role
     */
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public Map<String, String> admin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return Map.of(
            "message",  "This is an admin-only secure endpoint",
            "username", auth.getName()
        );
    }
}
