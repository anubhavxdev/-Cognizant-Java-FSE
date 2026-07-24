package com.cognizant.lbgateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * LoadBalancingFilter – Exercise 2
 *
 * A {@link GlobalFilter} that logs which downstream instance was chosen by the
 * Spring Cloud LoadBalancer for each request.
 *
 * Spring Cloud Gateway stores the chosen {@link ServiceInstance} and the
 * load-balanced URI in the {@link ServerWebExchange} attributes after the
 * ReactiveLoadBalancerClientFilter resolves it. This filter reads those
 * attributes to provide observability into load-balancing decisions.
 *
 * Attributes inspected:
 *  • {@link ServerWebExchangeUtils#GATEWAY_REQUEST_URL_ATTR}  – resolved URL
 *  • {@link ServerWebExchangeUtils#GATEWAY_ORIGINAL_REQUEST_URL_ATTR} – original lb:// URL
 *
 * Note: This filter runs AFTER the ReactiveLoadBalancerClientFilter
 * (order = {@code LOWEST_PRECEDENCE - 1}) so the instance attributes are
 * already populated when our code reads them.
 */
@Component
public class LoadBalancingFilter implements GlobalFilter, Ordered {

    private static final Logger log = LoggerFactory.getLogger(LoadBalancingFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return chain.filter(exchange)
                    .then(Mono.fromRunnable(() -> logLoadBalancingDecision(exchange)));
    }

    private void logLoadBalancingDecision(ServerWebExchange exchange) {
        // The resolved (actual) URL chosen by the load balancer
        URI resolvedUrl = exchange.getAttribute(
            ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR);

        // The original lb:// URL before resolution
        Set<URI> originalUrls = exchange.getAttribute(
            ServerWebExchangeUtils.GATEWAY_ORIGINAL_REQUEST_URL_ATTR);

        if (resolvedUrl != null && originalUrls != null && !originalUrls.isEmpty()) {
            URI originalUrl = originalUrls.iterator().next();
            log.info("[LB] {} → {} | status={}",
                     originalUrl,
                     resolvedUrl,
                     exchange.getResponse().getStatusCode());
        }
    }

    @Override
    public int getOrder() {
        // Run at very low priority so the LB filter has already resolved the URI
        return Ordered.LOWEST_PRECEDENCE - 1;
    }
}
