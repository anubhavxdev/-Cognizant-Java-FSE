# Cognizant Java FSE: PL/SQL Exercise 3 - Stored Procedures

## Objective
This exercise covers the design, implementation, and execution of Oracle PL/SQL stored procedures. It highlights key features such as parameter defaults, explicit cursors with `FOR LOOP` structures, departmental data aggregation, transactional integrity (`COMMIT`/`ROLLBACK`), and exception propagation.

---

## Folder Structure
```text
PL_SQL/
└── Exercise_3_Stored_Procedures/
      ├── schema.sql
      ├── sample_data.sql
      ├── ProcessMonthlyInterest.sql
      ├── UpdateEmployeeBonus.sql
      ├── TransferFunds.sql
      └── README.md
```

---

## Concepts Used
* **Stored Procedures**: Named PL/SQL blocks stored in the database catalog that can accept input (`IN`) and output (`OUT`/`IN OUT`) parameters.
* **Cursor FOR Loop**: Simplifies processing by automatically opening, fetching from, and closing the explicit cursor.
* **Default Parameters**: Declared default values (e.g. `p_interest_rate IN NUMBER DEFAULT 1.0`) so procedures can be run with or without arguments.
* **ROWID Locking (`WHERE CURRENT OF`)**: Optimizes updates inside cursors by utilizing the physical `ROWID` locator.
* **Transaction Control**: Governed by `COMMIT` and `ROLLBACK` to preserve consistency in multi-statement steps.
* **Advantages of Stored Procedures**:
  1. **Performance**: Reduces network traffic since SQL statements are grouped and executed on the server.
  2. **Security**: Allows users to update data through the procedure interface without direct table permissions.
  3. **Maintainability**: Centralizes business rules in the database layer.

---

## Execution Steps
To execute these files on Oracle SQL Developer or SQL*Plus, run them in the following sequence:
1. `@schema.sql` (Creates clean tables with structural constraints).
2. `@sample_data.sql` (Seeds tables with consistent records).
3. `@ProcessMonthlyInterest.sql` (Compiles the interest procedure and runs test cases).
4. `@UpdateEmployeeBonus.sql` (Compiles the bonus update procedure and runs departmental tests).
5. `@TransferFunds.sql` (Compiles the transfer procedure and runs funds transfer tests).

---

## File-by-File Documentation

### 1. schema.sql

#### Problem Statement
Design a core database schema containing `Customers`, `Accounts`, `Transactions`, `Loans`, and `Employees` tables to support standard banking procedures.

#### Objective
Establish a structured schema with Primary Keys, Foreign Keys, and CHECK constraints to ensure referential and domain integrity.

#### Oracle PL/SQL Code
See code inside the file [schema.sql](file:///c:/Users/anubh/OneDrive/Desktop/kranti/cognizant/myassests/-Cognizant-Java-FSE/PL_SQL/Exercise_3_Stored_Procedures/schema.sql).

#### Line-by-line Explanation
* **Lines 8–24**: Anonymous PL/SQL drops tables sequentially based on dependency to prevent unique constraint conflicts.
* **Lines 30–40**: Creates the `Customers` table. Includes `IsVIP` defaulting to `'FALSE'` with a check constraint limiting values to `'TRUE'` or `'FALSE'`.
* **Lines 43–52**: Creates `Accounts` referencing `CustomerID` with `ON DELETE CASCADE`.
* **Lines 55–64**: Creates the `Transactions` table tracking withdrawals and deposits.
* **Lines 67–79**: Creates the `Loans` table, validating interest rates and dates.
* **Lines 82–91**: Creates the `Employees` table with salary checks.

---

### 2. sample_data.sql

#### Problem Statement
Insert consistent, clean mock values to validate all procedural test suites.

#### Objective
Seed specific accounts with pre-determined balances (e.g., $5,000 in savings, $1,000 in checking) and set up employees across different departments (IT and HR).

#### Oracle PL/SQL Code
See code inside the file [sample_data.sql](file:///c:/Users/anubh/OneDrive/Desktop/kranti/cognizant/myassests/-Cognizant-Java-FSE/PL_SQL/Exercise_3_Stored_Procedures/sample_data.sql).

#### Line-by-line Explanation
* **Lines 8–12**: Runs cleanup `DELETE` statements on tables in reverse order of foreign keys to clear previous records.
* **Lines 15–23**: Inserts test Customers.
* **Lines 26–32**: Inserts Accounts linked to customers. Account 1001 contains $5000 (Savings); Account 1002 contains $1000 (Checking); Account 1003 contains $25000 (Savings).
* **Lines 35–37**: Inserts test Loans.
* **Lines 40–46**: Inserts Employees across 'IT' and 'HR' departments for bonus testing.
* **Line 49**: Commits the transaction to save changes.

---

### 3. ProcessMonthlyInterest.sql

#### Problem Statement
The bank needs to process monthly interest for all savings accounts. Create a stored procedure `ProcessMonthlyInterest` that updates balances with monthly interest based on an interest percentage parameter.

#### Objective
Implement an explicit cursor with a Cursor `FOR LOOP` to calculate and apply interest, and print transition states before committing.

#### Oracle PL/SQL Code
```sql
CREATE OR REPLACE PROCEDURE ProcessMonthlyInterest (
    p_interest_rate IN NUMBER DEFAULT 1.0
) IS
    CURSOR c_savings_accounts IS
        SELECT AccountID, CustomerID, Balance
        FROM Accounts
        WHERE AccountType = 'Savings'
        FOR UPDATE;
        
    v_interest_amount NUMBER(15, 2);
    v_new_balance NUMBER(15, 2);
    v_processed_count NUMBER := 0;

BEGIN
    DBMS_OUTPUT.PUT_LINE('============================================================');
    DBMS_OUTPUT.PUT_LINE('STARTING MONTHLY INTEREST PROCESSING');
    DBMS_OUTPUT.PUT_LINE('Annual Interest Rate Parameter: ' || TO_CHAR(p_interest_rate, '99.99') || '%');
    DBMS_OUTPUT.PUT_LINE('============================================================');

    FOR r_acc IN c_savings_accounts LOOP
        v_interest_amount := ROUND(r_acc.Balance * (p_interest_rate / 12) / 100, 2);
        v_new_balance     := r_acc.Balance + v_interest_amount;
        
        UPDATE Accounts
        SET Balance = v_new_balance, LastUpdate = SYSDATE
        WHERE CURRENT OF c_savings_accounts;
        
        v_processed_count := v_processed_count + 1;
        
        DBMS_OUTPUT.PUT_LINE('Account ID     : ' || r_acc.AccountID);
        DBMS_OUTPUT.PUT_LINE('Customer ID    : ' || r_acc.CustomerID);
        DBMS_OUTPUT.PUT_LINE('Old Balance    : ' || TO_CHAR(r_acc.Balance, '$99,999.00'));
        DBMS_OUTPUT.PUT_LINE('Interest Added : ' || TO_CHAR(v_interest_amount, '$99,999.00'));
        DBMS_OUTPUT.PUT_LINE('New Balance    : ' || TO_CHAR(v_new_balance, '$99,999.00'));
        DBMS_OUTPUT.PUT_LINE('------------------------------------------------------------');
    END LOOP;

    COMMIT;
    
    DBMS_OUTPUT.PUT_LINE('Total Savings Accounts Processed: ' || v_processed_count);
    DBMS_OUTPUT.PUT_LINE('Monthly Interest Processing Completed');
    DBMS_OUTPUT.PUT_LINE('============================================================');
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        DBMS_OUTPUT.PUT_LINE('ERROR: Failed to complete monthly interest processing. ' || SQLERRM);
END ProcessMonthlyInterest;
/
```

#### Line-by-line Explanation
* **Line 11**: Procedure accepts `p_interest_rate` defaulting to `1.0` if omitted.
* **Lines 13–17**: Cursor `c_savings_accounts` selects accounts where `AccountType = 'Savings'` and locks them using `FOR UPDATE`.
* **Line 28**: Opens, loops, and fetches records automatically using a `FOR LOOP`.
* **Line 31**: Calculates monthly interest: `Balance * (AnnualRate / 12 months) / 100`.
* **Lines 35–37**: Updates the locked row directly using `WHERE CURRENT OF c_savings_accounts`.
* **Line 51**: Commits the transaction to save interest updates permanently.

#### Sample Input / Output / Oracle Commands
```sql
SET SERVEROUTPUT ON;
-- Running with default rate (1%)
EXEC ProcessMonthlyInterest;

-- Running with custom rate (6%)
EXEC ProcessMonthlyInterest(6.0);
```

#### Sample Output (Test Case 2)
```text
============================================================
STARTING MONTHLY INTEREST PROCESSING
Annual Interest Rate Parameter:   6.00%
============================================================
Account ID     : 1001
Customer ID    : 1
Old Balance    :      $5,000.00
Interest Added :        $25.00
New Balance    :      $5,025.00
------------------------------------------------------------
Account ID     : 1003
Customer ID    : 3
Old Balance    :     $25,000.00
Interest Added :       $125.00
New Balance    :     $25,125.00
------------------------------------------------------------
Total Savings Accounts Processed: 2
Monthly Interest Processing Completed
============================================================
```

#### Common Errors
* Applying interest to Checking accounts (prevented by our `WHERE AccountType = 'Savings'` filter).

#### Viva & Interview Questions
1. **What is a Cursor FOR Loop, and what are its advantages?**
   A Cursor FOR Loop is a loop that implicitly declares its loop index as a record type matching the cursor's schema. It automatically opens, fetches, and closes the cursor, reducing boilerplate code and preventing resource leaks.
2. **How do we make parameters optional in PL/SQL procedures?**
   By assigning a default value using the `DEFAULT` keyword or the assignment operator `:=` in the parameter declaration list.

---

### 4. UpdateEmployeeBonus.sql

#### Problem Statement
Update the salary of employees belonging to a specific department by adding a bonus percentage.

#### Objective
Use a cursor and loop structure to calculate the bonus amount and update departmental employee salaries.

#### Oracle PL/SQL Code
```sql
CREATE OR REPLACE PROCEDURE UpdateEmployeeBonus (
    p_dept_name IN VARCHAR2,
    p_bonus_pct IN NUMBER
) IS
    CURSOR c_dept_employees IS
        SELECT EmployeeID, Name, Department, Salary
        FROM Employees
        WHERE Department = p_dept_name
        FOR UPDATE OF Salary;
        
    v_bonus_amount NUMBER(15, 2);
    v_new_salary NUMBER(15, 2);
    v_processed_count NUMBER := 0;
    e_invalid_bonus EXCEPTION;
BEGIN
    IF p_bonus_pct < 0 THEN
        RAISE e_invalid_bonus;
    END IF;

    FOR r_emp IN c_dept_employees LOOP
        v_bonus_amount := ROUND(r_emp.Salary * (p_bonus_pct / 100), 2);
        v_new_salary   := r_emp.Salary + v_bonus_amount;
        
        UPDATE Employees SET Salary = v_new_salary WHERE CURRENT OF c_dept_employees;
        v_processed_count := v_processed_count + 1;
        
        DBMS_OUTPUT.PUT_LINE('Employee ID   : ' || r_emp.EmployeeID);
        DBMS_OUTPUT.PUT_LINE('Employee Name : ' || r_emp.Name);
        DBMS_OUTPUT.PUT_LINE('Old Salary    : ' || TO_CHAR(r_emp.Salary, '$999,999.00'));
        DBMS_OUTPUT.PUT_LINE('Bonus Amount  : ' || TO_CHAR(v_bonus_amount, '$999,999.00'));
        DBMS_OUTPUT.PUT_LINE('New Salary    : ' || TO_CHAR(v_new_salary, '$999,999.00'));
        DBMS_OUTPUT.PUT_LINE('------------------------------------------------------------');
    END LOOP;

    COMMIT;
    DBMS_OUTPUT.PUT_LINE('Total Employees Updated: ' || v_processed_count);
    IF v_processed_count > 0 THEN
        DBMS_OUTPUT.PUT_LINE('Bonus Updated Successfully');
    ELSE
        DBMS_OUTPUT.PUT_LINE('No employees found in department: ' || p_dept_name);
    END IF;
EXCEPTION
    WHEN e_invalid_bonus THEN
        ROLLBACK;
        DBMS_OUTPUT.PUT_LINE('ERROR: Bonus percentage cannot be negative.');
    WHEN OTHERS THEN
        ROLLBACK;
        DBMS_OUTPUT.PUT_LINE('ERROR: Failed to update bonuses. ' || SQLERRM);
END UpdateEmployeeBonus;
/
```

#### Line-by-line Explanation
* **Lines 10–14**: Cursor accepts the department parameter dynamically in its filter.
* **Line 21**: Prevents negative bonus logic via a custom validation exception.
* **Line 25**: Loops through matching departmental records.
* **Line 28**: Computes bonus amount: `Salary * (Percentage / 100)`.
* **Line 31**: Updates the salary using `WHERE CURRENT OF c_dept_employees`.
* **Line 44**: Commits the updates.

#### Sample Input / Output / Oracle Commands
```sql
SET SERVEROUTPUT ON;
EXEC UpdateEmployeeBonus('IT', 10);
EXEC UpdateEmployeeBonus('HR', 5);
```

#### Sample Output (IT Department)
```text
TEST CASE 1: Applying 10% bonus to the "IT" Department
============================================================
STARTING EMPLOYEE BONUS PROCESS
Target Department : IT
Bonus Percentage  :  10.00%
============================================================
Employee ID   : 301
Employee Name : Alice Johnson
Department    : IT
Old Salary    :      $75,000.00
Bonus Amount  :       $7,500.00
New Salary    :      $82,500.00
------------------------------------------------------------
Employee ID   : 302
Employee Name : Bob Brown
Department    : IT
Old Salary    :      $50,000.00
Bonus Amount  :       $5,000.00
New Salary    :      $55,000.00
------------------------------------------------------------
Total Employees Updated: 2
Bonus Updated Successfully
============================================================
```

#### Viva & Interview Questions
1. **How do cursor parameters work?**
   You can declare parameters in a cursor definition (e.g. `CURSOR c(p VARCHAR2) IS SELECT ... WHERE col = p`). This allows you to open the cursor with different values and reuse the cursor definition.
2. **What does the `FOR UPDATE OF column_name` do?**
   It locks only the specific columns indicated, allowing updates to those columns while leaving others unlocked (where supported by the database engine).

---

### 5. TransferFunds.sql

#### Problem Statement
Transfer money between two accounts.

#### Objective
Validate accounts using `SELECT INTO`, check for sufficient balance, deduct from source, add to destination, write transaction logs, and commit or rollback as appropriate.

#### Oracle PL/SQL Code
See code inside the file [TransferFunds.sql](file:///c:/Users/anubh/OneDrive/Desktop/kranti/cognizant/myassests/-Cognizant-Java-FSE/PL_SQL/Exercise_3_Stored_Procedures/TransferFunds.sql).

#### Line-by-line Explanation
* **Lines 31–38**: Uses a nested block to check if the source account exists. If not found, catches `NO_DATA_FOUND` and raises custom `e_source_not_found`.
* **Lines 41–48**: Performs the same existence check and lock on the destination account.
* **Line 54**: Checks if the source balance is sufficient for the transfer.
* **Lines 59–64**: Executes the updates.
* **Lines 67–79**: Records deposit and withdrawal transactions for auditing.
* **Line 82**: Commits the entire transfer transaction.

#### Sample Input / Output / Oracle Commands
```sql
SET SERVEROUTPUT ON;
-- Valid Transfer
EXEC TransferFunds(1001, 1002, 1000.00);

-- Insufficient Balance
EXEC TransferFunds(1001, 1002, 200000.00);
```

#### Expected Result
Valid transfers update balances and insert logs. Invalid transfers roll back completely and report errors.

---

## Conclusion
Stored procedures form the core of secure and high-performance database transaction designs. By encapsulating logic, utilizing Cursor FOR Loops, applying transactional boundaries, and enforcing validations, banking applications ensure data integrity.
