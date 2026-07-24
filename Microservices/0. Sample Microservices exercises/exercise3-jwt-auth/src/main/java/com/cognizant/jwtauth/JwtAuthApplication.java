package com.cognizant.jwtauth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Exercise 3 – JSON Web Tokens (JWT) for Secure Communication
 *
 * This application implements a self-contained JWT authentication system:
 *
 *  • POST /auth/login   → validates credentials, issues a signed JWT
 *  • GET  /api/secure   → protected endpoint; requires a valid Bearer JWT
 *  • GET  /api/profile  → protected; returns claims extracted from the JWT
 *
 * Components:
 *  ┌────────────────────────────────────────────────────────────────────┐
 *  │  JwtConfig          – binds spring.security.jwt.* properties       │
 *  │  JwtTokenProvider   – creates, parses, and validates JWTs          │
 *  │  JwtTokenFilter     – OncePerRequestFilter; extracts Bearer token  │
 *  │  SecurityConfig     – sets up filter chain; adds JwtTokenFilter    │
 *  │  AuthController     – issues tokens on successful login            │
 *  │  SecuredController  – protected endpoints                          │
 *  └────────────────────────────────────────────────────────────────────┘
 */
@SpringBootApplication
public class JwtAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(JwtAuthApplication.class, args);
    }
}
