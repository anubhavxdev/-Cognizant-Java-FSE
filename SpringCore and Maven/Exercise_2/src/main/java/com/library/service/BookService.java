package com.library.service;

import com.library.repository.BookRepository;

/**
 * Service class demonstrating IoC and Setter Dependency Injection.
 * Wired through property tag references in applicationContext.xml.
 */
public class BookService {

    private BookRepository bookRepository;

    // Default constructor to verify instantiation order
    public BookService() {
        System.out.println("BookService: Instance created via default constructor.");
    }

    // Setter method for dependency injection
    public void setBookRepository(BookRepository bookRepository) {
        System.out.println("BookService: setBookRepository() setter invoked. Dependency injected successfully.");
        this.bookRepository = bookRepository;
    }

    public void manageBooks() {
        System.out.println("BookService: Managing library catalog...");
        if (bookRepository != null) {
            bookRepository.checkRepository();
        } else {
            System.out.println("BookRepository dependency is missing!");
        }
    }
}
