/**
 * Represents a Product in the E-commerce platform.
 * Contains attributes like productId, productName, and category.
 */
public class Product {
    // Private attributes
    private int productId;
    private String productName;
    private String category;

    /**
     * Parameterized constructor to initialize a Product.
     *
     * @param productId   the unique ID of the product
     * @param productName the name of the product
     * @param category    the category of the product
     */
    public Product(int productId, String productName, String category) {
        this.productId = productId;
        this.productName = productName;
        this.category = category;
    }

    // Getters and Setters

    /**
     * Gets the product ID.
     * @return the product ID
     */
    public int getProductId() {
        return productId;
    }

    /**
     * Sets the product ID.
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
     * Gets the product category.
     * @return the category of the product
     */
    public String getCategory() {
        return category;
    }

    /**
     * Sets the product category.
     * @param category the category to set
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * Returns a neatly formatted String representation of the product.
     * Formats:
     * ID : [productId]
     * Name : [productName]
     * Category : [category]
     */
    @Override
    public String toString() {
        return "ID : " + productId + "\n" +
               "Name : " + productName + "\n" +
               "Category : " + category;
    }
}
