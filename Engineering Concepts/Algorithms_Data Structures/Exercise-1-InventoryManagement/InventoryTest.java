/**
 * Test class for testing the Inventory Management System.
 * Performs additions, updates, deletions, and displays of products.
 */
public class InventoryTest {
    /**
     * Main method to execute the test scenarios.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        // Instantiate the InventoryManager
        InventoryManager manager = new InventoryManager();

        // 1. Add Product: 101 Laptop, 15, 65000
        manager.addProduct(new Product(101, "Laptop", 15, 65000.0));
        System.out.println();

        // 2. Add Product: 102 Keyboard, 50, 1200
        manager.addProduct(new Product(102, "Keyboard", 50, 1200.0));
        System.out.println();

        // 3. Add Product: 103 Mouse, 100, 700
        manager.addProduct(new Product(103, "Mouse", 100, 700.0));
        System.out.println();

        // 4. Display Inventory
        manager.displayProducts();

        // 5. Update Product: 102 Mechanical Keyboard, 40, 1800
        manager.updateProduct(102, "Mechanical Keyboard", 40, 1800.0);
        System.out.println();

        // 6. Display Inventory
        manager.displayProducts();

        // 7. Delete Product: 103
        manager.deleteProduct(103);
        System.out.println();

        // 8. Display Inventory
        manager.displayProducts();
    }
}
