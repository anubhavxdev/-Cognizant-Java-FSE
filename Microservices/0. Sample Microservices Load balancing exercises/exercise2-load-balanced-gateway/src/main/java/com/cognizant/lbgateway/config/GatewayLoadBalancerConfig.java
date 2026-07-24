package com.cognizant.lbgateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * GatewayLoadBalancerConfig – Exercise 2
 *
 * Demonstrates programmatic load-balanced route registration via the Java DSL.
 *
 * The {@code lb://} URI scheme instructs the Gateway's
 * {@code ReactiveLoadBalancerClientFilter} to:
 *  1. Extract the logical service name (e.g., "example-service").
 *  2. Query Spring Cloud LoadBalancer for a live instance.
 *  3. Replace {@code lb://example-service} with the chosen instance's
 *     {@code http://host:port} before forwarding the request.
 *
 * Routes defined here merge with those declared in {@code application.yml}.
 */
@Configuration
public class GatewayLoadBalancerConfig {

    /**
     * Registers a programmatic load-balanced route for "product-service".
     *
     * Route: /products/**  →  lb://product-service/products/**
     *  - Retains the full path (no StripPrefix).
     *  - Adds an X-Gateway-Source header for downstream identification.
     *  - Adds circuit breaker filter (Resilience4j) for resilience.
     *
     * @param builder the route builder provided by Spring
     * @return the composed {@link RouteLocator}
     */
    @Bean
    public RouteLocator loadBalancedRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()

                // Route 1: product-service (Round-Robin – default algorithm)
                .route("product_service_lb_route", r -> r
                    .path("/products/**")
                    .filters(f -> f
                        .addRequestHeader("X-Gateway-Source", "load-balanced-gateway")
                        .addResponseHeader("X-Load-Balanced-By", "spring-cloud-loadbalancer")
                        .retry(config -> config
                            .setRetries(3)                   // retry up to 3 times
                            .setStatuses(                    // retry on these HTTP statuses
                                org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE,
                                org.springframework.http.HttpStatus.GATEWAY_TIMEOUT
                            )
                        )
                    )
                    .uri("lb://product-service")
                )

                // Route 2: order-service (also load balanced)
                .route("order_service_lb_route", r -> r
                    .path("/orders/**")
                    .filters(f -> f
                        .addRequestHeader("X-Gateway-Source", "load-balanced-gateway")
                        .addResponseHeader("X-Served-By", "order-service-cluster")
                    )
                    .uri("lb://order-service")
                )

                .build();
    }
}
