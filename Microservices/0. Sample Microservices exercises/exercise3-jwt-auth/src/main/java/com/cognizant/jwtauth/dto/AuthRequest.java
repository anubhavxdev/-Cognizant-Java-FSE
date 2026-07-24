package com.cognizant.jwtauth.dto;

/**
 * AuthRequest – Exercise 3
 *
 * Incoming login request payload.
 * Using a Java record (Java 16+) for immutable, concise DTOs.
 *
 * Example JSON:
 * <pre>
 * {
 *   "username": "user",
 *   "password": "password"
 * }
 * </pre>
 */
public record AuthRequest(String username, String password) {}
