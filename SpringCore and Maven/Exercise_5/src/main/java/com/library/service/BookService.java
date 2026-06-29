package com.library.service;

import com.library.repository.BookRepository;

/**
 * Service class demonstrating IoC bean scopes (Prototype vs Singleton).
 */
public class BookService {

    private BookRepository bookRepository;

    public BookService() {
        System.out.println("[IOC INSTANTIATION] BookService: Default Constructor Executed.");
    }

    public void setBookRepository(BookRepository bookRepository) {
        System.out.println("[IOC DI] BookService: setBookRepository() setter invoked.");
        this.bookRepository = bookRepository;
    }

    public void manageBooks() {
        System.out.println("BookService: Managing library catalog...");
        if (bookRepository != null) {
            bookRepository.checkRepository();
        }
    }
}
