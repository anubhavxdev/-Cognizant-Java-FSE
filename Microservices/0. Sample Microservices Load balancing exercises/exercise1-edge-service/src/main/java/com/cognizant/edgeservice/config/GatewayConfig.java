package com.cognizant.edgeservice.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * GatewayConfig – Exercise 1
 *
 * Demonstrates programmatic (Java DSL) route registration as a complement to
 * the declarative YAML approach in {@code application.yml}.
 *
 * Both styles produce identical runtime behaviour. The Java DSL is preferred
 * when route logic depends on runtime conditions, Spring beans, or conditional
 * expressions that cannot be expressed in YAML.
 *
 * Routes defined here are MERGED with the ones in application.yml; neither
 * overrides the other.
 */
@Configuration
public class GatewayConfig {

    /**
     * Registers an additional route programmatically.
     *
     * Route: GET /status/** → https://httpbin.org/status/**
     *  - Rewrites the path by removing the /status prefix.
     *  - Adds a custom downstream request header.
     *  - Adds a custom upstream response header.
     *
     * @param builder the Spring-provided {@link RouteLocatorBuilder}
     * @return the composed {@link RouteLocator}
     */
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                // Route: /status/** → https://httpbin.org/status/**
                .route("programmatic_status_route", r -> r
                    .path("/status/**")
                    .filters(f -> f
                        .rewritePath("/status/(?<segment>.*)", "/status/${segment}")
                        .addRequestHeader("X-Forwarded-By", "edge-service-gateway")
                        .addResponseHeader("X-Powered-By", "Spring-Cloud-Gateway")
                    )
                    .uri("https://httpbin.org")
                )
                // Route: /get → https://httpbin.org/get  (echo endpoint for testing)
                .route("programmatic_get_route", r -> r
                    .path("/get")
                    .filters(f -> f
                        .addRequestHeader("X-Gateway-Version", "1.0")
                    )
                    .uri("https://httpbin.org")
                )
                .build();
    }
}
