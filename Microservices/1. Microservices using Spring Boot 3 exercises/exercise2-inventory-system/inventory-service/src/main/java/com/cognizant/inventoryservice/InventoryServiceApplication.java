package com.cognizant.inventoryservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Exercise 2 – Inventory Service
 * Tracks stock levels per product. Calls Product Service (via Eureka + LoadBalancer)
 * to check product existence before recording inventory transactions.
 */
@SpringBootApplication
public class InventoryServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(InventoryServiceApplication.class, args);
    }
}
