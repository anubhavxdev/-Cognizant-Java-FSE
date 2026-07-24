package com.cognizant.resourceserver.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * SecureController – Exercise 2
 *
 * Demonstrates securing REST endpoints in an OAuth2 Resource Server.
 *
 * Every endpoint in this controller requires a valid JWT Bearer token.
 * The token is validated by the Spring Security OAuth2 Resource Server
 * filter chain configured in {@link com.cognizant.resourceserver.config.ResourceServerConfig}.
 *
 * The {@link Jwt} principal is injected by Spring Security after successful
 * token validation, giving direct access to all JWT claims.
 */
@RestController
@RequestMapping("/api")
public class SecureController {

    /**
     * Basic secured endpoint — requires any valid JWT.
     *
     * Endpoint: GET /api/secure
     *
     * @param jwt the validated JWT injected by Spring Security
     * @return a message confirming secured access along with the token subject
     */
    @GetMapping("/secure")
    public Map<String, String> secure(@AuthenticationPrincipal Jwt jwt) {
        return Map.of(
            "message", "This is a secure endpoint",
            "subject", jwt.getSubject(),
            "issuer",  jwt.getIssuer().toString()
        );
    }

    /**
     * Admin-only endpoint — requires the SCOPE_admin claim in the JWT.
     *
     * @PreAuthorize checks the granted authority derived from the JWT scopes.
     * Spring Security maps JWT scopes to authorities prefixed with "SCOPE_".
     *
     * Endpoint: GET /api/admin
     */
    @GetMapping("/admin")
    @PreAuthorize("hasAuthority('SCOPE_admin')")
    public Map<String, String> admin(@AuthenticationPrincipal Jwt jwt) {
        return Map.of(
            "message",  "This is an admin-only endpoint",
            "subject",  jwt.getSubject(),
            "scopes",   jwt.getClaimAsString("scope")
        );
    }

    /**
     * Returns all claims from the validated JWT.
     *
     * Endpoint: GET /api/token/claims
     *
     * @param jwt the validated JWT
     * @return all JWT claims as a map
     */
    @GetMapping("/token/claims")
    public Map<String, Object> tokenClaims(@AuthenticationPrincipal Jwt jwt) {
        return jwt.getClaims();
    }

    /**
     * Public endpoint — no authentication required (see SecurityConfig).
     *
     * Endpoint: GET /public/info
     */
    @GetMapping("/public/info")
    public String publicInfo() {
        return "This endpoint is publicly accessible without a token.";
    }
}
