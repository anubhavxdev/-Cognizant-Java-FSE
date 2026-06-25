import java.util.HashMap;

/**
 * Manages the warehouse inventory.
 * Internally uses a HashMap<Integer, Product> for efficient operations.
 */
public class InventoryManager {
    // HashMap to store products with product ID as key
    private HashMap<Integer, Product> products;

    /**
     * Constructor to initialize the inventory manager.
     */
    public InventoryManager() {
        this.products = new HashMap<>();
    }

    /**
     * Adds a new product to the inventory.
     *
     * @param product the product to add
     */
    public void addProduct(Product product) {
        if (products.containsKey(product.getProductId())) {
            System.out.println("Product already exists.");
        } else {
            products.put(product.getProductId(), product);
            System.out.println("Product Added Successfully.");
        }
    }

    /**
     * Updates an existing product's attributes.
     *
     * @param id       the ID of the product to update
     * @param name     the new name of the product
     * @param quantity the new quantity of the product
     * @param price    the new price of the product
     */
    public void updateProduct(int id, String name, int quantity, double price) {
        if (products.containsKey(id)) {
            Product product = products.get(id);
            product.setProductName(name);
            product.setQuantity(quantity);
            product.setPrice(price);
            System.out.println("Product Updated Successfully.");
        } else {
            System.out.println("Product Not Found.");
        }
    }

    /**
     * Deletes a product from the inventory by its ID.
     *
     * @param id the ID of the product to delete
     */
    public void deleteProduct(int id) {
        if (products.containsKey(id)) {
            products.remove(id);
            System.out.println("Product Deleted Successfully.");
        } else {
            System.out.println("Product Not Found.");
        }
    }

    /**
     * Displays all the products present in the inventory.
     */
    public void displayProducts() {
        if (products.isEmpty()) {
            System.out.println("Inventory Empty");
            return;
        }
        System.out.println("========== INVENTORY ==========");
        System.out.println();
        for (Product product : products.values()) {
            System.out.println(product);
            System.out.println();
        }
    }
}
