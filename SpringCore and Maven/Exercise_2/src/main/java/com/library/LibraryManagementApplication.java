package com.library;

import com.library.service.BookService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Main application bootstrap for Exercise 2.
 * Instantiates the XML context, loading bean properties via Setter Injection.
 */
public class LibraryManagementApplication {
    public static void main(String[] args) {
        System.out.println("Initializing XML ApplicationContext...");
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        
        System.out.println("Retrieving bookService bean...");
        BookService service = (BookService) context.getBean("bookService");
        
        System.out.println("Executing service operations...");
        service.manageBooks();
    }
}
