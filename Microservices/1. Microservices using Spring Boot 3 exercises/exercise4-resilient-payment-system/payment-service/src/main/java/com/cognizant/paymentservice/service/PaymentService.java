package com.cognizant.paymentservice.service;

import com.cognizant.paymentservice.client.ThirdPartyPaymentClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Service
public class PaymentService {

    private static final Logger log = LoggerFactory.getLogger(PaymentService.class);
    private final ThirdPartyPaymentClient thirdPartyClient;

    public PaymentService(ThirdPartyPaymentClient thirdPartyClient) {
        this.thirdPartyClient = thirdPartyClient;
    }

    /**
     * Executes payment processing wrapped in a Resilience4j Circuit Breaker.
     * If the circuit breaker is OPEN or an exception occurs, fallbackProcessPayment is invoked.
     */
    @CircuitBreaker(name = "paymentGateway", fallbackMethod = "fallbackProcessPayment")
    public Map<String, Object> executePayment(Long orderId, BigDecimal amount, boolean simulateFailure, boolean simulateDelay) {
        String txnId = thirdPartyClient.processPayment(orderId, amount, simulateFailure, simulateDelay);
        log.info("Payment successful via external gateway. TxnID={}", txnId);

        return Map.of(
            "status", "SUCCESS",
            "transactionId", txnId,
            "orderId", orderId,
            "amount", amount,
            "timestamp", LocalDateTime.now().toString(),
            "source", "EXTERNAL_PAYMENT_GATEWAY"
        );
    }

    /**
     * Fallback method executed when Circuit Breaker is OPEN or when downstream call fails/times out.
     * Signature MUST match target method + Throwable parameter at the end.
     */
    public Map<String, Object> fallbackProcessPayment(Long orderId, BigDecimal amount, boolean simulateFailure, boolean simulateDelay, Throwable t) {
        log.error(">>> FALLBACK TRIGGERED for orderId={}. Cause: {} - {}", orderId, t.getClass().getSimpleName(), t.getMessage());

        return Map.of(
            "status", "QUEUED_FOR_RETRY",
            "message", "Payment gateway is currently experiencing issues. Your payment request has been logged safely.",
            "orderId", orderId,
            "amount", amount,
            "fallbackTriggered", true,
            "errorType", t.getClass().getSimpleName(),
            "errorMessage", t.getMessage() != null ? t.getMessage() : "Call timed out or circuit breaker OPEN",
            "timestamp", LocalDateTime.now().toString()
        );
    }
}
