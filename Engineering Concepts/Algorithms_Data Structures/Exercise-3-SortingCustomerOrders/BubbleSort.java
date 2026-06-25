/**
 * Class containing the Bubble Sort implementation for sorting Orders.
 */
public class BubbleSort {

    /**
     * Sorts an array of Orders in ascending order based on total price using Bubble Sort.
     *
     * @param orders the array of Order objects to sort
     */
    public static void sort(Order[] orders) {
        // Base case: check if array is null or has fewer than 2 elements
        if (orders == null || orders.length < 2) {
            return;
        }

        int n = orders.length;
        boolean swapped;

        // Outer loop: performs (n - 1) passes through the array
        for (int i = 0; i < n - 1; i++) {
            swapped = false; // Reset the swapped flag for each pass

            // Inner loop: performs pairwise adjacent comparisons
            // The range decreases by i in each pass because the largest elements bubble to the end
            for (int j = 0; j < n - 1 - i; j++) {
                
                // Retrieve adjacent orders
                Order current = orders[j];
                Order next = orders[j + 1];

                // Check if current order's price is greater than next order's price
                if (current.getTotalPrice() > next.getTotalPrice()) {
                    // Swap the two order objects
                    orders[j] = next;
                    orders[j + 1] = current;
                    
                    // Mark swapped as true to indicate a modification was made in this pass
                    swapped = true;
                }
            }

            // Optimization: If no elements were swapped in this pass, the array is already sorted.
            // We can break early to avoid unnecessary comparisons.
            if (!swapped) {
                break;
            }
        }
    }
}
