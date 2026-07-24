package com.cognizant.orderservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Exercise 1 – Order Service
 *
 * Manages orders placed by users. Communicates with User Service via WebClient
 * (Spring WebFlux reactive HTTP client) to validate that a user exists before
 * placing an order.
 */
@SpringBootApplication
public class OrderServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication.class, args);
    }
}
