/**
 * Class containing the Quick Sort implementation for sorting Orders.
 */
public class QuickSort {

    /**
     * Recursively sorts an array of Orders in ascending order based on total price using Quick Sort.
     *
     * @param orders the array of Order objects to sort
     * @param low    the starting index of the partition to sort
     * @param high   the ending index of the partition to sort
     */
    public static void quickSort(Order[] orders, int low, int high) {
        // Base case: execute only if the partition size is greater than 1
        if (low < high) {
            // Find the partitioning index. 
            // Elements smaller than pivot go left, elements larger than pivot go right.
            int pi = partition(orders, low, high);

            // Recursively sort the sub-array before partition
            quickSort(orders, low, pi - 1);

            // Recursively sort the sub-array after partition
            quickSort(orders, pi + 1, high);
        }
    }

    /**
     * Partitions the array around a pivot (last element).
     * Rearranges elements so that all elements with total price less than or equal to pivot's price
     * are placed to the left, and elements with price greater are placed to the right.
     *
     * @param orders the array of Order objects
     * @param low    the starting index of the partition
     * @param high   the ending index of the partition (contains the pivot element)
     * @return the correct sorted index of the pivot element
     */
    private static int partition(Order[] orders, int low, int high) {
        // Choose the last element's price as the pivot value
        double pivotPrice = orders[high].getTotalPrice();
        
        // Pointer for elements smaller than the pivot.
        // It starts just before the left boundary.
        int i = low - 1;

        // Iterate through elements from low to high - 1
        for (int j = low; j < high; j++) {
            // If the current element's price is less than or equal to the pivot's price
            if (orders[j].getTotalPrice() <= pivotPrice) {
                i++; // Increment pointer for smaller elements
                swap(orders, i, j); // Swap current element into the smaller elements section
            }
        }

        // Place the pivot element (currently at index 'high') at its correct sorted position (index i + 1)
        swap(orders, i + 1, high);

        // Return the final index of the pivot element
        return i + 1;
    }

    /**
     * Swaps two Order elements in the array.
     *
     * @param orders the array of Order objects
     * @param i      the index of the first element
     * @param j      the index of the second element
     */
    private static void swap(Order[] orders, int i, int j) {
        Order temp = orders[i];
        orders[i] = orders[j];
        orders[j] = temp;
    }
}
