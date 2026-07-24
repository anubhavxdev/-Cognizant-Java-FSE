package com.cognizant.paymentservice.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Random;

/**
 * Simulates a third-party payment gateway that may experience slowness or failures.
 */
@Component
public class ThirdPartyPaymentClient {

    private static final Logger log = LoggerFactory.getLogger(ThirdPartyPaymentClient.class);
    private final Random random = new Random();

    public String processPayment(Long orderId, BigDecimal amount, boolean simulateFailure, boolean simulateDelay) {
        log.info("Contacting external payment gateway for orderId={}, amount={}", orderId, amount);

        if (simulateDelay) {
            try {
                log.warn("External gateway experiencing high latency (delaying 4000ms)...");
                Thread.sleep(4000); // Exceeds slow call duration threshold
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        if (simulateFailure) {
            log.error("External payment gateway call failed with HTTP 503 Service Unavailable");
            throw new RuntimeException("External Payment Gateway API Failure");
        }

        return "TXN-" + System.currentTimeMillis() + "-" + random.nextInt(1000);
    }
}
