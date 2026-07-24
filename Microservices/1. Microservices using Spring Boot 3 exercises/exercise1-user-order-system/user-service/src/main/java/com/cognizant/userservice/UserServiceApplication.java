package com.cognizant.userservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Exercise 1 – User Service
 *
 * Manages user accounts. Exposes REST endpoints for CRUD operations.
 * Other microservices (e.g., Order Service) call this service to validate users.
 */
@SpringBootApplication
public class UserServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }
}
