/**
 * Driver class to test the search algorithms on the E-commerce platform.
 */
public class SearchTest {

    /**
     * Main method to run the search comparison.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        // Create a sorted Product array as specified in the example
        Product[] products = {
            new Product(101, "Laptop", "Electronics"),
            new Product(102, "Mouse", "Electronics"),
            new Product(103, "Keyboard", "Electronics"),
            new Product(104, "Monitor", "Electronics"),
            new Product(105, "Printer", "Electronics")
        };

        // 1. Perform Linear Search for Product ID: 103
        System.out.println("========= LINEAR SEARCH =========");
        System.out.println();
        System.out.println("Searching Product ID : 103");
        System.out.println();
        
        Product result1 = LinearSearch.search(products, 103);
        if (result1 != null) {
            System.out.println("Product Found");
            System.out.println();
            System.out.println("ID : " + result1.getProductId());
            System.out.println();
            System.out.println("Name : " + result1.getProductName());
            System.out.println();
            System.out.println("Category : " + result1.getCategory());
        } else {
            System.out.println("Product Not Found");
        }
        System.out.println();
        System.out.println("===============================");
        System.out.println();

        // 2. Perform Binary Search for Product ID: 104
        System.out.println("========= BINARY SEARCH =========");
        System.out.println();
        System.out.println("Searching Product ID : 104");
        System.out.println();

        Product result2 = BinarySearch.search(products, 104);
        if (result2 != null) {
            System.out.println("Product Found");
            System.out.println();
            System.out.println("ID : " + result2.getProductId());
            System.out.println();
            System.out.println("Name : " + result2.getProductName());
            System.out.println();
            System.out.println("Category : " + result2.getCategory());
        } else {
            System.out.println("Product Not Found");
        }
        System.out.println();
        System.out.println("===============================");
        System.out.println();

        // 3. Perform Search for non-existent Product ID: 999
        System.out.println("Searching Product ID : 999");
        System.out.println();
        
        Product result3 = BinarySearch.search(products, 999);
        if (result3 != null) {
            System.out.println("Product Found");
            System.out.println();
            System.out.println("ID : " + result3.getProductId());
            System.out.println();
            System.out.println("Name : " + result3.getProductName());
            System.out.println();
            System.out.println("Category : " + result3.getCategory());
        } else {
            System.out.println("Product Not Found");
        }
    }
}
