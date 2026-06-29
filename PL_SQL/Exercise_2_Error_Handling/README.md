# Cognizant Java FSE: PL/SQL Exercise 2 - Error Handling

## Objective
This exercise covers the design and implementation of database transaction structures using Oracle PL/SQL stored procedures with robust, enterprise-grade exception handling. It focuses on validating inputs, handling predefined system exceptions (`NO_DATA_FOUND`, `DUP_VAL_ON_INDEX`), defining user exceptions, executing safe rollbacks on failures, committing transactions on success, and using `RAISE_APPLICATION_ERROR` to report errors.

---

## Folder Structure
```text
PL_SQL/
└── Exercise_2_Error_Handling/
      ├── schema.sql
      ├── sample_data.sql
      ├── SafeTransferFunds.sql
      ├── UpdateSalary.sql
      ├── AddNewCustomer.sql
      └── README.md
```

---

## Concepts Used
* **Stored Procedures**: Reusable, precompiled schema blocks containing business logic.
* **Predefined Oracle Exceptions**: Handled Oracle errors like `NO_DATA_FOUND` (ORA-01403) and `DUP_VAL_ON_INDEX` (ORA-00001).
* **User-Defined Exceptions**: Custom exceptions declared using the `EXCEPTION` keyword and raised using `RAISE`.
* **RAISE_APPLICATION_ERROR**: A built-in Oracle procedure that associates a user-defined error message with a custom error code in the range `-20000` to `-20999`.
* **Transaction Control**: Checked boundaries using `COMMIT` (persist changes) and `ROLLBACK` (revert changes) inside exception blocks.
* **Row Locking (`FOR UPDATE`)**: Acquired rows locks to prevent write hazards during multi-step funds transactions.

---

## Execution Steps
To execute these files on Oracle SQL Developer or SQL*Plus, run them in the following sequence:
1. `@schema.sql` (Creates clean tables with structural constraints).
2. `@sample_data.sql` (Seeds tables with consistent records).
3. `@SafeTransferFunds.sql` (Compiles the transfer procedure and runs positive/negative test cases).
4. `@UpdateSalary.sql` (Compiles the salary modification procedure and runs positive/negative tests).
5. `@AddNewCustomer.sql` (Compiles the insertion procedure and runs positive/negative tests).

---

## File-by-File Documentation

### 1. schema.sql

#### Problem Statement
Design a core database schema containing `Customers`, `Accounts`, `Transactions`, `Loans`, and `Employees` tables to support standard banking procedures.

#### Objective
Establish a structured schema with Primary Keys, Foreign Keys, and CHECK constraints to ensure referential and domain integrity.

#### Oracle PL/SQL Code
```sql
-- =========================================================
-- ORACLE PL/SQL EXERCISE 2: ERROR HANDLING
-- FILE: schema.sql
-- PURPOSE: Create banking relational database schema tables
--          with Primary Keys, Foreign Keys, and Constraints.
-- =========================================================

-- 1. Drop existing tables to ensure clean execution (Re-runnable)
DECLARE
   PROCEDURE drop_table_if_exists(p_table_name VARCHAR2) IS
   BEGIN
      EXECUTE IMMEDIATE 'DROP TABLE ' || p_table_name || ' CASCADE CONSTRAINTS';
      DBMS_OUTPUT.PUT_LINE('Table ' || p_table_name || ' dropped successfully.');
   EXCEPTION
      WHEN OTHERS THEN
         NULL; -- Ignore exception if table does not exist
   END;
BEGIN
   drop_table_if_exists('Transactions');
   drop_table_if_exists('Accounts');
   drop_table_if_exists('Loans');
   drop_table_if_exists('Customers');
   drop_table_if_exists('Employees');
END;
/

-- 2. Create Tables

-- A. Customers Table
CREATE TABLE Customers (
    CustomerID NUMBER,
    Name VARCHAR2(100) NOT NULL,
    DOB DATE NOT NULL,
    Balance NUMBER(15, 2) DEFAULT 0,
    IsVIP VARCHAR2(5) DEFAULT 'FALSE',
    LastUpdate DATE DEFAULT SYSDATE,
    CONSTRAINT PK_Customers PRIMARY KEY (CustomerID),
    CONSTRAINT CHK_Customer_Balance CHECK (Balance >= 0),
    CONSTRAINT CHK_Customer_VIP CHECK (IsVIP IN ('TRUE', 'FALSE'))
);

-- B. Accounts Table
CREATE TABLE Accounts (
    AccountID NUMBER,
    CustomerID NUMBER,
    AccountType VARCHAR2(20) NOT NULL,
    Balance NUMBER(15, 2) DEFAULT 0,
    LastUpdate DATE DEFAULT SYSDATE,
    CONSTRAINT PK_Accounts PRIMARY KEY (AccountID),
    CONSTRAINT FK_Accounts_Customers FOREIGN KEY (CustomerID) REFERENCES Customers(CustomerID) ON DELETE CASCADE,
    CONSTRAINT CHK_Account_Type CHECK (AccountType IN ('Savings', 'Checking')),
    CONSTRAINT CHK_Account_Balance CHECK (Balance >= 0)
);

-- C. Transactions Table
CREATE TABLE Transactions (
    TransactionID NUMBER,
    AccountID NUMBER,
    TransactionDate DATE DEFAULT SYSDATE,
    Amount NUMBER(15, 2) NOT NULL,
    TransactionType VARCHAR2(20) NOT NULL,
    CONSTRAINT PK_Transactions PRIMARY KEY (TransactionID),
    CONSTRAINT FK_Transactions_Accounts FOREIGN KEY (AccountID) REFERENCES Accounts(AccountID) ON DELETE CASCADE,
    CONSTRAINT CHK_Trans_Type CHECK (TransactionType IN ('Deposit', 'Withdrawal')),
    CONSTRAINT CHK_Trans_Amount CHECK (Amount > 0)
);

-- D. Loans Table
CREATE TABLE Loans (
    LoanID NUMBER,
    CustomerID NUMBER,
    LoanAmount NUMBER(15, 2) NOT NULL,
    InterestRate NUMBER(5, 2) NOT NULL,
    StartDate DATE NOT NULL,
    EndDate DATE NOT NULL,
    CONSTRAINT PK_Loans PRIMARY KEY (LoanID),
    CONSTRAINT FK_Loans_Customers FOREIGN KEY (CustomerID) REFERENCES Customers(CustomerID) ON DELETE CASCADE,
    CONSTRAINT CHK_Loan_Amount CHECK (LoanAmount > 0),
    CONSTRAINT CHK_Interest_Rate CHECK (InterestRate >= 0),
    CONSTRAINT CHK_Loan_Dates CHECK (EndDate >= StartDate)
);

-- E. Employees Table
CREATE TABLE Employees (
    EmployeeID NUMBER,
    Name VARCHAR2(100) NOT NULL,
    Position VARCHAR2(50) NOT NULL,
    Salary NUMBER(15, 2) NOT NULL,
    Department VARCHAR2(50) NOT NULL,
    HireDate DATE NOT NULL,
    CONSTRAINT PK_Employees PRIMARY KEY (EmployeeID),
    CONSTRAINT CHK_Employee_Salary CHECK (Salary >= 0)
);

COMMIT;
```

#### Line-by-line Explanation
* **Lines 8–24**: Anonymous PL/SQL drops tables sequentially based on dependency to prevent unique constraint conflicts.
* **Lines 30–40**: Creates the `Customers` table. Includes `IsVIP` defaulting to `'FALSE'` with a check constraint limiting values to `'TRUE'` or `'FALSE'`.
* **Lines 43–52**: Creates `Accounts` referencing `CustomerID` with `ON DELETE CASCADE`.
* **Lines 55–64**: Creates the `Transactions` table tracking withdrawals and deposits.
* **Lines 67–79**: Creates the `Loans` table, validating interest rates and dates.
* **Lines 82–91**: Creates the `Employees` table with salary checks.

#### Sample Input / Output / Oracle Commands
Run `@schema.sql` in Oracle SQL Developer. It creates 5 tables.

#### Expected Result
All tables are created successfully and committed.

#### Common Errors
* ORA-00942: Table or view does not exist (handled gracefully by our PL/SQL block).

#### Interview Questions with Answers
1. **Why is it important to define specific constraint names in table schemas?**
   If a constraint is violated, Oracle reports the constraint name. Named constraints immediately tell the developer what went wrong (e.g. `CHK_Customer_Balance`), whereas system-generated names (e.g. `SYS_C007321`) require querying catalog views to locate the source column.

---

### 2. sample_data.sql

#### Problem Statement
Insert consistent, clean mock values to validate all procedural error handling test suites.

#### Objective
Seed specific accounts with pre-determined balances (e.g. source vs destination balance) and set up employees and customers.

#### Oracle PL/SQL Code
See code inside the file [sample_data.sql](file:///c:/Users/anubh/OneDrive/Desktop/kranti/cognizant/myassests/-Cognizant-Java-FSE/PL_SQL/Exercise_2_Error_Handling/sample_data.sql).

#### Line-by-line Explanation
* **Lines 8–12**: Runs cleanup `DELETE` statements on tables in reverse order of foreign keys to clear previous records.
* **Lines 15–23**: Inserts test Customers.
* **Lines 26–32**: Inserts Accounts linked to customers. Account 1001 contains $5000; Account 1002 contains $1000.
* **Lines 35–37**: Inserts test Loans.
* **Lines 40–44**: Inserts Employees for salary updates.
* **Line 47**: Commits the transaction to save changes.

#### Expected Result
Returns message: "Sample data for Error Handling exercise inserted successfully."

---

### 3. SafeTransferFunds.sql

#### Problem Statement
Create a stored procedure `SafeTransferFunds` that transfers a specified amount from one account to another, checking for account existence and sufficient funds.

#### Objective
Implement transaction boundaries (COMMIT/ROLLBACK), custom business exceptions, row-level locking to prevent race conditions, and write audit trail records for each operation.

#### Oracle PL/SQL Code
```sql
CREATE OR REPLACE PROCEDURE SafeTransferFunds (
    p_source_acc_id IN NUMBER,
    p_dest_acc_id IN NUMBER,
    p_amount IN NUMBER
) IS
    v_source_balance NUMBER(15, 2);
    v_dest_balance NUMBER(15, 2);
    v_source_exists NUMBER;
    v_dest_exists NUMBER;
    
    e_invalid_amount EXCEPTION;
    e_source_not_found EXCEPTION;
    e_dest_not_found EXCEPTION;
    e_insufficient_funds EXCEPTION;

BEGIN
    DBMS_OUTPUT.PUT_LINE('Transfer Started');
    
    IF p_amount <= 0 THEN
        RAISE e_invalid_amount;
    END IF;
    
    SELECT COUNT(*) INTO v_source_exists FROM Accounts WHERE AccountID = p_source_acc_id;
    IF v_source_exists = 0 THEN
        RAISE e_source_not_found;
    END IF;
    
    SELECT COUNT(*) INTO v_dest_exists FROM Accounts WHERE AccountID = p_dest_acc_id;
    IF v_dest_exists = 0 THEN
        RAISE e_dest_not_found;
    END IF;
    
    SELECT Balance INTO v_source_balance FROM Accounts WHERE AccountID = p_source_acc_id FOR UPDATE;
    SELECT Balance INTO v_dest_balance FROM Accounts WHERE AccountID = p_dest_acc_id FOR UPDATE;
    
    DBMS_OUTPUT.PUT_LINE('Source Balance     : ' || TO_CHAR(v_source_balance, '$99,999.00'));
    DBMS_OUTPUT.PUT_LINE('Destination Balance: ' || TO_CHAR(v_dest_balance, '$99,999.00'));
    
    IF v_source_balance < p_amount THEN
        RAISE e_insufficient_funds;
    END IF;
    
    UPDATE Accounts SET Balance = Balance - p_amount, LastUpdate = SYSDATE WHERE AccountID = p_source_acc_id;
    UPDATE Accounts SET Balance = Balance + p_amount, LastUpdate = SYSDATE WHERE AccountID = p_dest_acc_id;
    
    INSERT INTO Transactions (TransactionID, AccountID, TransactionDate, Amount, TransactionType)
    VALUES ((SELECT NVL(MAX(TransactionID), 0) + 1 FROM Transactions), p_source_acc_id, SYSDATE, p_amount, 'Withdrawal');
    
    INSERT INTO Transactions (TransactionID, AccountID, TransactionDate, Amount, TransactionType)
    VALUES ((SELECT NVL(MAX(TransactionID), 0) + 1 FROM Transactions), p_dest_acc_id, SYSDATE, p_amount, 'Deposit');
    
    COMMIT;
    DBMS_OUTPUT.PUT_LINE('Transfer Successful');
    
EXCEPTION
    WHEN e_invalid_amount THEN
        ROLLBACK;
        RAISE_APPLICATION_ERROR(-20001, 'Transfer Failed: Transfer amount must be positive.');
    WHEN e_source_not_found THEN
        ROLLBACK;
        RAISE_APPLICATION_ERROR(-20002, 'Transfer Failed: Source account ' || p_source_acc_id || ' does not exist.');
    WHEN e_dest_not_found THEN
        ROLLBACK;
        RAISE_APPLICATION_ERROR(-20003, 'Transfer Failed: Destination account ' || p_dest_acc_id || ' does not exist.');
    WHEN e_insufficient_funds THEN
        ROLLBACK;
        RAISE_APPLICATION_ERROR(-20004, 'Transfer Failed: Insufficient funds.');
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE_APPLICATION_ERROR(-20999, 'Transfer Failed: System error. ' || SQLERRM);
END SafeTransferFunds;
/
```

#### Line-by-line Explanation
* **Lines 10–13**: Declares custom exceptions for business rule violations.
* **Lines 20–22**: Throws error if transfer amount is negative or zero.
* **Lines 25–34**: Uses `COUNT(*)` queries to check if the source and destination accounts exist.
* **Lines 37–39**: Queries and locks source/destination account rows using `FOR UPDATE`. This prevents other sessions from altering balances during transfer calculations.
* **Lines 44–45**: Raises `e_insufficient_funds` if the source account does not have enough money.
* **Lines 48–56**: Deducts from source, adds to destination, and inserts audit records in the `Transactions` table.
* **Lines 61–78**: The exception block catches failures, runs a `ROLLBACK` to protect data, and logs the failure using `RAISE_APPLICATION_ERROR`.

#### Sample Input / Output / Oracle Commands
To test valid and invalid transfers:
```sql
SET SERVEROUTPUT ON;
-- Valid transfer
EXEC SafeTransferFunds(1001, 1002, 500.00);

-- Insufficient funds transfer (fails)
EXEC SafeTransferFunds(1001, 1002, 999999.00);
```

#### Sample Output (Test Case 2)
```text
Transfer Started
Source Account ID: 1001 | Destination Account ID: 1002
Transfer Amount  :   $100,000.00
Source Balance     :      $4,500.00
Destination Balance:      $1,500.00
Test 2 Caught Exception: ORA-20004: Transfer Failed: Insufficient funds.
```

#### Common Errors
* ORA-00060: Deadlock detected (occurs if two sessions try to transfer funds between the same two accounts in reverse order simultaneously. Row locking order prevents this).

#### Interview Questions with Answers
1. **What is the significance of `FOR UPDATE` in banking transactions?**
   It prevents race conditions. Without `FOR UPDATE`, two concurrent transactions could read the same balance, verify that funds exist, and perform updates simultaneously, leading to overdrafts.

---

### 4. UpdateSalary.sql

#### Problem Statement
Write a stored procedure `UpdateSalary` that increases an employee's salary by a given percentage.

#### Objective
Validate employee existence using a `SELECT INTO` block, handle the predefined exception `NO_DATA_FOUND` if the employee is not found, and log both old and new salaries.

#### Oracle PL/SQL Code
```sql
CREATE OR REPLACE PROCEDURE UpdateSalary (
    p_emp_id IN NUMBER,
    p_percentage IN NUMBER
) IS
    v_old_salary NUMBER(15, 2);
    v_new_salary NUMBER(15, 2);
    v_emp_name VARCHAR2(100);
    v_position VARCHAR2(50);
    v_dept VARCHAR2(50);
    e_invalid_percentage EXCEPTION;
BEGIN
    IF p_percentage < 0 THEN
        RAISE e_invalid_percentage;
    END IF;
    
    SELECT Name, Position, Salary, Department
    INTO v_emp_name, v_position, v_old_salary, v_dept
    FROM Employees
    WHERE EmployeeID = p_emp_id;
    
    v_new_salary := v_old_salary * (1 + (p_percentage / 100));
    
    UPDATE Employees SET Salary = v_new_salary WHERE EmployeeID = p_emp_id;
    COMMIT;
    
    DBMS_OUTPUT.PUT_LINE('Employee Details:');
    DBMS_OUTPUT.PUT_LINE('Name          : ' || v_emp_name);
    DBMS_OUTPUT.PUT_LINE('Old Salary    : ' || TO_CHAR(v_old_salary, '$999,999.00'));
    DBMS_OUTPUT.PUT_LINE('New Salary    : ' || TO_CHAR(v_new_salary, '$999,999.00'));
EXCEPTION
    WHEN NO_DATA_FOUND THEN
        ROLLBACK;
        RAISE_APPLICATION_ERROR(-20005, 'Salary Update Failed: Employee with ID ' || p_emp_id || ' does not exist.');
    WHEN e_invalid_percentage THEN
        ROLLBACK;
        RAISE_APPLICATION_ERROR(-20006, 'Salary Update Failed: Percentage increase cannot be negative.');
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE_APPLICATION_ERROR(-20999, 'Salary Update Failed: ' || SQLERRM);
END UpdateSalary;
/
```

#### Line-by-line Explanation
* **Lines 17–20**: Queries employee details. If the `EmployeeID` is not found, Oracle automatically raises `NO_DATA_FOUND`.
* **Line 22**: Calculates the new salary.
* **Line 24**: Updates the record.
* **Line 25**: Commits the salary change.
* **Lines 31–33**: Handles `NO_DATA_FOUND` by rolling back and raising ORA-20005.

#### Sample Input / Output / Oracle Commands
```sql
SET SERVEROUTPUT ON;
EXEC UpdateSalary(301, 10);
EXEC UpdateSalary(999, 5); -- Non-existent ID
```

#### Sample Output (Failing Case)
```text
Test 2 Caught Exception: ORA-20005: Salary Update Failed: Employee with ID 999 does not exist.
```

#### Common Errors
* Using a `SELECT COUNT(*)` check instead of letting `SELECT INTO` raise `NO_DATA_FOUND`: while both work, handling `NO_DATA_FOUND` directly is cleaner and faster in PL/SQL as it bypasses a double lookup.

#### Interview Questions with Answers
1. **Under what conditions does `SELECT INTO` throw an exception?**
   It raises:
   - `NO_DATA_FOUND` if the query returns 0 rows.
   - `TOO_MANY_ROWS` if the query returns more than 1 row.

---

### 5. AddNewCustomer.sql

#### Problem Statement
Write a stored procedure `AddNewCustomer` to insert a new customer into the database.

#### Objective
Validate that the `CustomerID` is unique before inserting. Handle both custom duplicate errors and Oracle unique constraint violations (`DUP_VAL_ON_INDEX`).

#### Oracle PL/SQL Code
```sql
CREATE OR REPLACE PROCEDURE AddNewCustomer (
    p_cust_id IN NUMBER,
    p_name IN VARCHAR2,
    p_dob IN DATE,
    p_balance IN NUMBER
) IS
    v_exists NUMBER;
    e_duplicate_customer EXCEPTION;
BEGIN
    SELECT COUNT(*) INTO v_exists FROM Customers WHERE CustomerID = p_cust_id;
    IF v_exists > 0 THEN
        RAISE e_duplicate_customer;
    END IF;
    
    INSERT INTO Customers (CustomerID, Name, DOB, Balance, IsVIP, LastUpdate)
    VALUES (p_cust_id, p_name, p_dob, p_balance, 'FALSE', SYSDATE);
    
    COMMIT;
    DBMS_OUTPUT.PUT_LINE('Customer Added Successfully');
EXCEPTION
    WHEN e_duplicate_customer THEN
        ROLLBACK;
        DBMS_OUTPUT.PUT_LINE('Customer Already Exists');
        RAISE_APPLICATION_ERROR(-20007, 'Insertion Failed: Customer ID ' || p_cust_id || ' already exists.');
    WHEN DUP_VAL_ON_INDEX THEN
        ROLLBACK;
        DBMS_OUTPUT.PUT_LINE('Customer Already Exists (Database Index Constraint)');
        RAISE_APPLICATION_ERROR(-20008, 'Insertion Failed: Unique key constraint violated.');
    WHEN OTHERS THEN
        ROLLBACK;
        DBMS_OUTPUT.PUT_LINE('Error: ' || SQLERRM);
        RAISE_APPLICATION_ERROR(-20999, 'Insertion Failed: ' || SQLERRM);
END AddNewCustomer;
/
```

#### Line-by-line Explanation
* **Lines 10–13**: Queries customer count. If `COUNT(*) > 0`, it raises a custom `e_duplicate_customer` exception.
* **Lines 15–16**: Inserts the new record.
* **Line 18**: Commits the new record.
* **Lines 21–25**: Catch `e_duplicate_customer`, roll back, output warning, and raise ORA-20007.
* **Lines 26–29**: Catch `DUP_VAL_ON_INDEX` (backup check) to handle potential race conditions.

#### Sample Input / Output / Oracle Commands
```sql
SET SERVEROUTPUT ON;
EXEC AddNewCustomer(5, 'Sarah Connor', TO_DATE('1984-11-26', 'YYYY-MM-DD'), 3500.00);
EXEC AddNewCustomer(1, 'John Doe', TO_DATE('1985-05-15', 'YYYY-MM-DD'), 5000.00); -- Duplicate ID
```

#### Sample Output (Failing Case)
```text
TEST CASE 2: Invalid Customer Insertion (ID 1 Already Exists)
AddNewCustomer initiated for ID: 1
Customer Already Exists
Test 2 Caught Exception: ORA-20007: Insertion Failed: Customer ID 1 already exists in database.
```

---

## Conclusion
This suite demonstrates how professional PL/SQL development handles runtime failures. By combining explicit validations, row-level locking, predefined system exceptions, custom exceptions, and transactional block rollbacks, the database maintains maximum concurrency and total consistency under error states.
