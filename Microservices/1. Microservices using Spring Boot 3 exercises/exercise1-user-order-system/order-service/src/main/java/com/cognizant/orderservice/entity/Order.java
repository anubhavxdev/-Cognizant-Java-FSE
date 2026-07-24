package com.cognizant.orderservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Order entity mapped to the {@code orders} table.
 * Each order belongs to a user (referenced by userId) managed by User Service.
 */
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Foreign key reference to the user (resolved via User Service REST call). */
    @NotNull(message = "User ID is required")
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @NotBlank(message = "Product name is required")
    @Column(nullable = false)
    private String productName;

    @Min(value = 1, message = "Quantity must be at least 1")
    private int quantity;

    @Positive(message = "Price must be positive")
    @Column(precision = 10, scale = 2)
    private BigDecimal totalPrice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status = OrderStatus.PENDING;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist  protected void onCreate() { createdAt = updatedAt = LocalDateTime.now(); }
    @PreUpdate   protected void onUpdate() { updatedAt = LocalDateTime.now(); }

    public enum OrderStatus { PENDING, CONFIRMED, SHIPPED, DELIVERED, CANCELLED }

    // ── Constructors ──────────────────────────────────────────────────────
    public Order() {}
    public Order(Long userId, String productName, int quantity, BigDecimal totalPrice) {
        this.userId = userId; this.productName = productName;
        this.quantity = quantity; this.totalPrice = totalPrice;
    }

    // ── Getters & Setters ─────────────────────────────────────────────────
    public Long getId()                          { return id; }
    public Long getUserId()                      { return userId; }
    public void setUserId(Long userId)           { this.userId = userId; }
    public String getProductName()               { return productName; }
    public void setProductName(String n)         { this.productName = n; }
    public int getQuantity()                     { return quantity; }
    public void setQuantity(int quantity)        { this.quantity = quantity; }
    public BigDecimal getTotalPrice()            { return totalPrice; }
    public void setTotalPrice(BigDecimal price)  { this.totalPrice = price; }
    public OrderStatus getStatus()               { return status; }
    public void setStatus(OrderStatus status)    { this.status = status; }
    public LocalDateTime getCreatedAt()          { return createdAt; }
    public LocalDateTime getUpdatedAt()          { return updatedAt; }
}
