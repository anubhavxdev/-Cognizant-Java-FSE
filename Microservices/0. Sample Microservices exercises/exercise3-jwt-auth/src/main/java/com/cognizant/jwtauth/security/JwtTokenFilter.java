package com.cognizant.jwtauth.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JwtTokenFilter – Exercise 3
 *
 * A Servlet filter that runs exactly once per HTTP request
 * ({@link OncePerRequestFilter}) and performs JWT-based authentication:
 *
 *  1. Extract the Bearer token from the {@code Authorization} header.
 *  2. Validate it using {@link JwtTokenProvider}.
 *  3. If valid, build a Spring Security {@link Authentication} and store it
 *     in the {@link SecurityContextHolder} so subsequent filters and controllers
 *     see an authenticated principal.
 *
 * If no token is present or the token is invalid the request proceeds
 * unauthenticated; the security filter chain will then deny access to
 * protected endpoints with HTTP 401.
 *
 * Note (Spring Boot 3 / Jakarta EE 10):
 *  - {@code javax.servlet.*} is replaced by {@code jakarta.servlet.*}.
 */
public class JwtTokenFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtTokenFilter.class);

    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtTokenProvider jwtTokenProvider;

    public JwtTokenFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    // ── Filter logic ───────────────────────────────────────────────────────

    @Override
    protected void doFilterInternal(HttpServletRequest  request,
                                    HttpServletResponse response,
                                    FilterChain         filterChain)
            throws ServletException, IOException {

        String token = resolveToken(request);

        if (StringUtils.hasText(token) && jwtTokenProvider.validateToken(token)) {
            Authentication auth = jwtTokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(auth);
            log.debug("Set Authentication for user '{}' from JWT",
                      jwtTokenProvider.getUsername(token));
        }

        filterChain.doFilter(request, response);
    }

    // ── Private helpers ────────────────────────────────────────────────────

    /**
     * Extracts the raw JWT from the {@code Authorization: Bearer <token>} header.
     *
     * @param request the current HTTP request
     * @return the JWT string, or {@code null} if the header is absent / malformed
     */
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(BEARER_PREFIX.length());
        }
        return null;
    }
}
