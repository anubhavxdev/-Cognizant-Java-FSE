package com.cognizant.orderservice.service;

import com.cognizant.orderservice.client.UserServiceClient;
import com.cognizant.orderservice.entity.Order;
import com.cognizant.orderservice.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Order business logic layer.
 * Validates user existence via {@link UserServiceClient} before creating orders.
 */
@Service
@Transactional
public class OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository    orderRepository;
    private final UserServiceClient  userServiceClient;

    public OrderService(OrderRepository orderRepository,
                        UserServiceClient userServiceClient) {
        this.orderRepository   = orderRepository;
        this.userServiceClient = userServiceClient;
    }

    /**
     * Create a new order after validating the user with User Service via WebClient.
     */
    public Order createOrder(Order order) {
        // ── Inter-service call: validate user exists ─────────────────────
        if (!userServiceClient.userExists(order.getUserId())) {
            throw new IllegalArgumentException(
                "Cannot create order: user not found with id=" + order.getUserId());
        }
        order.setStatus(Order.OrderStatus.PENDING);
        Order saved = orderRepository.save(order);
        log.info("Created order id={} for userId={}", saved.getId(), saved.getUserId());
        return saved;
    }

    @Transactional(readOnly = true)
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                              .orElseThrow(() -> new RuntimeException("Order not found: " + id));
    }

    @Transactional(readOnly = true)
    public List<Order> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    public Order updateOrderStatus(Long id, Order.OrderStatus newStatus) {
        Order order = getOrderById(id);
        order.setStatus(newStatus);
        log.info("Updated order id={} status → {}", id, newStatus);
        return orderRepository.save(order);
    }

    public void cancelOrder(Long id) {
        Order order = getOrderById(id);
        order.setStatus(Order.OrderStatus.CANCELLED);
        orderRepository.save(order);
        log.info("Cancelled order id={}", id);
    }
}
