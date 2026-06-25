/**
 * Class containing the Binary Search implementation.
 */
public class BinarySearch {

    /**
     * Searches for a product in a sorted array by its ID using Binary Search.
     *
     * @param products the sorted array of products to search in
     * @param id       the ID of the product to search for
     * @return the matching Product, or null if not found
     */
    public static Product search(Product[] products, int id) {
        // Check if the array is null or empty
        if (products == null || products.length == 0) {
            return null;
        }

        // Initialize low pointer to the beginning of the array (index 0)
        int low = 0;

        // Initialize high pointer to the end of the array (index n - 1)
        int high = products.length - 1;

        // Loop as long as the search interval is valid (low pointer is less than or equal to high pointer)
        while (low <= high) {
            // Calculate the middle index to split the search interval.
            // Using (low + (high - low) / 2) instead of ((low + high) / 2) to prevent potential integer overflow.
            int mid = low + (high - low) / 2;

            // Retrieve the product at the middle index
            Product midProduct = products[mid];

            // If the product at mid is null, we can skip or treat it, but assuming well-formed array
            if (midProduct == null) {
                // If there are null entries in the array, it breaks standard binary search assumptions.
                // Assuming array has no null elements, but handles safely.
                return null;
            }

            // Retrieve the ID of the product at mid index
            int midId = midProduct.getProductId();

            // Check if we found the target ID
            if (midId == id) {
                return midProduct; // Target product found, return it
            }

            // If the target ID is greater than the mid ID, ignore the left half
            // Shift the low pointer to mid + 1
            if (midId < id) {
                low = mid + 1;
            } 
            // If the target ID is smaller than the mid ID, ignore the right half
            // Shift the high pointer to mid - 1
            else {
                high = mid - 1;
            }
        }

        // If the low pointer exceeds the high pointer, the target is not in the array
        return null;
    }
}
