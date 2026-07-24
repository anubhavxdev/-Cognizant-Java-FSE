package com.cognizant.orderservice.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Map;

/**
 * UserServiceClient – Exercise 1
 *
 * Reactive HTTP client that communicates with the User Service using
 * Spring WebFlux's {@link WebClient}.
 *
 * WebClient is the modern, non-blocking, reactive alternative to RestTemplate.
 * It is the recommended approach in Spring Boot 3 for service-to-service calls.
 *
 * This client is used by the Order Service to:
 *  1. Verify a user exists before creating an order.
 *  2. Enrich order responses with user details.
 */
@Component
public class UserServiceClient {

    private static final Logger log = LoggerFactory.getLogger(UserServiceClient.class);

    private final WebClient webClient;

    public UserServiceClient(@Value("${services.user-service.url}") String userServiceUrl) {
        this.webClient = WebClient.builder()
                                  .baseUrl(userServiceUrl)
                                  .build();
    }

    /**
     * Checks whether the given userId exists in the User Service.
     *
     * The call is synchronous (block()) here for simplicity in a servlet context.
     * In a fully reactive application, return {@code Mono<Boolean>} instead.
     *
     * @param userId the user ID to validate
     * @return {@code true} if the user exists; {@code false} on 404
     */
    public boolean userExists(Long userId) {
        try {
            webClient.get()
                     .uri("/api/users/{id}", userId)
                     .retrieve()
                     .bodyToMono(Map.class)
                     .block();  // blocking call; acceptable in servlet context
            return true;
        } catch (WebClientResponseException.NotFound e) {
            log.warn("User not found with id={}", userId);
            return false;
        } catch (Exception e) {
            log.error("Error calling User Service for userId={}: {}", userId, e.getMessage());
            // Fail closed: if user service is unreachable, reject the order
            throw new RuntimeException("User Service is unavailable. Please try later.", e);
        }
    }

    /**
     * Fetches user details from the User Service.
     *
     * @param userId the user ID
     * @return a Map containing user fields (id, name, email, phone)
     */
    public Map<String, Object> getUserDetails(Long userId) {
        return webClient.get()
                        .uri("/api/users/{id}", userId)
                        .retrieve()
                        .bodyToMono(Map.class)
                        .doOnSuccess(u -> log.debug("Fetched user details for id={}", userId))
                        .doOnError(e  -> log.error("Failed to fetch user id={}: {}", userId, e.getMessage()))
                        .block();
    }
}
