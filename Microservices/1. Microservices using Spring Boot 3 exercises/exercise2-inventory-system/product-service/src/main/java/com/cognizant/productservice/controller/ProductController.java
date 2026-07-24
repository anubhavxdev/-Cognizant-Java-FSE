package com.cognizant.productservice.controller;

import com.cognizant.productservice.entity.Product;
import com.cognizant.productservice.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

/**
 * Product REST Controller.
 * POST   /api/products                    → Create product
 * GET    /api/products                    → List all
 * GET    /api/products/{id}               → Get by ID
 * GET    /api/products/category/{cat}     → Filter by category
 * PUT    /api/products/{id}               → Update product
 * PATCH  /api/products/{id}/reserve       → Reserve stock (called by Inventory Service)
 * PATCH  /api/products/{id}/restore       → Restore stock
 * DELETE /api/products/{id}               → Delete
 */
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;
    public ProductController(ProductService productService) { this.productService = productService; }

    @PostMapping
    public ResponseEntity<Product> create(@Valid @RequestBody Product p) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(p));
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAll() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getById(id));
    }

    @GetMapping("/category/{cat}")
    public ResponseEntity<List<Product>> getByCategory(@PathVariable String cat) {
        return ResponseEntity.ok(productService.getByCategory(cat));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> update(@PathVariable Long id, @Valid @RequestBody Product p) {
        return ResponseEntity.ok(productService.updateProduct(id, p));
    }

    @PatchMapping("/{id}/reserve")
    public ResponseEntity<Map<String, Object>> reserveStock(@PathVariable Long id,
                                                            @RequestBody Map<String, Integer> body) {
        int qty = body.getOrDefault("quantity", 0);
        boolean ok = productService.reserveStock(id, qty);
        return ok ? ResponseEntity.ok(Map.of("success", true, "message", "Stock reserved"))
                  : ResponseEntity.status(HttpStatus.CONFLICT)
                                  .body(Map.of("success", false, "message", "Insufficient stock"));
    }

    @PatchMapping("/{id}/restore")
    public ResponseEntity<Void> restoreStock(@PathVariable Long id,
                                             @RequestBody Map<String, Integer> body) {
        productService.restoreStock(id, body.getOrDefault("quantity", 0));
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
