/**
 * Class containing the Linear Search implementation for library books.
 */
public class LinearSearch {

    /**
     * Searches for a book by its title in an array using Linear Search.
     * Compares titles using case-insensitive matching.
     *
     * @param books the array of books to search in
     * @param title the title of the book to search for
     * @return the matching Book object, or null if not found
     */
    public static Book search(Book[] books, String title) {
        // Check if the array is null or empty, or if the search title is null
        if (books == null || books.length == 0 || title == null) {
            return null;
        }

        // Iterate through the array of books sequentially from index 0 to n - 1
        for (int i = 0; i < books.length; i++) {
            Book currentBook = books[i];

            // If the current book is not null and its title matches the target title (ignoring case)
            if (currentBook != null && currentBook.getTitle().equalsIgnoreCase(title)) {
                return currentBook; // Matching book found, return it immediately
            }
        }

        // If the entire array has been traversed and no match was found, return null
        return null;
    }
}
