package com.cognizant.lbgateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Exercise 2 – Load Balancing in an API Gateway
 *
 * Demonstrates client-side load balancing integrated with Spring Cloud Gateway.
 *
 * How it works:
 *  1. Gateway receives a request matching a route with uri=lb://service-name.
 *  2. Spring Cloud LoadBalancer resolves the service-name to a list of instances
 *     (via Eureka or a static configuration).
 *  3. The selected load-balancing algorithm (Round-Robin by default, or Random
 *     via the custom {@link com.cognizant.lbgateway.config.LoadBalancerConfiguration})
 *     picks one instance.
 *  4. The Gateway forwards the request to the chosen instance.
 *
 * Components:
 *  ┌──────────────────────────────────────────────────────────────────────┐
 *  │  application.yml            – lb:// route definitions               │
 *  │  LoadBalancerConfiguration  – RandomLoadBalancer per-client config  │
 *  │  GatewayLoadBalancerConfig  – programmatic route registration       │
 *  │  LoadBalancingFilter        – GlobalFilter: logs chosen instance    │
 *  └──────────────────────────────────────────────────────────────────────┘
 */
@SpringBootApplication
public class LoadBalancedGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(LoadBalancedGatewayApplication.class, args);
    }
}
