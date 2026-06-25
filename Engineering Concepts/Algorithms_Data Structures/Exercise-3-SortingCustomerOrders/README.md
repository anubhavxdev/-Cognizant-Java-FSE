# Exercise 3 – Sorting Customer Orders

## Objective
The objective of this project is to implement, compare, and analyze two fundamental sorting algorithms: **Bubble Sort** and **Quick Sort**. By developing a Customer Order Management System, we sort customer orders based on their `totalPrice` in ascending order and compare the performance characteristics (time and space complexities, stability, execution paradigms) of both approaches.

## Problem Statement
A Customer Order Management System needs to present orders sorted by their transaction value (`totalPrice`). 
The system must support:
1. **Bubble Sort:** A comparison-based algorithm that bubbbles up elements.
2. **Quick Sort:** An efficient divide-and-conquer algorithm using pivot elements.

We sort a test array of orders and display them before and after sorting to verify correctness and analyze algorithmic efficiency.

---

## Folder Structure
```text
Exercise-3-SortingCustomerOrders/
│
├── Order.java               # Order entity class (orderId, customerName, totalPrice)
├── BubbleSort.java          # Manual implementation of Bubble Sort
├── QuickSort.java           # Manual implementation of Quick Sort (Lomuto partition)
├── SortingTest.java         # Driver test program simulating order sorting
├── README.md                # Comprehensive documentation and analysis
└── output.txt               # Verification log containing captured console output
```

---

## Sorting Algorithms Used

### 1. Bubble Sort
* **Concept:** Simplest sorting algorithm that repeatedly steps through the list, compares adjacent elements, and swaps them if they are in the wrong order.
* **Working Mechanism:**
  - **Adjacent Comparison:** Evaluates elements at index `j` and `j + 1`.
  - **Swapping:** Swaps positions if `orders[j].totalPrice > orders[j+1].totalPrice`.
  - **Passes:** In each pass $i$, the largest unsorted element "bubbles" up to its correct position at the end of the array. The algorithm runs up to $n - 1$ passes.
  - **Early Exit Optimization:** If a complete pass occurs without any swaps, the array is already sorted, and the algorithm terminates immediately.

### 2. Quick Sort
* **Concept:** Highly efficient divide-and-conquer sorting algorithm.
* **Working Mechanism:**
  - **Pivot Selection:** Chooses a pivot element (the last element of the partition in our Lomuto implementation).
  - **Partitioning:** Re-arranges the array such that all elements with values less than or equal to the pivot are moved to its left, and all elements with values greater are moved to its right.
  - **Recursive Calls:** Recursively applies the same pivot-and-partition operation to the resulting left and right sub-arrays until the base case (partition size $< 2$) is met.

---

## Complexity Analysis

### Time Complexity Table

| Algorithm | Best Case | Average Case | Worst Case |
| :--- | :--- | :--- | :--- |
| **Bubble Sort** | $O(n)$ <br>*(Array is already sorted; exits in first pass)* | $O(n^2)$ <br>*(Quadratic comparisons)* | $O(n^2)$ <br>*(Reverse sorted array)* |
| **Quick Sort** | $O(n \log n)$ <br>*(Pivot divides array evenly at each step)* | $O(n \log n)$ <br>*(Logarithmic depth of recursion)* | $O(n^2)$ <br>*(Pivot selection is highly skewed, e.g. already sorted)* |

### Space Complexity Table

| Algorithm | Space Complexity | Explanation |
| :--- | :--- | :--- |
| **Bubble Sort** | $O(1)$ | Sorts completely in-place. Only requires a few index variable allocations. |
| **Quick Sort** | $O(\log n)$ average <br> $O(n)$ worst-case | In-place sorting, but consumes memory on the execution call stack due to recursion. |

### Detailed Comparison Table

| Feature | Bubble Sort | Quick Sort |
| :--- | :--- | :--- |
| **Time (Average)** | $O(n^2)$ | $O(n \log n)$ |
| **Space (Auxiliary)** | $O(1)$ | $O(\log n)$ |
| **Stability** | **Yes** *(Does not swap equal elements)* | **No** *(Swaps elements across the pivot)* |
| **In-Place** | Yes | Yes |
| **Performance on Large Data**| Extremely Slow | Very Fast |
| **Elegance / Simplicity** | Very Simple | Moderate |

---

## Expected Output
The console output of running `SortingTest` (as stored in `output.txt`):

```text
========= ORIGINAL ORDERS =========

101 Rahul 2500

102 Priya 1200

103 Aman 8900

104 Neha 4200

105 Karan 3000

===============================

========= BUBBLE SORT =========

102 Priya 1200

101 Rahul 2500

105 Karan 3000

104 Neha 4200

103 Aman 8900

===============================

========= QUICK SORT =========

102 Priya 1200

101 Rahul 2500

105 Karan 3000

104 Neha 4200

103 Aman 8900
```

---

## Advantages and Limitations

### Bubble Sort
* **Advantages:**
  - Simple to understand, write, and debug.
  - Stable sort (keeps relative order of elements with equal values).
  - $O(1)$ auxiliary space (in-place).
* **Limitations:**
  - Highly inefficient for large datasets due to $O(n^2)$ time complexity.
  - Performs a large number of write/swap operations in memory.

### Quick Sort
* **Advantages:**
  - Very fast in practice with a small constant factor.
  - Excellent cache locality, making it CPU-cache friendly.
  - Performs sorting in-place, saving memory over Merge Sort.
* **Limitations:**
  - Unstable sort (can change relative order of identical elements).
  - Worst-case time complexity of $O(n^2)$ if pivot selection is poor.

---

## Performance Analysis

* **Why Quick Sort is preferred in real-world applications:** In real-world workloads, Quick Sort operates in $O(n \log n)$ time. It processes memory contiguous chunks efficiently (cache locality) and sorts in-place. It runs much faster than Heap Sort or Merge Sort in practice due to fewer overhead operations.
* **Why Bubble Sort is mainly used for learning:** Its logic directly maps to how humans might sorted physical cards, making it an excellent teaching aid. However, its $O(n^2)$ complexity makes it unusable for commercial applications.
* **Which algorithm performs better on large datasets:** Quick Sort performs exponentially better as the dataset size grows. For $100,000$ items, Bubble Sort requires up to $10,000,000,000$ operations, taking minutes, whereas Quick Sort takes about $1,700,000$ operations, executing in milliseconds.

---

## Learning Outcomes
* Gained experience writing nested loop algorithms (Bubble Sort) and recursive partition algorithms (Quick Sort).
* Learned to analyze partition boundaries (`low`, `high`, `mid`) and handle pivot selections.
* Discovered the trade-off between stability (Bubble Sort) and speed (Quick Sort).
* Experienced using in-place operations to limit space overhead.

---

## Additional Explanation

### 1. Why sorting is important
Sorting is a fundamental operation in computer science. Organizing unsorted data into order is crucial because:
* It enables rapid retrieval (e.g., Binary Search requires a sorted array).
* It groups identical or duplicate items together instantly.
* It makes data human-readable and enables structural reporting (like transaction history, leaderboards).
* Many complex algorithms (Kruskal’s MST, Closest pair of points) require sorted inputs to work correctly.

### 2. Bubble Sort vs Quick Sort
Bubble Sort relies on slow, local comparisons and swaps of adjacent elements, leading to $O(n^2)$ work. Quick Sort uses a global divide-and-conquer strategy, selecting a pivot and positioning elements around it. This reduces the problem size logarithmically, resulting in $O(n \log n)$ work.

### 3. Stable vs Unstable sorting
* **Stable Sorting:** A sorting algorithm is stable if it preserves the relative order of records with equal keys. If we sort an array of orders containing `[A, price=100]` and `[B, price=100]`, a stable sort guarantees `A` remains before `B`.
* **Unstable Sorting:** Does not guarantee preservation of relative order. During partition and swap operations in Quick Sort, elements are swapped across long distances, which can disrupt their original sequence.

### 4. In-place sorting
An in-place sorting algorithm modifies the input array directly without utilizing auxiliary data structures (like temporary arrays) proportional to the input size. Both Bubble Sort ($O(1)$ auxiliary space) and Quick Sort ($O(\log n)$ stack space) are in-place, whereas Merge Sort is not in-place because it requires a temporary array of size $O(n)$ to merge segments.

### 5. Divide and Conquer technique
Divide and Conquer is an algorithmic design paradigm that solves a problem using three steps:
1. **Divide:** Break the large problem into smaller sub-problems. (In Quick Sort, partition the array using a pivot).
2. **Conquer:** Solve the sub-problems recursively. (Sort the left and right sub-arrays).
3. **Combine:** Merge the sub-problem solutions. (In Quick Sort, since elements are sorted in-place, no explicit combine step is needed).

### 6. Real-world applications of sorting algorithms
* **Commercial Databases:** Sort search results by dates, prices, or relevance.
* **Logistics & Delivery:** Sort delivery destinations geographically to calculate the shortest path.
* **Media Players:** Sorting music tracks alphabetically or by duration.
* **Search Engine Rankings:** Ordering webpages by PageRank score.

### 7. Five Viva Questions with Answers

#### Q1: What makes Quick Sort degrade to its worst-case complexity of $O(n^2)$?
**Answer:** The worst case occurs when the partition is highly unbalanced. For example, if the array is already sorted in ascending or descending order and we always select the last element as the pivot, one partition gets $0$ elements and the other gets $n - 1$ elements. The recursion depth becomes $n$, leading to $O(n^2)$ time.

#### Q2: How can we prevent the worst-case behavior of Quick Sort?
**Answer:** We can use **Randomized Quick Sort** (selecting a random element as pivot and swapping it with the last element before partitioning) or the **Median-of-Three** method (choosing the median of the first, middle, and last elements as pivot). These techniques reduce the probability of encountering the worst case to practically zero.

#### Q3: What is the benefit of the `swapped` flag in Bubble Sort?
**Answer:** Without the `swapped` flag, Bubble Sort will always run all $(n - 1)$ passes, taking $O(n^2)$ comparisons even if the array is already sorted. The flag checks if any swap occurred during a pass. If no swap occurred, it immediately halts, reducing the best-case time complexity to $O(n)$.

#### Q4: Why does Quick Sort have a space complexity of $O(\log n)$ if it sorts in-place?
**Answer:** Although it does not allocate auxiliary arrays, Quick Sort is recursive. Each recursive call places a stack frame on the system's execution call stack to remember boundaries. In the average case, the height of the recursion tree is $\log_2(n)$, leading to $O(\log n)$ space overhead.

#### Q5: Is Quick Sort stable? Explain why or why not.
**Answer:** Quick Sort is **not stable**. During the partitioning process, elements are swapped over the pivot across large distances. This swapping can easily move an element past another element with the same key, breaking their original relative ordering.

---

### 8. Five Interview Questions related to sorting algorithms

#### Q1: Compare Quick Sort and Merge Sort.
**Answer:**
* **Time:** Both have $O(n \log n)$ average time. Merge Sort guarantees $O(n \log n)$ in the worst-case, while Quick Sort can degrade to $O(n^2)$.
* **Space:** Quick Sort is in-place ($O(\log n)$ stack space), whereas Merge Sort requires $O(n)$ extra space to merge sub-arrays.
* **Stability:** Merge Sort is stable, whereas Quick Sort is unstable.
* **Workloads:** Merge Sort is preferred for linked lists and massive external sorting; Quick Sort is preferred for arrays in memory.

#### Q2: What is "Dual-Pivot Quick Sort" and where is it used?
**Answer:** Dual-Pivot Quick Sort uses two pivots instead of one, dividing the array into three partitions. It reduces the number of memory scans and comparisons. Java's `Arrays.sort()` uses a highly optimized implementation of Dual-Pivot Quick Sort for sorting primitive types.

#### Q3: How does the "Pivot" selection affect recursion depth in Quick Sort?
**Answer:** If the pivot splits the array into two equal halves, the recursion tree height is $\log_2(n)$ (balanced). If the pivot splits the array into sizes $0$ and $n-1$ at each step, the recursion depth is $n$ (skewed). A larger recursion depth means more stack frames, risking a StackOverflowError for large arrays.

#### Q4: What is Hybrid Sorting (e.g., IntroSort or Timsort)?
**Answer:** Hybrid sorting algorithms combine multiple algorithms to optimize performance:
* **IntroSort:** Starts with Quick Sort, switches to Heap Sort if the recursion depth exceeds a threshold (to avoid $O(n^2)$), and uses Insertion Sort for small partitions.
* **Timsort:** Combines Merge Sort and Insertion Sort. It is stable and highly optimized for real-world sorted runs. Used in Java's objects sorting (`Collections.sort()`).

#### Q5: Explain how you would sort an array of 1 million integers that are in the range of 1 to 100.
**Answer:** Since the range of values is very small ($1$ to $100$) compared to the number of elements ($1,000,000$), we should use **Counting Sort**. We create a frequency array of size $100$ and count occurrences in a single pass ($O(n)$ time). Then, we rewrite the array based on counts. This runs in $O(n)$ time and $O(1)$ auxiliary space, bypassing comparisons completely.
