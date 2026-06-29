package com.library.repository;

import org.springframework.stereotype.Repository;

/**
 * Repository class participating in component scanning.
 * Annotated with @Repository stereotype annotation.
 */
@Repository
public class BookRepository {

    public void checkRepository() {
        System.out.println("BookRepository: Checking library database connection... (Scan Successful)");
    }
}
