public class DependencyInjectionTest {

    public static void main(String[] args) {
        System.out.println("=============================================");
        System.out.println("   Dependency Injection Pattern - Customer App");
        System.out.println("=============================================");
        System.out.println();

        CustomerRepository repository = new CustomerRepositoryImpl();

        CustomerService service = new CustomerService(repository);

        int targetId = 101;
        String customerName = service.findCustomerById(targetId);

        if (customerName != null) {
            System.out.println("Customer Found:");
            System.out.println("ID: " + targetId);
            System.out.println("Name: " + customerName);
        } else {
            System.out.println("Customer with ID " + targetId + " not found.");
        }

        System.out.println();
        System.out.println("--- Testing with a non-existent customer ID ---");
        int testId = 999;
        String testCustomer = service.findCustomerById(testId);
        if (testCustomer != null) {
            System.out.println("Customer Found:");
            System.out.println("ID: " + testId);
            System.out.println("Name: " + testCustomer);
        } else {
            System.out.println("Customer with ID " + testId + " not found in the repository.");
        }

        System.out.println();
        System.out.println("=============================================");
        System.out.println("   DI Demonstration completed successfully!  ");
        System.out.println("=============================================");
    }
}
