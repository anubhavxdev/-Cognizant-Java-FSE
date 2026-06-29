package com.library;

import com.library.service.BookService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Main application bootstrap for Exercise 3.
 * Runs BookService actions, verifying the console output for AOP timing logging.
 */
public class LibraryManagementApplication {
    public static void main(String[] args) {
        System.out.println("Loading XML Context for AOP demonstration...");
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        
        BookService service = (BookService) context.getBean("bookService");
        
        System.out.println("Invoking BookService.manageBooks()...");
        service.manageBooks();
    }
}
