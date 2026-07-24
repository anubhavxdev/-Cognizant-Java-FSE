package com.cognizant.resilientgateway.filter;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * ResilienceMetricsFilter – Exercise 3
 *
 * A {@link GlobalFilter} that logs the current state of registered Resilience4j
 * circuit breakers on every request, providing real-time observability into the
 * health of downstream services.
 *
 * In production, these metrics are typically scraped by Prometheus via
 * {@code /actuator/prometheus} and visualised in Grafana dashboards.
 *
 * Logged information per circuit breaker:
 *  - State (CLOSED / OPEN / HALF_OPEN)
 *  - Current failure rate percentage
 *  - Number of buffered calls
 *  - Number of failed calls
 *  - Number of slow calls
 */
@Component
public class ResilienceMetricsFilter implements GlobalFilter, Ordered {

    private static final Logger log = LoggerFactory.getLogger(ResilienceMetricsFilter.class);

    private final CircuitBreakerRegistry circuitBreakerRegistry;

    public ResilienceMetricsFilter(CircuitBreakerRegistry circuitBreakerRegistry) {
        this.circuitBreakerRegistry = circuitBreakerRegistry;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return chain.filter(exchange)
                    .then(Mono.fromRunnable(this::logCircuitBreakerStates));
    }

    private void logCircuitBreakerStates() {
        circuitBreakerRegistry.getAllCircuitBreakers().forEach(cb -> {
            CircuitBreaker.Metrics metrics = cb.getMetrics();
            log.debug("[CB-STATE] name={} | state={} | failureRate={}% | "
                      + "bufferedCalls={} | failedCalls={} | slowCalls={}",
                      cb.getName(),
                      cb.getState(),
                      String.format("%.1f", metrics.getFailureRate()),
                      metrics.getNumberOfBufferedCalls(),
                      metrics.getNumberOfFailedCalls(),
                      metrics.getNumberOfSlowCalls());
        });
    }

    @Override
    public int getOrder() {
        // Run after all other filters so response status is already set
        return Ordered.LOWEST_PRECEDENCE;
    }
}
