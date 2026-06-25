/**
 * Represents a Product in the Inventory Management System.
 * Contains attributes like productId, productName, quantity, and price.
 */
public class Product {
    // Private attributes
    private int productId;
    private String productName;
    private int quantity;
    private double price;

    /**
     * Parameterized constructor to initialize a Product.
     *
     * @param productId   the unique ID of the product
     * @param productName the name of the product
     * @param quantity    the available quantity of the product
     * @param price       the price of the product
     */
    public Product(int productId, String productName, int quantity, double price) {
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
    }

    // Getters and Setters

    /**
     * Gets the unique product ID.
     * @return the product ID
     */
    public int getProductId() {
        return productId;
    }

    /**
     * Sets the unique product ID.
     * @param productId the product ID to set
     */
    public void setProductId(int productId) {
        this.productId = productId;
    }

    /**
     * Gets the product name.
     * @return the product name
     */
    public String getProductName() {
        return productName;
    }

    /**
     * Sets the product name.
     * @param productName the product name to set
     */
    public void setProductName(String productName) {
        this.productName = productName;
    }

    /**
     * Gets the product quantity.
     * @return the quantity of the product
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Sets the product quantity.
     * @param quantity the quantity to set
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * Gets the product price.
     * @return the price of the product
     */
    public double getPrice() {
        return price;
    }

    /**
     * Sets the product price.
     * @param price the price to set
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Returns a neatly formatted String representation of the product.
     * Formats:
     * ID : [productId]
     * Name : [productName]
     * Quantity : [quantity]
     * Price : ₹[price]
     */
    @Override
    public String toString() {
        return "ID : " + productId + "\n" +
               "Name : " + productName + "\n" +
               "Quantity : " + quantity + "\n" +
               "Price : ₹" + price;
    }
}
