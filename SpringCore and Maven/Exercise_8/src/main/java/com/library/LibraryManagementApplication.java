package com.library;

import com.library.service.BookService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Main application bootstrap for Exercise 8.
 * Executes service methods to verify basic @Before and @After AOP logs.
 */
public class LibraryManagementApplication {
    public static void main(String[] args) {
        System.out.println("Initializing XML AOP context...");
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        
        BookService service = (BookService) context.getBean("bookService");
        
        System.out.println("Executing manageBooks()...");
        service.manageBooks();
    }
}
