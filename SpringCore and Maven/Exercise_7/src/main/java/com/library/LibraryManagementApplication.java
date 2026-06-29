package com.library;

import com.library.service.BookService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Main application bootstrap for Exercise 7.
 * Fetches and compares the execution outputs of Constructor-injected and Setter-injected service beans.
 */
public class LibraryManagementApplication {
    public static void main(String[] args) {
        System.out.println("Initializing XML Context...");
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        System.out.println("XML Context initialized successfully.\n");

        System.out.println("--- DEMONSTRATING CONSTRUCTOR INJECTION ---");
        BookService serviceConst = (BookService) context.getBean("bookServiceConstructor");
        serviceConst.manageBooks();
        System.out.println();

        System.out.println("--- DEMONSTRATING SETTER INJECTION ---");
        BookService serviceSetter = (BookService) context.getBean("bookServiceSetter");
        serviceSetter.manageBooks();
        System.out.println();
    }
}
