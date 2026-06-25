/**
 * Represents a Customer Order.
 * Contains order ID, customer name, and total price.
 */
public class Order {
    // Private attributes
    private int orderId;
    private String customerName;
    private double totalPrice;

    /**
     * Parameterized constructor to initialize an Order.
     *
     * @param orderId      the unique ID of the order
     * @param customerName the name of the customer who placed the order
     * @param totalPrice   the total monetary value of the order
     */
    public Order(int orderId, String customerName, double totalPrice) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.totalPrice = totalPrice;
    }

    // Getters and Setters

    /**
     * Gets the order ID.
     * @return the order ID
     */
    public int getOrderId() {
        return orderId;
    }

    /**
     * Sets the order ID.
     * @param orderId the order ID to set
     */
    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    /**
     * Gets the customer name.
     * @return the customer name
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     * Sets the customer name.
     * @param customerName the customer name to set
     */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    /**
     * Gets the total price of the order.
     * @return the total price
     */
    public double getTotalPrice() {
        return totalPrice;
    }

    /**
     * Sets the total price of the order.
     * @param totalPrice the total price to set
     */
    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    /**
     * Returns a neatly formatted String representation of the order.
     * Formats:
     * Order ID : [orderId]
     * Customer : [customerName]
     * Total Price : ₹[totalPrice]
     */
    @Override
    public String toString() {
        return "Order ID : " + orderId + "\n" +
               "Customer : " + customerName + "\n" +
               "Total Price : ₹" + String.format("%.2f", totalPrice);
    }
}
