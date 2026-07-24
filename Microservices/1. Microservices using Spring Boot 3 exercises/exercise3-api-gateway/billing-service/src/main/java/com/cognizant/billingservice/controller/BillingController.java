package com.cognizant.billingservice.controller;

import com.cognizant.billingservice.entity.Bill;
import com.cognizant.billingservice.repository.BillRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/billing")
public class BillingController {

    private final BillRepository repository;

    public BillingController(BillRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public ResponseEntity<Bill> createBill(@Valid @RequestBody Bill bill) {
        return ResponseEntity.status(HttpStatus.CREATED).body(repository.save(bill));
    }

    @GetMapping
    public ResponseEntity<List<Bill>> getAll() {
        return ResponseEntity.ok(repository.findAll());
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<Bill>> getByCustomerId(@PathVariable Long customerId) {
        return ResponseEntity.ok(repository.findByCustomerId(customerId));
    }
}
