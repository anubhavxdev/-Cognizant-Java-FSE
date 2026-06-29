# Exercise 4: Functions - Reference Guide

## Objective
This exercise covers the creation and execution of Oracle PL/SQL functions to encapsulate reusable business calculations (such as age calculations, EMI installments, and balance verifications).

---

## Folder Structure
```text
PL_SQL/
└── Exercise_4_Functions/
      ├── schema.sql
      ├── sample_data.sql
      ├── CalculateAge.sql
      ├── CalculateMonthlyInstallment.sql
      ├── HasSufficientBalance.sql
      └── README.md
```

---

## Concepts Used
* **Stored Functions**: Schema-level PL/SQL objects that accept parameters and must return a single value of a specified datatype.
* **Predefined Date Functions**: Used `MONTHS_BETWEEN` to compute month differences.
* **Predefined Math Functions**: Used `POWER` and `ROUND` for calculations.
* **Datatypes**: Investigated PL/SQL specific datatypes like `BOOLEAN` and their constraints in standard SQL.

---

## Advantages of Stored Functions
1. **Reusability**: Write calculation logic once and reuse it across multiple SQL queries and PL/SQL blocks.
2. **Readability**: Keeps queries clean and readable by hiding complex logic.
3. **Consistency**: Changes to business logic need to be applied in only one place.

---

## File-by-File Documentation

### 1. schema.sql
Declares core tables (`Customers`, `Accounts`, `Transactions`, `Loans`, `Employees`) to support standard banking procedures.
* **Problem & Objective**: Setup clean banking schemas with referential constraints.
* **Complete Code**: See [schema.sql](file:///c:/Users/anubh/OneDrive/Desktop/kranti/cognizant/myassests/-Cognizant-Java-FSE/PL_SQL/Exercise_4_Functions/schema.sql) in the exercise directory.
* **Common Errors**: ORA-02291 integrity constraint (foreign key) violations when inserting dependent table values before parent records.

---

### 2. sample_data.sql
Seeds bank customer, account, employee, and loan records.
* **Problem & Objective**: Populate tables with realistic data.
* **Complete Code**: See [sample_data.sql](file:///c:/Users/anubh/OneDrive/Desktop/kranti/cognizant/myassests/-Cognizant-Java-FSE/PL_SQL/Exercise_4_Functions/sample_data.sql).

---

### 3. CalculateAge.sql

#### Problem Statement
Write a function `CalculateAge` that accepts a customer's Date of Birth (DOB) and returns their current age in years.

#### Objective
Calculate age using Oracle's `MONTHS_BETWEEN()` function, rounding down to the nearest whole year, and handling null parameters safely.

#### Complete Oracle PL/SQL Code
```sql
CREATE OR REPLACE FUNCTION CalculateAge (
    p_dob IN DATE
) RETURN NUMBER IS
    v_age NUMBER;
BEGIN
    IF p_dob IS NULL THEN
        RETURN NULL;
    END IF;
    
    v_age := FLOOR(MONTHS_BETWEEN(SYSDATE, p_dob) / 12);
    
    RETURN v_age;
EXCEPTION
    WHEN OTHERS THEN
        RETURN NULL;
END CalculateAge;
/
```

#### Detailed Line-by-Line Explanation
* **Line 1**: Declares function signature with input parameter `p_dob` (DATE type) and return parameter (NUMBER type).
* **Line 7**: Checks if input is `NULL`. If true, returns `NULL` immediately.
* **Line 12**: Uses `MONTHS_BETWEEN(SYSDATE, p_dob)` to calculate elapsed months, divides by 12, and uses `FLOOR` to round down to the nearest whole year.
* **Line 14**: Returns the calculated age.
* **Line 15–17**: Catches exceptions and returns `NULL`.

#### Sample Input / Output
* **Input**: `TO_DATE('1995-08-25', 'YYYY-MM-DD')`
* **Output**: `30` (depending on the current year).

#### Execution Commands
```sql
SELECT CustomerID, Name, CalculateAge(DOB) FROM Customers;
```

#### Viva & Interview Questions
1. **How does `MONTHS_BETWEEN` compute fractions?**
   If dates are the same day of the month or both are the last days of the month, the result is a whole number. Otherwise, it calculates fractions based on a 31-day month.
2. **What happens if the first parameter of `MONTHS_BETWEEN` is earlier than the second?**
   It returns a negative number.

#### Best Practices & Complexity
* **Complexity**: $O(1)$ time complexity.
* **Best Practice**: Use `FLOOR` instead of `ROUND` for age, as a person does not turn a year older until their actual birth date.

---

### 4. CalculateMonthlyInstallment.sql

#### Problem Statement
Write a function `CalculateMonthlyInstallment` that computes the Equated Monthly Installment (EMI) for a loan based on principal, annual rate, and years.

#### Objective
Implement the standard EMI calculation formula, handle a 0% interest rate case, and round to 2 decimal places.

#### Complete Oracle PL/SQL Code
```sql
CREATE OR REPLACE FUNCTION CalculateMonthlyInstallment (
    p_loan_amount IN NUMBER,
    p_interest_rate IN NUMBER,
    p_duration_years IN NUMBER
) RETURN NUMBER IS
    v_monthly_rate NUMBER;
    v_total_months NUMBER;
    v_emi NUMBER;
BEGIN
    IF p_loan_amount IS NULL OR p_interest_rate IS NULL OR p_duration_years IS NULL THEN
        RETURN 0;
    END IF;
    IF p_loan_amount <= 0 OR p_duration_years <= 0 OR p_interest_rate < 0 THEN
        RETURN 0;
    END IF;
    
    v_total_months := p_duration_years * 12;
    
    IF p_interest_rate = 0 THEN
        v_emi := p_loan_amount / v_total_months;
    ELSE
        v_monthly_rate := (p_interest_rate / 12) / 100;
        v_emi := (p_loan_amount * v_monthly_rate * POWER(1 + v_monthly_rate, v_total_months)) /
                 (POWER(1 + v_monthly_rate, v_total_months) - 1);
    END IF;
    
    RETURN ROUND(v_emi, 2);
END CalculateMonthlyInstallment;
/
```

#### Detailed Line-by-Line Explanation
* **Lines 10–15**: Validates that parameters are non-null and positive.
* **Line 17**: Computes total months: `p_duration_years * 12`.
* **Line 19–20**: If interest rate is 0%, calculates EMI as simple principal division `p_loan_amount / v_total_months` to avoid division-by-zero errors.
* **Lines 22–24**: Calculates monthly rate and applies the standard EMI formula: $EMI = \frac{P \times r \times (1+r)^n}{(1+r)^n - 1}$.
* **Line 28**: Rounds to 2 decimal places and returns.

#### Sample Input / Output
* **Input**: Principal = 10,000, Rate = 12%, Years = 1.
* **Output**: `888.49`

#### Execution Commands
```sql
SELECT LoanID, CalculateMonthlyInstallment(LoanAmount, InterestRate, 5) FROM Loans;
```

#### Viva & Interview Questions
1. **What math function is used for exponents in PL/SQL?**
   Oracle's built-in `POWER(base, exponent)` function.

#### Best Practices & Complexity
* **Complexity**: $O(1)$ time complexity.
* **Best Practice**: Always check for divisor inputs matching zero before division to avoid ORA-01476.

---

### 5. HasSufficientBalance.sql

#### Problem Statement
Write a function `HasSufficientBalance` that verifies if an account has enough balance for a specified transaction.

#### Objective
Query the `Accounts` table, compare balances, return a PL/SQL `BOOLEAN` value, and handle exceptions.

#### Complete Oracle PL/SQL Code
```sql
CREATE OR REPLACE FUNCTION HasSufficientBalance (
    p_account_id IN NUMBER,
    p_amount IN NUMBER
) RETURN BOOLEAN IS
    v_balance NUMBER(15, 2);
BEGIN
    IF p_amount < 0 THEN
        RETURN FALSE;
    END IF;
    
    SELECT Balance INTO v_balance FROM Accounts WHERE AccountID = p_account_id;
    
    IF v_balance >= p_amount THEN
        RETURN TRUE;
    ELSE
        RETURN FALSE;
    END IF;
EXCEPTION
    WHEN NO_DATA_FOUND THEN
        RETURN FALSE;
    WHEN OTHERS THEN
        RETURN FALSE;
END HasSufficientBalance;
/
```

#### Detailed Line-by-Line Explanation
* **Line 11**: Rejects negative withdrawal amount requests.
* **Line 15**: Queries the account balance using `SELECT INTO`.
* **Lines 17–21**: Compares balance and returns `TRUE` if sufficient, else `FALSE`.
* **Lines 22–24**: Catches `NO_DATA_FOUND` and returns `FALSE`.

#### Sample Input / Output
* **Input**: Account 1001 (Balance $5,000), Amount = $3,000.
* **Output**: `TRUE` (in a PL/SQL block).

#### Execution Commands
Invoked inside a PL/SQL block:
```sql
DECLARE
    v_result BOOLEAN;
BEGIN
    v_result := HasSufficientBalance(1001, 3000);
    IF v_result THEN
        DBMS_OUTPUT.PUT_LINE('Sufficient balance');
    END IF;
END;
/
```

#### Viva & Interview Questions
1. **Why can't a function returning a `BOOLEAN` be used in a SQL statement?**
   Oracle SQL does not support the PL/SQL `BOOLEAN` datatype in standard table operations (prior to Oracle 12c/21c). SQL queries only support character, numeric, and date types.

---

## Conclusion
Functions are useful for encapsulating calculations that do not modify database states. Using parameters, checking edge cases, and implementing exception handling makes PL/SQL applications robust.
