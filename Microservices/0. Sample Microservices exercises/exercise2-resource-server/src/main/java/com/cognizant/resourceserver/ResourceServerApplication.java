package com.cognizant.resourceserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Exercise 2 – Configuring Authorization Servers and Resource Servers
 *
 * This application acts as an OAuth2 Resource Server. It validates JWT Bearer
 * tokens that were issued by an external Authorization Server (e.g., Keycloak,
 * Okta, Auth0).
 *
 * The application does NOT issue tokens itself. Token issuance is the
 * responsibility of the configured Authorization Server.
 */
@SpringBootApplication
public class ResourceServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ResourceServerApplication.class, args);
    }
}
