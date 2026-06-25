/**
 * Class containing the Binary Search implementation for library books.
 */
public class BinarySearch {

    /**
     * Searches for a book by its title in a sorted array using Binary Search.
     * Compares titles alphabetically ignoring case considerations.
     *
     * @param books the sorted array of books (alphabetical order by title)
     * @param title the title of the book to search for
     * @return the matching Book object, or null if not found
     */
    public static Book search(Book[] books, String title) {
        // Check if the array is null or empty, or if the search title is null
        if (books == null || books.length == 0 || title == null) {
            return null;
        }

        // Initialize low pointer to index 0 (start of the array)
        int low = 0;
        
        // Initialize high pointer to index n - 1 (end of the array)
        int high = books.length - 1;

        // Loop as long as the search window is valid (low does not cross high)
        while (low <= high) {
            // Compute the midpoint index of the search space.
            // Using (low + (high - low) / 2) to prevent potential integer overflow.
            int mid = low + (high - low) / 2;
            Book midBook = books[mid];

            // If the middle element is null, safety check to return null
            if (midBook == null) {
                return null;
            }

            // Compare the title of the middle book with the search title ignoring case
            int comparison = midBook.getTitle().compareToIgnoreCase(title);

            // Case 1: The middle book's title is equal to the target title
            if (comparison == 0) {
                return midBook; // Book found, return it
            }

            // Case 2: The middle book's title is alphabetically smaller than the target title.
            // This means our target lies in the right half of the array.
            // Shift the low pointer to mid + 1
            if (comparison < 0) {
                low = mid + 1;
            } 
            // Case 3: The middle book's title is alphabetically larger than the target title.
            // This means our target lies in the left half of the array.
            // Shift the high pointer to mid - 1
            else {
                high = mid - 1;
            }
        }

        // Return null if search space is exhausted without finding a match
        return null;
    }
}
