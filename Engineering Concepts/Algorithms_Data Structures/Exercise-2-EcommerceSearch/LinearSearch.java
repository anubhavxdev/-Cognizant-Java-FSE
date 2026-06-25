/**
 * Class containing the Linear Search implementation.
 */
public class LinearSearch {

    /**
     * Searches for a product in an array by its ID using Linear Search.
     *
     * @param products the array of products to search in
     * @param id       the ID of the product to search for
     * @return the matching Product, or null if not found
     */
    public static Product search(Product[] products, int id) {
        // Check if the array is null or empty
        if (products == null || products.length == 0) {
            return null;
        }

        // Iterate sequentially through the array from the first index (0) to the last (n - 1)
        for (int i = 0; i < products.length; i++) {
            // Retrieve the current product at index i
            Product currentProduct = products[i];

            // If the current product is not null and its ID matches the target ID, return it
            if (currentProduct != null && currentProduct.getProductId() == id) {
                return currentProduct; // Product found, return immediately
            }
        }

        // If the entire array has been traversed and no match was found, return null
        return null;
    }
}
