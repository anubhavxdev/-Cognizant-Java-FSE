package com.cognizant.oauth2oidc;

import com.cognizant.oauth2oidc.controller.UserController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.Map;
import java.util.Set;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oidcLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * UserControllerTest – Exercise 1
 *
 * Verifies that:
 *  1. The /user endpoint returns 200 with OIDC claims when the user is authenticated.
 *  2. The /user endpoint returns 302 redirect to login when unauthenticated.
 *  3. The / (home) endpoint is publicly accessible.
 */
@WebMvcTest(UserController.class)
@Import(com.cognizant.oauth2oidc.config.SecurityConfig.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // ── Helper: build a mock OidcUser ──────────────────────────────────────

    private OidcUser mockOidcUser() {
        Map<String, Object> claims = Map.of(
            "sub",   "1234567890",
            "email", "testuser@example.com",
            "name",  "Test User"
        );
        OidcIdToken idToken = new OidcIdToken(
            "token-value",
            Instant.now(),
            Instant.now().plusSeconds(3600),
            claims
        );
        return new DefaultOidcUser(Set.of(), idToken);
    }

    // ── Tests ──────────────────────────────────────────────────────────────

    @Test
    void homeIsPubliclyAccessible() throws Exception {
        mockMvc.perform(get("/"))
               .andExpect(status().isOk())
               .andExpect(content().string(
                   "Welcome! Navigate to /user after logging in via OAuth2/OIDC."));
    }

    @Test
    void userEndpointRequiresAuthentication() throws Exception {
        mockMvc.perform(get("/user"))
               .andExpect(status().is3xxRedirection()); // redirect to login
    }

    @Test
    void userEndpointReturnsClaimsWhenAuthenticated() throws Exception {
        mockMvc.perform(get("/user").with(oidcLogin().oidcUser(mockOidcUser())))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.sub").value("1234567890"))
               .andExpect(jsonPath("$.email").value("testuser@example.com"));
    }

    @Test
    void userInfoEndpointReturnsSubsetOfClaims() throws Exception {
        mockMvc.perform(get("/user/info").with(oidcLogin().oidcUser(mockOidcUser())))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.subject").value("1234567890"))
               .andExpect(jsonPath("$.email").value("testuser@example.com"));
    }
}
