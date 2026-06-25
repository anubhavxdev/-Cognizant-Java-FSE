/**
 * Driver class to test the search algorithms of the Library Management System.
 */
public class LibraryManagementTest {

    /**
     * Main method to run the library search test flows.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        // Create an array of books sorted alphabetically by title
        Book[] books = {
            new Book(101, "Clean Code", "Robert C. Martin"),
            new Book(102, "Design Patterns", "Gang of Four"),
            new Book(103, "Effective Java", "Joshua Bloch"),
            new Book(104, "Java Complete Reference", "Herbert Schildt"),
            new Book(105, "Thinking in Java", "Bruce Eckel")
        };

        // 1. Display all books in the library
        System.out.println("========= LIBRARY BOOKS =========");
        System.out.println();
        for (int i = 0; i < books.length; i++) {
            System.out.println(books[i]);
            if (i < books.length - 1) {
                System.out.println();
                System.out.println("----------------------------");
                System.out.println();
            }
        }
        System.out.println();
        System.out.println("===============================");
        System.out.println();

        // 2. Perform Linear Search for "Effective Java"
        System.out.println("========= LINEAR SEARCH =========");
        System.out.println();
        System.out.println("Searching: Effective Java");
        System.out.println();
        Book result1 = LinearSearch.search(books, "Effective Java");
        if (result1 != null) {
            System.out.println("Book Found");
            System.out.println();
            System.out.println(result1);
        } else {
            System.out.println("Book Not Found.");
        }
        System.out.println();
        System.out.println("===============================");
        System.out.println();

        // 3. Perform Binary Search for "Java Complete Reference"
        System.out.println("========= BINARY SEARCH =========");
        System.out.println();
        System.out.println("Searching: Java Complete Reference");
        System.out.println();
        Book result2 = BinarySearch.search(books, "Java Complete Reference");
        if (result2 != null) {
            System.out.println("Book Found");
            System.out.println();
            System.out.println(result2);
        } else {
            System.out.println("Book Not Found.");
        }
        System.out.println();
        System.out.println("===============================");
        System.out.println();

        // 4. Search for a non-existent book
        System.out.println("Searching: Python Programming");
        System.out.println();
        Book result3 = BinarySearch.search(books, "Python Programming");
        if (result3 != null) {
            System.out.println("Book Found");
            System.out.println();
            System.out.println(result3);
        } else {
            System.out.println("Book Not Found.");
        }
    }
}
