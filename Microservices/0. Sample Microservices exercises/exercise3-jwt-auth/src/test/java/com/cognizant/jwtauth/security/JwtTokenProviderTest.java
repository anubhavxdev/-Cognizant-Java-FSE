package com.cognizant.jwtauth.security;

import com.cognizant.jwtauth.config.JwtConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * JwtTokenProviderTest – Exercise 3
 *
 * Unit tests for {@link JwtTokenProvider} covering:
 *  1. Token creation returns a non-empty JWT string.
 *  2. A freshly created token is valid.
 *  3. An invalid / tampered token fails validation.
 *  4. Username is correctly extracted from a valid token.
 *  5. Authentication object contains the expected principal and authorities.
 */
class JwtTokenProviderTest {

    private JwtTokenProvider provider;

    @BeforeEach
    void setUp() {
        // 256-bit secret key (32 bytes) required for HS256
        JwtConfig config = new JwtConfig();
        config.setSecret("test-secret-key-that-is-long-enough-for-hs256-algorithm");
        config.setExpirationMs(3_600_000L);  // 1 hour
        provider = new JwtTokenProvider(config);
    }

    @Test
    void createToken_returnsNonEmptyString() {
        String token = provider.createToken("alice", List.of("USER"));
        assertThat(token).isNotBlank();
        // JWT has three dot-separated parts
        assertThat(token.split("\\.")).hasSize(3);
    }

    @Test
    void validateToken_returnsTrueForValidToken() {
        String token = provider.createToken("alice", List.of("USER"));
        assertThat(provider.validateToken(token)).isTrue();
    }

    @Test
    void validateToken_returnsFalseForInvalidToken() {
        assertThat(provider.validateToken("not.a.valid.jwt")).isFalse();
    }

    @Test
    void validateToken_returnsFalseForTamperedToken() {
        String token  = provider.createToken("alice", List.of("USER"));
        String tampered = token + "tampered";
        assertThat(provider.validateToken(tampered)).isFalse();
    }

    @Test
    void getUsername_extractsSubjectClaim() {
        String token = provider.createToken("bob", List.of("USER", "ADMIN"));
        assertThat(provider.getUsername(token)).isEqualTo("bob");
    }

    @Test
    void getAuthentication_buildsCorrectAuthenticationObject() {
        String token = provider.createToken("carol", List.of("USER", "ADMIN"));
        Authentication auth = provider.getAuthentication(token);

        assertThat(auth.getName()).isEqualTo("carol");
        assertThat(auth.isAuthenticated()).isTrue();
        assertThat(auth.getAuthorities())
            .extracting(Object::toString)
            .containsExactlyInAnyOrder("ROLE_USER", "ROLE_ADMIN");
    }
}
