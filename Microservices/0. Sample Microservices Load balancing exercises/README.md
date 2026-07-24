# Edge Services and API Gateway with Spring Boot 3 and Spring Cloud

> **Module:** Sample Microservices Load Balancing Exercises  
> **Topic:** Edge Services · Load Balancing · Resilience Patterns

---

## Overview

Three complete, runnable Spring Boot 3 + Spring Cloud projects demonstrating
production-grade API Gateway patterns.

| # | Project | Pattern | Port |
|---|---------|---------|------|
| 1 | `exercise1-edge-service` | Routing & Filtering (GlobalFilter) | 8080 |
| 2 | `exercise2-load-balanced-gateway` | Client-Side Load Balancing (lb://) | 8080 |
| 3 | `exercise3-resilient-gateway` | Circuit Breaker · Retry · Rate Limiter | 8080 |

> **Spring Boot 3 / Spring Cloud 2023.x corrections applied:**
> - Spring Cloud BOM `2023.0.0` (aligned with Spring Boot 3.2.x)
> - `resilience4j-spring-boot2` → `spring-cloud-starter-circuitbreaker-reactor-resilience4j`
> - `RandomLoadBalancer` scoped correctly via `@LoadBalancerClient` (not `@Configuration`)
> - `javax.servlet.*` → `jakarta.servlet.*`
> - `http.authorizeRequests()` → `http.authorizeHttpRequests()`

---

## Exercise 1 – Implementing Edge Services for Routing and Filtering

### What it does
Acts as the single entry point for all client traffic. Matches requests against
route predicates and applies filters before forwarding to downstream services.

### Key Files
```
exercise1-edge-service/
├── pom.xml
└── src/main/
    ├── resources/application.yml              ← declarative route definitions
    └── java/com/cognizant/edgeservice/
        ├── EdgeServiceApplication.java
        ├── config/GatewayConfig.java          ← programmatic (Java DSL) routes
        └── filter/
            ├── LoggingFilter.java             ← GlobalFilter: logs every request
            └── RequestIdFilter.java           ← GlobalFilter: injects X-Request-Id
```

### Filter Pipeline (execution order)

```
Inbound Request
      │
      ▼  [Order = MIN_VALUE]   RequestIdFilter  → assigns X-Request-Id
      ▼  [Order = MIN_VALUE+1] LoggingFilter    → logs URI, method, client
      ▼  Built-in Gateway filters (routing, load balancing, etc.)
      ▼
  Downstream Service
      │
      ▼  (response flows back)
      ▼  LoggingFilter.then()  → logs status code + elapsed time
      ▼  RequestIdFilter.then() → adds X-Request-Id to response headers
      ▼
Outbound Response
```

### Routes Configured

| Route ID | Incoming Path | Forwarded To | Filters Applied |
|----------|--------------|--------------|-----------------|
| `example_route` | `/example/**` | http://example.org | StripPrefix, AddResponseHeader |
| `httpbin_route` | `/httpbin/**` | https://httpbin.org | StripPrefix, AddRequestHeader |
| `programmatic_status_route` | `/status/**` | https://httpbin.org | RewritePath, AddResponseHeader |
| `programmatic_get_route` | `/get` | https://httpbin.org | AddRequestHeader |

### Run & Test
```bash
cd exercise1-edge-service
mvn spring-boot:run

# Test routing
curl http://localhost:8080/httpbin/get
curl http://localhost:8080/get

# View all registered routes
curl http://localhost:8080/actuator/gateway/routes
```

---

## Exercise 2 – Load Balancing in an API Gateway

### What it does
Demonstrates client-side load balancing using Spring Cloud LoadBalancer.
The `lb://` URI scheme triggers the `ReactiveLoadBalancerClientFilter`, which
resolves a logical service name to a physical instance using Eureka (or a
static instance list).

### Architecture

```
Client → Gateway (lb://example-service)
              │
              ▼ ReactiveLoadBalancerClientFilter
         LoadBalancer resolves instances
              │
         ┌────┴────┐
         ▼         ▼
   instance1    instance2    (RandomLoadBalancer for example-service)
  :8091         :8092
```

### Key Files
```
exercise2-load-balanced-gateway/
├── pom.xml
└── src/main/
    ├── resources/application.yml
    └── java/com/cognizant/lbgateway/
        ├── LoadBalancedGatewayApplication.java
        ├── config/
        │   ├── LoadBalancerConfiguration.java   ← RandomLoadBalancer bean
        │   ├── LoadBalancerClientConfig.java     ← @LoadBalancerClients scoping
        │   └── GatewayLoadBalancerConfig.java    ← Java DSL lb:// routes + Retry
        └── filter/
            └── LoadBalancingFilter.java          ← GlobalFilter: logs chosen instance
```

### Load Balancer Configuration Detail

```
@LoadBalancerClients({
    @LoadBalancerClient(name = "example-service",
                        configuration = LoadBalancerConfiguration.class)
})
```

> **Why NOT `@Configuration` on `LoadBalancerConfiguration`?**  
> Adding `@Configuration` at class level would put the `RandomLoadBalancer` bean
> in the **global** application context and apply it to **all** services. The
> correct approach is to use it as a non-`@Configuration` class referenced via
> `@LoadBalancerClient`, which scopes it to a child context for that service only.

### Run & Test (with Eureka)
```bash
# Start Eureka Server on port 8761, then start two instances of example-service
# on :8091 and :8092

cd exercise2-load-balanced-gateway
mvn spring-boot:run

# Call the load-balanced route (will alternate between instances)
curl http://localhost:8080/loadbalanced/endpoint
curl http://localhost:8080/loadbalanced/endpoint

# View LB choices in logs — watch for [LB] lb://example-service → http://localhost:809x
```

### Run Without Eureka (static instances)
Uncomment the static instances block in `application.yml`:
```yaml
spring:
  cloud:
    loadbalancer:
      clients:
        example-service:
          instances:
            - instance-id: instance1
              uri: http://localhost:8091
            - instance-id: instance2
              uri: http://localhost:8092
```

---

## Exercise 3 – Resilience Patterns in an API Gateway

### What it does
Demonstrates four resilience patterns integrated directly in the Gateway:

| Pattern | Purpose | Spring Cloud Filter |
|---------|---------|---------------------|
| **Circuit Breaker** | Stops calling failing services | `CircuitBreaker` GatewayFilter |
| **Retry** | Re-attempts failed requests | `Retry` GatewayFilter |
| **Rate Limiter** | Throttles request rate | `RequestRateLimiter` GatewayFilter |
| **Time Limiter** | Cancels slow requests | Configured in `TimeLimiterConfig` |
| **Fallback** | Returns default response | `FallbackController` |

### Key Files
```
exercise3-resilient-gateway/
├── pom.xml
└── src/main/
    ├── resources/application.yml             ← CB instances + filter config
    └── java/com/cognizant/resilientgateway/
        ├── ResilientGatewayApplication.java
        ├── config/
        │   ├── ResilienceConfiguration.java  ← Customizer<ReactiveResilience4JCBFactory>
        │   └── GatewayResilienceConfig.java  ← Java DSL routes with CB + Retry
        ├── controller/
        │   └── FallbackController.java       ← /fallback/* endpoints (503/504)
        └── filter/
            └── ResilienceMetricsFilter.java  ← GlobalFilter: logs CB states
```

### Circuit Breaker State Machine

```
             failure rate ≥ 50%          wait 10s
  CLOSED ──────────────────────► OPEN ──────────────► HALF-OPEN
    ▲                              │                      │
    │          success             │     3 test calls     │
    └──────────────────────────────┘◄─────────────────────┘
                                    failure → OPEN again
```

### Spring Boot 3 Fix Applied
```xml
<!-- ❌ WRONG (Spring Boot 2 only, breaks with Spring Boot 3) -->
<dependency>
    <groupId>io.github.resilience4j</groupId>
    <artifactId>resilience4j-spring-boot2</artifactId>
</dependency>

<!-- ✅ CORRECT for Spring Boot 3 + reactive Gateway -->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-circuitbreaker-reactor-resilience4j</artifactId>
</dependency>
```

### Run & Test
```bash
cd exercise3-resilient-gateway
mvn spring-boot:run

# Test fallback endpoints directly (simulate open circuit breaker)
curl http://localhost:8080/fallback/example
# → { "status": 503, "message": "example-service is currently unavailable..." }

curl http://localhost:8080/fallback/timeout
# → { "status": 504, "error": "Gateway Timeout", ... }

# View circuit breaker states
curl http://localhost:8080/actuator/health | jq '.components.circuitBreakers'

# View circuit breaker events
curl http://localhost:8080/actuator/circuitbreakerevents

# Prometheus metrics (all CB states)
curl http://localhost:8080/actuator/prometheus | grep resilience4j
```

### Circuit Breaker Instances Configured

| Instance Name | Window | Failure Threshold | Wait in OPEN | Timeout |
|---------------|--------|------------------|--------------|---------|
| `exampleCircuitBreaker` | 20 calls | 40% | 15s | 2s |
| `apiCircuitBreaker` | 20 calls | 40% | 15s | 5s |
| `timeoutCircuitBreaker` | 5 calls | 60% | 30s | 500ms |
| Default (all others) | 10 calls | 50% | 10s | 3s |

---

## Running Tests

```bash
# Exercise 1
cd exercise1-edge-service && mvn test

# Exercise 2 (Eureka disabled in tests automatically)
cd exercise2-load-balanced-gateway && mvn test

# Exercise 3 (includes fallback endpoint tests)
cd exercise3-resilient-gateway && mvn test
```

---

## Overall Architecture

```
                         ┌──────────────────────────────────────┐
                         │         API GATEWAY (Port 8080)      │
                         │                                      │
  Client ───────────────►│  Predicates → Filters → LB → CB     │
                         │                                      │
                         │  GlobalFilters (run on ALL routes):  │
                         │    1. RequestIdFilter  (tracing)     │
                         │    2. LoggingFilter    (audit)       │
                         │    3. LoadBalancingFilter (LB obs.)  │
                         │    4. ResilienceMetricsFilter        │
                         └────────────────┬─────────────────────┘
                                          │  lb:// URI
                                          ▼
                         ┌────────────────────────────────────┐
                         │   Spring Cloud LoadBalancer         │
                         │   (Round-Robin / Random)           │
                         └────┬──────────┬────────────────────┘
                              │          │
                    ┌─────────▼──┐  ┌────▼──────────┐
                    │ Service    │  │  Service       │
                    │ Instance 1 │  │  Instance 2    │
                    │ :8091      │  │  :8092         │
                    └────────────┘  └────────────────┘
```

---

## Key Dependency Corrections (Original Exercise → Spring Boot 3)

| Original | Corrected |
|----------|-----------|
| `resilience4j-spring-boot2` | `spring-cloud-starter-circuitbreaker-reactor-resilience4j` |
| No Spring Cloud BOM | Added `spring-cloud-dependencies:2023.0.0` BOM |
| `@Configuration` on `LoadBalancerConfiguration` | Removed — use `@LoadBalancerClient` to scope |
| No `@Import` of Spring Cloud BOM | `<dependencyManagement>` with SC BOM added |
| `application.properties` | Migrated to `application.yml` (more readable) |
