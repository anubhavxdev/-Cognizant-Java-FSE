package com.library;

import com.library.service.BookService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Main application bootstrap for Exercise 6.
 * Loads scanned components and runs service tasks.
 */
public class LibraryManagementApplication {
    public static void main(String[] args) {
        System.out.println("Initializing context with Component Scanning...");
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        
        System.out.println("Retrieving autowired BookService...");
        BookService service = context.getBean(BookService.class);
        
        service.manageBooks();
    }
}
