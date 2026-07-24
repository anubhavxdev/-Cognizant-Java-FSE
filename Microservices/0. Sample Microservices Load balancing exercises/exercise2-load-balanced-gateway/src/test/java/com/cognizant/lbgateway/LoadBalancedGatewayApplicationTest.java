package com.cognizant.lbgateway;

import com.cognizant.lbgateway.filter.LoadBalancingFilter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.gateway.route.RouteLocator;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * LoadBalancedGatewayApplicationTest – Exercise 2
 *
 * Integration tests verifying that the application context loads with all
 * load-balancing components correctly wired.
 *
 * Note on Eureka: The application.yml is configured with fetchRegistry=true,
 * which means the app attempts to connect to Eureka on startup. In a test
 * environment with no Eureka server we need to disable the Eureka client.
 * This is done via the properties below.
 */
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    properties = {
        // Disable Eureka discovery in tests (no Eureka server in CI)
        "eureka.client.enabled=false",
        "spring.cloud.discovery.enabled=false",
        "spring.cloud.loadbalancer.ribbon.enabled=false"
    }
)
class LoadBalancedGatewayApplicationTest {

    @Autowired
    private LoadBalancingFilter loadBalancingFilter;

    @Autowired
    private RouteLocator routeLocator;

    // ── Context load ──────────────────────────────────────────────────────

    @Test
    void contextLoads() {
        // Passes if the Spring application context starts without errors
    }

    @Test
    void loadBalancingFilterBeanIsRegistered() {
        assertThat(loadBalancingFilter).isNotNull();
    }

    @Test
    void loadBalancingFilterRunsAtLowestPriority() {
        assertThat(loadBalancingFilter.getOrder())
            .isGreaterThan(0); // LOWEST_PRECEDENCE - 1 is a large positive number
    }

    @Test
    void multipleLoadBalancedRoutesAreDefined() {
        long count = routeLocator.getRoutes().count().block();
        // We have routes from both application.yml and GatewayLoadBalancerConfig
        assertThat(count).isGreaterThanOrEqualTo(2);
    }
}
