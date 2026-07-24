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

import java.util.UUID;

/**
 * RequestIdFilter – Exercise 1
 *
 * A {@link GlobalFilter} that assigns a unique correlation/request-ID to every
 * inbound request and propagates it both downstream (as a request header) and
 * back to the caller (as a response header).
 *
 * This is an industry-standard practice for distributed tracing and log
 * correlation across microservices. When combined with the {@link LoggingFilter}
 * the same ID appears in every service's logs.
 *
 * Header name: {@code X-Request-Id}
 *
 * If the caller already provides an {@code X-Request-Id} header the existing
 * value is preserved (pass-through) instead of generating a new one, which
 * supports end-to-end tracing from the client through the gateway to services.
 */
@Component
public class RequestIdFilter implements GlobalFilter, Ordered {

    private static final Logger log           = LoggerFactory.getLogger(RequestIdFilter.class);
    private static final String REQUEST_ID_HDR = "X-Request-Id";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        ServerHttpRequest request   = exchange.getRequest();
        String            requestId = request.getHeaders().getFirst(REQUEST_ID_HDR);

        // Reuse existing ID if provided by upstream; otherwise generate a new one
        if (requestId == null || requestId.isBlank()) {
            requestId = UUID.randomUUID().toString();
        }

        final String finalRequestId = requestId;

        // Mutate request to add the X-Request-Id header for downstream services
        ServerHttpRequest mutatedRequest = request.mutate()
                                                  .header(REQUEST_ID_HDR, finalRequestId)
                                                  .build();

        ServerWebExchange mutatedExchange = exchange.mutate()
                                                    .request(mutatedRequest)
                                                    .build();

        log.debug("[GATEWAY] Assigned request-id={}", finalRequestId);

        return chain.filter(mutatedExchange)
                    .then(Mono.fromRunnable(() ->
                        // Propagate request-id in the response so clients can correlate
                        exchange.getResponse()
                                .getHeaders()
                                .add(REQUEST_ID_HDR, finalRequestId)
                    ));
    }

    @Override
    public int getOrder() {
        // Run before LoggingFilter so request-id is already set when LoggingFilter logs
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
