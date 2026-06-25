/**
 * Represents a Book in the library.
 * Holds attributes like bookId, title, and author.
 */
public class Book {
    // Private attributes
    private int bookId;
    private String title;
    private String author;

    /**
     * Parameterized constructor to initialize a Book.
     *
     * @param bookId the unique ID of the book
     * @param title  the title of the book
     * @param author the author of the book
     */
    public Book(int bookId, String title, String author) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
    }

    // Getters and Setters

    /**
     * Gets the book ID.
     * @return the book ID
     */
    public int getBookId() {
        return bookId;
    }

    /**
     * Sets the book ID.
     * @param bookId the book ID to set
     */
    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    /**
     * Gets the book title.
     * @return the book title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the book title.
     * @param title the book title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the book author.
     * @return the book author
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Sets the book author.
     * @param author the book author to set
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * Returns a neatly formatted String representation of the Book.
     * Formats attributes double-spaced to match the expected console output:
     * Book ID : [bookId]
     *
     * Title : [title]
     *
     * Author : [author]
     */
    @Override
    public String toString() {
        return "Book ID : " + bookId + "\n\n" +
               "Title : " + title + "\n\n" +
               "Author : " + author;
    }
}
