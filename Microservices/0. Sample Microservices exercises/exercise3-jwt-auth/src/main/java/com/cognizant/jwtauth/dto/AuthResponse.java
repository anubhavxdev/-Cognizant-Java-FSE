package com.cognizant.jwtauth.dto;

/**
 * AuthResponse – Exercise 3
 *
 * Login response payload containing the signed JWT.
 * Using a Java record for immutable, concise DTOs.
 *
 * Example JSON:
 * <pre>
 * {
 *   "token": "eyJhbGciOiJIUzI1NiJ9..."
 * }
 * </pre>
 */
public record AuthResponse(String token) {}
