package com.cognizant.resourceserver;

import com.cognizant.resourceserver.controller.SecureController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * SecureControllerTest – Exercise 2
 *
 * Verifies that:
 *  1. /api/secure returns 401 without a Bearer token.
 *  2. /api/secure returns 200 with a valid mock JWT.
 *  3. /api/admin returns 403 when the JWT lacks the admin scope.
 *  4. /api/admin returns 200 when the JWT contains the admin scope.
 */
@WebMvcTest(SecureController.class)
@Import(com.cognizant.resourceserver.config.ResourceServerConfig.class)
class SecureControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void secureEndpointRequiresBearerToken() throws Exception {
        mockMvc.perform(get("/api/secure"))
               .andExpect(status().isUnauthorized());
    }

    @Test
    void secureEndpointAccessibleWithValidJwt() throws Exception {
        mockMvc.perform(get("/api/secure")
                .with(jwt().jwt(builder -> builder
                    .subject("user-123")
                    .issuer("https://issuer.example.com")
                )))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.message").value("This is a secure endpoint"))
               .andExpect(jsonPath("$.subject").value("user-123"));
    }

    @Test
    void adminEndpointForbiddenWithoutAdminScope() throws Exception {
        mockMvc.perform(get("/api/admin")
                .with(jwt().jwt(builder -> builder.subject("user-123"))))
               .andExpect(status().isForbidden());
    }

    @Test
    void adminEndpointAccessibleWithAdminScope() throws Exception {
        mockMvc.perform(get("/api/admin")
                .with(jwt()
                    .jwt(builder -> builder
                        .subject("admin-user")
                        .claim("scope", "admin"))
                    .authorities(new SimpleGrantedAuthority("SCOPE_admin"))
                ))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.message").value("This is an admin-only endpoint"));
    }

    @Test
    void tokenClaimsEndpointReturnsJwtClaims() throws Exception {
        mockMvc.perform(get("/api/token/claims")
                .with(jwt().jwt(builder -> builder
                    .subject("user-123")
                    .issuer("https://issuer.example.com")
                    .claim("email", "user@example.com")
                )))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.sub").value("user-123"))
               .andExpect(jsonPath("$.email").value("user@example.com"));
    }
}
