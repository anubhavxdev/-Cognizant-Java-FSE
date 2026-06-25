/**
 * Represents a Task in the Task Management System.
 * Holds attributes like taskId, taskName, and status.
 */
public class Task {
    // Private attributes
    private int taskId;
    private String taskName;
    private String status;

    /**
     * Parameterized constructor to initialize a Task.
     *
     * @param taskId   the unique ID of the task
     * @param taskName the name/description of the task
     * @param status   the status of the task (e.g., Pending, In Progress, Completed)
     */
    public Task(int taskId, String taskName, String status) {
        this.taskId = taskId;
        this.taskName = taskName;
        this.status = status;
    }

    // Getters and Setters

    /**
     * Gets the task ID.
     * @return the task ID
     */
    public int getTaskId() {
        return taskId;
    }

    /**
     * Sets the task ID.
     * @param taskId the task ID to set
     */
    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    /**
     * Gets the task name.
     * @return the task name
     */
    public String getTaskName() {
        return taskName;
    }

    /**
     * Sets the task name.
     * @param taskName the task name to set
     */
    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    /**
     * Gets the task status.
     * @return the task status
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the task status.
     * @param status the task status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Returns a neatly formatted String representation of the Task.
     * Formats attributes double-spaced to match the expected console output:
     * Task ID : [taskId]
     *
     * Task Name : [taskName]
     *
     * Status : [status]
     */
    @Override
    public String toString() {
        return "Task ID : " + taskId + "\n\n" +
               "Task Name : " + taskName + "\n\n" +
               "Status : " + status;
    }
}
