package com.cognizant.inventoryservice.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/** Tracks stock level transactions per product. */
@Entity
@Table(name = "inventory")
public class Inventory {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_id", nullable = false, unique = true)
    private Long productId;

    @Column(nullable = false)
    private int availableStock;

    @Column(nullable = false)
    private int reservedStock;

    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;

    @PrePersist @PreUpdate
    protected void onUpdate() { lastUpdated = LocalDateTime.now(); }

    public Inventory() {}
    public Inventory(Long productId, int availableStock) {
        this.productId = productId;
        this.availableStock = availableStock;
        this.reservedStock = 0;
    }

    // ── Getters & Setters ─────────────────────────────────────────────────
    public Long getId()                         { return id; }
    public Long getProductId()                  { return productId; }
    public void setProductId(Long pid)          { this.productId = pid; }
    public int getAvailableStock()              { return availableStock; }
    public void setAvailableStock(int s)        { this.availableStock = s; }
    public int getReservedStock()               { return reservedStock; }
    public void setReservedStock(int s)         { this.reservedStock = s; }
    public int getTotalStock()                  { return availableStock + reservedStock; }
    public LocalDateTime getLastUpdated()       { return lastUpdated; }
}
