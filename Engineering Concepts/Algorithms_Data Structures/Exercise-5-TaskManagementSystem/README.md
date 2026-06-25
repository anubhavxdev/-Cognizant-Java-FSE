# Exercise 5 – Task Management System

## Objective
The objective of this project is to design, implement, and analyze a **Task Management System** using a custom **Singly Linked List** in Java. This exercise demonstrates how dynamic node structures allocate memory on-demand, manages references, and compares the runtime efficiencies of linked lists against sequential arrays.

## Problem Statement
A warehouse or office needs a system to manage tasks. Tasks are dynamic in nature and undergo frequent insertions and deletions. The system must support the following:
1. **Add Task:** Inserts a task at the end (tail) of the list.
2. **Search Task:** Locates a task by its unique ID by traversing nodes.
3. **Display Tasks:** Traverses the entire list and prints active task records.
4. **Delete Task:** Removes a task by its ID and patches references to cover boundary cases (Empty list, Head node, Middle node, Tail node).

To implement this efficiently, we build a custom Singly Linked List consisting of `TaskNode` objects.

---

## Data Structure Used: Singly Linked List
A **Singly Linked List** is a linear data structure where elements are not stored in contiguous memory locations. Instead, elements are stored in independent entities called **Nodes**.

### Core Components:
* **Node:** The container representing a single entry.
* **Data:** The actual payload (in this case, the `Task` object) held inside the Node.
* **Next Pointer:** A reference variable storing the memory address of the subsequent node in the list. The last node's `next` pointer refers to `null`.
* **Head Node:** The first node of the list, serving as the entry point for all operations.
* **Traversal:** Navigating the list sequentially starting from `head` and following the `next` pointers until `null` is encountered.

---

## Folder Structure
```text
Exercise-5-TaskManagementSystem/
│
├── Task.java                    # Task entity (taskId, taskName, status)
├── TaskNode.java                # Node wrapper containing data and next pointer
├── TaskLinkedList.java          # Controller class implementing Singly Linked List logic
├── TaskManagementTest.java      # Driver class executing tasks operations
├── README.md                    # Comprehensive documentation and analysis
└── output.txt                   # Verification log containing captured console output
```

---

## Files Description

1. **[Task.java](file:///c:/Users/anubh/OneDrive/Desktop/kranti/cognizant/myassests/-Cognizant-Java-FSE/week1/Algorithms_Data%20Structures/Exercise-5-TaskManagementSystem/Task.java):** Houses the private properties of a task (`taskId`, `taskName`, `status`), constructor, getters/setters, and a double-spaced `toString()` method.
2. **[TaskNode.java](file:///c:/Users/anubh/OneDrive/Desktop/kranti/cognizant/myassests/-Cognizant-Java-FSE/week1/Algorithms_Data%20Structures/Exercise-5-TaskManagementSystem/TaskNode.java):** Package-private node wrapper containing the `Task data` reference and `TaskNode next` reference.
3. **[TaskLinkedList.java](file:///c:/Users/anubh/OneDrive/Desktop/kranti/cognizant/myassests/-Cognizant-Java-FSE/week1/Algorithms_Data%20Structures/Exercise-5-TaskManagementSystem/TaskLinkedList.java):** Contains list head pointer. Implements recursive/iterative tail insertion, sequential matching for searches, element printing with delimiters, and reference rewiring for deletions.
4. **[TaskManagementTest.java](file:///c:/Users/anubh/OneDrive/Desktop/kranti/cognizant/myassests/-Cognizant-Java-FSE/week1/Algorithms_Data%20Structures/Exercise-5-TaskManagementSystem/TaskManagementTest.java):** Driver test runner that initializes the list, adds 4 tasks, prints the list, searches for ID 102, deletes ID 103, prints the updated list, and searches for ID 999.
5. **[output.txt](file:///c:/Users/anubh/OneDrive/Desktop/kranti/cognizant/myassests/-Cognizant-Java-FSE/week1/Algorithms_Data%20Structures/Exercise-5-TaskManagementSystem/output.txt):** The verified execution output.

---

## Complexity Analysis

### Time Complexity Table

| Operation | Time Complexity | Explanation |
| :--- | :--- | :--- |
| **Insert at Beginning** | $O(1)$ | Direct insertion: create node, point its `next` to current `head`, update `head`. |
| **Insert at End** | $O(n)$ | Must traverse through all $n$ nodes to locate the current tail node before linking. |
| **Search** | $O(n)$ | Requires sequential traversal from the `head` to match the target ID. |
| **Delete** | $O(n)$ | Must traverse to locate the target node and its predecessor to rewire references. |
| **Traverse** | $O(n)$ | Requires visiting all nodes in the list. |

#### Why Insert at End is $O(n)$:
Unless a tail pointer is maintained, we must start at the `head` and step through each node using `temp = temp.next` until `temp.next == null`. This traversal scales linearly with the list size.

#### Why Search and Delete are $O(n)$:
Unlike arrays, elements are scattered in memory, and we cannot compute addresses directly. We must walk the list sequentially. For deletion, once the target node is found, we must link the predecessor node directly to the successor node ($O(n)$ for search + $O(1)$ for reference update).

### Space Complexity
* **Space Complexity:** $O(n)$ where $n$ is the active number of tasks. No memory is pre-allocated or wasted; each task consumes exactly one Node allocation in heap memory.

---

## Theory Section

### 1. Types of Linked Lists
* **Singly Linked List:** Each node contains data and a single pointer referencing the next node. Navigation is strictly uni-directional (forward-only).
* **Doubly Linked List:** Each node contains data, a `next` pointer, and a `prev` pointer referencing the predecessor node. Enables bi-directional navigation and easier deletion of a node if its reference is known.
* **Circular Linked List:** The `next` pointer of the tail node refers back to the `head` node instead of `null`, forming a continuous loop. Useful in circular queues and round-robin scheduler systems.

### 2. Comparison: Linked List vs. Array

| Feature | Array | Linked List |
| :--- | :--- | :--- |
| **Memory Allocation** | Contiguous block in memory. | Scattered/Dynamic blocks in heap memory. |
| **Size** | Fixed/Static (declared at start). | Dynamic (grows and shrinks on-demand). |
| **Insertion** | Costly $O(n)$ (requires shifting elements). | Easy $O(1)$ once location is found (rewire pointers). |
| **Deletion** | Costly $O(n)$ (requires shifting elements). | Easy $O(1)$ once location is found (rewire pointers). |
| **Search** | $O(n)$ (Linear Search). | $O(n)$ (Linear Traversal). |
| **Random Access** | Yes $O(1)$ via indexing. | No (must traverse sequentially from `head`). |

---

## Analysis: Advantages & Limitations

### Advantages of Linked Lists:
* **Dynamic Size:** Can grow or shrink indefinitely without memory reallocation or copy-overhead.
* **Efficient Insertions/Deletions:** Inserting or removing a node does not require shifting elements; it only requires modifying two pointer reference fields.
* **No Pre-allocated Waste:** Memory is allocated only when a new item is added.

### Limitations of Linked Lists:
* **Random Access Denied:** Finding the $i$-th element requires $O(n)$ operations, making algorithms like Binary Search impossible to implement efficiently.
* **Pointers Overhead:** Every node must store at least one reference pointer, increasing memory consumption compared to primitive arrays.
* **Cache Unfriendly:** Because nodes are scattered randomly in heap memory, they do not exhibit spatial locality, causing frequent CPU cache misses.

---

## Sample Output
The console output of running `TaskManagementTest` (as stored in `output.txt`):

```text
Task Added Successfully.

Task Added Successfully.

Task Added Successfully.

Task Added Successfully.

========= TASK LIST =========

Task ID : 101

Task Name : Complete Assignment

Status : Pending

------------------------

Task ID : 102

Task Name : Learn Java

Status : In Progress

------------------------

Task ID : 103

Task Name : Prepare Presentation

Status : Pending

------------------------

Task ID : 104

Task Name : Submit Report

Status : Completed

============================

Searching Task : 102

Task Found.

Task ID : 102

Task Name : Learn Java

Status : In Progress

Task Deleted Successfully.

========= UPDATED TASK LIST =========

Task ID : 101

Task Name : Complete Assignment

Status : Pending

------------------------

Task ID : 102

Task Name : Learn Java

Status : In Progress

------------------------

Task ID : 104

Task Name : Submit Report

Status : Completed

============================

Searching Task : 999

Task Not Found.
```

---

## Additional Explanation

### 1. What is a Linked List?
A Linked List is a linear collection of data elements where order is not given by physical memory layout. Instead, each element (node) points to the next. It is a self-referential structure, meaning a node class contains a reference field of its own class type.

### 2. Why Linked List is dynamic
Unlike arrays, which require a single contiguous block of memory allocated at compile or startup time, linked list nodes are allocated dynamically at runtime using Java's `new` keyword. Each node is created in the JVM heap space on-demand and connected via references. When a node is deleted, its reference is removed, and Java's Garbage Collector automatically reclaims the memory.

### 3. Applications of Linked List
* **Undo/Redo Stack:** Browser back buttons or text editor undo stacks (implemented using doubly linked lists).
* **Playlist Management:** Music players use circular linked lists to loop songs.
* **Memory Management:** Operating systems track free memory blocks using linked lists of available holes.
* **Hash Collisions:** HashMap buckets use singly linked lists to resolve hash collisions (Chaining).

---

### 4. Five Viva Questions with Answers

#### Q1: What is a "Self-Referential Class" in Java?
**Answer:** A self-referential class is one that contains an attribute pointing to an object of the same class type. For example, in `TaskNode.java`, the variable `TaskNode next` is a reference pointing to another `TaskNode` instance. This structure is essential for implementing linked lists, trees, and graphs.

#### Q2: How can we make insertion at the end of a Singly Linked List $O(1)$?
**Answer:** By maintaining a **`tail` pointer** (a reference pointing to the last node of the list) in addition to the `head` pointer. When adding a new node, we link `tail.next = newNode` and update `tail = newNode` in $O(1)$ time, eliminating the need to traverse the entire list.

#### Q3: What is a "Dummy Node" or "Sentinel Node" and why is it used?
**Answer:** A sentinel node is a dummy node inserted at the beginning of a linked list that does not store any actual data. It is used to simplify edge-case code. With a sentinel node, the `head` is never null, eliminating the need for special checks when inserting or deleting the first node of the list.

#### Q4: Why does a Singly Linked List require a loop to delete a node, while a Doubly Linked List does not (assuming we have a reference to the node to be deleted)?
**Answer:** To delete a node, we must connect its predecessor directly to its successor. In a Singly Linked List, a node only knows its successor. To find the predecessor, we must traverse the list from the `head` using a loop. In a Doubly Linked List, a node stores a `prev` reference pointing directly to its predecessor, allowing deletion in $O(1)$ time without traversal.

#### Q5: What is a memory leak in a linked list?
**Answer:** A memory leak occurs if a node is bypassed (unlinked) but still holds references or is referred to by other active components, preventing the Garbage Collector from freeing it. In Java, as long as a node is not reachable from any active root reference (like `head`), the Garbage Collector automatically reclaims it. However, it is a best practice to set unused references to `null` to ensure immediate reclamation.

---

### 5. Five Interview Questions related to Linked Lists

#### Q1: Write a pseudo-code or explain how to reverse a Singly Linked List in-place.
**Answer:** We can reverse a singly linked list iteratively in $O(n)$ time and $O(1)$ space using three pointers (`prev`, `curr`, `next`):
1. Initialize `prev = null` and `curr = head`.
2. Loop through the list:
   - Save the next node: `next = curr.next`.
   - Reverse the current pointer: `curr.next = prev`.
   - Shift pointers forward: `prev = curr`, `curr = next`.
3. Set `head = prev`.

#### Q2: How do you detect a cycle (loop) in a Singly Linked List?
**Answer:** Using **Floyd’s Cycle-Finding Algorithm (Tortoise and Hare)**:
* Initialize two pointers, `slow` and `fast`, to the `head`.
* Move `slow` by one step (`slow = slow.next`) and `fast` by two steps (`fast = fast.next.next`).
* If there is a cycle, the `fast` pointer will eventually meet the `slow` pointer (`slow == fast`).
* If `fast` or `fast.next` becomes `null`, there is no cycle in the list.

#### Q3: How do you find the middle element of a Singly Linked List in a single pass?
**Answer:** By using the **two-pointer technique**:
* Initialize `slow` and `fast` pointers to the `head`.
* Move `slow` by one step and `fast` by two steps.
* When the `fast` pointer reaches the end of the list (`fast == null` or `fast.next == null`), the `slow` pointer will be pointing to the exact middle node of the list.

#### Q4: How do you find the N-th node from the end of a Singly Linked List in a single pass?
**Answer:** Using two pointers:
1. Initialize `first` and `second` pointers to `head`.
2. Move the `first` pointer $N$ steps forward.
3. Move both `first` and `second` pointers forward one step at a time until `first` reaches `null`.
4. The `second` pointer will be pointing to the $N$-th node from the end.

#### Q5: What is the main disadvantage of recursion when traversing or reversing a Linked List?
**Answer:** Recursion creates a stack frame on the call stack for every node visited. For a linked list with $100,000$ elements, recursion requires $100,000$ stack frames. This will consume significant memory and trigger a **`StackOverflowError`** in Java. Iterative solutions using loops are always preferred in production.
