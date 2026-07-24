package com.cognizant.edgeservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Exercise 1 – Implementing Edge Services for Routing and Filtering
 *
 * This application acts as an API Gateway / Edge Service built on
 * Spring Cloud Gateway (reactive, WebFlux-based).
 *
 * Responsibilities:
 *  • Route incoming requests to downstream microservices based on predicates
 *    (Path, Method, Header, etc.)
 *  • Apply global and per-route filters (logging, header manipulation, rate
 *    limiting, etc.)
 *  • Provide a single entry point into the microservices cluster
 *
 * Key components:
 *  ┌─────────────────────────────────────────────────────────┐
 *  │  application.yml  – declarative route definitions       │
 *  │  LoggingFilter    – GlobalFilter: logs every request    │
 *  │  RequestIdFilter  – GlobalFilter: attaches a request ID │
 *  │  GatewayConfig    – programmatic route registration     │
 *  └─────────────────────────────────────────────────────────┘
 */
@SpringBootApplication
public class EdgeServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(EdgeServiceApplication.class, args);
    }
}
