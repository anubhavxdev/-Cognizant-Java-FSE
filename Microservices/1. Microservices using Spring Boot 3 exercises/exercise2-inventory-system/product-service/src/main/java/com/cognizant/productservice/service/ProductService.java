package com.cognizant.productservice.service;

import com.cognizant.productservice.entity.Product;
import com.cognizant.productservice.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class ProductService {

    private static final Logger log = LoggerFactory.getLogger(ProductService.class);
    private final ProductRepository repo;

    public ProductService(ProductRepository repo) { this.repo = repo; }

    public Product createProduct(Product p) {
        Product saved = repo.save(p);
        log.info("Created product id={}, name={}", saved.getId(), saved.getName());
        return saved;
    }

    @Transactional(readOnly = true)
    public List<Product> getAllProducts()               { return repo.findAll(); }

    @Transactional(readOnly = true)
    public Product getById(Long id) {
        return repo.findById(id).orElseThrow(() -> new RuntimeException("Product not found: " + id));
    }

    @Transactional(readOnly = true)
    public List<Product> getByCategory(String cat)     { return repo.findByCategory(cat); }

    public Product updateProduct(Long id, Product updates) {
        Product p = getById(id);
        p.setName(updates.getName()); p.setDescription(updates.getDescription());
        p.setCategory(updates.getCategory()); p.setPrice(updates.getPrice());
        return repo.save(p);
    }

    /** Decrement stock atomically; returns false if insufficient stock. */
    public boolean reserveStock(Long id, int qty) {
        int updated = repo.decrementStock(id, qty);
        if (updated == 0) { log.warn("Insufficient stock for product id={}", id); return false; }
        log.info("Reserved {} units of product id={}", qty, id);
        return true;
    }

    /** Increment stock (e.g., on order cancellation or restocking). */
    public void restoreStock(Long id, int qty) {
        repo.incrementStock(id, qty);
        log.info("Restored {} units to product id={}", qty, id);
    }

    public void deleteProduct(Long id) { repo.deleteById(id); }
}
