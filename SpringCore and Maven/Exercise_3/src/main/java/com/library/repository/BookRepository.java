package com.library.repository;

/**
 * Simple repository bean used to verify logging aspect performance monitoring.
 */
public class BookRepository {

    public void checkRepository() {
        System.out.println("BookRepository: Checking library database connection...");
        try {
            // Sleep briefly to simulate database latency for execution logging
            Thread.sleep(50);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
