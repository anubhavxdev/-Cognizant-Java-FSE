package com.library.repository;

/**
 * Repository class demonstrating Bean loading behaviors.
 */
public class BookRepository {

    public BookRepository() {
        System.out.println("[IOC INSTANTIATION] BookRepository: Default Constructor Executed.");
    }

    public void checkRepository() {
        System.out.println("BookRepository: Checking library database connection...");
    }
}
