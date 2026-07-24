package com.cognizant.paymentservice.config;

import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.circuitbreaker.event.CircuitBreakerEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;

@Configuration
public class ResilienceMonitoringConfig {

    private static final Logger log = LoggerFactory.getLogger(ResilienceMonitoringConfig.class);

    private final CircuitBreakerRegistry circuitBreakerRegistry;

    public ResilienceMonitoringConfig(CircuitBreakerRegistry circuitBreakerRegistry) {
        this.circuitBreakerRegistry = circuitBreakerRegistry;
    }

    /**
     * Registers event listeners on all Resilience4j circuit breakers to monitor
     * state changes, errors, and fallback events.
     */
    @PostConstruct
    public void registerEventListeners() {
        circuitBreakerRegistry.getAllCircuitBreakers().forEach(cb -> {
            cb.getEventPublisher()
              .onStateTransition(event ->
                  log.warn("[CB-MONITOR] Circuit Breaker '{}' transitioned from {} to {}",
                      event.getCircuitBreakerName(),
                      event.getStateTransition().getFromState(),
                      event.getStateTransition().getToState()))
              .onError(event ->
                  log.error("[CB-MONITOR] Circuit Breaker '{}' recorded an ERROR (duration={}ms): {}",
                      event.getCircuitBreakerName(),
                      event.getElapsedDuration().toMillis(),
                      event.getThrowable().getMessage()))
              .onSuccess(event ->
                  log.info("[CB-MONITOR] Circuit Breaker '{}' recorded a SUCCESS (duration={}ms)",
                      event.getCircuitBreakerName(),
                      event.getElapsedDuration().toMillis()))
              .onCallNotPermitted(event ->
                  log.error("[CB-MONITOR] Circuit Breaker '{}' REJECTED CALL because circuit is OPEN",
                      event.getCircuitBreakerName()));
        });
    }
}
