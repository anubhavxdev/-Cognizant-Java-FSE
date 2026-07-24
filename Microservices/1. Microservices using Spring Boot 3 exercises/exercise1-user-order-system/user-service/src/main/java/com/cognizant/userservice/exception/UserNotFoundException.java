package com.cognizant.userservice.exception;

/** Thrown when a requested user is not found in the database. */
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long id) {
        super("User not found with id: " + id);
    }
    public UserNotFoundException(String email) {
        super("User not found with email: " + email);
    }
}
