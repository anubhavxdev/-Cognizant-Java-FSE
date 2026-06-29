package com.library.service;

import com.library.repository.BookRepository;

/**
 * Service class targeted by Aspect advices to record execution speed.
 */
public class BookService {

    private BookRepository bookRepository;

    public void setBookRepository(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public void manageBooks() {
        System.out.println("BookService: Managing library catalog...");
        if (bookRepository != null) {
            bookRepository.checkRepository();
        }
        try {
            // Sleep briefly to simulate catalog processing delay
            Thread.sleep(30);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
