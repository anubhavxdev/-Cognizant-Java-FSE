package com.cognizant.resilientgateway.config;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

/**
 * ResilienceConfiguration – Exercise 3
 *
 * Provides {@link Customizer} beans that configure the
 * {@link ReactiveResilience4JCircuitBreakerFactory} programmatically.
 *
 * Spring Boot 3 note:
 *  The original exercise used {@code ReactiveResilience4JCircuitBreakerFactory}
 *  from {@code io.github.resilience4j:resilience4j-spring-boot2}. In Spring Boot 3
 *  this class comes from {@code spring-cloud-starter-circuitbreaker-reactor-resilience4j}
 *  and the import package is {@code org.springframework.cloud.circuitbreaker.resilience4j.*}.
 *
 * Two customizers are defined:
 *  1. {@code defaultCustomizer}  – applies to ALL circuit breakers not otherwise configured.
 *  2. {@code strictCustomizer}   – applies only to "exampleCircuitBreaker" with tighter settings.
 *
 * Precedence: named customizers take precedence over the default.
 *
 * Note: Properties in {@code application.yml} under {@code resilience4j.*} can also
 * configure circuit breakers declaratively. Bean-based configuration here OVERRIDES
 * the YAML-based configuration for the same circuit breaker ID.
 */
@Configuration
public class ResilienceConfiguration {

    /**
     * Default circuit breaker configuration applied to ALL instances
     * that have no specific customizer registered.
     *
     * Settings:
     *  - 50% failure rate threshold (open circuit on ≥ 50% failures)
     *  - 3-second time limiter (cancel if no response within 3s)
     *  - 10-call sliding window
     *  - Wait 10 seconds in OPEN state before transitioning to HALF_OPEN
     */
    @Bean
    public Customizer<ReactiveResilience4JCircuitBreakerFactory> defaultCustomizer() {
        return factory -> factory.configureDefault(id ->
            new Resilience4JConfigBuilder(id)
                .circuitBreakerConfig(
                    CircuitBreakerConfig.custom()
                        .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
                        .slidingWindowSize(10)
                        .failureRateThreshold(50)
                        .waitDurationInOpenState(Duration.ofSeconds(10))
                        .permittedNumberOfCallsInHalfOpenState(3)
                        .automaticTransitionFromOpenToHalfOpenEnabled(true)
                        .minimumNumberOfCalls(5)
                        .build()
                )
                .timeLimiterConfig(
                    TimeLimiterConfig.custom()
                        .timeoutDuration(Duration.ofSeconds(3))
                        .cancelRunningFuture(true)
                        .build()
                )
                .build()
        );
    }

    /**
     * Named customizer for "exampleCircuitBreaker" with stricter settings.
     *
     * Used by the route defined in application.yml and GatewayResilienceConfig:
     *   {@code CircuitBreaker filter → name: exampleCircuitBreaker}
     *
     * Settings (stricter than default):
     *  - 40% failure rate threshold
     *  - 2-second time limiter (faster fail)
     *  - 20-call sliding window
     *  - 15-second wait in OPEN state
     */
    @Bean
    public Customizer<ReactiveResilience4JCircuitBreakerFactory> exampleCircuitBreakerCustomizer() {
        return factory -> factory.configure(builder ->
            builder
                .circuitBreakerConfig(
                    CircuitBreakerConfig.custom()
                        .slidingWindowSize(20)
                        .failureRateThreshold(40)
                        .waitDurationInOpenState(Duration.ofSeconds(15))
                        .permittedNumberOfCallsInHalfOpenState(5)
                        .minimumNumberOfCalls(10)
                        .slowCallRateThreshold(50)
                        .slowCallDurationThreshold(Duration.ofSeconds(2))
                        .build()
                )
                .timeLimiterConfig(
                    TimeLimiterConfig.custom()
                        .timeoutDuration(Duration.ofSeconds(2))
                        .cancelRunningFuture(true)
                        .build()
                ),
            "exampleCircuitBreaker"  // applied only to this named circuit breaker
        );
    }

    /**
     * Named customizer for "timeoutCircuitBreaker" with very short timeout.
     * Demonstrates how to configure a time-sensitive circuit breaker separately.
     */
    @Bean
    public Customizer<ReactiveResilience4JCircuitBreakerFactory> timeoutCircuitBreakerCustomizer() {
        return factory -> factory.configure(builder ->
            builder
                .circuitBreakerConfig(CircuitBreakerConfig.ofDefaults())
                .timeLimiterConfig(
                    TimeLimiterConfig.custom()
                        .timeoutDuration(Duration.ofMillis(500))
                        .build()
                ),
            "timeoutCircuitBreaker"
        );
    }
}
