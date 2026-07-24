package com.cognizant.oauth2oidc.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * UserController – Exercise 1
 *
 * Exposes a simple REST endpoint that returns the authenticated user's
 * OIDC claims after a successful OAuth2/OIDC login.
 *
 * Spring Security injects the {@link OidcUser} principal automatically once
 * the OAuth2 login flow completes. The principal contains the ID Token claims
 * (sub, email, name, picture, etc.) obtained from the identity provider.
 */
@RestController
public class UserController {

    /**
     * Returns all OIDC claims of the currently authenticated user.
     *
     * Endpoint: GET /user
     * Access:   Requires authentication (enforced by SecurityConfig)
     *
     * @param oidcUser the OIDC principal injected by Spring Security
     * @return a map of OIDC claim names → values
     */
    @GetMapping("/user")
    public Map<String, Object> user(@AuthenticationPrincipal OidcUser oidcUser) {
        return oidcUser.getClaims();
    }

    /**
     * Returns a subset of user information for display purposes.
     *
     * Endpoint: GET /user/info
     *
     * @param oidcUser the OIDC principal
     * @return selected user attributes
     */
    @GetMapping("/user/info")
    public Map<String, Object> userInfo(@AuthenticationPrincipal OidcUser oidcUser) {
        return Map.of(
            "subject",  oidcUser.getSubject(),
            "email",    oidcUser.getEmail(),
            "name",     oidcUser.getFullName(),
            "picture",  oidcUser.getPicture()
        );
    }

    /**
     * Public home endpoint — no authentication required.
     *
     * Endpoint: GET /
     */
    @GetMapping("/")
    public String home() {
        return "Welcome! Navigate to /user after logging in via OAuth2/OIDC.";
    }
}
