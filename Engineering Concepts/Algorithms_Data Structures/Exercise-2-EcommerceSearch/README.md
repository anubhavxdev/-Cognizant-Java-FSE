# Exercise 2 – E-commerce Platform Search

## Objective
The objective of this project is to implement and compare the search functionalities of an E-commerce platform using **Linear Search** and **Binary Search** algorithms in Java. This helps in understanding the performance differences, preconditions, and time complexities of these searching techniques.

## Problem Statement
Search functionality is a core component of any E-commerce platform. When a user queries a product by its unique `productId`, the search module must locate it quickly. 
This project implements:
1. **Linear Search:** Iterates through the list of products one-by-one until it finds the matching product.
2. **Binary Search:** Operates on a sorted array of products by continuously halving the search space.

We evaluate both algorithms by searching for specific product IDs (such as 103 and 104) and non-existent IDs (such as 999), demonstrating search speed and behavioral constraints.

---

## Folder Structure
```text
Exercise-2-EcommerceSearch/
│
├── Product.java             # Product entity representation
├── LinearSearch.java        # Sequential search implementation
├── BinarySearch.java        # Divide-and-conquer binary search implementation
├── SearchTest.java          # Driver program to run searches and print outputs
├── README.md                # Project documentation and theoretical analysis
└── output.txt               # Verification log containing console output
```

---

## Algorithms Used

### 1. Linear Search
* **Concept:** Starts from the first element of the array and checks each element sequentially.
* **Preconditions:** None. Works on both sorted and unsorted arrays.
* **Flow:**
  - Loop from index `0` to `n - 1`.
  - Check if the element at the current index has a `productId` equal to the target ID.
  - Return the product immediately if found.
  - Return `null` if the loop finishes without a match.

### 2. Binary Search
* **Concept:** Works on the divide-and-conquer strategy by dividing the search range in half.
* **Preconditions:** The array **must** be sorted based on the search key (`productId`).
* **Flow:**
  - Initialize pointers `low = 0` and `high = n - 1`.
  - While `low <= high`:
    - Calculate the midpoint `mid = low + (high - low) / 2`.
    - If the product at `mid` matches the target ID, return it.
    - If the target ID is greater than the `mid` product's ID, shift the search window to the right: `low = mid + 1`.
    - If the target ID is smaller than the `mid` product's ID, shift the search window to the left: `high = mid - 1`.
  - Return `null` if the target is not found.

---

## Complexity Analysis

### Time Complexity Table
Detailed breakdown of best, average, and worst-case scenarios:

| Algorithm | Best Case | Average Case | Worst Case |
| :--- | :--- | :--- | :--- |
| **Linear Search** | $O(1)$ <br>*(Target is the first item)* | $O(n)$ <br>*(Target is in the middle)* | $O(n)$ <br>*(Target is at the end or missing)* |
| **Binary Search** | $O(1)$ <br>*(Target is at the exact midpoint)* | $O(\log n)$ <br>*(Dividing search space logarithmic times)* | $O(\log n)$ <br>*(Target is at the boundaries or missing)* |

### Comparison Table

| Feature | Linear Search | Binary Search |
| :--- | :--- | :--- |
| **Time Complexity** | $O(n)$ | $O(\log n)$ |
| **Sorted Data Required** | No | Yes |
| **Easy to Implement** | Yes | Moderate |
| **Faster** | No | Yes |
| **Best suited for** | Small datasets or unsorted collections | Large, static, pre-sorted datasets |

### Space Complexity
* **Space Complexity:** $O(1)$ auxiliary space for both algorithms, as they execute in-place using simple pointers without allocating extra arrays or recursive call stacks.

---

## Output
Here is the exact output captured from running the `SearchTest` program (written to `output.txt`):

```text
========= LINEAR SEARCH =========

Searching Product ID : 103

Product Found

ID : 103

Name : Keyboard

Category : Electronics

===============================

========= BINARY SEARCH =========

Searching Product ID : 104

Product Found

ID : 104

Name : Monitor

Category : Electronics

===============================

Searching Product ID : 999

Product Not Found
```

---

## Advantages and Limitations

### Linear Search
* **Advantages:**
  - Extremely simple to understand and implement.
  - No preprocessing (like sorting) is required.
  - Highly efficient for very small arrays (e.g., $< 10$ elements).
* **Limitations:**
  - Inefficient for large datasets, as execution time scales linearly with the number of items.

### Binary Search
* **Advantages:**
  - Incredibly fast for large datasets. Searching through $1,000,000$ products takes a maximum of only $20$ comparisons.
  - Highly scalable.
* **Limitations:**
  - Requires the data to be sorted beforehand. Sorting takes $O(n \log n)$ time, which can be an overhead if the dataset updates frequently.

---

## Theory Section

### 1. Big O Notation (Beginner-Friendly Explanation)
Big O notation is a mathematical notation used to describe the efficiency of an algorithm in terms of execution time or space complexity as the input size ($n$) grows. It measures the worst-case scenario.

* **$O(1)$ - Constant Time:** The algorithm takes the same amount of time regardless of data size. Example: Fetching an array element by index.
* **$O(\log n)$ - Logarithmic Time:** The search space is halved in each step. Even if the data size doubles, the time only increases by a single step. Example: Binary Search.
* **$O(n)$ - Linear Time:** Execution time increases proportionally with the input size. Example: Linear Search.
* **$O(n^2)$ - Quadratic Time:** Execution time grows quadratically. Usually occurs in nested loops. Example: Bubble Sort.

#### Why is Big O Important?
When writing small applications, performance differences are unnoticeable. However, in production systems handling millions of users or entries, an $O(n)$ search algorithm might take seconds, causing latency, while an $O(\log n)$ algorithm will run in microseconds. Big O helps engineers choose algorithms that scale well.

### 2. Search Complexity Details
* **Linear Search:** The algorithm inspects every element from indices $0$ to $n-1$. In the average and worst case, it compares about $n/2$ and $n$ times respectively, yielding $O(n)$.
* **Binary Search:** In each comparison, we discard half of the array. The number of elements remaining after $k$ steps is $n / 2^k$. Setting this to $1$ gives $k = \log_2(n)$ steps, yielding $O(\log n)$.

### 3. Analysis: When to Use Which
* **Why Binary Search is faster:** Rather than scanning each element, it uses the sorted property of the array to instantly eliminate half of the search options at each step.
* **Why Binary Search requires sorted array:** If the array is unsorted, we cannot reliably determine whether the target lies in the left or right half of the midpoint, breaking the algorithm's core logic.
* **Use Linear Search when:**
  - The dataset is small.
  - The dataset is unsorted and updates frequently, making sorting overhead impractical.
  - You only need to run a search occasionally.
* **Use Binary Search when:**
  - The dataset is large.
  - The array is sorted once and queried multiple times (read-heavy operations).

---

## Additional Explanation

### 1. Difference between Linear Search and Binary Search
The core difference lies in how they search. **Linear Search** scans sequentially (one element at a time), which is slow but requires zero preparation. **Binary Search** uses a divide-and-conquer approach, selecting the middle element and narrowing the window. It is extremely fast but mandates that the array be sorted first.

### 2. Why Binary Search needs sorting
Without sorting, we cannot draw inferences about the position of other elements relative to the middle element. For instance, if the middle element is 10, and our target is 15, we can only assume the target lies to the right if the array elements are monotonically increasing. If unsorted, 15 could easily be on either side.

### 3. Real-world applications of Binary Search
* **Database Indexing:** Database engines sort primary keys so they can use binary search variants (like B+ Tree lookups) to find rows instantly.
* **Version Control System (Git bisect):** Git uses binary search to find which specific commit introduced a bug in a codebase.
* **Compiler Symbols Table Lookup:** Compilers store keywords and symbol tokens in sorted structures to quickly parse identifiers using binary search.
* **Dictionary or Phonebook Lookups:** Searching alphabetically by splitting chapters.

### 4. Five Viva Questions with Answers

#### Q1: What is the recurrence relation for Binary Search?
**Answer:** The recurrence relation is $T(n) = T(n/2) + c$. This indicates that at each step, the problem of size $n$ is reduced to a subproblem of size $n/2$, plus a constant amount of work ($c$) for comparisons. Solving this relation using Master's Theorem yields $T(n) = O(\log n)$.

#### Q2: Can Binary Search be implemented on a LinkedList?
**Answer:** Technically yes, but practically it is highly inefficient. A `LinkedList` does not support random access (accessing elements by index in $O(1)$). Finding the middle element requires traversing from the head node, taking $O(n)$ time. Consequently, Binary Search on a LinkedList yields $O(n \log n)$ time, which is worse than Linear Search ($O(n)$).

#### Q3: Why is the calculation `mid = low + (high - low) / 2` preferred over `mid = (low + high) / 2`?
**Answer:** In Java, if `low` and `high` are very large integers (close to `Integer.MAX_VALUE`), their sum `low + high` can exceed the maximum integer limit ($2^{31}-1$). This causes integer overflow, resulting in a negative number and throwing an `ArrayIndexOutOfBoundsException`. The formula `low + (high - low) / 2` avoids overflow.

#### Q4: What is the space complexity of recursive vs. iterative Binary Search?
**Answer:** 
* **Iterative Binary Search:** $O(1)$ space because it uses only a few index variables and a loop.
* **Recursive Binary Search:** $O(\log n)$ space because each recursive call creates a new stack frame on the call stack. The maximum depth of the call stack matches the search depth, which is logarithmic.

#### Q5: Under what condition is Linear Search better than Binary Search?
**Answer:** Linear search is better when the array is small (e.g., less than 10-15 elements) because the overhead of sorting and pointer manipulations in binary search is not worth the gain. It is also better for unsorted arrays that undergo frequent insertions and deletions, where sorting before every search would cost $O(n \log n)$ time.

---

### 5. Five Interview Questions related to searching algorithms

#### Q1: Given an array sorted in descending order, how would you modify Binary Search to find an element?
**Answer:** You only need to invert the comparison conditions. In a descending array, if the target is greater than the middle element, it must lie to the left (set `high = mid - 1`). If the target is smaller than the middle element, it must lie to the right (set `low = mid + 1`).

#### Q2: What is Ternary Search and how does it compare to Binary Search?
**Answer:** Ternary Search divides the sorted search space into three equal parts using two midpoints (`mid1` and `mid2`) instead of one. Its time complexity is $O(\log_3 n)$. However, it performs more comparisons per step than binary search. As a result, the constant factor in Ternary Search is higher, making it slower in practice than Binary Search.

#### Q3: How does Interpolation Search work, and what is its time complexity?
**Answer:** Interpolation search is an improvement over binary search for sorted and uniformly distributed arrays. Instead of searching the exact middle, it estimates the position based on the value of the target (similar to looking up "Z" in a phonebook near the end). Its average-case time complexity is $O(\log(\log n))$, though it degrades to $O(n)$ if data distribution is highly non-uniform.

#### Q4: How do you search for an element in an infinite sorted array?
**Answer:** Since we do not know the `high` boundary, we must find it dynamically. We start at index `0` and index `1`, and double our bounds (`low = high`, `high = high * 2`) repeatedly as long as the element at `high` is smaller than the target. Once we find a range where the target lies, we perform a standard Binary Search within that range. This is known as Exponential Search, taking $O(\log p)$ time where $p$ is the target's position.

#### Q5: Explain the search strategy on a 2D matrix where each row and column is sorted in ascending order.
**Answer:** We can start from the top-right corner of the matrix (row 0, col $M-1$). If the current element equals the target, return it. If the current element is greater than the target, we know the entire column cannot contain it, so move left (`col--`). If the current element is smaller than the target, the entire row to the left cannot contain it, so move down (`row++`). This takes $O(N + M)$ time.

---

### 6. Asymptotic Notations Explained in Simple Language
Asymptotic notation is a mathematical language used to describe the running time of algorithms in relation to the input size. It abstracts away hardware differences (CPU speed, RAM) and focuses on growth rate.
* **Big O ($O$):** Describes the **upper bound** (worst-case scenario). It represents the maximum time or space an algorithm will take. (e.g., "The algorithm will take *at most* $f(n)$ steps").
* **Omega ($\Omega$):** Describes the **lower bound** (best-case scenario). It represents the minimum time or space an algorithm requires. (e.g., "The algorithm will take *at least* $f(n)$ steps").
* **Theta ($\Theta$):** Describes the **tight bound** (exact rate of growth). It occurs when the best case and worst case have the same rate of growth. (e.g., "The algorithm will take *exactly* $f(n)$ steps on average").
