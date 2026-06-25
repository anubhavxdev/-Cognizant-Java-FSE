# Exercise 6 – Library Management System

## Objective
The objective of this project is to implement and compare **Linear Search** and **Binary Search** algorithms in Java for searching books by their titles. This hands-on exercise highlights how the sorted status of a dataset impacts search complexity, pointer management, string comparisons, and logarithmic scalability.

## Problem Statement
In a library database, users query books by their titles.
The system needs to support:
1. **Linear Search:** Scans the book array sequentially, performing case-insensitive string checks.
2. **Binary Search:** Operates on an alphabetically sorted array of books, splitting the search interval in half at each step using case-insensitive alphabetical comparisons.

We evaluate both algorithms by searching for active titles ("Effective Java", "Java Complete Reference") and a non-existent title ("Python Programming"), demonstrating implementation complexity and performance.

---

## Folder Structure
```text
Exercise-6-LibraryManagementSystem/
│
├── Book.java                    # Book entity (bookId, title, author)
├── LinearSearch.java            # Linear Search implementation on book titles
├── BinarySearch.java            # Binary Search implementation on sorted book titles
├── LibraryManagementTest.java   # Driver class running searches and displaying results
├── README.md                    # Comprehensive documentation and analysis
└── output.txt                   # Verification log containing captured console output
```

---

## Files Description

1. **[Book.java](file:///c:/Users/anubh/OneDrive/Desktop/kranti/cognizant/myassests/-Cognizant-Java-FSE/week1/Algorithms_Data%20Structures/Exercise-6-LibraryManagementSystem/Book.java):** Declares book fields (`bookId`, `title`, `author`), constructor, standard getters/setters, and a double-spaced `toString()` method.
2. **[LinearSearch.java](file:///c:/Users/anubh/OneDrive/Desktop/kranti/cognizant/myassests/-Cognizant-Java-FSE/week1/Algorithms_Data%20Structures/Exercise-6-LibraryManagementSystem/LinearSearch.java):** Implements linear search matching titles using `title.equalsIgnoreCase(target)`.
3. **[BinarySearch.java](file:///c:/Users/anubh/OneDrive/Desktop/kranti/cognizant/myassests/-Cognizant-Java-FSE/week1/Algorithms_Data%20Structures/Exercise-6-LibraryManagementSystem/BinarySearch.java):** Implements manual binary search on the alphabetically sorted book array, using `low`, `high`, and `mid` pointers, and evaluating alphabetical ordering with `midTitle.compareToIgnoreCase(target)`.
4. **[LibraryManagementTest.java](file:///c:/Users/anubh/OneDrive/Desktop/kranti/cognizant/myassests/-Cognizant-Java-FSE/week1/Algorithms_Data%20Structures/Exercise-6-LibraryManagementSystem/LibraryManagementTest.java):** The driver program initializing five sorted books, printing the list, performing a Linear Search, a Binary Search, and a search for a non-existent item.
5. **[output.txt](file:///c:/Users/anubh/OneDrive/Desktop/kranti/cognizant/myassests/-Cognizant-Java-FSE/week1/Algorithms_Data%20Structures/Exercise-6-LibraryManagementSystem/output.txt):** The verified execution log.

---

## Complexity Analysis

### Time Complexity Table

| Algorithm | Best Case | Average Case | Worst Case | Sorted Required | Implementation |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **Linear Search** | $O(1)$ | $O(n)$ | $O(n)$ | No | Yes (Very Simple) |
| **Binary Search** | $O(1)$ | $O(\log n)$ | $O(\log n)$ | **Yes** | Moderate |

### Space Complexity
* **Space Complexity:** $O(1)$ auxiliary space for both searching algorithms. They execute iteratively, maintaining only pointers (`low`, `high`, `mid`) and local references without allocating new structures.

---

## Theory Section

### 1. Linear Search
* **Definition:** A search method that inspects every element in a sequence one-by-one until the target is found or the collection is exhausted.
* **Working:** Starts at index $0$. If the book title at index $0$ matches (case-insensitive), returns it. Otherwise, increments the index and continues. If it reaches index $n$, returns `null`.
* **Advantages:** No sorted data requirement; works on arrays, lists, linked structures; extremely simple to code.
* **Limitations:** Performance degrades linearly with data size, making it slow for databases.

### 2. Binary Search
* **Definition:** A divide-and-conquer search algorithm that finds the position of a target value within a sorted array.
* **Working:** Starts at the middle element of the sorted array. If the target is equal to the middle element, returns it. If the target is alphabetically smaller, discards the right half and shifts the search window to the left. If larger, shifts the search window to the right. Repeat until found or window bounds cross.
* **Why Sorted Data is Required:** The algorithm draws mathematical inferences from the midpoint comparison. If the middle book title is "Effective Java" and the target is "Java Complete Reference", we can only discard the left half if we are certain that all books to the left are alphabetically smaller than "Effective Java". If unsorted, the target could be anywhere.
* **Advantages:** Fast logarithmic search times. A database of $1,000,000$ books requires at most $20$ comparisons.
* **Limitations:** Mandatory sorted precondition. Sorting costs $O(n \log n)$ time, which can be an overhead if write operations are frequent.

---

## Analysis: When to Use Which
* **Why Binary Search is faster:** Rather than checking elements one-by-one, it discards half of the remaining elements at each step. This logarithmic division scales extremely well.
* **When to use Linear Search:** Small datasets (e.g., $< 15$ elements) where sorting overhead is not justified, or highly dynamic datasets where elements are continuously added/deleted in an unsorted manner.
* **When to use Binary Search:** Large datasets that are static or read-heavy (sorted once, searched millions of times).
* **Which is suitable for large datasets:** Binary Search is the only viable option for large datasets. An $O(n)$ scan on $100$ million items is sluggish, whereas an $O(\log n)$ lookup takes less than $30$ comparisons.

---

## Expected Output
The console output of running `LibraryManagementTest` (as stored in `output.txt`):

```text
========= LIBRARY BOOKS =========

Book ID : 101

Title : Clean Code

Author : Robert C. Martin

----------------------------

Book ID : 102

Title : Design Patterns

Author : Gang of Four

----------------------------

Book ID : 103

Title : Effective Java

Author : Joshua Bloch

----------------------------

Book ID : 104

Title : Java Complete Reference

Author : Herbert Schildt

----------------------------

Book ID : 105

Title : Thinking in Java

Author : Bruce Eckel

===============================

========= LINEAR SEARCH =========

Searching: Effective Java

Book Found

Book ID : 103

Title : Effective Java

Author : Joshua Bloch

===============================

========= BINARY SEARCH =========

Searching: Java Complete Reference

Book Found

Book ID : 104

Title : Java Complete Reference

Author : Herbert Schildt

===============================

Searching: Python Programming

Book Not Found.
```

---

## Learning Outcomes
* Mastered string comparison search parameters using `equalsIgnoreCase()` and `compareToIgnoreCase()`.
* Implemented manual pointer-based binary search on non-numeric (String) fields.
* Verified that sorted array structures are required to allow logarithmic search scaling.
* Handled search miss conditions gracefully.

---

## Additional Explanation

### 1. Difference between Linear Search and Binary Search
Linear Search checks every slot sequentially ($O(n)$ complexity), requiring no setup. Binary Search checks the midpoint and halves the search space ($O(\log n)$ complexity), requiring the array to be sorted alphabetically beforehand.

### 2. Why Binary Search requires sorted data
If the array is unsorted, there is no relationship between adjacent elements. Comparing the target with the midpoint yields no information about whether the target lies to the left or right of the midpoint. Hence, we cannot discard either half, rendering the divide-and-conquer technique useless.

### 3. Real-world applications of searching algorithms
* **Library Catalogs (OPAC):** Searching for books by ISBN, title, or author.
* **Autocompletion & Dictionary Lookups:** Autocomplete search engines use binary search on alphabetical prefix trees.
* **DNS IP Lookups:** Domain Name Servers use binary search to locate IP addresses in sorted registries.
* **Git Bisect:** Finding the exact commit that introduced a regression using binary search on commit history.

---

### 4. Five Viva Questions with Answers

#### Q1: What does `compareToIgnoreCase` return, and how is it used in Binary Search?
**Answer:** It compares two strings lexicographically, ignoring case differences. It returns:
* A **negative integer** if the calling string is alphabetically smaller than the argument string.
* **Zero** if the strings are equal.
* A **positive integer** if the calling string is alphabetically larger.
In Binary Search, if `midBookTitle.compareToIgnoreCase(targetTitle) < 0`, it means the mid book title is alphabetically smaller than our target, so we search the right half (`low = mid + 1`).

#### Q2: What is the time complexity of sorting an unsorted array of size $N$ and then performing a Binary Search?
**Answer:** Sorting an unsorted array takes $O(N \log N)$ time (using algorithms like Quick Sort or Merge Sort). Performing a Binary Search takes $O(\log N)$ time. The overall complexity is $O(N \log N) + O(\log N) = O(N \log N)$. Therefore, if you only need to run a search once on an unsorted array, it is faster to use Linear Search ($O(N)$) rather than sorting it first.

#### Q3: How does Binary Search handle duplicate elements in an array?
**Answer:** Standard Binary Search will return the index of *any* matching element it encounters first (which could be in the middle of duplicates). To find the *first* or *last* occurrence of a duplicate element, the algorithm must be modified: when a match is found, instead of returning immediately, we record the index and continue searching in the left half (for first occurrence) or right half (for last occurrence).

#### Q4: Why is Binary Search faster than Linear Search for large $N$?
**Answer:** Linear search scales linearly ($N$ steps for $N$ elements). Binary search scales logarithmically ($\log_2 N$ steps). For $N = 1,000,000$:
* Linear Search takes up to $1,000,000$ steps.
* Binary Search takes at most $\approx 20$ steps ($2^{20} \approx 1,048,576$).
As $N$ grows, the difference becomes massive.

#### Q5: Can Binary Search be implemented using recursion?
**Answer:** Yes. A recursive binary search method takes the array, target, `low` and `high` boundaries as arguments. If `low > high`, it returns null. Otherwise, it calculates `mid` and calls itself with modified boundary arguments (`low` to `mid - 1` or `mid + 1` to `high`). It has the same time complexity but consumes $O(\log N)$ stack space.

---

### 5. Five Interview Questions related to searching algorithms

#### Q1: Explain how you would perform Binary Search on a circular sorted array (rotated sorted array).
**Answer:** In a rotated sorted array (e.g., `[4, 5, 6, 7, 0, 1, 2]`), at least one half of the array (left of mid or right of mid) is always sorted. 
1. Calculate `mid`.
2. Check if the element at `mid` is the target.
3. Check if the left half is sorted (`arr[low] <= arr[mid]`). If yes, check if the target lies within the range `[arr[low], arr[mid]]`. If so, search the left half; otherwise, search the right.
4. If the left half is not sorted, then the right half must be sorted. Check if the target lies within `[arr[mid], arr[high]]`. If so, search the right; otherwise, search the left.
This runs in $O(\log n)$ time.

#### Q2: What is "Exponential Search" and when is it preferred?
**Answer:** Exponential Search is used for searching in unbounded or infinite sorted lists. It starts at index 1 and doubles the index repeatedly (`1, 2, 4, 8, 16...`) until it finds a boundary value that is greater than the target. Once the range `[i/2, i]` is found, it performs a standard Binary Search within that range. It is preferred when the target is located near the beginning of a very large array.

#### Q3: Explain "Fibonacci Search".
**Answer:** Fibonacci Search is a comparison-based search algorithm that uses Fibonacci numbers to divide the sorted array search space. Compared to Binary Search, it does not use division operations (which are slow on some hardware architectures) but relies on addition and subtraction. It runs in $O(\log n)$ time.

#### Q4: Write an algorithm to find the peak element in an array where elements first increase and then decrease.
**Answer:** This can be solved in $O(\log n)$ time using Binary Search:
* Calculate `mid`.
* Check if `arr[mid] > arr[mid - 1]` and `arr[mid] > arr[mid + 1]`. If yes, `mid` is the peak.
* If `arr[mid] < arr[mid + 1]`, the peak must lie in the right half (`low = mid + 1`).
* If `arr[mid] < arr[mid - 1]`, the peak must lie in the left half (`high = mid - 1`).

#### Q5: How does a SQL database retrieve a record instantly by its primary key?
**Answer:** When a primary key is created, the database automatically builds a **B+ Tree Index** (a self-balancing search tree variant). When a query is run, the database performs a binary-search-like traversal down the B+ Tree from the root node to the leaf node, locating the record in $O(\log n)$ time.

---

### 6. Big O Notation Explained in Simple Language
Big O notation is a tool used by software engineers to describe how much slower an algorithm gets as you feed it more data. It ignores specific CPU speeds or RAM sizes and focuses on the **growth rate** of the steps.
* **$O(1)$ (Constant Time):** "No matter how big the data is, it takes the same amount of time." (Example: looking at the first page of a book).
* **$O(\log N)$ (Logarithmic Time):** "In every step, you throw away half of the data." (Example: opening a dictionary in the middle, throwing away the wrong half).
* **$O(N)$ (Linear Time):** "If the data doubles, the time doubles." (Example: scanning a book page-by-page from the start).
* **$O(N^2)$ (Quadratic Time):** "If the data doubles, the time quadruples." (Example: comparing every book in the library with every other book).
