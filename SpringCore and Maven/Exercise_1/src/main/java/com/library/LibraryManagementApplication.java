package com.library;

import com.library.service.BookService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Main application bootstrap for Exercise 1.
 * Loads context from applicationContext.xml and executes catalog actions.
 */
public class LibraryManagementApplication {
    public static void main(String[] args) {
        // Instantiate the XML ClassPath Context container
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        
        // Fetch the BookService bean from container
        BookService service = (BookService) context.getBean("bookService");
        
        // Invoke service action
        service.manageBooks();
    }
}
