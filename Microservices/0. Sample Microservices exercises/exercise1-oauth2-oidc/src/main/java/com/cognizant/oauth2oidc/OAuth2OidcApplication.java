package com.cognizant.oauth2oidc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Exercise 1 – Centralized Authentication with OAuth 2.1 / OIDC
 *
 * This application demonstrates how to integrate Google (or any OIDC-compliant
 * identity provider) as the centralised authentication authority using Spring
 * Security's first-class OAuth 2.0 / OIDC client support introduced in
 * Spring Boot 3.
 */
@SpringBootApplication
public class OAuth2OidcApplication {

    public static void main(String[] args) {
        SpringApplication.run(OAuth2OidcApplication.class, args);
    }
}
