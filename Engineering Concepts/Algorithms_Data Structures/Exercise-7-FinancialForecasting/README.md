# Exercise 7 – Financial Forecasting

## Objective
The objective of this project is to design, implement, and analyze a **Financial Forecasting System** in Java using **Recursion**. This exercise explores the mechanics of recursive programming, base cases, recursive cases, and execution stack memory allocation while calculating compound interest values over multiple years.

## Problem Statement
Predicting the future value of an investment is key to financial planning. Given a current investment amount ($P$), a constant annual growth rate ($r$), and a term in years ($n$), we want to project the future value.
The formula for compound interest applied iteratively is:
$$FV = PV \times (1 + r)^n$$

In this exercise, we implement this compound projection using a **recursive algorithm** only, where each year of growth corresponds to a recursive call that reduces the problem size ($n \to n - 1$) until the base case ($n = 0$) is reached.

---

## Folder Structure
```text
Exercise-7-FinancialForecasting/
│
├── FinancialForecast.java       # Financial calculations containing recursive logic
├── ForecastTest.java            # Test driver executing and formatting cases
├── README.md                    # Comprehensive documentation and analysis
└── output.txt                   # Verification log containing captured console output
```

---

## Files Description

1. **[FinancialForecast.java](file:///c:/Users/anubh/OneDrive/Desktop/kranti/cognizant/myassests/-Cognizant-Java-FSE/week1/Algorithms_Data%20Structures/Exercise-7-FinancialForecasting/FinancialForecast.java):** Houses the core business logic. It contains the recursive method `predictFutureValue` that checks the base case (`years <= 0`) and makes recursive self-calls using the formula `predictFutureValue(currentValue * (1 + growthRate), growthRate, years - 1)`.
2. **[ForecastTest.java](file:///c:/Users/anubh/OneDrive/Desktop/kranti/cognizant/myassests/-Cognizant-Java-FSE/week1/Algorithms_Data%20Structures/Exercise-7-FinancialForecasting/ForecastTest.java):** Driver class that triggers the system with test values (Investment ₹10,000 at 10% for 5 years; ₹50,000 at 8% for 10 years; ₹75,000 at 12% for 15 years; and a base case test of 0 years).
3. **[output.txt](file:///c:/Users/anubh/OneDrive/Desktop/kranti/cognizant/myassests/-Cognizant-Java-FSE/week1/Algorithms_Data%20Structures/Exercise-7-FinancialForecasting/output.txt):** The verified console log.

---

## Working Principle: Recursive Forecasting
A recursive function is a function that calls itself directly or indirectly to solve smaller instances of the same problem. 

### Step-by-Step execution diagram for `predict(10000, 10%, 3)`:
```text
predict(10000.0, 0.10, 3)
   │
   ├── nextValue = 10000 * 1.10 = 11000.0
   └── predict(11000.0, 0.10, 2)
          │
          ├── nextValue = 11000 * 1.10 = 12100.0
          └── predict(12100.0, 0.10, 1)
                 │
                 ├── nextValue = 12100 * 1.10 = 13310.0
                 └── predict(13310.0, 0.10, 0)
                        │
                        └── [Base Case Reached: years <= 0]
                            └── Returns 13310.0
```

---

## Complexity Analysis

### Time Complexity: $O(n)$
To project values for $n$ years, the function calls itself exactly $n$ times. Each call performs constant-time arithmetic operations ($O(1)$), leading to a total time complexity of $O(n)$.

### Space Complexity: $O(n)$
Since each recursive call waits for the results of the next call, the JVM cannot discard the stack frames. For $n$ years, $n$ recursive frames are placed on the **Recursive Call Stack** in stack memory. Therefore, the space complexity is $O(n)$.

---

## Optimization and Alternatives

### Why Recursion is Not Always the Best Option:
For very large inputs (e.g., forecasting daily growth over 30 years, $n = 10950$), recursion will create $10,950$ stack frames. This will consume significant memory and trigger a `StackOverflowError`. 

### Optimization Techniques:

1. **Iteration:** Uses a simple loop (`for` or `while`) to compute the value. It runs in $O(n)$ time but takes **$O(1)$ space** because it doesn't create recursive stack frames.
2. **Tail Recursion:** A special form of recursion where the recursive call is the absolute last operation in the function. In languages that support Tail Call Optimization (TCO), the compiler reuses the current stack frame, reducing space complexity to $O(1)$. (Note: Standard Java JVM does not support TCO).
3. **Memoization:** Storing the results of expensive function calls in a cache (like an array or map) so they can be retrieved instantly if the same parameters are requested again.
4. **Dynamic Programming (DP):** A bottom-up approach that solves smaller subproblems first, stores their results, and builds up to the main solution. Useful for overlapping subproblems (e.g., Fibonacci series).

---

## Expected Output
The console output of running `ForecastTest` (as stored in `output.txt`):

```text
========= FINANCIAL FORECAST =========

Investment : ₹10000

Growth Rate : 10%

Years : 5

Predicted Value : ₹16105.10

-----------------------------------------

Investment : ₹50000

Growth Rate : 8%

Years : 10

Predicted Value : ₹107946.25

-----------------------------------------

Investment : ₹75000

Growth Rate : 12%

Years : 15

Predicted Value : ₹410000+

-----------------------------------------

Years = 0

Predicted Value : Current Investment
```

---

## Advantages and Limitations

### Advantages:
* **Code Clarity:** Recursive solutions map directly to mathematical induction formulas, leading to clean and readable code.
* **No Manual State Tracking:** The execution call stack manages variables and parameters automatically.

### Limitations:
* **Memory Overhead:** High memory usage on the call stack ($O(n)$ space).
* **Risk of Stack Overflow:** Large values of $n$ lead to program crashes.
* **Performance Cost:** Pushing and popping frames to and from stack memory adds execution overhead compared to simple loops.

---

## Learning Outcomes
* Understood the concepts of **Base Case** (termination condition) and **Recursive Case** (problem reduction).
* Explored how the JVM call stack stores state during recursion.
* Analyzed the time and space costs of recursive algorithms.
* Examined alternative optimization structures (Iteration, Tail Recursion, Dynamic Programming).

---

## Additional Explanation

### 1. What is Recursion?
Recursion is a programming technique where a method calls itself to solve a problem. It works by breaking a complex problem down into simpler, smaller subproblems of the same nature, until a trivial "base case" is reached.

### 2. What is a Base Case?
The basecase is the termination condition of a recursive function. It is the branch of code that does *not* make a recursive call, returning a value directly. Without a base case, the function would call itself indefinitely.

### 3. What happens if there is no Base Case?
If a recursive function lacks a base case, or if the parameters are modified in a way that never triggers it, the function enters **Infinite Recursion**. The method will call itself repeatedly until the JVM's stack memory is exhausted, throwing a **`java.lang.StackOverflowError`** and crashing the application.

### 4. Difference between Recursion and Iteration

| Feature | Recursion | Iteration |
| :--- | :--- | :--- |
| **Control Structure** | Uses selection statements (`if-else`) and self-calls. | Uses loops (`for`, `while`). |
| **Termination** | Reaches the base case. | Loop condition becomes false. |
| **Space Complexity** | $O(n)$ due to call stack frames. | $O(1)$ (no stack overhead). |
| **Speed** | Marginally slower (due to function call overhead). | Faster. |
| **Infinite Execution**| Causes `StackOverflowError`. | Runs forever (consumes CPU, not stack memory). |

### 5. Advantages of Recursion
* Reduces code length and simplifies complex data structure traversals (like Binary Trees, Graphs).
* Avoids maintaining complex loops and variables in code.

### 6. Disadvantages of Recursion
* High memory overhead ($O(n)$ stack space).
* Harder to debug and trace stack frames.
* Potential to crash via stack overflow.

### 7. Real-world applications of Recursion
* **Directory Traversal:** Searching files in a nested folder hierarchy.
* **JSON/XML Parsing:** Compilers parsing hierarchical document structures.
* **Graph Traversals:** Depth-First Search (DFS) in networking or map routing.
* **Divide and Conquer Algorithms:** Quick Sort, Merge Sort, and Binary Search.

### 8. Financial forecasting use cases
* **Retirement Planning:** Calculating long-term 401(k) compounding.
* **Real Estate Projections:** Forecasting home value appreciation over decades.
* **Inflation Tracking:** Calculating the future purchasing power of currency.
* **Corporate Valuation:** Projecting revenue growth or depreciation schedules.

---

### 9. Five Viva Questions with Answers

#### Q1: What is Stack Overflow in Java?
**Answer:** A `StackOverflowError` is a runtime error thrown by the JVM when the call stack memory allocated for thread execution is completely filled. This usually happens when a recursive method calls itself too many times without reaching a base case, exhausting the stack space.

#### Q2: What is the maximum recursion depth in Java?
**Answer:** There is no fixed limit. The maximum recursion depth depends on the stack size allocated to the JVM thread (configured using the `-Xss` parameter) and the number of local variables and parameters inside the recursive method (larger stack frames exhaust memory faster). Typically, it is around $10,000$ to $20,000$ calls.

#### Q3: Does Java support Tail Call Optimization (TCO)?
**Answer:** **No**, the standard Oracle/OpenJDK HotSpot JVM does not support Tail Call Optimization. Even if you write a tail-recursive method, Java will still push a new stack frame for each call, meaning the space complexity remains $O(n)$.

#### Q4: Why does recursion consume more memory than iteration?
**Answer:** When a loop runs, it simply updates local variables in the same stack frame. When a recursive method is called, the JVM must freeze the current method's execution, allocate a new stack frame in memory to hold the next call's parameters, return address, and local variables, and push it onto the call stack. This allocation multiplies for each recursive depth.

#### Q5: Can any recursive algorithm be converted into an iterative one?
**Answer:** **Yes**. Any recursive algorithm can be rewritten iteratively using loops, often utilizing an explicit `Stack` data structure (like `java.util.Stack`) to mimic the JVM's call stack.

---

### 10. Five Interview Questions related to Recursion

#### Q1: Explain the difference between Direct and Indirect Recursion.
**Answer:**
* **Direct Recursion:** A method calls itself directly (e.g., Method A calls Method A).
* **Indirect Recursion:** A method calls another method, which in turn calls the first method (e.g., Method A calls Method B, and Method B calls Method A).

#### Q2: How does Memoization optimize recursive Fibonacci calculations?
**Answer:** A naive recursive Fibonacci algorithm recalculates the same values multiple times, leading to $O(2^n)$ exponential time. Memoization stores computed values in an array. When `fib(k)` is requested, it checks the array first. If present, it returns the value in $O(1)$, reducing the time complexity to $O(n)$.

#### Q3: What is "Backtracking" and how does it relate to Recursion?
**Answer:** Backtracking is a systematic search algorithm that tries to build a solution incrementally and abandons a path ("backtracks") as soon as it determines that the path cannot lead to a valid solution. Recursion is the natural way to implement backtracking, as returning from a recursive call automatically restores the previous state. Examples: N-Queens problem, Maze solving.

#### Q4: Write a recursive function to check if a String is a palindrome.
**Answer:**
```java
public boolean isPalindrome(String s) {
    if (s.length() <= 1) return true; // Base Case
    if (s.charAt(0) != s.charAt(s.length() - 1)) return false;
    return isPalindrome(s.substring(1, s.length() - 1)); // Recursive Case
}
```

#### Q5: Explain the execution flow of the Towers of Hanoi puzzle using Recursion.
**Answer:** Towers of Hanoi is a classic recursive puzzle. To move $N$ disks from Source ($A$) to Destination ($C$) using an Auxiliary peg ($B$):
1. Recursively move $N - 1$ disks from $A$ to $B$ using $C$.
2. Move the remaining largest disk directly from $A$ to $C$.
3. Recursively move the $N - 1$ disks from $B$ to $C$ using $A$.
The recurrence relation is $T(n) = 2T(n-1) + 1$, yielding a time complexity of $O(2^n)$.
