# Microservices with Spring Boot 3.0 Exercises

> **Module:** 1. Microservices using Spring Boot 3 exercises  
> **Topic:** Microservices Architecture, Inter-Service Communication, Service Discovery, Config Server, API Gateway, Resilience4j Circuit Breaker

---

## Overview

This repository contains full, working Spring Boot 3 implementations for all 4 exercises in the module.

| Exercise | Application / Services | Key Features & Architecture |
|---|---|---|
| **Exercise 1** | `user-service`, `order-service` | User & Order Management, REST APIs, Inter-Service Communication via **Spring WebFlux WebClient** |
| **Exercise 2** | `eureka-server`, `product-service`, `inventory-service` | Product & Inventory System with **Netflix Eureka Service Discovery** & **Spring Cloud Config Server** |
| **Exercise 3** | `api-gateway`, `customer-service`, `billing-service` | **Spring Cloud Gateway** with **Path Rewriting**, **Rate Limiting**, and **Caching Headers** |
| **Exercise 4** | `payment-service` | Resilient Payment Service calling slow third-party API using **Resilience4j Circuit Breaker, Fallbacks & Event Monitoring** |

---

## Exercise Details & Instructions

### Exercise 1: Build a User and Order Management System

#### Services:
1. `user-service` (Port: `8081`)
   - Manages users in database (H2 in-memory, easily configured for MySQL/PostgreSQL in `application.yml`).
   - REST Endpoints: `POST /api/users`, `GET /api/users`, `GET /api/users/{id}`, `GET /api/users/email/{email}`.
2. `order-service` (Port: `8082`)
   - Manages order placement and status updates.
   - Calls `user-service` via reactive `WebClient` to validate user existence before placing an order.
   - REST Endpoints: `POST /api/orders`, `GET /api/orders`, `GET /api/orders/user/{userId}`, `PATCH /api/orders/{id}/status`.

#### How to Run:
```bash
# Terminal 1: Start User Service
cd exercise1-user-order-system/user-service
mvn spring-boot:run

# Terminal 2: Start Order Service
cd exercise1-user-order-system/order-service
mvn spring-boot:run
```

---

### Exercise 2: Inventory Management System with Service Discovery & Config Server

#### Services:
1. `eureka-server` (Port: `8761`)
   - Spring Cloud Netflix Eureka Server dashboard: `http://localhost:8761`.
2. `product-service` (Port: `8083`)
   - Manages product catalog and stock. Registers with Eureka and imports config from Config Server.
3. `inventory-service` (Port: `8084`)
   - Tracks inventory levels per product.

#### How to Run:
```bash
# 1. Start Eureka Server
cd exercise2-inventory-system/eureka-server
mvn spring-boot:run

# 2. Start Product Service
cd exercise2-inventory-system/product-service
mvn spring-boot:run

# 3. Start Inventory Service
cd exercise2-inventory-system/inventory-service
mvn spring-boot:run
```

---

### Exercise 3: Implement an API Gateway

#### Services:
1. `api-gateway` (Port: `8080`)
   - Route 1: `/api/v1/customers/**` -> Rewritten to `/customers/**` -> `customer-service`
   - Route 2: `/api/v1/billing/**` -> Rewritten to `/billing/**` -> `billing-service`
   - Key Features: Path rewriting (`RewritePath`), Rate Limiting (`RequestRateLimiter` via IP KeyResolver), Cache Control headers (`AddResponseHeader`).
2. `customer-service` (Port: `8085`)
3. `billing-service` (Port: `8086`)

#### How to Run:
```bash
cd exercise3-api-gateway/api-gateway && mvn spring-boot:run
cd exercise3-api-gateway/customer-service && mvn spring-boot:run
cd exercise3-api-gateway/billing-service && mvn spring-boot:run
```

---

### Exercise 4: Resilient Microservices with Circuit Breaker

#### Service:
- `payment-service` (Port: `8087`)
  - Calls a third-party payment gateway (`ThirdPartyPaymentClient`).
  - Uses Resilience4j `@CircuitBreaker(name = "paymentGateway", fallbackMethod = "fallbackProcessPayment")`.
  - Event listener `ResilienceMonitoringConfig` logs state transitions (CLOSED -> OPEN -> HALF_OPEN), errors, and fallback activations.

#### Testing Circuit Breaker & Fallback:
```bash
cd exercise4-resilient-payment-system/payment-service
mvn spring-boot:run

# 1. Successful payment call
curl -X POST "http://localhost:8087/payments/process?orderId=101&amount=150.00"

# 2. Simulate Failure (triggers fallback)
curl -X POST "http://localhost:8087/payments/process?orderId=102&amount=250.00&simulateFailure=true"

# 3. Simulate High Latency (triggers timeout & circuit breaker)
curl -X POST "http://localhost:8087/payments/process?orderId=103&amount=350.00&simulateDelay=true"

# 4. Monitor Circuit Breaker state via Actuator:
curl http://localhost:8087/actuator/health
curl http://localhost:8087/actuator/circuitbreakerevents
```
