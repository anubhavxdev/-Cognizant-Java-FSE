package com.cognizant.jwtauth;

import com.cognizant.jwtauth.config.JwtConfig;
import com.cognizant.jwtauth.controller.SecuredController;
import com.cognizant.jwtauth.security.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * SecuredControllerTest – Exercise 3
 *
 * Verifies that:
 *  1. /api/secure returns 401 without authentication.
 *  2. /api/secure returns 200 for an authenticated user.
 *  3. /api/admin returns 403 for a user without the ADMIN role.
 *  4. /api/admin returns 200 for a user with the ADMIN role.
 */
@WebMvcTest(SecuredController.class)
@Import({com.cognizant.jwtauth.config.SecurityConfig.class,
         SecuredControllerTest.TestConfig.class})
class SecuredControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    // ── Helper configuration for tests ────────────────────────────────────

    @TestConfiguration
    static class TestConfig {
        @Bean
        public JwtConfig jwtConfig() {
            JwtConfig cfg = new JwtConfig();
            cfg.setSecret("test-secret-key-that-is-long-enough-for-hs256-algorithm");
            cfg.setExpirationMs(3_600_000L);
            return cfg;
        }

        @Bean
        public JwtTokenProvider jwtTokenProvider(JwtConfig jwtConfig) {
            return new JwtTokenProvider(jwtConfig);
        }
    }

    // ── Tests ──────────────────────────────────────────────────────────────

    @Test
    void secureEndpointReturns401WhenNotAuthenticated() throws Exception {
        mockMvc.perform(get("/api/secure"))
               .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void secureEndpointReturns200WhenAuthenticated() throws Exception {
        mockMvc.perform(get("/api/secure"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.message").value("This is a secure endpoint"))
               .andExpect(jsonPath("$.username").value("user"));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void adminEndpointReturns403ForNonAdminUser() throws Exception {
        mockMvc.perform(get("/api/admin"))
               .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
    void adminEndpointReturns200ForAdminUser() throws Exception {
        mockMvc.perform(get("/api/admin"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.message").value("This is an admin-only secure endpoint"))
               .andExpect(jsonPath("$.username").value("admin"));
    }
}
