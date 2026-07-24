package com.cognizant.inventoryservice.controller;

import com.cognizant.inventoryservice.entity.Inventory;
import com.cognizant.inventoryservice.repository.InventoryRepository;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

/**
 * Inventory REST Controller.
 *
 * POST   /api/inventory                  → Register product inventory
 * GET    /api/inventory                  → List all inventory
 * GET    /api/inventory/product/{pid}    → Get inventory for a product
 * PATCH  /api/inventory/product/{pid}/reserve  → Reserve stock
 * PATCH  /api/inventory/product/{pid}/release  → Release reserved stock
 * PATCH  /api/inventory/product/{pid}/restock  → Restock
 */
@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryRepository repo;
    public InventoryController(InventoryRepository repo) { this.repo = repo; }

    @PostMapping
    public ResponseEntity<Inventory> register(@RequestBody Inventory inv) {
        if (repo.existsByProductId(inv.getProductId()))
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        return ResponseEntity.status(HttpStatus.CREATED).body(repo.save(inv));
    }

    @GetMapping
    public ResponseEntity<List<Inventory>> getAll() { return ResponseEntity.ok(repo.findAll()); }

    @GetMapping("/product/{pid}")
    public ResponseEntity<Inventory> getByProduct(@PathVariable Long pid) {
        return repo.findByProductId(pid)
                   .map(ResponseEntity::ok)
                   .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/product/{pid}/reserve")
    public ResponseEntity<Map<String, Object>> reserve(@PathVariable Long pid,
                                                       @RequestBody Map<String, Integer> body) {
        int qty = body.getOrDefault("quantity", 0);
        return repo.findByProductId(pid).map(inv -> {
            if (inv.getAvailableStock() < qty)
                return ResponseEntity.status(HttpStatus.CONFLICT)
                    .<Map<String, Object>>body(Map.of("success", false, "message", "Insufficient stock"));
            inv.setAvailableStock(inv.getAvailableStock() - qty);
            inv.setReservedStock(inv.getReservedStock() + qty);
            repo.save(inv);
            return ResponseEntity.ok(Map.<String, Object>of("success", true, "available", inv.getAvailableStock()));
        }).orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/product/{pid}/release")
    public ResponseEntity<Inventory> release(@PathVariable Long pid,
                                             @RequestBody Map<String, Integer> body) {
        int qty = body.getOrDefault("quantity", 0);
        return repo.findByProductId(pid).map(inv -> {
            inv.setReservedStock(Math.max(0, inv.getReservedStock() - qty));
            inv.setAvailableStock(inv.getAvailableStock() + qty);
            return ResponseEntity.ok(repo.save(inv));
        }).orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/product/{pid}/restock")
    public ResponseEntity<Inventory> restock(@PathVariable Long pid,
                                             @RequestBody Map<String, Integer> body) {
        int qty = body.getOrDefault("quantity", 0);
        return repo.findByProductId(pid).map(inv -> {
            inv.setAvailableStock(inv.getAvailableStock() + qty);
            return ResponseEntity.ok(repo.save(inv));
        }).orElse(ResponseEntity.notFound().build());
    }
}
