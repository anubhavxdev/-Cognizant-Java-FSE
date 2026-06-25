/**
 * Test driver class to compare Bubble Sort and Quick Sort algorithms on Orders.
 */
public class SortingTest {

    /**
     * Main method to execute the sorting tests.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        // 1. Instantiate the initial array of orders
        Order[] orders = getSampleOrders();

        // 2. Display Original Orders
        System.out.println("========= ORIGINAL ORDERS =========");
        System.out.println();
        printOrders(orders);
        System.out.println("===============================");
        System.out.println();

        // 3. Perform Bubble Sort and display results
        BubbleSort.sort(orders);
        System.out.println("========= BUBBLE SORT =========");
        System.out.println();
        printOrders(orders);
        System.out.println("===============================");
        System.out.println();

        // 4. Recreate/Reset the original unsorted array of orders
        orders = getSampleOrders();

        // 5. Perform Quick Sort and display results
        QuickSort.quickSort(orders, 0, orders.length - 1);
        System.out.println("========= QUICK SORT =========");
        System.out.println();
        printOrders(orders);
    }

    /**
     * Helper method to generate the sample dataset of unsorted orders.
     *
     * @return a new array of unsorted Order objects
     */
    private static Order[] getSampleOrders() {
        return new Order[]{
            new Order(101, "Rahul", 2500.0),
            new Order(102, "Priya", 1200.0),
            new Order(103, "Aman", 8900.0),
            new Order(104, "Neha", 4200.0),
            new Order(105, "Karan", 3000.0)
        };
    }

    /**
     * Helper method to print order details in the requested space-separated format with newlines.
     * Output format: [orderId] [customerName] [totalPrice as integer]
     *
     * @param orders the array of Order objects to print
     */
    private static void printOrders(Order[] orders) {
        for (Order order : orders) {
            if (order != null) {
                System.out.println(order.getOrderId() + " " + order.getCustomerName() + " " + (int) order.getTotalPrice());
                System.out.println();
            }
        }
    }
}
