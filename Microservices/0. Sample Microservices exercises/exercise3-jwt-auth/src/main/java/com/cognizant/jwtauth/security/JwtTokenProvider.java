package com.cognizant.jwtauth.security;

import com.cognizant.jwtauth.config.JwtConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * JwtTokenProvider – Exercise 3
 *
 * Central service responsible for:
 *  1. {@link #createToken(String, List)}   – signing and building JWTs
 *  2. {@link #validateToken(String)}       – verifying signature and expiry
 *  3. {@link #getAuthentication(String)}   – constructing a Spring Security
 *                                            {@link Authentication} from a token
 *  4. {@link #getUsername(String)}         – extracting the subject claim
 *
 * Uses the modern JJWT 0.12.x API:
 *  - {@code Jwts.parser()} replaces the deprecated {@code Jwts.parserBuilder()}
 *  - {@code Keys.hmacShaKeyFor()} derives a type-safe {@link SecretKey}
 *  - {@code SignatureAlgorithm} enum replaced by {@code Jwts.SIG.HS256}
 */
@Component
public class JwtTokenProvider {

    private static final Logger log = LoggerFactory.getLogger(JwtTokenProvider.class);

    private final SecretKey signingKey;
    private final long      expirationMs;

    public JwtTokenProvider(JwtConfig jwtConfig) {
        // Derive a SecretKey from the configured secret string.
        // Keys.hmacShaKeyFor() ensures the key is valid for HS256.
        this.signingKey   = Keys.hmacShaKeyFor(
                jwtConfig.getSecret().getBytes(StandardCharsets.UTF_8));
        this.expirationMs = jwtConfig.getExpirationMs();
    }

    // ── Token Creation ─────────────────────────────────────────────────────

    /**
     * Creates a signed JWT for the given username and roles.
     *
     * @param username the subject (typically the authenticated user's username)
     * @param roles    list of role strings to embed in the "roles" claim
     * @return compact, signed JWT string (e.g., "eyJ...")
     */
    public String createToken(String username, List<String> roles) {
        Date now     = new Date();
        Date expires = new Date(now.getTime() + expirationMs);

        return Jwts.builder()
                   .subject(username)               // "sub" claim
                   .claim("roles", roles)            // custom claim
                   .issuedAt(now)                    // "iat" claim
                   .expiration(expires)              // "exp" claim
                   .signWith(signingKey)             // HS256 by default with SecretKey
                   .compact();
    }

    // ── Token Validation ───────────────────────────────────────────────────

    /**
     * Validates the given token's signature and expiry.
     *
     * @param token the JWT string to validate
     * @return {@code true} if valid; {@code false} otherwise
     */
    public boolean validateToken(String token) {
        try {
            parseClaims(token);   // throws on invalid / expired token
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.warn("JWT validation failed: {}", e.getMessage());
            return false;
        }
    }

    // ── Authentication Extraction ──────────────────────────────────────────

    /**
     * Builds a Spring Security {@link Authentication} object from a valid token.
     * Used by {@link JwtTokenFilter} to populate the {@link
     * org.springframework.security.core.context.SecurityContext}.
     *
     * @param token a previously validated JWT
     * @return a fully populated {@link UsernamePasswordAuthenticationToken}
     */
    public Authentication getAuthentication(String token) {
        Claims claims = parseClaims(token);

        @SuppressWarnings("unchecked")
        List<String> roles = claims.get("roles", List.class);

        List<SimpleGrantedAuthority> authorities =
            (roles == null ? List.<String>of() : roles)
                .stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toList());

        String username = claims.getSubject();
        return new UsernamePasswordAuthenticationToken(username, null, authorities);
    }

    /**
     * Extracts the username (subject) from a valid token.
     *
     * @param token the JWT string
     * @return the subject claim value
     */
    public String getUsername(String token) {
        return parseClaims(token).getSubject();
    }

    // ── Private Helpers ────────────────────────────────────────────────────

    private Claims parseClaims(String token) {
        return Jwts.parser()
                   .verifyWith(signingKey)
                   .build()
                   .parseSignedClaims(token)
                   .getPayload();
    }
}
