# Exercise 4 – Employee Management System

## Objective
The objective of this project is to design, implement, and analyze an **Employee Management System** using a custom-built, fixed-size **Array** data structure in Java. This hands-on exercise explores the internal mechanics of arrays—including contiguous memory allocation, direct indexing, sequential linear searches, and element shifting during deletion—providing a clear understanding of array advantages and limitations.

## Problem Statement
A company needs to maintain employee records. The system must support basic CRUD operations:
1. **Add Employee:** Adds an employee to the system at the next available array position.
2. **Search Employee:** Searches for an employee by their unique ID using Linear Search.
3. **Display All Employees:** Outputs details of all active employee records.
4. **Delete Employee:** Removes an employee by their ID and shifts subsequent elements left to maintain array continuity.

The system uses a fixed-size `Employee[]` array (capacity 100) and an integer `count` variable.

---

## Data Structure Used: Array
An **Array** is a linear data structure containing a collection of elements of the same data type stored in contiguous memory locations. 

### Core Concepts:
* **Memory Representation:** Elements are stored side-by-side in memory. If the array starts at memory address $A$, the element at index $i$ is located at $A + (i \times \text{size of single element})$.
* **Contiguous Allocation:** The system allocates a single block of memory of size $(\text{capacity} \times \text{element size})$ during initialization.
* **Fixed Size:** Once initialized, the capacity of an array is static. It cannot grow or shrink dynamically.
* **Indexing:** Elements are accessed using $0$-based integer indices ($0, 1, \dots, n-1$). Accessing an element by its index is a direct mathematical calculation taking $O(1)$ constant time.

---

## Folder Structure
```text
Exercise-4-EmployeeManagementSystem/
│
├── Employee.java                # Employee entity class (ID, name, position, salary)
├── EmployeeManagementSystem.java # Database controller managing the fixed-size array
├── EmployeeManagementTest.java   # Driver class running additions, searches, and deletions
├── README.md                    # Comprehensive documentation and analysis
└── output.txt                   # Verification log containing captured console output
```

---

## Files Description

1. **[Employee.java](file:///c:/Users/anubh/OneDrive/Desktop/kranti/cognizant/myassests/-Cognizant-Java-FSE/week1/Algorithms_Data%20Structures/Exercise-4-EmployeeManagementSystem/Employee.java):** Contains private attributes (`employeeId`, `name`, `position`, `salary`), parameterized constructor, getters/setters, and a double-spaced `toString()` method for formatting.
2. **[EmployeeManagementSystem.java](file:///c:/Users/anubh/OneDrive/Desktop/kranti/cognizant/myassests/-Cognizant-Java-FSE/week1/Algorithms_Data%20Structures/Exercise-4-EmployeeManagementSystem/EmployeeManagementSystem.java):** Contains the fixed-size array (`Employee[] employees`) and `count` variable. Implements array insertion, sequential traversal search, display iterations, and left-shifting deletion logic.
3. **[EmployeeManagementTest.java](file:///c:/Users/anubh/OneDrive/Desktop/kranti/cognizant/myassests/-Cognizant-Java-FSE/week1/Algorithms_Data%20Structures/Exercise-4-EmployeeManagementSystem/EmployeeManagementTest.java):** Driver program that initializes the system, inserts four employees, prints the list, searches for ID 102, deletes ID 103, prints the updated list, and searches for a non-existent ID 999.
4. **[output.txt](file:///c:/Users/anubh/OneDrive/Desktop/kranti/cognizant/myassests/-Cognizant-Java-FSE/week1/Algorithms_Data%20Structures/Exercise-4-EmployeeManagementSystem/output.txt):** The verified console output file.

---

## Complexity Analysis

### Time Complexity Table

| Operation | Time Complexity | Explanation |
| :--- | :--- | :--- |
| **Add** | $O(1)$ | Directly placed at `employees[count]`. No traversal or shifting required. |
| **Search** | $O(n)$ | Must scan sequentially from index $0$ to $\text{count}-1$ (Linear Search) because the array is unsorted. |
| **Traverse** | $O(n)$ | Iterates through all $n$ active elements to print them. |
| **Delete** | $O(n)$ | Once located, all elements to the right of the deleted index must be shifted one position to the left. |

#### Why Deletion is $O(n)$:
When an element at index $k$ is deleted, it leaves a gap. To maintain contiguous memory allocation (a requirement for array indexing), we must shift all subsequent elements from index $k+1$ to $\text{count}-1$ one slot to the left. In the worst case (deleting the first element), we shift $n - 1$ elements, making the complexity linear $O(n)$.

#### Why Search is $O(n)$:
Since the array is not sorted by employee ID, we cannot use binary search. We must perform a Linear Search, checking each slot one-by-one. If the target employee is at the last index or not present in the system, we make $n$ comparisons, yielding $O(n)$.

### Space Complexity
* **Overall Space Complexity:** $O(N)$ where $N$ is the fixed capacity allocated for the array ($N = 100$). The system reserves space for 100 object references in memory regardless of how many employees are added.

---

## Output
The console output of running `EmployeeManagementTest` (as stored in `output.txt`):

```text
Employee Added Successfully.

Employee Added Successfully.

Employee Added Successfully.

Employee Added Successfully.

========= EMPLOYEE LIST =========

Employee ID : 101

Name : Rahul

Position : Software Engineer

Salary : ₹75000

--------------------------

Employee ID : 102

Name : Priya

Position : HR Manager

Salary : ₹60000

--------------------------

Employee ID : 103

Name : Aman

Position : QA Engineer

Salary : ₹55000

--------------------------

Employee ID : 104

Name : Neha

Position : Project Manager

Salary : ₹90000

=================================

Searching Employee : 102

Employee Found

Employee ID : 102

Name : Priya

Position : HR Manager

Salary : ₹60000

Employee Deleted Successfully.

========= UPDATED LIST =========

Employee ID : 101

Name : Rahul

Position : Software Engineer

Salary : ₹75000

--------------------------

Employee ID : 102

Name : Priya

Position : HR Manager

Salary : ₹60000

--------------------------

Employee ID : 104

Name : Neha

Position : Project Manager

Salary : ₹90000

=================================

Searching Employee : 999

Employee Not Found.
```

---

## Advantages and Limitations of Arrays

### Advantages:
1. **$O(1)$ Direct Access:** Accessing any element using its index is extremely fast because it is computed mathematically.
2. **Memory Efficiency:** Since elements are stored contiguously, there is no overhead for storing node pointers (like in Linked Lists) or hash table metadata.
3. **Cache Friendliness:** Because array elements are adjacent in physical memory, they are loaded into the CPU cache together (spatial locality), speeding up iteration scans.

### Limitations:
1. **Fixed Size:** The array capacity cannot be adjusted dynamically. If we run out of slots, we must allocate a new, larger array and copy all elements over.
2. **Costly Insertions/Deletions:** Inserting or deleting an element in the middle requires shifting multiple elements to maintain contiguous storage, costing $O(n)$ time.
3. **Memory Wastage:** If we allocate an array of size 1000 but store only 5 items, the memory for the remaining 995 references is wasted.

---

## Analysis: Array vs. ArrayList

| Feature | Array | ArrayList |
| :--- | :--- | :--- |
| **Size** | Fixed (Static). | Dynamic (Resizes automatically when full). |
| **Type** | Stores primitives and objects. | Stores only objects (uses wrapper classes for primitives). |
| **Performance** | Faster access due to lack of boxing/unboxing and wrapper overhead. | Marginally slower due to resizing and object casting. |
| **Generics Support** | Does not support generics (cannot write `new T[10]`). | Fully supports generics (`ArrayList<T>`). |
| **Methods** | Basic features; size is obtained via `.length` field. | Rich API (`add()`, `remove()`, `contains()`, `clear()`). |

### When to use Arrays:
* When the size of the dataset is known beforehand and remains static.
* When maximum execution performance is required, or you are working in low-memory/embedded environments.
* When working with multi-dimensional grid layouts (e.g., matrices, game boards).

### When NOT to use Arrays:
* When the size of the dataset is highly volatile, with frequent additions and deletions.
* When you need to avoid manual memory resizing and element shifting code.

---

## Learning Outcomes
* Built a custom array controller, reinforcing understanding of $0$-based indexing and array bounds checking.
* Mastered array element shifting using loops for element deletion.
* Understood why contiguous memory allocation makes deletions computationally expensive.
* Analyzed performance trade-offs between static arrays and dynamic list collections.

---

## Additional Explanation

### 1. Why Arrays are important
Arrays are the most fundamental data structure. Almost all complex data structures (including ArrayLists, HashMaps, Heaps, Hash Tables, and String buffers) are implemented internally using arrays. Understanding arrays is crucial for writing memory-efficient algorithms and debugging memory-management issues.

### 2. How arrays are represented in memory
When you declare `Employee[] employees = new Employee[100];`, Java allocates a contiguous block of memory to store 100 references (which point to Employee objects). The array variable `employees` stores the memory address of the first slot (index 0). 
To find the address of element at index $i$:
$$\text{Address}(employees[i]) = \text{Base Address} + (i \times \text{Size of Reference})$$
This calculation is done in $O(1)$ constant time by the JVM, which is why array indexing is independent of array size.

### 3. Why deletion in arrays is costly
Deleting an element leaves an empty slot in memory. If we leave the slot empty, the array becomes "sparse", and indexing logic breaks (we can no longer find the $i$-th active element directly). To prevent this, we must shift all elements located after the deleted index one slot to the left. This shifting loop scales linearly with the number of elements.

### 4. Real-world applications of Arrays
* **Contact List / Phonebooks:** Storing static contacts.
* **Image Processing:** Images are stored as 2D arrays of pixels (RGB values).
* **Buffer Management:** Input/Output buffers for network sockets and audio/video streaming.
* **Game Development:** 2D grid boards for Chess, Tic-Tac-Toe, or pathfinding grids.

---

### 5. Five Viva Questions with Answers

#### Q1: What is the default value of elements in an object array in Java?
**Answer:** In Java, when an array of objects is allocated (e.g., `Employee[] employees = new Employee[100];`), all slots are automatically initialized to **`null`**. If it were a primitive numeric array (like `int[]`), elements would default to `0` or `0.0`.

#### Q2: What happens if you try to access an index out of the array's boundaries?
**Answer:** The JVM will throw an **`ArrayIndexOutOfBoundsException`** at runtime. This is a subclass of `RuntimeException`, meaning it is an unchecked exception that occurs when trying to access index $< 0$ or $\ge \text{array length}$.

#### Q3: Does Java support true multidimensional arrays?
**Answer:** Java does not support true contiguous multidimensional arrays. Instead, it supports **"arrays of arrays"**. A 2D array in Java is an array whose elements are references pointing to other 1D arrays. This allows Java to have "jagged arrays", where rows can have different lengths.

#### Q4: Why is it that array size cannot be changed once created?
**Answer:** Because memory allocation is contiguous. When the array is allocated, the OS reserves a specific contiguous block of memory of that size. If we want to expand the array, the memory adjacent to it might already be occupied by other program variables. Therefore, dynamic expansion is impossible in place.

#### Q5: What is the difference between `array.length` and `list.size()`?
**Answer:** 
* `array.length` is a public **final property** of array objects that returns the total allocated capacity of the array.
* `list.size()` is a **method** implemented by collection classes (like `ArrayList`) that returns the number of active elements currently stored in the collection.

---

### 6. Five Interview Questions related to Arrays

#### Q1: Explain how `ArrayList` resizes dynamically under the hood.
**Answer:** When an element is added to an `ArrayList` and the internal array is full, the class:
1. Allocates a new array with a capacity that is typically **1.5 times** the original capacity (`newCapacity = oldCapacity + (oldCapacity >> 1)`).
2. Copies all elements from the old array to the new array using `System.arraycopy()`.
3. Discards the old array reference (leaving it to the Garbage Collector) and starts writing to the new array.
The amortized time complexity of addition remains $O(1)$.

#### Q2: What is the "Three-Way Partitioning" or Dutch National Flag algorithm on Arrays?
**Answer:** It is an algorithm used to sort an array containing three types of elements (e.g., 0s, 1s, and 2s) in a single linear pass $O(n)$ with $O(1)$ space. It maintains three pointers (`low`, `mid`, `high`) and swaps elements to partition them into three contiguous segments.

#### Q3: How do you find the duplicate number in an array of $N+1$ integers where each integer is between 1 and $N$?
**Answer:** This can be solved in $O(n)$ time and $O(1)$ auxiliary space using **Floyd's Cycle-Finding Algorithm (Tortoise and Hare)**. By treating the array values as pointers to indices, we detect the starting node of the cycle, which corresponds to the duplicate number.

#### Q4: How do you rotate an array of size $N$ to the right by $K$ steps in-place?
**Answer:** You can achieve this in $O(n)$ time and $O(1)$ space using three reverse operations:
1. Reverse the entire array.
2. Reverse the first $K$ elements.
3. Reverse the remaining $N-K$ elements.
For example: `[1,2,3,4,5]` rotated by $K=2$: reverse all -> `[5,4,3,2,1]`, reverse first 2 -> `[4,5,3,2,1]`, reverse rest -> `[4,5,1,2,3]`.

#### Q5: What is the difference between shallow copy and deep copy of an object array?
**Answer:**
* **Shallow Copy:** Copies the array structure but not the objects. The new array contains references pointing to the exact same objects in memory as the original array (`System.arraycopy()` or `clone()`). Modifying an object's field in the copy will reflect in the original.
* **Deep Copy:** Allocates a new array, instantiates new objects for each slot, and copies the data fields over. Modifying objects in the copy will not affect the original.
