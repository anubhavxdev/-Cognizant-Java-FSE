/**
 * Singly Linked List implementation to manage Task records.
 */
public class TaskLinkedList {
    // Reference to the head (first node) of the linked list
    private TaskNode head;

    /**
     * Constructor to initialize an empty TaskLinkedList.
     */
    public TaskLinkedList() {
        this.head = null;
    }

    /**
     * Adds a task to the end (tail) of the linked list.
     * If the list is empty, the head points to the new node.
     *
     * @param task the Task object to add
     */
    public void addTask(Task task) {
        TaskNode newNode = new TaskNode(task);

        // Case 1: List is empty
        if (head == null) {
            head = newNode;
        } 
        // Case 2: List contains elements
        else {
            TaskNode temp = head;
            // Traverse to the last node
            while (temp.next != null) {
                temp = temp.next;
            }
            // Link the last node to the new node
            temp.next = newNode;
        }
        System.out.println("Task Added Successfully.");
    }

    /**
     * Searches for a task by its ID.
     * Traverses the list and returns the task if found.
     *
     * @param taskId the ID of the task to search for
     * @return the Task object if found, or null if not found
     */
    public Task searchTask(int taskId) {
        TaskNode temp = head;

        // Traverse the linked list sequentially
        while (temp != null) {
            if (temp.data.getTaskId() == taskId) {
                return temp.data; // Return task if ID matches
            }
            temp = temp.next; // Move to the next node
        }
        // Return null if search finishes without finding the task
        return null;
    }

    /**
     * Displays all tasks in the list.
     * Prints "No Tasks Available." if the list is empty.
     */
    public void displayTasks() {
        // Check if the list is empty
        if (head == null) {
            System.out.println("No Tasks Available.");
            return;
        }

        TaskNode temp = head;
        // Traverse and print each task's details
        while (temp != null) {
            System.out.println(temp.data);
            temp = temp.next;

            // Print divider if there are more elements in the list
            if (temp != null) {
                System.out.println();
                System.out.println("------------------------");
                System.out.println();
            }
        }
    }

    /**
     * Deletes a task by its ID.
     * Handles Empty List, First Node, Middle Node, and Last Node deletion cases.
     *
     * @param taskId the ID of the task to delete
     */
    public void deleteTask(int taskId) {
        // Case 1: Empty List
        if (head == null) {
            System.out.println("Task Not Found.");
            return;
        }

        // Case 2: First Node (Head Node matches taskId)
        if (head.data.getTaskId() == taskId) {
            head = head.next; // Move head reference to the next node
            System.out.println("Task Deleted Successfully.");
            return;
        }

        // Case 3 & 4: Middle or Last Node
        TaskNode prev = head;
        TaskNode curr = head.next;

        // Traverse the list to find the matching node
        while (curr != null) {
            if (curr.data.getTaskId() == taskId) {
                // Link previous node to current node's next, bypassing current node
                prev.next = curr.next;
                System.out.println("Task Deleted Successfully.");
                return;
            }
            prev = curr; // Shift pointers forward
            curr = curr.next;
        }

        // If the task ID was not found after complete traversal
        System.out.println("Task Not Found.");
    }
}
