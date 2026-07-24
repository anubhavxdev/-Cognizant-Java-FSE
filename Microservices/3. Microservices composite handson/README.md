# Microservices Composite Pattern Hands-on Exercise

This project demonstrates the **Microservice Aggregator / Composite Pattern** in a Banking domain using Spring Boot 3 & Spring Cloud 2023.0.0.

---

## 📁 Repository Structure

```
3. Microservices composite handson/
├── README.md
├── eureka-server/           (Port 8761 - Eureka Discovery Registry)
├── account-service/         (Port 8082 - Account Microservice: GET /accounts/{number})
├── loan-service/            (Port 8081 - Loan Microservice: GET /loans/{number})
├── composite-service/       (Port 8085 - Composite Microservice: Aggregates Account + Loan)
└── api-gateway/             (Port 9090 - Spring Cloud API Gateway)
```

---

## 🏗️ Architecture & Pattern Overview

```
                      ┌────────────────────────────────────────┐
                      │        API Gateway (Port 9090)         │
                      └───────────────────┬────────────────────┘
                                          │
                                          ▼
                      ┌────────────────────────────────────────┐
                      │    Composite Service (Port 8085)       │
                      │    (Aggregates Account & Loan Data)    │
                      └───────────┬───────────────┬────────────┘
                                  │               │
                 WebClient        │               │ WebClient
             (Mono.zip in parallel)               │
                                  ▼               ▼
                      ┌───────────────┐       ┌───────────────┐
                      │Account-Service│       │ Loan-Service  │
                      │ (Port 8082)   │       │  (Port 8081)  │
                      └───────────────┘       └───────────────┘
```

1. **`account-service`** (`http://localhost:8082`): Returns account details for an account number.
2. **`loan-service`** (`http://localhost:8081`): Returns loan details for a loan number.
3. **`composite-service`** (`http://localhost:8085`): Uses a load-balanced reactive `WebClient` and `Mono.zip()` to fetch both Account and Loan details concurrently from Eureka and combines them into a unified `CustomerPortfolioResponse`.
4. **`api-gateway`** (`http://localhost:9090`): Single entry point for client requests.

---

## 🚀 Execution Instructions

### Step 1: Start Eureka Discovery Server
```bash
cd eureka-server
mvn spring-boot:run
```
*Verify Eureka Dashboard at `http://localhost:8761`.*

### Step 2: Start Core Microservices
```bash
# Terminal 2: Account Service
cd account-service && mvn spring-boot:run

# Terminal 3: Loan Service
cd loan-service && mvn spring-boot:run
```

### Step 3: Start Composite Service & API Gateway
```bash
# Terminal 4: Composite Service
cd composite-service && mvn spring-boot:run

# Terminal 5: API Gateway
cd api-gateway && mvn spring-boot:run
```

---

## 🧪 Testing Composite Function Endpoint

1. **Call Composite Service Directly:**
   ```bash
   curl http://localhost:8085/composite/portfolio/00987987973432/H00987987972342
   ```

2. **Call Composite Service through API Gateway:**
   ```bash
   curl http://localhost:9090/composite-service/composite/portfolio/00987987973432/H00987987972342
   ```

3. **Sample JSON Response:**
   ```json
   {
     "account": {
       "number": "00987987973432",
       "type": "savings",
       "balance": 234343.0
     },
     "loan": {
       "number": "H00987987972342",
       "type": "car",
       "loan": 400000.0,
       "emi": 3258.0,
       "tenure": 18
     },
     "status": "SUCCESS",
     "timestamp": "2026-07-24T14:15:00"
   }
   ```
