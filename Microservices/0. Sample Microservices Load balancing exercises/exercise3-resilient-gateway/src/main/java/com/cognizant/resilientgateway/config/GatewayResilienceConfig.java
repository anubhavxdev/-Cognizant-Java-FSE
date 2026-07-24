package com.cognizant.resilientgateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * GatewayResilienceConfig – Exercise 3
 *
 * Programmatic route definitions demonstrating resilience patterns via
 * the Spring Cloud Gateway Java DSL.
 *
 * This complements the declarative YAML config in {@code application.yml}.
 * Programmatic routes are useful when:
 *  - Filter arguments depend on runtime beans or environment values.
 *  - Route construction is conditional.
 *  - You prefer type-safe, refactorable Java code over YAML strings.
 *
 * Resilience4j integration points in Gateway:
 *  • {@code CircuitBreaker} GatewayFilter  – wraps downstream call with CB
 *  • {@code Retry}          GatewayFilter  – retries on configurable statuses
 *  • {@code RequestRateLimiter} filter     – throttles traffic
 *  The {@link ResilienceConfiguration} customizer bean configures the CB factory
 *  that backs the {@code CircuitBreaker} gateway filter.
 */
@Configuration
public class GatewayResilienceConfig {

    /**
     * Defines programmatic routes with resilience filters attached.
     *
     * @param builder provided by Spring Cloud Gateway
     * @return the composed {@link RouteLocator}
     */
    @Bean
    public RouteLocator resilientRoutes(RouteLocatorBuilder builder) {
        return builder.routes()

                // ── Route with Circuit Breaker + fallback (Java DSL) ────────
                .route("java_dsl_circuit_breaker_route", r -> r
                    .path("/service/**")
                    .filters(f -> f
                        .stripPrefix(1)
                        // Circuit Breaker: trips → redirect to /fallback/service
                        .circuitBreaker(config -> config
                            .setName("exampleCircuitBreaker")
                            .setFallbackUri("forward:/fallback/service")
                        )
                        // Retry: 2 additional attempts on 503/504 for GET
                        .retry(config -> config
                            .setRetries(2)
                            .setStatuses(
                                org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE,
                                org.springframework.http.HttpStatus.GATEWAY_TIMEOUT
                            )
                            .setMethods(org.springframework.http.HttpMethod.GET)
                            .setBackoff(
                                java.time.Duration.ofMillis(50),   // firstBackoff
                                java.time.Duration.ofMillis(500),  // maxBackoff
                                2,                                  // factor
                                false                               // basedOnPreviousValue
                            )
                        )
                        .addResponseHeader("X-Resilience-Pattern", "circuit-breaker+retry")
                    )
                    .uri("lb://backend-service")
                )

                // ── Route demonstrating only Retry (no Circuit Breaker) ─────
                .route("java_dsl_retry_only_route", r -> r
                    .path("/unstable/**")
                    .filters(f -> f
                        .stripPrefix(1)
                        .retry(config -> config
                            .setRetries(3)
                            .setStatuses(
                                org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR,
                                org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE
                            )
                        )
                        .addResponseHeader("X-Resilience-Pattern", "retry")
                    )
                    .uri("lb://unstable-service")
                )

                .build();
    }
}
