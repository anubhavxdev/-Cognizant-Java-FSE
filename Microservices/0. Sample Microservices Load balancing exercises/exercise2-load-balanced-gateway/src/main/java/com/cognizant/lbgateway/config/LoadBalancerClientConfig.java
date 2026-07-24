package com.cognizant.lbgateway.config;

import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClients;
import org.springframework.context.annotation.Configuration;

/**
 * LoadBalancerClientConfig – Exercise 2
 *
 * Registers the custom {@link LoadBalancerConfiguration} for specific service
 * clients using {@link LoadBalancerClients}.
 *
 * This is the correct way to scope a custom load balancer:
 *  - "example-service" → uses RandomLoadBalancer (via LoadBalancerConfiguration)
 *  - "api-service"     → uses the default RoundRobinLoadBalancer
 *
 * The {@link LoadBalancerClient#configuration()} attribute points to the class
 * that defines the beans for that specific client's child application context.
 */
@Configuration
@LoadBalancerClients({
    @LoadBalancerClient(
        name          = "example-service",
        configuration = LoadBalancerConfiguration.class
    )
    // Add more clients here if needed:
    // @LoadBalancerClient(name = "another-service", configuration = AnotherLBConfig.class)
})
public class LoadBalancerClientConfig {
    // Intentionally empty – configuration is declared via annotations above
}
