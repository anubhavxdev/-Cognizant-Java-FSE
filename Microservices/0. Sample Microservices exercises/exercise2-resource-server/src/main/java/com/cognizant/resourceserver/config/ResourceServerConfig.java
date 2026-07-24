package com.cognizant.resourceserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

/**
 * ResourceServerConfig – Exercise 2
 *
 * Configures this Spring Boot application as a stateless OAuth2 Resource Server
 * that validates JWT Bearer tokens on every request.
 *
 * Key design decisions (Spring Boot 3 / Spring Security 6):
 *
 *  1. WebSecurityConfigurerAdapter is REMOVED — use {@link SecurityFilterChain} beans.
 *
 *  2. Sessions are STATELESS — a Resource Server should never create an HTTP session;
 *     every request must carry a valid Bearer token.
 *
 *  3. CSRF protection is DISABLED for REST APIs protected by JWT (token itself
 *     provides the CSRF protection guarantees).
 *
 *  4. The JwtDecoder is auto-configured by Spring Boot from the
 *     spring.security.oauth2.resourceserver.jwt.issuer-uri property; no manual
 *     bean definition is required.
 *
 *  5. @EnableMethodSecurity enables @PreAuthorize / @PostAuthorize on controllers.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity   // enables @PreAuthorize, @PostAuthorize, @Secured
public class ResourceServerConfig {

    /**
     * HTTP security filter chain for the Resource Server.
     *
     * @param http the {@link HttpSecurity} builder
     * @return the configured {@link SecurityFilterChain}
     * @throws Exception if configuration fails
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // ── Stateless session management ───────────────────────────────────
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            // ── Disable CSRF for stateless JWT-secured REST API ────────────────
            .csrf(csrf -> csrf.disable())

            // ── Authorization rules ────────────────────────────────────────────
            .authorizeHttpRequests(auth -> auth
                // Public health-check endpoint
                .requestMatchers("/actuator/health", "/public/**").permitAll()
                // All other requests must carry a valid JWT
                .anyRequest().authenticated()
            )

            // ── Configure as OAuth2 Resource Server with JWT ───────────────────
            // Spring auto-wires a JwtDecoder that fetches the JWKS from the
            // Authorization Server and validates every incoming Bearer token.
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> { /* default JWT decoder from application.yml */ })
            );

        return http.build();
    }
}
