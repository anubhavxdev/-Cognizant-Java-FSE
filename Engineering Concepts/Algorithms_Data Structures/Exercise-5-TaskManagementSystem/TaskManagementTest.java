/**
 * Test class to execute and verify the Task Management System operations.
 */
public class TaskManagementTest {

    /**
     * Main method to run the Task Management System tests.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        // Initialize the custom Task LinkedList
        TaskLinkedList list = new TaskLinkedList();

        // 1. Add Tasks
        list.addTask(new Task(101, "Complete Assignment", "Pending"));
        System.out.println();
        list.addTask(new Task(102, "Learn Java", "In Progress"));
        System.out.println();
        list.addTask(new Task(103, "Prepare Presentation", "Pending"));
        System.out.println();
        list.addTask(new Task(104, "Submit Report", "Completed"));
        System.out.println();

        // 2. Display Tasks
        System.out.println("========= TASK LIST =========");
        System.out.println();
        list.displayTasks();
        System.out.println();
        System.out.println("============================");
        System.out.println();

        // 3. Search Task: 102
        System.out.println("Searching Task : 102");
        System.out.println();
        Task foundTask = list.searchTask(102);
        if (foundTask != null) {
            System.out.println("Task Found.");
            System.out.println();
            System.out.println(foundTask);
        } else {
            System.out.println("Task Not Found.");
        }
        System.out.println();

        // 4. Delete Task: 103
        list.deleteTask(103);
        System.out.println();

        // 5. Display Tasks Again
        System.out.println("========= UPDATED TASK LIST =========");
        System.out.println();
        list.displayTasks();
        System.out.println();
        System.out.println("============================");
        System.out.println();

        // 6. Search Task: 999
        System.out.println("Searching Task : 999");
        System.out.println();
        Task notFoundTask = list.searchTask(999);
        if (notFoundTask != null) {
            System.out.println("Task Found.");
            System.out.println();
            System.out.println(notFoundTask);
        } else {
            System.out.println("Task Not Found.");
        }
    }
}
