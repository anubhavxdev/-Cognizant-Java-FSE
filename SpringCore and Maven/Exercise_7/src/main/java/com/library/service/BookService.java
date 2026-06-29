package com.library.service;

import com.library.repository.BookRepository;

/**
 * Service class demonstrating both Constructor Injection and Setter Injection.
 */
public class BookService {

    private BookRepository bookRepository;
    private String injectionType;

    // 1. Default constructor (Invoked for Setter Injection)
    public BookService() {
        System.out.println("BookService: Default constructor invoked.");
        this.injectionType = "Setter Injection";
    }

    // 2. Parametrized constructor (Invoked for Constructor Injection)
    public BookService(BookRepository bookRepository) {
        System.out.println("BookService: Parametrized constructor (Constructor Injection) invoked.");
        this.bookRepository = bookRepository;
        this.injectionType = "Constructor Injection";
    }

    // 3. Setter method (Invoked for Setter Injection)
    public void setBookRepository(BookRepository bookRepository) {
        System.out.println("BookService: setBookRepository() setter method invoked.");
        this.bookRepository = bookRepository;
    }

    public void manageBooks() {
        System.out.println("BookService: Managing catalog using: " + injectionType);
        if (bookRepository != null) {
            bookRepository.checkRepository();
        } else {
            System.out.println("BookRepository dependency is missing!");
        }
    }
}
