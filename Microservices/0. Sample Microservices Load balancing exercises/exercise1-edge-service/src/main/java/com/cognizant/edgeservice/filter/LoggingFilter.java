package com.cognizant.edgeservice.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.Instant;

/**
 * LoggingFilter – Exercise 1
 *
 * A {@link GlobalFilter} that intercepts every request passing through the
 * Spring Cloud Gateway and logs:
 *  • Request URI and HTTP method
 *  • Remote client address
 *  • Request timestamp
 *  • Response status code (logged post-filter via {@code then()})
 *  • Elapsed time in milliseconds
 *
 * GlobalFilter vs GatewayFilter:
 *  • {@link GlobalFilter} applies to ALL routes automatically.
 *  • A named {@code GatewayFilter} must be explicitly listed in a route's
 *    {@code filters} section in application.yml.
 *
 * Ordering: this filter runs at {@code Ordered.HIGHEST_PRECEDENCE + 1}
 * (just after built-in Gateway infrastructure filters), ensuring the URI
 * is fully resolved before logging.
 *
 * Reactive note:
 *  {@code chain.filter(exchange)} returns a {@code Mono<Void>} that
 *  represents the downstream processing. By calling {@code .then()} on it
 *  we can execute post-processing (response logging) after the response
 *  has been written to the client — without blocking the event loop.
 */
@Component
public class LoggingFilter implements GlobalFilter, Ordered {

    private static final Logger log = LoggerFactory.getLogger(LoggingFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        ServerHttpRequest request   = exchange.getRequest();
        long              startTime = Instant.now().toEpochMilli();

        // ── PRE-filter: log incoming request ──────────────────────────────
        log.info("[GATEWAY] ▶ {} {} | client={} | id={}",
                 request.getMethod(),
                 request.getURI(),
                 request.getRemoteAddress(),
                 request.getId());

        // ── Forward to downstream service ─────────────────────────────────
        return chain.filter(exchange)
                    // ── POST-filter: runs after response is written ────────
                    .then(Mono.fromRunnable(() -> {
                        long elapsed = Instant.now().toEpochMilli() - startTime;
                        log.info("[GATEWAY] ◀ {} {} | status={} | elapsed={}ms",
                                 request.getMethod(),
                                 request.getURI(),
                                 exchange.getResponse().getStatusCode(),
                                 elapsed);
                    }));
    }

    /**
     * Filter execution order.
     * Lower value = higher priority.
     * {@code HIGHEST_PRECEDENCE} = {@link Integer#MIN_VALUE}.
     */
    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 1;
    }
}
