package com.library;

import com.library.repository.BookRepository;
import com.library.service.BookService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Main application bootstrap for Exercise 5.
 * Demonstrates eagerly loaded Singleton beans versus lazy/repeatedly loaded Prototype beans.
 */
public class LibraryManagementApplication {
    public static void main(String[] args) {
        System.out.println("--- STEP 1: INITIALIZING CONTAINER STARTUP ---");
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        System.out.println("--- CONTAINER STARTUP COMPLETED ---\n");

        System.out.println("--- STEP 2: DEMONSTRATING SINGLETON SCOPE (BookRepository) ---");
        // Fetch BookRepository bean twice
        BookRepository repo1 = (BookRepository) context.getBean("bookRepository");
        BookRepository repo2 = (BookRepository) context.getBean("bookRepository");

        // Compare references (should point to the same object)
        System.out.println("Are both BookRepository references equal? " + (repo1 == repo2));
        System.out.println("repo1 memory hash: " + repo1.hashCode());
        System.out.println("repo2 memory hash: " + repo2.hashCode() + "\n");

        System.out.println("--- STEP 3: DEMONSTRATING PROTOTYPE SCOPE (BookService) ---");
        // Fetch BookService bean twice
        BookService service1 = (BookService) context.getBean("bookService");
        BookService service2 = (BookService) context.getBean("bookService");

        // Compare references (should point to different objects)
        System.out.println("Are both BookService references equal? " + (service1 == service2));
        System.out.println("service1 memory hash: " + service1.hashCode());
        System.out.println("service2 memory hash: " + service2.hashCode() + "\n");
    }
}
