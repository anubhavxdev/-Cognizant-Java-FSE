package com.library.service;

import com.library.repository.BookRepository;

/**
 * Service class targeted by Aspect advices to verify @Before and @After executions.
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
    }
}
