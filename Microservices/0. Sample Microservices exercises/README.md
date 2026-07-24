# Centralized Authentication and SSO with Spring Boot 3 and Spring Cloud

> **Module:** Sample Microservices Exercises  
> **Topic:** OAuth 2.1 / OIDC · Authorization/Resource Servers · JWT Authentication

---

## Overview

This folder contains three complete, runnable Spring Boot 3 projects demonstrating
centralized authentication patterns used in modern microservice architectures.

| # | Project Folder | Pattern |
|---|----------------|---------|
| 1 | `exercise1-oauth2-oidc` | OAuth 2.1 / OIDC client login (Google) |
| 2 | `exercise2-resource-server` | JWT-based Resource Server |
| 3 | `exercise3-jwt-auth` | Custom JWT authentication filter |

> **Spring Boot 3 / Spring Security 6 notes applied to all exercises:**
> - `WebSecurityConfigurerAdapter` is **removed** → `SecurityFilterChain` bean used instead  
> - `javax.servlet.*` → `jakarta.servlet.*`  
> - JJWT upgraded from deprecated `0.9.1` → modern `0.12.3` (modular API)  
> - `@EnableMethodSecurity` replaces `@EnableGlobalMethodSecurity`

---

## Exercise 1 – Centralized Authentication with OAuth 2.1 / OIDC

**Port:** `8080`

### What it does
Delegates authentication entirely to an external OIDC provider (Google in this
demo). Spring Security performs the full Authorization Code + PKCE flow and
exposes the authenticated user's ID Token claims via REST.

### Key Files
```
exercise1-oauth2-oidc/
├── pom.xml
└── src/main/
    ├── resources/application.yml          ← OAuth2 client config
    └── java/com/cognizant/oauth2oidc/
        ├── OAuth2OidcApplication.java     ← Entry point
        ├── config/SecurityConfig.java     ← SecurityFilterChain + oauth2Login()
        └── controller/UserController.java ← /user, /user/info endpoints
```

### Setup
1. Create an OAuth2 client in [Google Cloud Console](https://console.cloud.google.com/).
2. Add `http://localhost:8080/login/oauth2/code/my-client` as an Authorized redirect URI.
3. Update `application.yml`:
   ```yaml
   client-id: YOUR_GOOGLE_CLIENT_ID
   client-secret: YOUR_GOOGLE_CLIENT_SECRET
   ```
4. Run: `mvn spring-boot:run`
5. Visit `http://localhost:8080/user` → redirected to Google login → returns OIDC claims as JSON.

### Endpoints
| Method | Path | Auth Required | Description |
|--------|------|---------------|-------------|
| GET | `/` | No | Welcome page |
| GET | `/user` | Yes | All OIDC claims |
| GET | `/user/info` | Yes | Name, email, picture |

---

## Exercise 2 – Configuring Authorization Servers and Resource Servers

**Port:** `8081`

### What it does
Configures this application as a **stateless OAuth2 Resource Server**. It validates
JWT Bearer tokens issued by an external Authorization Server (Keycloak / Okta / Auth0)
by fetching the JWKS from the issuer's discovery endpoint.

### Key Files
```
exercise2-resource-server/
├── pom.xml
└── src/main/
    ├── resources/application.yml               ← issuer-uri config
    └── java/com/cognizant/resourceserver/
        ├── ResourceServerApplication.java
        ├── config/ResourceServerConfig.java    ← jwt() resource server setup
        └── controller/SecureController.java    ← /api/secure, /api/admin, /api/token/claims
```

### Setup
1. Configure any OIDC-compliant Authorization Server (Keycloak recommended for local dev).
2. Update `application.yml`:
   ```yaml
   spring.security.oauth2.resourceserver.jwt.issuer-uri: http://localhost:9090/realms/myrealm
   ```
3. Run: `mvn spring-boot:run`
4. Obtain a token from the Auth Server and call:
   ```bash
   curl -H "Authorization: Bearer <TOKEN>" http://localhost:8081/api/secure
   ```

### Endpoints
| Method | Path | Required Scope | Description |
|--------|------|----------------|-------------|
| GET | `/api/secure` | Any valid JWT | Returns subject and issuer |
| GET | `/api/admin` | `SCOPE_admin` | Admin-only endpoint |
| GET | `/api/token/claims` | Any valid JWT | Returns all JWT claims |

---

## Exercise 3 – JSON Web Tokens (JWT) for Secure Communication

**Port:** `8082`

### What it does
Implements a **self-contained JWT authentication system** without an external
Authorization Server. Users log in with username/password; the app issues a
signed JWT; subsequent requests include the JWT as a Bearer token.

### Key Files
```
exercise3-jwt-auth/
├── pom.xml
└── src/main/
    ├── resources/application.yml
    └── java/com/cognizant/jwtauth/
        ├── JwtAuthApplication.java
        ├── config/
        │   ├── JwtConfig.java              ← @ConfigurationProperties binding
        │   └── SecurityConfig.java         ← Filter chain + in-memory users
        ├── security/
        │   ├── JwtTokenProvider.java       ← Create / validate / parse JWTs
        │   └── JwtTokenFilter.java         ← OncePerRequestFilter extracts Bearer
        ├── controller/
        │   ├── AuthController.java         ← POST /auth/login → issues JWT
        │   └── SecuredController.java      ← GET /api/secure, /api/profile, /api/admin
        └── dto/
            ├── AuthRequest.java            ← { username, password }
            └── AuthResponse.java           ← { token }
```

### Setup
1. Update `application.yml` with a strong secret key (≥ 32 characters):
   ```yaml
   spring.security.jwt.secret: your-strong-secret-key-here
   ```
2. Run: `mvn spring-boot:run`

### Demo Users (in-memory)
| Username | Password | Roles |
|----------|----------|-------|
| `user` | `password` | USER |
| `admin` | `admin123` | USER, ADMIN |

### Usage Flow

#### Step 1 – Login and get a token
```bash
curl -X POST http://localhost:8082/auth/login \
     -H "Content-Type: application/json" \
     -d '{"username":"user","password":"password"}'

# Response:
# { "token": "eyJhbGciOiJIUzI1NiJ9..." }
```

#### Step 2 – Call a secured endpoint
```bash
TOKEN="eyJhbGciOiJIUzI1NiJ9..."

curl http://localhost:8082/api/secure \
     -H "Authorization: Bearer $TOKEN"

# Response:
# { "message": "This is a secure endpoint", "username": "user" }
```

#### Step 3 – Call the admin endpoint (requires admin role)
```bash
# Login as admin first
curl -X POST http://localhost:8082/auth/login \
     -H "Content-Type: application/json" \
     -d '{"username":"admin","password":"admin123"}'

curl http://localhost:8082/api/admin \
     -H "Authorization: Bearer $ADMIN_TOKEN"
```

### Endpoints
| Method | Path | Auth Required | Role | Description |
|--------|------|---------------|------|-------------|
| POST | `/auth/login` | No | — | Authenticate and get JWT |
| GET | `/api/secure` | Bearer JWT | Any | Basic secured endpoint |
| GET | `/api/profile` | Bearer JWT | Any | JWT claims / roles |
| GET | `/api/admin` | Bearer JWT | ADMIN | Admin-only endpoint |

---

## Running Tests

Each project has its own test suite:
```bash
# Exercise 1
cd exercise1-oauth2-oidc && mvn test

# Exercise 2
cd exercise2-resource-server && mvn test

# Exercise 3
cd exercise3-jwt-auth && mvn test
```

---

## Architecture Diagram

```
┌─────────────────────────────────────────────────────────────────┐
│                     Exercise 1 – OAuth2/OIDC                    │
│                                                                  │
│  Browser ──► Spring Boot App ──► Google (OIDC Provider)         │
│                ▲ (oauth2Login)    Authorization Code + PKCE      │
│                │                                                  │
│              /user returns OIDC claims (sub, email, name...)     │
└─────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────┐
│              Exercise 2 – Authorization & Resource Server        │
│                                                                  │
│  Client ──► [Auth Server: Keycloak/Okta] ──► JWT issued         │
│             Bearer JWT                                           │
│  Client ──► [Resource Server: this app] ──► validates JWT       │
│                 via JWKS endpoint of Auth Server                 │
└─────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────┐
│                  Exercise 3 – Custom JWT Auth                    │
│                                                                  │
│  Client POST /auth/login (username+password)                     │
│       ◄── JWT (signed HS256)                                     │
│  Client GET /api/secure (Bearer JWT)                             │
│    │                                                             │
│    ▼ JwtTokenFilter (OncePerRequestFilter)                       │
│       validates sig + expiry ──► sets SecurityContext           │
│    ▼ Controller returns protected resource                       │
└─────────────────────────────────────────────────────────────────┘
```

---

## Key Spring Security 6 Changes from Original Exercise

| Original (deprecated) | Spring Boot 3 / Security 6 |
|----------------------|---------------------------|
| `extends WebSecurityConfigurerAdapter` | `@Bean SecurityFilterChain` |
| `http.authorizeRequests()` | `http.authorizeHttpRequests()` |
| `javax.servlet.http.HttpServletRequest` | `jakarta.servlet.http.HttpServletRequest` |
| `io.jsonwebtoken:jjwt:0.9.1` | `io.jsonwebtoken:jjwt-api/impl/jackson:0.12.3` |
| `Jwts.parserBuilder()` | `Jwts.parser()` |
| `SignatureAlgorithm.HS256` enum | Auto-inferred from `SecretKey` type |
| `Principal principal` in controller | `@AuthenticationPrincipal OidcUser oidcUser` |
| `@EnableGlobalMethodSecurity` | `@EnableMethodSecurity` |
