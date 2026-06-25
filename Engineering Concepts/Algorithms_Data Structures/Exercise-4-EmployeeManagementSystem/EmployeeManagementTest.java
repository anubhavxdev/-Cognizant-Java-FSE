/**
 * Test class to execute and verify the Employee Management System operations.
 */
public class EmployeeManagementTest {

    /**
     * Main method to run the Employee Management System tests.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        // Initialize the Employee Management System
        EmployeeManagementSystem system = new EmployeeManagementSystem();

        // 1. Add Employees
        system.addEmployee(new Employee(101, "Rahul", "Software Engineer", 75000.0));
        System.out.println();
        system.addEmployee(new Employee(102, "Priya", "HR Manager", 60000.0));
        System.out.println();
        system.addEmployee(new Employee(103, "Aman", "QA Engineer", 55000.0));
        System.out.println();
        system.addEmployee(new Employee(104, "Neha", "Project Manager", 90000.0));
        System.out.println();

        // 2. Display Employees
        System.out.println("========= EMPLOYEE LIST =========");
        System.out.println();
        system.displayEmployees();
        System.out.println();
        System.out.println("=================================");
        System.out.println();

        // 3. Search Employee: 102
        System.out.println("Searching Employee : 102");
        System.out.println();
        Employee foundEmployee = system.searchEmployee(102);
        if (foundEmployee != null) {
            System.out.println("Employee Found");
            System.out.println();
            System.out.println(foundEmployee);
        } else {
            System.out.println("Employee Not Found.");
        }
        System.out.println();

        // 4. Delete Employee: 103
        system.deleteEmployee(103);
        System.out.println();

        // 5. Display Employees Again
        System.out.println("========= UPDATED LIST =========");
        System.out.println();
        system.displayEmployees();
        System.out.println();
        System.out.println("=================================");
        System.out.println();

        // 6. Search Employee: 999
        System.out.println("Searching Employee : 999");
        System.out.println();
        Employee notFoundEmployee = system.searchEmployee(999);
        if (notFoundEmployee != null) {
            System.out.println("Employee Found");
            System.out.println();
            System.out.println(notFoundEmployee);
        } else {
            System.out.println("Employee Not Found.");
        }
    }
}
