# Microservices with API Gateway Hands-on Exercises

This repository contains the complete hands-on solution for **Microservices with API Gateway** using Spring Boot 3 & Spring Cloud 2023.0.0.

---

## 📁 Repository Structure

```
2. Microservices with API gateway/
├── README.md
├── eureka-server/           (Port 8761 - Eureka Service Discovery Server)
├── greet-service/            (Port 8080 - Greet Microservice)
├── account/                  (Port 8082 - Account Microservice)
├── loan/                     (Port 8081 - Loan Microservice)
└── api-gateway/              (Port 9090 - Spring Cloud API Gateway with LogFilter)
```

---

## 🛠️ Microservices Overview

### 1. `eureka-server` (Port 8761)
- **Role:** Eureka Service Discovery Registry.
- **Main Class:** `@EnableEurekaServer`
- **Dashboard URL:** `http://localhost:8761`

### 2. `greet-service` (Port 8080)
- **Role:** Simple REST microservice returning "Hello World!!".
- **Endpoint:** `GET /greet`
- **Eureka Name:** `greet-service`

### 3. `account` (Port 8082)
- **Role:** Account management microservice.
- **Endpoint:** `GET /accounts/{number}`
- **Sample Response:**
  ```json
  { "number": "00987987973432", "type": "savings", "balance": 234343 }
  ```
- **Eureka Name:** `account-service`

### 4. `loan` (Port 8081)
- **Role:** Loan management microservice.
- **Endpoint:** `GET /loans/{number}`
- **Sample Response:**
  ```json
  { "number": "H00987987972342", "type": "car", "loan": 400000, "emi": 3258, "tenure": 18 }
  ```
- **Eureka Name:** `loan-service`

### 5. `api-gateway` (Port 9090)
- **Role:** Central Spring Cloud API Gateway with global logging filter.
- **Configuration:**
  - `spring.cloud.gateway.discovery.locator.enabled=true`
  - `spring.cloud.gateway.discovery.locator.lower-case-service-id=true`
- **Global Filter (`LogFilter`):** Implements `GlobalFilter` to log every incoming request URL:
  ```java
  logger.info("====>Request URL {}", exchange.getRequest().getURI());
  ```

---

## 🚀 Execution Instructions

### Step 1: Start Eureka Discovery Server
```bash
cd eureka-server
mvn spring-boot:run
```
*Verify at `http://localhost:8761`.*

### Step 2: Start Microservices
```bash
# Terminal 2: Start Greet Service
cd greet-service && mvn spring-boot:run

# Terminal 3: Start Account Service
cd account && mvn spring-boot:run

# Terminal 4: Start Loan Service
cd loan && mvn spring-boot:run
```

### Step 3: Start API Gateway
```bash
cd api-gateway
mvn spring-boot:run
```

---

## 🧪 Testing Gateway Routing & Global Filter

Access services through the API Gateway on port `9090`:

1. **Greet Service through Gateway:**
   ```bash
   curl http://localhost:9090/greet-service/greet
   # Output: Hello World!!
   ```

2. **Account Service through Gateway:**
   ```bash
   curl http://localhost:9090/account-service/accounts/00987987973432
   ```

3. **Loan Service through Gateway:**
   ```bash
   curl http://localhost:9090/loan-service/loans/H00987987972342
   ```

4. **Verify Gateway Log Console:**
   Check the `api-gateway` console log output:
   ```
   ====>Request URL http://localhost:9090/greet-service/greet
   ```
