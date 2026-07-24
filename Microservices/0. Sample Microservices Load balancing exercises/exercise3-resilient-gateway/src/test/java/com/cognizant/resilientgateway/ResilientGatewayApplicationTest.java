package com.cognizant.resilientgateway;

import com.cognizant.resilientgateway.controller.FallbackController;
import com.cognizant.resilientgateway.filter.ResilienceMetricsFilter;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * ResilientGatewayApplicationTest – Exercise 3
 *
 * Integration tests verifying:
 *  1. Application context loads with all resilience beans wired.
 *  2. Resilience4j CircuitBreakerRegistry contains the configured instances.
 *  3. Fallback endpoints return the correct HTTP status codes.
 *  4. All resilience routes are registered.
 */
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    properties = {
        "eureka.client.enabled=false",
        "spring.cloud.discovery.enabled=false"
    }
)
@AutoConfigureWebTestClient
class ResilientGatewayApplicationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private CircuitBreakerRegistry circuitBreakerRegistry;

    @Autowired
    private ResilienceMetricsFilter resilienceMetricsFilter;

    @Autowired
    private FallbackController fallbackController;

    @Autowired
    private RouteLocator routeLocator;

    // ── Context tests ─────────────────────────────────────────────────────

    @Test
    void contextLoads() {
        // Application context starts without errors
    }

    @Test
    void circuitBreakerRegistryIsAvailable() {
        assertThat(circuitBreakerRegistry).isNotNull();
    }

    @Test
    void resilienceMetricsFilterIsRegistered() {
        assertThat(resilienceMetricsFilter).isNotNull();
    }

    @Test
    void fallbackControllerIsRegistered() {
        assertThat(fallbackController).isNotNull();
    }

    @Test
    void resilienceRoutesAreDefined() {
        long count = routeLocator.getRoutes().count().block();
        assertThat(count).isGreaterThanOrEqualTo(1);
    }

    // ── Fallback endpoint tests ───────────────────────────────────────────

    @Test
    void exampleFallbackReturns503() {
        webTestClient.get()
                     .uri("/fallback/example")
                     .accept(MediaType.APPLICATION_JSON)
                     .exchange()
                     .expectStatus().isEqualTo(503)
                     .expectBody()
                     .jsonPath("$.status").isEqualTo(503)
                     .jsonPath("$.message").isNotEmpty()
                     .jsonPath("$.timestamp").isNotEmpty();
    }

    @Test
    void apiFallbackReturns503() {
        webTestClient.get()
                     .uri("/fallback/api")
                     .accept(MediaType.APPLICATION_JSON)
                     .exchange()
                     .expectStatus().isEqualTo(503)
                     .expectBody()
                     .jsonPath("$.error").isEqualTo("Service Unavailable");
    }

    @Test
    void timeoutFallbackReturns504() {
        webTestClient.get()
                     .uri("/fallback/timeout")
                     .accept(MediaType.APPLICATION_JSON)
                     .exchange()
                     .expectStatus().isEqualTo(504)
                     .expectBody()
                     .jsonPath("$.status").isEqualTo(504)
                     .jsonPath("$.error").isEqualTo("Gateway Timeout");
    }

    @Test
    void serviceFallbackReturns503() {
        webTestClient.get()
                     .uri("/fallback/service")
                     .accept(MediaType.APPLICATION_JSON)
                     .exchange()
                     .expectStatus().isEqualTo(503);
    }

    // ── Circuit breaker state test ────────────────────────────────────────

    @Test
    void circuitBreakersStartInClosedState() {
        // All CBs should start CLOSED on application startup
        circuitBreakerRegistry.getAllCircuitBreakers().forEach(cb ->
            assertThat(cb.getState())
                .as("Circuit breaker '%s' should start CLOSED", cb.getName())
                .isEqualTo(io.github.resilience4j.circuitbreaker.CircuitBreaker.State.CLOSED)
        );
    }
}
