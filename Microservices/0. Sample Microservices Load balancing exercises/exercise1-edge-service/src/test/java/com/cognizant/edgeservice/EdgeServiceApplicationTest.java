package com.cognizant.edgeservice;

import com.cognizant.edgeservice.filter.LoggingFilter;
import com.cognizant.edgeservice.filter.RequestIdFilter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * EdgeServiceApplicationTest – Exercise 1
 *
 * Integration tests verifying:
 *  1. Application context loads correctly (all beans wired).
 *  2. LoggingFilter bean is registered.
 *  3. RequestIdFilter bean is registered.
 *  4. At least one route is configured.
 *
 * NOTE: Actual HTTP routing tests (calling example.org / httpbin.org)
 * are not run in CI because they require live external endpoints.
 * For those, use the WireMock-based test pattern shown in the comment below.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class EdgeServiceApplicationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private LoggingFilter loggingFilter;

    @Autowired
    private RequestIdFilter requestIdFilter;

    @Autowired
    private RouteLocator routeLocator;

    // ── Context load test ────────────────────────────────────────────────

    @Test
    void contextLoads() {
        // Passes if the Spring application context starts without errors
    }

    @Test
    void loggingFilterBeanIsRegistered() {
        assertThat(loggingFilter).isNotNull();
    }

    @Test
    void requestIdFilterBeanIsRegistered() {
        assertThat(requestIdFilter).isNotNull();
    }

    @Test
    void atLeastOneRouteIsDefined() {
        long count = routeLocator.getRoutes().count().block();
        assertThat(count).isGreaterThan(0);
    }

    // ── Filter ordering test ─────────────────────────────────────────────

    @Test
    void requestIdFilterRunsBeforeLoggingFilter() {
        assertThat(requestIdFilter.getOrder())
            .isLessThan(loggingFilter.getOrder());
    }

    /*
     * ── How to test actual routing (WireMock example) ───────────────────
     *
     * Add wiremock-spring-boot to your pom.xml test scope, then:
     *
     * @WireMockTest(httpPort = 9090)
     * @Test
     * void routeForwardsToDownstream(WireMockRuntimeInfo wmRuntimeInfo) {
     *     stubFor(get("/").willReturn(ok("Hello from downstream")));
     *
     *     WebTestClient client = WebTestClient.bindToServer()
     *         .baseUrl("http://localhost:" + port)
     *         .build();
     *
     *     client.get().uri("/example/")
     *           .exchange()
     *           .expectStatus().isOk();
     * }
     */
}
