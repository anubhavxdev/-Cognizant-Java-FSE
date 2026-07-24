package com.cognizant.jwtauth.config;

import com.cognizant.jwtauth.security.JwtTokenFilter;
import com.cognizant.jwtauth.security.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * SecurityConfig – Exercise 3
 *
 * Configures Spring Security for JWT-based stateless authentication.
 *
 * Design decisions (Spring Boot 3 / Spring Security 6):
 *
 *  1. WebSecurityConfigurerAdapter is REMOVED — use {@link SecurityFilterChain} bean.
 *
 *  2. Sessions are STATELESS — authentication state is carried entirely in the JWT.
 *
 *  3. CSRF protection is DISABLED for stateless REST APIs (the JWT acts as
 *     the anti-CSRF token because it must be explicitly included in each request).
 *
 *  4. In-memory user store (demo only). In production replace with a
 *     JPA-backed UserDetailsService.
 *
 *  5. {@link JwtTokenFilter} is inserted BEFORE the standard
 *     {@link UsernamePasswordAuthenticationFilter} so JWTs are evaluated first.
 *
 *  6. The {@link AuthenticationManager} is exposed as a bean so the
 *     {@link com.cognizant.jwtauth.controller.AuthController} can use it to
 *     authenticate the login request and then issue a token.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;

    public SecurityConfig(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    // ── Security Filter Chain ──────────────────────────────────────────────

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Stateless — no server-side session
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            // Disable CSRF for stateless JWT REST API
            .csrf(csrf -> csrf.disable())

            // Authorization rules
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/**").permitAll()       // public: login, register
                .requestMatchers("/public/**").permitAll()     // public info endpoints
                .anyRequest().authenticated()                  // everything else: JWT required
            )

            // Insert our JWT filter before the username/password filter
            .addFilterBefore(
                new JwtTokenFilter(jwtTokenProvider),
                UsernamePasswordAuthenticationFilter.class
            );

        return http.build();
    }

    // ── Authentication Manager ─────────────────────────────────────────────

    /**
     * Exposes {@link AuthenticationManager} as a Spring bean.
     * Required by {@link com.cognizant.jwtauth.controller.AuthController}
     * to programmatically authenticate the login request.
     */
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    // ── User Store (demo — replace with DB in production) ─────────────────

    /**
     * In-memory user details for demonstration.
     *
     * <pre>
     *   username: user     password: password   roles: USER
     *   username: admin    password: admin123   roles: USER, ADMIN
     * </pre>
     */
    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder encoder) {
        var manager = new InMemoryUserDetailsManager();
        manager.createUser(
            User.withUsername("user")
                .password(encoder.encode("password"))
                .roles("USER")
                .build()
        );
        manager.createUser(
            User.withUsername("admin")
                .password(encoder.encode("admin123"))
                .roles("USER", "ADMIN")
                .build()
        );
        return manager;
    }

    // ── Password Encoder ───────────────────────────────────────────────────

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
