package com.cognizant.jwtauth.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * JwtConfig – Exercise 3
 *
 * Binds the {@code spring.security.jwt.*} properties from {@code application.yml}
 * to a strongly-typed configuration bean.
 *
 * Using {@link ConfigurationProperties} is the idiomatic Spring Boot 3 approach
 * instead of injecting individual {@code @Value} fields, because it:
 *  - groups related properties,
 *  - supports IDE auto-completion via spring-boot-configuration-processor, and
 *  - simplifies testing (just set fields on the POJO).
 */
@Configuration
@ConfigurationProperties(prefix = "spring.security.jwt")
public class JwtConfig {

    /**
     * HMAC-SHA256 signing secret.
     * Must be at least 256 bits (32 characters) for HS256.
     */
    private String secret;

    /**
     * Token validity in milliseconds (default 1 hour = 3_600_000 ms).
     */
    private long expirationMs = 3_600_000L;

    // ── Getters & setters ──────────────────────────────────────────────────

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public long getExpirationMs() {
        return expirationMs;
    }

    public void setExpirationMs(long expirationMs) {
        this.expirationMs = expirationMs;
    }
}
