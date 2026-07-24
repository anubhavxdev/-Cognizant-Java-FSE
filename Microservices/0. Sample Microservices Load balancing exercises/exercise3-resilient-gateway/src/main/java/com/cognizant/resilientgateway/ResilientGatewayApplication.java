package com.cognizant.resilientgateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Exercise 3 – Resilience Patterns in an API Gateway
 *
 * Demonstrates industry-standard resilience patterns applied inside a
 * Spring Cloud Gateway using Resilience4j (Spring Boot 3 compatible):
 *
 *  ┌────────────────────────────────────────────────────────────────────┐
 *  │  Pattern          │ Mechanism                                      │
 *  ├───────────────────┼────────────────────────────────────────────────┤
 *  │  Circuit Breaker  │ Stops calling failing services automatically   │
 *  │  Retry            │ Re-attempts failed requests with back-off      │
 *  │  Time Limiter     │ Cancels requests that exceed a timeout         │
 *  │  Rate Limiter     │ Throttles request rate to protect services     │
 *  │  Fallback         │ Returns a default response when CB is open     │
 *  └────────────────────────────────────────────────────────────────────┘
 *
 * Key components:
 *  ┌──────────────────────────────────────────────────────────────────────┐
 *  │  application.yml          – routes with CircuitBreaker/Retry filters │
 *  │  ResilienceConfiguration  – customizes Resilience4j factories        │
 *  │  FallbackController       – serves fallback responses                │
 *  │  ResilienceMetricsFilter  – logs circuit breaker state per request   │
 *  └──────────────────────────────────────────────────────────────────────┘
 *
 * Spring Boot 3 note:
 *   The original exercise used {@code resilience4j-spring-boot2}, which is
 *   NOT compatible with Spring Boot 3. The correct artifact for reactive
 *   Spring Boot 3 is:
 *   {@code spring-cloud-starter-circuitbreaker-reactor-resilience4j}
 */
@SpringBootApplication
public class ResilientGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ResilientGatewayApplication.class, args);
    }
}
