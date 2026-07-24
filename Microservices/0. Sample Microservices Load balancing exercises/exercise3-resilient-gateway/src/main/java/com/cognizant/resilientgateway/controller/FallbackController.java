package com.cognizant.resilientgateway.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.Map;

/**
 * FallbackController – Exercise 3
 *
 * Handles fallback responses when a Circuit Breaker trips or a route times out.
 *
 * Spring Cloud Gateway's CircuitBreaker filter redirects to a fallback URI
 * (e.g., {@code forward:/fallback/example}) using an internal forward.
 * This controller serves those forward-redirected requests.
 *
 * Each fallback endpoint:
 *  - Returns HTTP 503 (Service Unavailable) to accurately signal the client.
 *  - Includes structured JSON with a message, timestamp, and circuit-breaker name.
 *  - Logs the fallback invocation for observability.
 *
 * The Circuit Breaker filter stores metadata in the exchange attributes
 * ({@code circuitBreakerName}, {@code gatewayOriginalRequestUrl}) which can
 * be used to enrich fallback responses.
 */
@RestController
@RequestMapping("/fallback")
public class FallbackController {

    private static final Logger log = LoggerFactory.getLogger(FallbackController.class);

    /**
     * Generic fallback for the "exampleCircuitBreaker" route.
     *
     * Forwarded to when: /example/** circuit breaker trips.
     */
    @GetMapping("/example")
    public Mono<Map<String, Object>> exampleFallback(ServerWebExchange exchange) {
        log.warn("[FALLBACK] example-service is unavailable. Returning fallback response.");
        exchange.getResponse().setStatusCode(HttpStatus.SERVICE_UNAVAILABLE);
        return Mono.just(Map.of(
            "status",          503,
            "error",           "Service Unavailable",
            "message",         "example-service is currently unavailable. Please retry later.",
            "circuitBreaker",  "exampleCircuitBreaker",
            "timestamp",       Instant.now().toString()
        ));
    }

    /**
     * Fallback for the API service circuit breaker.
     *
     * Forwarded to when: /api/** circuit breaker trips.
     */
    @GetMapping("/api")
    public Mono<Map<String, Object>> apiFallback(ServerWebExchange exchange) {
        log.warn("[FALLBACK] api-service circuit breaker is OPEN.");
        exchange.getResponse().setStatusCode(HttpStatus.SERVICE_UNAVAILABLE);
        return Mono.just(Map.of(
            "status",    503,
            "error",     "Service Unavailable",
            "message",   "API service is temporarily unavailable. The circuit breaker is open.",
            "retryAfter", "10 seconds",
            "timestamp", Instant.now().toString()
        ));
    }

    /**
     * Fallback for timeout scenarios.
     *
     * Forwarded to when: /slow/** times out.
     */
    @GetMapping("/timeout")
    public Mono<Map<String, Object>> timeoutFallback(ServerWebExchange exchange) {
        log.warn("[FALLBACK] Request to slow-service timed out.");
        exchange.getResponse().setStatusCode(HttpStatus.GATEWAY_TIMEOUT);
        return Mono.just(Map.of(
            "status",    504,
            "error",     "Gateway Timeout",
            "message",   "The upstream service did not respond in time.",
            "timestamp", Instant.now().toString()
        ));
    }

    /**
     * Fallback for the programmatic service route.
     *
     * Forwarded to when: /service/** circuit breaker trips.
     */
    @GetMapping("/service")
    public Mono<Map<String, Object>> serviceFallback(ServerWebExchange exchange) {
        log.warn("[FALLBACK] backend-service circuit breaker tripped.");
        exchange.getResponse().setStatusCode(HttpStatus.SERVICE_UNAVAILABLE);
        return Mono.just(Map.of(
            "status",    503,
            "error",     "Service Unavailable",
            "message",   "backend-service is down. Please try again later.",
            "timestamp", Instant.now().toString()
        ));
    }
}
