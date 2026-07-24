package com.cognizant.paymentservice.controller;

import com.cognizant.paymentservice.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    /**
     * Endpoint to process a payment.
     * Query params allow testing failure and latency modes:
     * - simulateFailure=true
     * - simulateDelay=true
     */
    @PostMapping("/process")
    public ResponseEntity<Map<String, Object>> processPayment(
            @RequestParam Long orderId,
            @RequestParam BigDecimal amount,
            @RequestParam(defaultValue = "false") boolean simulateFailure,
            @RequestParam(defaultValue = "false") boolean simulateDelay) {

        Map<String, Object> response = paymentService.executePayment(orderId, amount, simulateFailure, simulateDelay);
        return ResponseEntity.ok(response);
    }
}
