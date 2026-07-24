package com.cognizant.lbgateway.config;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.loadbalancer.core.RandomLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ReactorLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

/**
 * LoadBalancerConfiguration – Exercise 2
 *
 * Replaces the default Round-Robin load balancer with a {@link RandomLoadBalancer}
 * for the "example-service" client.
 *
 * IMPORTANT ANNOTATIONS NOTE:
 * ─────────────────────────────
 * This class must NOT be annotated with {@code @Configuration} at the class level.
 * It should be registered via {@code @LoadBalancerClient} on the main application
 * class or a dedicated {@code @Configuration} class (see {@link LoadBalancerClientConfig}).
 *
 * Annotating this class with {@code @Configuration} would place it in the global
 * application context and apply the RandomLoadBalancer to ALL service clients,
 * not just "example-service". This is a common Spring Cloud LoadBalancer pitfall.
 *
 * Reference: https://docs.spring.io/spring-cloud-commons/docs/current/reference/html/
 *            #spring-cloud-loadbalancer
 *
 * How it works:
 *  - {@link LoadBalancerClientFactory} creates a child application context per
 *    service client name.
 *  - {@link ReactorLoadBalancer} is queried inside each child context to pick
 *    one {@link ServiceInstance} from the available list.
 *  - {@link ServiceInstanceListSupplier} provides the instance list from Eureka
 *    (or the static fallback list).
 */
public class LoadBalancerConfiguration {

    /**
     * Declares a {@link RandomLoadBalancer} for the service whose name is
     * bound from the {@link LoadBalancerClientFactory#PROPERTY_NAME} environment
     * property of the child context.
     *
     * @param environment              child context environment (provides service name)
     * @param loadBalancerClientFactory factory to retrieve the lazy instance list supplier
     * @return a {@link ReactorLoadBalancer} that picks instances randomly
     */
    @Bean
    public ReactorLoadBalancer<ServiceInstance> randomLoadBalancer(
            Environment environment,
            LoadBalancerClientFactory loadBalancerClientFactory) {

        String name = environment.getProperty(LoadBalancerClientFactory.PROPERTY_NAME);

        return new RandomLoadBalancer(
            loadBalancerClientFactory.getLazyProvider(name, ServiceInstanceListSupplier.class),
            name
        );
    }
}
