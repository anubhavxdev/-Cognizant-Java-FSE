package com.cognizant.orderservice.repository;

import com.cognizant.orderservice.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    /** Fetch all orders for a specific user. */
    List<Order> findByUserId(Long userId);
    /** Fetch orders by status. */
    List<Order> findByStatus(Order.OrderStatus status);
}
