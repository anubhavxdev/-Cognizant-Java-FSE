package com.cognizant.oauth2oidc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * SecurityConfig – Exercise 1
 *
 * Configures Spring Security for OAuth 2.0 / OIDC login.
 *
 * Key points (Spring Boot 3 / Spring Security 6):
 *  - WebSecurityConfigurerAdapter is REMOVED; use SecurityFilterChain beans instead.
 *  - oauth2Login() delegates authentication entirely to the configured OIDC provider.
 *  - All requests require authentication; the /error path is permitted so that
 *    error pages are rendered without an infinite redirect loop.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Defines the HTTP security filter chain.
     *
     * @param http the {@link HttpSecurity} builder provided by Spring Security
     * @return configured {@link SecurityFilterChain}
     * @throws Exception if configuration fails
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // ── Authorization rules ────────────────────────────────────────────
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/error", "/webjars/**").permitAll()
                .anyRequest().authenticated()
            )
            // ── OAuth2 / OIDC Login ────────────────────────────────────────────
            // Spring Security handles the full Authorization Code + PKCE flow.
            // On successful login the user is redirected to the default success URL.
            .oauth2Login(oauth2 -> oauth2
                .loginPage("/login")              // custom login page (optional)
                .defaultSuccessUrl("/user", true) // redirect after login
            )
            // ── Logout ────────────────────────────────────────────────────────
            .logout(logout -> logout
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .deleteCookies("JSESSIONID")
            );

        return http.build();
    }
}
