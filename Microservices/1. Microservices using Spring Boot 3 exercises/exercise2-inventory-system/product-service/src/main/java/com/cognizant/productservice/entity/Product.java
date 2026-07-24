package com.cognizant.productservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/** Product entity — stores catalog information and stock quantity. */
@Entity
@Table(name = "products")
public class Product {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank @Size(max = 200)
    @Column(nullable = false)
    private String name;

    @Column(length = 1000)
    private String description;

    @NotBlank
    private String category;

    @Positive @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal price;

    @Min(0) @Column(nullable = false)
    private int stockQuantity;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist protected void onCreate() { createdAt = LocalDateTime.now(); }

    // ── Constructors ──────────────────────────────────────────────────────
    public Product() {}
    public Product(String name, String description, String category,
                   BigDecimal price, int stockQuantity) {
        this.name = name; this.description = description;
        this.category = category; this.price = price;
        this.stockQuantity = stockQuantity;
    }

    // ── Getters & Setters ─────────────────────────────────────────────────
    public Long getId()                           { return id; }
    public String getName()                       { return name; }
    public void setName(String name)              { this.name = name; }
    public String getDescription()                { return description; }
    public void setDescription(String d)          { this.description = d; }
    public String getCategory()                   { return category; }
    public void setCategory(String c)             { this.category = c; }
    public BigDecimal getPrice()                  { return price; }
    public void setPrice(BigDecimal price)        { this.price = price; }
    public int getStockQuantity()                 { return stockQuantity; }
    public void setStockQuantity(int qty)         { this.stockQuantity = qty; }
    public LocalDateTime getCreatedAt()           { return createdAt; }
}
