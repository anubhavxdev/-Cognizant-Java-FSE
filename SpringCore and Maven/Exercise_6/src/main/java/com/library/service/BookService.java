package com.library.service;

import com.library.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service class participating in component scanning.
 * Annotated with @Service and utilizes @Autowired on setter injection.
 */
@Service
public class BookService {

    private BookRepository bookRepository;

    // Autowired annotation instructs the container to perform automatic injection
    @Autowired
    public void setBookRepository(BookRepository bookRepository) {
        System.out.println("BookService: @Autowired setter method executed. BookRepository dependency injected.");
        this.bookRepository = bookRepository;
    }

    public void manageBooks() {
        System.out.println("BookService: Managing library catalog...");
        if (bookRepository != null) {
            bookRepository.checkRepository();
        }
    }
}
