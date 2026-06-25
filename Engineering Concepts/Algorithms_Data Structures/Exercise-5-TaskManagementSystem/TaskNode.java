/**
 * Represents a Node in the Task Singly Linked List.
 * Contains task data and a reference to the next node.
 */
public class TaskNode {
    // Attributes
    Task data;
    TaskNode next;

    /**
     * Constructor to initialize a TaskNode with Task data.
     * Sets next pointer to null.
     *
     * @param data the Task data to store in this node
     */
    public TaskNode(Task data) {
        this.data = data;
        this.next = null;
    }
}
