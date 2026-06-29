package com.library.repository;

/**
 * Simple repository bean used to verify AOP pointcuts.
 */
public class BookRepository {

    public void checkRepository() {
        System.out.println("BookRepository: Checking library database connection...");
    }
}
