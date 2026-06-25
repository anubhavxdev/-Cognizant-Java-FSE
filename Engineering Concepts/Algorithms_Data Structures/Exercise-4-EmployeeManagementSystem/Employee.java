/**
 * Represents an Employee in the organization.
 * Holds details such as employee ID, name, position, and salary.
 */
public class Employee {
    // Private attributes
    private int employeeId;
    private String name;
    private String position;
    private double salary;

    /**
     * Parameterized constructor to initialize an Employee.
     *
     * @param employeeId the unique ID of the employee
     * @param name       the name of the employee
     * @param position   the job role/position of the employee
     * @param salary     the monthly or annual salary of the employee
     */
    public Employee(int employeeId, String name, String position, double salary) {
        this.employeeId = employeeId;
        this.name = name;
        this.position = position;
        this.salary = salary;
    }

    // Getters and Setters

    /**
     * Gets the employee ID.
     * @return the employee ID
     */
    public int getEmployeeId() {
        return employeeId;
    }

    /**
     * Sets the employee ID.
     * @param employeeId the employee ID to set
     */
    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    /**
     * Gets the employee's name.
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the employee's name.
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the employee's position.
     * @return the position
     */
    public String getPosition() {
        return position;
    }

    /**
     * Sets the employee's position.
     * @param position the position to set
     */
    public void setPosition(String position) {
        this.position = position;
    }

    /**
     * Gets the employee's salary.
     * @return the salary
     */
    public double getSalary() {
        return salary;
    }

    /**
     * Sets the employee's salary.
     * @param salary the salary to set
     */
    public void setSalary(double salary) {
        this.salary = salary;
    }

    /**
     * Returns a neatly formatted String representation of the Employee.
     * Displays attributes double-spaced to match expected console output.
     */
    @Override
    public String toString() {
        // Format salary: display as integer if it represents a whole number, otherwise show 2 decimal places.
        String salaryStr = (salary == (long) salary) 
                ? String.format("%d", (long) salary) 
                : String.format("%.2f", salary);

        return "Employee ID : " + employeeId + "\n\n" +
               "Name : " + name + "\n\n" +
               "Position : " + position + "\n\n" +
               "Salary : ₹" + salaryStr;
    }
}
