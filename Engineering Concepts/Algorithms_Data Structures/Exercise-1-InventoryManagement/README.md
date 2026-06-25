# Exercise 1 – Inventory Management System

## Objective
The objective of this project is to design and implement an efficient **Inventory Management System** for a warehouse using appropriate data structures in Java. The system must allow high-speed CRUD operations (Create, Read, Update, Delete) on products using their unique product IDs.

## Problem Statement
In a busy warehouse environment, tracking inventory efficiently is critical to operational success. The system needs to support:
1. **Adding Products** to the inventory.
2. **Updating Product details** (such as name, quantity, and price) using their unique ID.
3. **Deleting Products** from the inventory.
4. **Displaying all Products** currently stored.

To minimize latency during retrieval and modification, we need a data structure that provides constant-time complexity ($O(1)$) for searching, updating, and deleting by key. Thus, we utilize a `HashMap<Integer, Product>`.

---

## Data Structure Used: HashMap
A **HashMap** is a part of Java's Collection framework (`java.util.HashMap`) that stores elements in key-value pairs. 

### Why HashMap?
* **Direct Access via Keys:** In an inventory system, we uniquely identify each product by its `productId`. Using `productId` as the key (`Integer`) and the `Product` object as the value, we can perform lookups directly.
* **Internal Hashing Mechanism:** HashMap uses a hash function to compute an index in an array of buckets. When we look up or update an ID, the hash code points us directly to the bucket, bypassing the need to search the entire structure.
* **Constant Time Operations:** On average, the time complexity for addition, search, update, and deletion is $O(1)$, making it exceptionally fast even as the inventory grows to millions of products.

---

## Folder Structure
The project is structured as follows:

```text
Exercise-1-InventoryManagement/
│
├── Product.java             # Domain class representing a product entity
├── InventoryManager.java    # Controller class managing inventory operations using HashMap
├── InventoryTest.java       # Test driver class executing the required use cases
├── README.md                # Project documentation and conceptual analysis
└── output.txt               # Verification log containing captured console output
```

---

## Files Description

1. **[Product.java](file:///c:/Users/anubh/OneDrive/Desktop/kranti/cognizant/myassests/-Cognizant-Java-FSE/week1/Algorithms_Data%20Structures/Exercise-1-InventoryManagement/Product.java):** Contains private attributes (`productId`, `productName`, `quantity`, `price`), a constructor, getter/setter methods, and an overridden `toString()` method configured to display product details in a neat card layout.
2. **[InventoryManager.java](file:///c:/Users/anubh/OneDrive/Desktop/kranti/cognizant/myassests/-Cognizant-Java-FSE/week1/Algorithms_Data%20Structures/Exercise-1-InventoryManagement/InventoryManager.java):** Houses the core business logic. It encapsulates a `HashMap<Integer, Product>` and exposes public methods (`addProduct`, `updateProduct`, `deleteProduct`, `displayProducts`) with validation checks (e.g., checking for duplicates or non-existent items).
3. **[InventoryTest.java](file:///c:/Users/anubh/OneDrive/Desktop/kranti/cognizant/myassests/-Cognizant-Java-FSE/week1/Algorithms_Data%20Structures/Exercise-1-InventoryManagement/InventoryTest.java):** The test driver class containing the `main` method. It initializes the manager, runs the sequence of CRUD instructions specified in the exercise, and prints the output to the console.
4. **[output.txt](file:///c:/Users/anubh/OneDrive/Desktop/kranti/cognizant/myassests/-Cognizant-Java-FSE/week1/Algorithms_Data%20Structures/Exercise-1-InventoryManagement/output.txt):** The exact execution log showing the results of running the test driver.

---

## Complexity Analysis

### Time Complexity Table

| Operation | Average Case | Worst Case | Explanation |
| :--- | :--- | :--- | :--- |
| **Add** | $O(1)$ | $O(n)$ | Direct insertion in bucket. Worst case occurs during hash collisions where all keys hash to the same bucket. |
| **Search** | $O(1)$ | $O(n)$ | Hashed index access. Worst case occurs if all elements are in a single bucket (resolved via linked list or tree). |
| **Update** | $O(1)$ | $O(n)$ | Retrieves the object by key in $O(1)$ and modifies its attributes. |
| **Delete** | $O(1)$ | $O(n)$ | Removes the key-value mapping from the bucket directly. |
| **Display (Traversal)** | $O(n)$ | $O(n)$ | Requires iterating through all entries/values in the map. |

### Space Complexity
* **Overall Space Complexity:** $O(n)$, where $n$ is the number of products stored in the inventory. Each product mapping consumes a bucket entry in memory.

---

## Output
Below is the exact output captured from running the `InventoryTest` main application:

```text
Product Added Successfully.

Product Added Successfully.

Product Added Successfully.

========== INVENTORY ==========

ID : 101
Name : Laptop
Quantity : 15
Price : ₹65000.0

ID : 102
Name : Keyboard
Quantity : 50
Price : ₹1200.0

ID : 103
Name : Mouse
Quantity : 100
Price : ₹700.0

Product Updated Successfully.

========== INVENTORY ==========

ID : 101
Name : Laptop
Quantity : 15
Price : ₹65000.0

ID : 102
Name : Mechanical Keyboard
Quantity : 40
Price : ₹1800.0

ID : 103
Name : Mouse
Quantity : 100
Price : ₹700.0

Product Deleted Successfully.

========== INVENTORY ==========

ID : 101
Name : Laptop
Quantity : 15
Price : ₹65000.0

ID : 102
Name : Mechanical Keyboard
Quantity : 40
Price : ₹1800.0
```

---

## Advantages of Using HashMap
* **Fast Searching:** Lookups are performed in $O(1)$ average time, making query speed independent of the inventory size.
* **Fast Updates:** We can fetch and mutate the product properties immediately without traversing other elements.
* **Fast Deletion:** Elements are deleted without shifting elements in memory.
* **Better than Linear Search:** In an array or list, searching for an item requires scanning sequentially, which scales linearly $O(n)$. HashMap eliminates this performance bottleneck.

---

## Additional Conceptual Section

### 1. Why are Data Structures important?
Data structures are the foundational building blocks of software engineering. They dictate how data is organized, stored, and manipulated in memory. Choosing the right data structure is important because:
* **Resource Optimization:** It minimizes CPU execution time and memory overhead.
* **Scalability:** It ensures applications perform predictably and efficiently as data volume increases.
* **Clarity:** It models real-world business domains logically, resulting in cleaner and more maintainable code.

### 2. Why HashMap instead of ArrayList?
If we use an `ArrayList` to manage the inventory:
* **Searching by ID** would require iterating through the list line-by-line (Linear Search), resulting in $O(n)$ time complexity. For $1,000,000$ products, this would require up to $1,000,000$ comparisons in the worst case.
* **Updating and Deleting** would also require finding the element first, costing $O(n)$ time. Additionally, deleting an item in an `ArrayList` requires shifting all subsequent elements, which is memory expensive.
* **HashMap** solves this by converting the `productId` into a hash index. We directly jump to the product's bucket, yielding $O(1)$ time for Search, Update, and Delete.

### 3. Difference between Array and HashMap
| Feature | Array | HashMap |
| :--- | :--- | :--- |
| **Data Organization** | Index-based (sequential memory elements). | Key-Value pairs mapped via hashing. |
| **Size** | Fixed size (must be declared at initialization). | Dynamic size (resizes automatically as needed). |
| **Keys** | Only non-negative integers starting from $0$. | Any object (e.g., String, Integer, custom objects). |
| **Search Speed** | $O(1)$ if index is known; $O(n)$ if searching by value. | $O(1)$ average case by using the key. |
| **Memory Allocation** | Contiguous block of memory. | Distributed buckets across memory using nodes. |

### 4. Real-world applications of Inventory Management Systems
* **E-Commerce Warehouses (e.g., Amazon, Flipkart):** To monitor millions of items in real-time and handle orders, update stock levels, and signal replenishment.
* **Retail POS (Point of Sale) Systems:** Instantly updates stock levels when products are scanned at a cash counter.
* **Hospital Pharmacy Tracking:** Ensures critical life-saving medications are in stock and alerts staff when quantities fall below a threshold.
* **Manufacturing Supply Chains:** Tracks raw materials, work-in-progress components, and finished goods to optimize manufacturing schedules.

### 5. Five Viva Questions with Answers

#### Q1: What is the "Load Factor" in a HashMap and what is its default value?
**Answer:** The load factor is a measure of how full the hash map is allowed to get before its capacity is automatically doubled (rehashed). The default load factor in Java's HashMap is **0.75** (i.e., when the map is 75% full, it doubles its capacity). This value provides a good balance between time and space cost.

#### Q2: How does HashMap handle "Hash Collisions" in Java 8?
**Answer:** In Java 8, if multiple keys hash to the same bucket, they are initially stored in a singly linked list. However, if the bucket size exceeds a threshold (called `TREEIFY_THRESHOLD`, which is **8**) and the overall map capacity is at least **64**, the linked list is converted into a self-balancing binary search tree (specifically a **Red-Black Tree**). This improves the worst-case search performance within that bucket from $O(n)$ to $O(\log n)$.

#### Q3: Can we store a null key and null values in a HashMap?
**Answer:** Yes. A `HashMap` allows exactly **one null key** (which is always stored at bucket index `0` because its hash code is treated as `0`) and **multiple null values**.

#### Q4: What is the main difference between HashMap and Hashtable?
**Answer:** 
1. **Thread Safety:** `HashMap` is not synchronized and not thread-safe, whereas `Hashtable` is synchronized and thread-safe.
2. **Null Values:** `HashMap` allows one null key and multiple null values, whereas `Hashtable` does not allow any null keys or null values (throws `NullPointerException`).
3. **Legacy status:** `Hashtable` is a legacy class, whereas `HashMap` is a modern part of Java Collections Framework.

#### Q5: Why is it recommended to use immutable classes (like String or Integer) as keys in HashMap?
**Answer:** If a key's internal state changes after being inserted, its `hashCode()` will change. When you try to retrieve the value using the modified key, the HashMap will compute the new hash, look in a different bucket, and fail to find the original value (causing a silent data leak). Immutable classes like `String` and `Integer` guarantee that their hash code remains constant throughout their lifetime.

---

### 6. Interview Questions related to HashMap

#### Q1: Explain the internal workings of `put(key, value)` in HashMap.
**Answer:**
1. **Hash Code Calculation:** HashMap calls the key's `hashCode()` and applies a secondary hash function to prevent poor hash distributions.
2. **Bucket Indexing:** The hash is mapped to an index in the internal bucket array using the formula `(n - 1) & hash` (where `n` is the array length).
3. **Node Traversal:** 
   - If the bucket is empty, a new Node is created and inserted at that index.
   - If a collision occurs:
     - The map checks if the key already exists (using `.equals()`). If it matches, the value is overwritten.
     - If the key is new, it traverses the bucket. If it is a tree bucket, it inserts the node into the Red-Black Tree. If it is a linked list bucket, it appends the node. If the length exceeds 8, the list is converted to a tree.
4. **Resizing Check:** If the total size exceeds the threshold (capacity * load factor), the map doubles its capacity and rehashes all elements.

#### Q2: What is "Rehashing" and why is it expensive?
**Answer:** Rehashing is the process of doubling the capacity of the internal array bucket structure (e.g., from 16 to 32) when the number of elements exceeds the threshold. It is expensive because:
* A new, larger array is allocated.
* Every single key-value pair's hash code is recalculated, and its index is recomputed using the new capacity.
* The elements are then moved to their new positions in the new array.
* If a map has millions of elements, rehashing causes a temporary spike in CPU and garbage collection activity.

#### Q3: How do `hashCode()` and `equals()` contract work in HashMap?
**Answer:** HashMap relies on the contract between `hashCode()` and `equals()` to store and retrieve values correctly:
* If two objects are equal according to the `equals(Object)` method, then calling `hashCode()` on each of them must produce the same integer result.
* If two objects have the same hash code, they do not necessarily have to be equal.
* If you override `equals()`, you **must** override `hashCode()`. Failing to do so violates the contract, meaning equal keys could generate different hash codes and end up in different buckets, leading to duplicate keys and retrieval failures.
