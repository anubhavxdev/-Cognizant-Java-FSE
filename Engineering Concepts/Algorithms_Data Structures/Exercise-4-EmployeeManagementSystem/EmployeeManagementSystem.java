/**
 * Manages employee records using a fixed-size array.
 * Provides methods to add, search, display, and delete employees.
 */
public class EmployeeManagementSystem {
    // Fixed-size array to store employees
    private Employee[] employees;
    // Counter to track the number of active employees
    private int count;

    /**
     * Constructor to initialize the Employee Management System with a default capacity of 100.
     */
    public EmployeeManagementSystem() {
        this.employees = new Employee[100];
        this.count = 0;
    }

    /**
     * Adds an employee to the system.
     *
     * @param employee the Employee object to add
     */
    public void addEmployee(Employee employee) {
        // Check if the array has reached its maximum capacity
        if (count >= employees.length) {
            System.out.println("Array Full. Cannot add employee.");
            return;
        }
        
        // Add the employee at the next available position and increment the count
        employees[count] = employee;
        count++;
        System.out.println("Employee Added Successfully.");
    }

    /**
     * Searches for an employee by their ID using Linear Search.
     *
     * @param employeeId the ID of the employee to search for
     * @return the Employee object if found, or null if not found
     */
    public Employee searchEmployee(int employeeId) {
        // Perform a linear search through the active elements of the array
        for (int i = 0; i < count; i++) {
            // Compare the ID of each employee with the search ID
            if (employees[i].getEmployeeId() == employeeId) {
                return employees[i]; // Return employee immediately if found
            }
        }
        // Return null if search finishes without a match
        return null;
    }

    /**
     * Displays all active employee records.
     */
    public void displayEmployees() {
        // Check if the system is empty
        if (count == 0) {
            System.out.println("No Employees Available.");
            return;
        }

        // Print each employee followed by a horizontal divider
        for (int i = 0; i < count; i++) {
            System.out.println(employees[i]);
            
            // Print a divider between elements, but omit it after the last element
            if (i < count - 1) {
                System.out.println();
                System.out.println("--------------------------");
                System.out.println();
            }
        }
    }

    /**
     * Deletes an employee by their ID.
     * Shifts subsequent elements to the left to maintain contiguous memory storage.
     *
     * @param employeeId the ID of the employee to delete
     */
    public void deleteEmployee(int employeeId) {
        int deleteIndex = -1;

        // Find the index of the employee to delete
        for (int i = 0; i < count; i++) {
            if (employees[i].getEmployeeId() == employeeId) {
                deleteIndex = i;
                break;
            }
        }

        // If the employee exists in the array
        if (deleteIndex != -1) {
            // Shift all elements to the left by one position to overwrite the deleted employee
            for (int j = deleteIndex; j < count - 1; j++) {
                employees[j] = employees[j + 1];
            }
            
            // Set the last element to null to prevent memory leak
            employees[count - 1] = null;
            
            // Decrement the active employee count
            count--;
            System.out.println("Employee Deleted Successfully.");
        } else {
            // Print error if the employee ID was not found
            System.out.println("Employee Not Found.");
        }
    }
}
