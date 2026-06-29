# Cognizant Java FSE: PL/SQL Exercise 1 - Control Structures

## Objective
This exercise demonstrates the use of Oracle PL/SQL procedural features (explicit cursors, loops, conditional IF-THEN-ELSE statements, date arithmetic, built-in functions, and transactions) to manage and manipulate relational tables representing a banking system.

---

## Folder Structure
```text
PL_SQL/
└── Exercise_1_Control_Structures/
      ├── schema.sql
      ├── sample_data.sql
      ├── scenario1_discount_interest.sql
      ├── scenario2_vip_customers.sql
      ├── scenario3_loan_reminder.sql
      └── README.md
```

---

## Concepts Used
* **Explicit Cursor**: Handled row-by-row fetching of query results.
* **Control Structures**: Utilized basic loops (`LOOP ... END LOOP`) and conditional branches (`IF-THEN-ELSE`).
* **Date Functions**: Used `MONTHS_BETWEEN()` to determine age in years, and date arithmetic (e.g., `EndDate - SYSDATE`) to determine remaining days.
* **Data Manipulation Language (DML)**: Executed `UPDATE` commands conditionally inside cursors.
* **Transaction Control Language (TCL)**: Managed changes using `COMMIT` and `ROLLBACK` for transaction safety.
* **Dynamic DDL**: Used `EXECUTE IMMEDIATE` inside PL/SQL to alter tables dynamically for re-runnability.

---

## Execution Steps (Overview)
Connect to your Oracle database using Oracle SQL Developer or SQL*Plus and run the files in the following order:
1. `@schema.sql` (Drops old tables, creates empty schema tables with primary/foreign keys).
2. `@sample_data.sql` (Inserts realistic records for customers, accounts, loans, transactions, and employees).
3. `@scenario1_discount_interest.sql` (Reduces interest rate by 1% for senior citizens).
4. `@scenario2_vip_customers.sql` (Alters `Customers` table to add `IsVIP` and populates status).
5. `@scenario3_loan_reminder.sql` (Prints reminders for loans maturing in the next 30 days).

---

## File-by-File Documentation

### 1. schema.sql

#### Purpose
To set up the banking database tables (`Customers`, `Accounts`, `Transactions`, `Loans`, `Employees`) with clean formatting and strong referential integrity (Primary Keys, Foreign Keys, and CHECK constraints). It includes a re-runnable drop utility at the top.

#### Complete Code
```sql
-- =========================================================
-- ORACLE PL/SQL EXERCISE 1: CONTROL STRUCTURES
-- FILE: schema.sql
-- PURPOSE: Set up the relational database tables with proper
--          Primary Keys, Foreign Keys, and Constraints.
-- =========================================================

-- ---------------------------------------------------------
-- 1. Drop existing tables to ensure clean execution (Re-runnable)
-- ---------------------------------------------------------
DECLARE
   PROCEDURE drop_table_if_exists(p_table_name VARCHAR2) IS
   BEGIN
      EXECUTE IMMEDIATE 'DROP TABLE ' || p_table_name || ' CASCADE CONSTRAINTS';
      DBMS_OUTPUT.PUT_LINE('Table ' || p_table_name || ' dropped successfully.');
   EXCEPTION
      WHEN OTHERS THEN
         NULL; -- Catch and ignore if table doesn't exist
   END;
BEGIN
   drop_table_if_exists('Transactions');
   drop_table_if_exists('Accounts');
   drop_table_if_exists('Loans');
   drop_table_if_exists('Customers');
   drop_table_if_exists('Employees');
END;
/

-- ---------------------------------------------------------
-- 2. Create Tables
-- ---------------------------------------------------------

-- A. Customers Table
CREATE TABLE Customers (
    CustomerID NUMBER,
    Name VARCHAR2(100) NOT NULL,
    DOB DATE NOT NULL,
    Balance NUMBER(15, 2) DEFAULT 0,
    LastUpdate DATE DEFAULT SYSDATE,
    CONSTRAINT PK_Customers PRIMARY KEY (CustomerID),
    CONSTRAINT CHK_Customer_Balance CHECK (Balance >= 0)
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

#### Line-by-Line Explanation
* **Lines 9–25**: A PL/SQL anonymous block defining an inner helper procedure `drop_table_if_exists`. It uses dynamic SQL (`EXECUTE IMMEDIATE`) to drop tables by name and catches standard exceptions so that dropping a non-existent table on the first run does not crash the script.
* **Lines 31–40**: Creates the `Customers` table. Customer ID is the primary key. `DOB` is required. `Balance` defaults to 0 and has a `CHECK` constraint ensuring it cannot go negative.
* **Lines 43–52**: Creates the `Accounts` table. Includes foreign key reference linking it to `Customers`. Constrains `AccountType` to only allow `'Savings'` or `'Checking'`.
* **Lines 55–64**: Creates the `Transactions` table. Links it to `Accounts`. Transaction amount must be positive (`> 0`) and type must be `'Deposit'` or `'Withdrawal'`.
* **Lines 67–79**: Creates the `Loans` table. Links it to `Customers`. Checks that `EndDate >= StartDate` and interest rates are non-negative.
* **Lines 82–91**: Creates the `Employees` table. Tracks salary, department, hire date, and role.
* **Line 93**: Runs a `COMMIT` to save structure definitions in database dictionary.

#### Expected Output
```text
Table Transactions dropped successfully.
Table Accounts dropped successfully.
Table Loans dropped successfully.
Table Customers dropped successfully.
Table Employees dropped successfully.

PL/SQL procedure successfully completed.

Table Customers created.
Table Accounts created.
Table Transactions created.
Table Loans created.
Table Employees created.

Commit complete.
```

#### Interview Questions
1. **What is the difference between `DELETE` and `DROP` commands in Oracle?**
   `DROP` is a DDL command that removes the entire table structure, indexes, triggers, and data from the database (it is auto-committed). `DELETE` is a DML command that removes rows from a table and can be rolled back.
2. **Why do we use `CASCADE CONSTRAINTS` when dropping tables?**
   If a parent table (like `Customers`) has active foreign keys pointing to it from child tables (like `Loans`), dropping the parent table will fail. `CASCADE CONSTRAINTS` automatically drops all referential integrity constraints pointing to the parent table.

#### Common Mistakes
* Forgetting `CASCADE CONSTRAINTS` and running into ORA-02449: "unique/primary keys in table referenced by foreign keys".
* Leaving out constraint names: naming constraints (e.g., `CONSTRAINT PK_Customers`) makes debugging table schema violations much easier compared to system-generated names.

#### Oracle commands to execute
```sql
@schema.sql;
```

---

### 2. sample_data.sql

#### Purpose
To populate the database with consistent, realistic sample values allowing proper testing of scenarios (senior citizen ages, VIP boundaries, and loan deadlines).

#### Complete Code
```sql
-- =========================================================
-- ORACLE PL/SQL EXERCISE 1: CONTROL STRUCTURES
-- FILE: sample_data.sql
-- PURPOSE: Populates banking tables with realistic test data.
--          Uses TO_DATE to be independent of NLS settings.
-- =========================================================

DELETE FROM Transactions;
DELETE FROM Accounts;
DELETE FROM Loans;
DELETE FROM Customers;
DELETE FROM Employees;

-- 2. Insert into Customers Table
INSERT INTO Customers (CustomerID, Name, DOB, Balance, LastUpdate)
VALUES (1, 'John Doe', TO_DATE('1955-05-15', 'YYYY-MM-DD'), 12000.00, SYSDATE - 10);

INSERT INTO Customers (CustomerID, Name, DOB, Balance, LastUpdate)
VALUES (2, 'Jane Smith', TO_DATE('1980-07-20', 'YYYY-MM-DD'), 8500.00, SYSDATE - 5);

INSERT INTO Customers (CustomerID, Name, DOB, Balance, LastUpdate)
VALUES (3, 'Robert Miller', TO_DATE('1950-11-10', 'YYYY-MM-DD'), 25000.00, SYSDATE - 15);

INSERT INTO Customers (CustomerID, Name, DOB, Balance, LastUpdate)
VALUES (4, 'Emily Davis', TO_DATE('1995-03-12', 'YYYY-MM-DD'), 4000.00, SYSDATE - 2);

-- 3. Insert into Accounts Table
INSERT INTO Accounts (AccountID, CustomerID, AccountType, Balance, LastUpdate)
VALUES (1001, 1, 'Savings', 12000.00, SYSDATE - 10);

INSERT INTO Accounts (AccountID, CustomerID, AccountType, Balance, LastUpdate)
VALUES (1002, 2, 'Checking', 8500.00, SYSDATE - 5);

INSERT INTO Accounts (AccountID, CustomerID, AccountType, Balance, LastUpdate)
VALUES (1003, 3, 'Savings', 25000.00, SYSDATE - 15);

INSERT INTO Accounts (AccountID, CustomerID, AccountType, Balance, LastUpdate)
VALUES (1004, 4, 'Checking', 4000.00, SYSDATE - 2);

-- 4. Insert into Transactions Table
INSERT INTO Transactions (TransactionID, AccountID, TransactionDate, Amount, TransactionType)
VALUES (2001, 1001, TO_DATE('2026-06-15', 'YYYY-MM-DD'), 1000.00, 'Deposit');

INSERT INTO Transactions (TransactionID, AccountID, TransactionDate, Amount, TransactionType)
VALUES (2002, 1002, TO_DATE('2026-06-20', 'YYYY-MM-DD'), 500.00, 'Withdrawal');

INSERT INTO Transactions (TransactionID, AccountID, TransactionDate, Amount, TransactionType)
VALUES (2003, 1003, TO_DATE('2026-06-10', 'YYYY-MM-DD'), 5000.00, 'Deposit');

INSERT INTO Transactions (TransactionID, AccountID, TransactionDate, Amount, TransactionType)
VALUES (2004, 1004, TO_DATE('2026-06-25', 'YYYY-MM-DD'), 200.00, 'Deposit');

-- 5. Insert into Loans Table
INSERT INTO Loans (LoanID, CustomerID, LoanAmount, InterestRate, StartDate, EndDate)
VALUES (101, 1, 50000.00, 8.50, TO_DATE('2020-01-01', 'YYYY-MM-DD'), TO_DATE('2026-07-15', 'YYYY-MM-DD'));

INSERT INTO Loans (LoanID, CustomerID, LoanAmount, InterestRate, StartDate, EndDate)
VALUES (102, 2, 20000.00, 7.20, TO_DATE('2022-05-01', 'YYYY-MM-DD'), TO_DATE('2027-05-01', 'YYYY-MM-DD'));

INSERT INTO Loans (LoanID, CustomerID, LoanAmount, InterestRate, StartDate, EndDate)
VALUES (103, 3, 30000.00, 6.80, TO_DATE('2021-10-10', 'YYYY-MM-DD'), TO_DATE('2026-07-25', 'YYYY-MM-DD'));

-- 6. Insert into Employees Table
INSERT INTO Employees (EmployeeID, Name, Position, Salary, Department, HireDate)
VALUES (301, 'Alice Johnson', 'Manager', 75000.00, 'IT', TO_DATE('2015-06-15', 'YYYY-MM-DD'));

INSERT INTO Employees (EmployeeID, Name, Position, Salary, Department, HireDate)
VALUES (302, 'Bob Brown', 'Analyst', 50000.00, 'Finance', TO_DATE('2018-09-10', 'YYYY-MM-DD'));

INSERT INTO Employees (EmployeeID, Name, Position, Salary, Department, HireDate)
VALUES (303, 'Charlie Green', 'Developer', 60000.00, 'IT', TO_DATE('2021-02-01', 'YYYY-MM-DD'));

COMMIT;
```

#### Line-by-Line Explanation
* **Lines 8–12**: Runs cleanup `DELETE` statements on tables in reverse order of foreign keys to clear previous records.
* **Lines 15–26**: Inserts test Customers. John Doe (1955) and Robert Miller (1950) are senior citizens (> 60 years). Jane Smith and Emily Davis are younger than 60. John and Robert have balances above $10,000.
* **Lines 29–38**: Inserts Accounts with initial balances matching Customer records.
* **Lines 41–50**: Inserts basic transactions (Deposits and Withdrawals) for accounting history.
* **Lines 53–62**: Inserts loans. John Doe (Loan 101) and Robert Miller (Loan 103) have active loans due in July 2026 (maturing within the next 30 days).
* **Lines 65–71**: Inserts sample banking employees.
* **Line 73**: Commits the transaction to persist the test data.

#### Expected Output
```text
5 rows deleted.
4 rows deleted.
3 rows deleted.
4 rows deleted.
3 rows deleted.

1 row inserted. (repeated for all inserts)

Commit complete.
Sample data inserted and committed successfully.
```

#### Interview Questions
1. **Why is it critical to use `TO_DATE()` with a format mask when inserting date values?**
   Using implicit date conversion like `'15-MAY-55'` is unsafe because it depends on the session's `NLS_DATE_FORMAT`. If a user runs the script on a database configured with a different format, the script will crash or insert incorrect values. `TO_DATE` guarantees consistency.

#### Common Mistakes
* Inserting data in an incorrect order: attempting to insert into `Loans` or `Accounts` before `Customers` raises reference parent key constraints.
* Omitting the `COMMIT` statement: without a commit, changes are only visible in your current SQL Developer session.

#### Oracle commands to execute
```sql
@sample_data.sql;
```

---

### 3. scenario1_discount_interest.sql

#### Purpose
Iterates through all loan accounts using an explicit cursor. For senior customers (age > 60), the interest rate on their loan is updated and reduced by 1%. Old and new rates are displayed in a clean console layout.

#### Complete Code
```sql
-- =========================================================
-- ORACLE PL/SQL EXERCISE 1: CONTROL STRUCTURES
-- FILE: scenario1_discount_interest.sql
-- PURPOSE: Apply a 1% interest rate discount to loans of senior 
--          citizens (customers aged above 60 years).
-- =========================================================

SET SERVEROUTPUT ON;

DECLARE
    -- 1. Explicit Cursor to fetch customers and their associated loans
    CURSOR c_customer_loans IS
        SELECT c.CustomerID,
               c.Name AS CustomerName,
               c.DOB,
               l.LoanID,
               l.InterestRate AS OldInterestRate
        FROM Customers c
        JOIN Loans l ON c.CustomerID = l.CustomerID;
        
    -- Record variable to hold cursor data
    v_loan_rec c_customer_loans%ROWTYPE;
    
    -- Variables for calculations
    v_age NUMBER;
    v_new_interest_rate NUMBER(5,2);
    v_updated_count NUMBER := 0;

BEGIN
    DBMS_OUTPUT.PUT_LINE('------------------------------------------------------------');
    DBMS_OUTPUT.PUT_LINE('STARTING INTEREST RATE UPDATE FOR SENIOR CITIZENS (> 60 YEARS)');
    DBMS_OUTPUT.PUT_LINE('------------------------------------------------------------');

    -- 2. Open the explicit cursor
    OPEN c_customer_loans;
    
    -- 3. Loop through each record fetched by the cursor
    LOOP
        -- Fetch current row into record variable
        FETCH c_customer_loans INTO v_loan_rec;
        
        -- Exit loop when no more records are found
        EXIT WHEN c_customer_loans%NOTFOUND;
        
        -- 4. Calculate customer age in years using MONTHS_BETWEEN
        v_age := FLOOR(MONTHS_BETWEEN(SYSDATE, v_loan_rec.DOB) / 12);
        
        -- 5. Conditional check: Verify if the customer is above 60 years old
        IF v_age > 60 THEN
            -- Calculate new interest rate (reduce by 1%)
            v_new_interest_rate := v_loan_rec.OldInterestRate - 1;
            
            -- Ensure interest rate does not drop below 0%
            IF v_new_interest_rate < 0 THEN
                v_new_interest_rate := 0;
            END IF;
            
            -- 6. Update the Loans table with the new interest rate
            UPDATE Loans
            SET InterestRate = v_new_interest_rate
            WHERE LoanID = v_loan_rec.LoanID;
            
            -- Increment update counter
            v_updated_count := v_updated_count + 1;
            
            -- 7. Print customer and interest rate details
            DBMS_OUTPUT.PUT_LINE('Customer Name    : ' || v_loan_rec.CustomerName);
            DBMS_OUTPUT.PUT_LINE('Current Age      : ' || v_age || ' years');
            DBMS_OUTPUT.PUT_LINE('Old Interest Rate: ' || TO_CHAR(v_loan_rec.OldInterestRate, '99.99') || '%');
            DBMS_OUTPUT.PUT_LINE('New Interest Rate: ' || TO_CHAR(v_new_interest_rate, '99.99') || '%');
            DBMS_OUTPUT.PUT_LINE('Status           : Interest Updated');
            DBMS_OUTPUT.PUT_LINE('------------------------------------------------------------');
        END IF;
    END LOOP;
    
    -- 8. Close the cursor to free system resources
    CLOSE c_customer_loans;
    
    -- Commit the changes to make them permanent
    COMMIT;
    
    -- Print summary
    DBMS_OUTPUT.PUT_LINE('Process completed. Total loans updated: ' || v_updated_count);
    DBMS_OUTPUT.PUT_LINE('------------------------------------------------------------');

EXCEPTION
    WHEN OTHERS THEN
        IF c_customer_loans%ISOPEN THEN
            CLOSE c_customer_loans;
        END IF;
        ROLLBACK;
        DBMS_OUTPUT.PUT_LINE('ERROR: An unexpected error occurred. transaction rolled back.');
        DBMS_OUTPUT.PUT_LINE('Error Message: ' || SQLERRM);
END;
/
```

#### Line-by-Line Explanation
* **Line 11**: Defines the cursor `c_customer_loans` that joins `Customers` and `Loans` to locate which loans belong to which customers.
* **Line 19**: Variable `v_loan_rec` uses `%ROWTYPE` to anchor its fields dynamically to the cursor schema.
* **Lines 31–32**: Opens the cursor and begins the `LOOP`.
* **Line 34**: Fetches the next record of data.
* **Line 37**: Exits the loop when `c_customer_loans%NOTFOUND` evaluates to TRUE.
* **Line 40**: Calculates age using `FLOOR(MONTHS_BETWEEN(SYSDATE, DOB) / 12)`.
* **Line 43**: Employs an `IF` statement to check if the calculated age is strictly greater than 60.
* **Lines 45–48**: Subtracts `1` from the interest rate and checks that it doesn't fall below zero.
* **Lines 51–53**: Executes the `UPDATE` query on the `Loans` table.
* **Lines 59–64**: Displays the customer name, current age, old rate, new rate, and the phrase `"Status : Interest Updated"`.
* **Line 69**: Closes the cursor.
* **Line 72**: Commits changes to the database.

#### Expected Output
```text
------------------------------------------------------------
STARTING INTEREST RATE UPDATE FOR SENIOR CITIZENS (> 60 YEARS)
------------------------------------------------------------
Customer Name    : John Doe
Current Age      : 71 years
Old Interest Rate:   8.50%
New Interest Rate:   7.50%
Status           : Interest Updated
------------------------------------------------------------
Customer Name    : Robert Miller
Current Age      : 75 years
Old Interest Rate:   6.80%
New Interest Rate:   5.80%
Status           : Interest Updated
------------------------------------------------------------
Process completed. Total loans updated: 2
------------------------------------------------------------

PL/SQL procedure successfully completed.
```

#### Interview Questions
1. **Why do we close the cursor inside the EXCEPTION block?**
   If a runtime error occurs during processing, the execution jumps straight to the exception handler. If we do not close the cursor inside the exception block, it will remain open, leading to memory leaks and resource exhaustion.
2. **What are the cursor attributes used here?**
   * `%NOTFOUND`: Evaluates to `TRUE` if the last `FETCH` failed to return a row.
   * `%ISOPEN`: Evaluates to `TRUE` if the cursor is open (used to safely close the cursor inside the exception handler).

#### Common Mistakes
* Placing the `FETCH` statement *after* the exit condition: this causes an infinite loop because `%NOTFOUND` is checked against an old or empty state.
* Forgetting to close explicit cursors.

#### Oracle commands to execute
```sql
SET SERVEROUTPUT ON;
@scenario1_discount_interest.sql;
```

---

### 4. scenario2_vip_customers.sql

#### Purpose
Alters the `Customers` table to include the `IsVIP` indicator (if it doesn't exist). A cursor then processes each customer, updates their status based on balance criteria (> $10,000 becomes `TRUE`, otherwise `FALSE`), and prints the details.

#### Complete Code
```sql
-- =========================================================
-- ORACLE PL/SQL EXERCISE 1: CONTROL STRUCTURES
-- FILE: scenario2_vip_customers.sql
-- PURPOSE: Classify bank customers as VIP (IsVIP = 'TRUE') if
--          their balance exceeds $10,000, else 'FALSE'.
-- =========================================================

SET SERVEROUTPUT ON;

-- ---------------------------------------------------------
-- 1. Alter Customers Table (Dynamic check to ensure re-runnability)
-- ---------------------------------------------------------
DECLARE
    v_col_count NUMBER;
BEGIN
    SELECT COUNT(*)
    INTO v_col_count
    FROM USER_TAB_COLS
    WHERE TABLE_NAME = 'CUSTOMERS' AND COLUMN_NAME = 'ISVIP';
    
    IF v_col_count = 0 THEN
        EXECUTE IMMEDIATE 'ALTER TABLE Customers ADD (IsVIP VARCHAR2(5))';
        DBMS_OUTPUT.PUT_LINE('Table Altered: Column IsVIP added to Customers table.');
    ELSE
        DBMS_OUTPUT.PUT_LINE('Table Alter Check: Column IsVIP already exists in Customers.');
    END IF;
EXCEPTION
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('Error during ALTER TABLE: ' || SQLERRM);
END;
/

-- ---------------------------------------------------------
-- 2. PL/SQL Block to update IsVIP status and print details
-- ---------------------------------------------------------
DECLARE
    -- Explicit Cursor with FOR UPDATE clause for transactional safety and in-place updates
    CURSOR c_customers IS
        SELECT CustomerID, Name, Balance
        FROM Customers
        FOR UPDATE OF IsVIP;
        
    v_cust_rec c_customers%ROWTYPE;
    v_vip_status VARCHAR2(5);
    v_processed_count NUMBER := 0;

BEGIN
    DBMS_OUTPUT.PUT_LINE('------------------------------------------------------------');
    DBMS_OUTPUT.PUT_LINE('STARTING CUSTOMER VIP STATUS UPDATE');
    DBMS_OUTPUT.PUT_LINE('------------------------------------------------------------');
    
    OPEN c_customers;
    
    LOOP
        FETCH c_customers INTO v_cust_rec;
        EXIT WHEN c_customers%NOTFOUND;
        
        -- Conditional check to determine VIP status based on Balance
        IF v_cust_rec.Balance > 10000 THEN
            v_vip_status := 'TRUE';
        ELSE
            v_vip_status := 'FALSE';
        END IF;
        
        -- Update the row currently locked by the cursor
        UPDATE Customers
        SET IsVIP = v_vip_status
        WHERE CURRENT OF c_customers;
        
        v_processed_count := v_processed_count + 1;
        
        -- Print customer VIP details
        DBMS_OUTPUT.PUT_LINE('Customer ID: ' || v_cust_rec.CustomerID);
        DBMS_OUTPUT.PUT_LINE('Name       : ' || v_cust_rec.Name);
        DBMS_OUTPUT.PUT_LINE('Balance    : ' || TO_CHAR(v_cust_rec.Balance, '$99,999.00'));
        DBMS_OUTPUT.PUT_LINE('VIP Status : ' || v_vip_status);
        DBMS_OUTPUT.PUT_LINE('------------------------------------------------------------');
    END LOOP;
    
    CLOSE c_customers;
    
    COMMIT;
    
    DBMS_OUTPUT.PUT_LINE('VIP Classification completed. Total records processed: ' || v_processed_count);
    DBMS_OUTPUT.PUT_LINE('------------------------------------------------------------');

EXCEPTION
    WHEN OTHERS THEN
        IF c_customers%ISOPEN THEN
            CLOSE c_customers;
        END IF;
        ROLLBACK;
        DBMS_OUTPUT.PUT_LINE('ERROR: An unexpected error occurred. transaction rolled back.');
        DBMS_OUTPUT.PUT_LINE('Error Message: ' || SQLERRM);
END;
/
```

#### Line-by-Line Explanation
* **Lines 13–27**: Uses a dynamic check inside the first block: if the column `IsVIP` is not found in `USER_TAB_COLS` for the `CUSTOMERS` table, we run the DDL query dynamically.
* **Line 37**: Cursor `c_customers` is declared with `FOR UPDATE OF IsVIP`. This locks the fetched rows to prevent other sessions from modifying them during execution.
* **Line 57**: Evaluates if the customer's balance exceeds 10,000.
* **Line 64**: Performs `UPDATE Customers SET IsVIP = ... WHERE CURRENT OF c_customers`. The `WHERE CURRENT OF` clause updates the exact database row that was last fetched by the cursor, which is highly efficient.
* **Line 81**: Commits the transaction to save VIP flags.

#### Expected Output
```text
Table Altered: Column IsVIP added to Customers table.

PL/SQL procedure successfully completed.

------------------------------------------------------------
STARTING CUSTOMER VIP STATUS UPDATE
------------------------------------------------------------
Customer ID: 1
Name       : John Doe
Balance    :  $12,000.00
VIP Status : TRUE
------------------------------------------------------------
Customer ID: 2
Name       : Jane Smith
Balance    :   $8,500.00
VIP Status : FALSE
------------------------------------------------------------
Customer ID: 3
Name       : Robert Miller
Balance    :  $25,000.00
VIP Status : TRUE
------------------------------------------------------------
Customer ID: 4
Name       : Emily Davis
Balance    :   $4,000.00
VIP Status : FALSE
------------------------------------------------------------
VIP Classification completed. Total records processed: 4
------------------------------------------------------------

PL/SQL procedure successfully completed.
```

#### Interview Questions
1. **What does the `FOR UPDATE` clause do in an explicit cursor?**
   It tells the database to acquire exclusive row-level locks on the records returned by the cursor query. This guarantees that no other transaction can alter or delete these rows until our transaction commits or rolls back, avoiding concurrency issues.
2. **What are the performance benefits of `WHERE CURRENT OF`?**
   It allows you to bypass searching the table using primary keys again during an update. The database uses the internal `ROWID` pointer held by the active cursor to target and modify the record directly.

#### Common Mistakes
* Omitting the `FOR UPDATE` clause in the cursor declaration while trying to use `WHERE CURRENT OF` in the update query (results in ORA-01410: "invalid ROWID").
* Attempting to run DDL statements (`ALTER TABLE`) directly in a PL/SQL block without using `EXECUTE IMMEDIATE`.

#### Oracle commands to execute
```sql
SET SERVEROUTPUT ON;
@scenario2_vip_customers.sql;
```

---

### 5. scenario3_loan_reminder.sql

#### Purpose
Locates loans maturing within the next 30 days. Uses an explicit cursor with a join between `Customers` and `Loans` tables, calculates remaining days, and outputs structured, professional notifications.

#### Complete Code
```sql
-- =========================================================
-- ORACLE PL/SQL EXERCISE 1: CONTROL STRUCTURES
-- FILE: scenario3_loan_reminder.sql
-- PURPOSE: Find loans due within the next 30 days and print
--          a professional reminder message for each.
-- =========================================================

SET SERVEROUTPUT ON;

DECLARE
    -- 1. Explicit Cursor to fetch customers and loans ending in the next 30 days
    CURSOR c_due_loans IS
        SELECT c.Name AS CustomerName,
               l.LoanID,
               l.LoanAmount,
               l.EndDate
        FROM Customers c
        JOIN Loans l ON c.CustomerID = l.CustomerID
        WHERE l.EndDate BETWEEN SYSDATE AND (SYSDATE + 30);
        
    v_loan_rec c_due_loans%ROWTYPE;
    v_remaining_days NUMBER;
    v_count NUMBER := 0;

BEGIN
    DBMS_OUTPUT.PUT_LINE('============================================================');
    DBMS_OUTPUT.PUT_LINE('               LOAN REPAYMENT REMINDERS (DUE IN 30 DAYS)    ');
    DBMS_OUTPUT.PUT_LINE('============================================================');
    
    OPEN c_due_loans;
    
    LOOP
        FETCH c_due_loans INTO v_loan_rec;
        EXIT WHEN c_due_loans%NOTFOUND;
        
        v_count := v_count + 1;
        
        -- Calculate remaining days using date arithmetic (EndDate - SYSDATE)
        -- Using CEIL to round up to the nearest whole day
        v_remaining_days := CEIL(v_loan_rec.EndDate - SYSDATE);
        
        -- Print a professional reminder message
        DBMS_OUTPUT.PUT_LINE('Reminder #' || v_count);
        DBMS_OUTPUT.PUT_LINE('Customer Name : ' || v_loan_rec.CustomerName);
        DBMS_OUTPUT.PUT_LINE('Loan ID       : ' || v_loan_rec.LoanID);
        DBMS_OUTPUT.PUT_LINE('Loan Amount   : ' || TO_CHAR(v_loan_rec.LoanAmount, '$999,999.00'));
        DBMS_OUTPUT.PUT_LINE('Maturity Date : ' || TO_CHAR(v_loan_rec.EndDate, 'YYYY-MM-DD'));
        DBMS_OUTPUT.PUT_LINE('Days Remaining: ' || v_remaining_days || ' day(s)');
        DBMS_OUTPUT.PUT_LINE('Reminder Msg  : Dear ' || v_loan_rec.CustomerName || ', this is a friendly reminder ');
        DBMS_OUTPUT.PUT_LINE('                that your outstanding loan of ' || TO_CHAR(v_loan_rec.LoanAmount, '$999,999.00'));
        DBMS_OUTPUT.PUT_LINE('                is due for full repayment on ' || TO_CHAR(v_loan_rec.EndDate, 'YYYY-MM-DD') || '.');
        DBMS_OUTPUT.PUT_LINE('                Please ensure adequate funds are available.');
        DBMS_OUTPUT.PUT_LINE('------------------------------------------------------------');
    END LOOP;
    
    CLOSE c_due_loans;
    
    -- Print summary
    IF v_count = 0 THEN
        DBMS_OUTPUT.PUT_LINE('No loans are due within the next 30 days.');
    ELSE
        DBMS_OUTPUT.PUT_LINE('Total reminders generated: ' || v_count);
    END IF;
    DBMS_OUTPUT.PUT_LINE('============================================================');

EXCEPTION
    WHEN OTHERS THEN
        IF c_due_loans%ISOPEN THEN
            CLOSE c_due_loans;
        END IF;
        DBMS_OUTPUT.PUT_LINE('ERROR: An error occurred while generating loan reminders.');
        DBMS_OUTPUT.PUT_LINE('Error Message: ' || SQLERRM);
END;
/
```

#### Line-by-Line Explanation
* **Line 11**: Declares `c_due_loans` joining `Customers` and `Loans` where the loan `EndDate` is between the current system date (`SYSDATE`) and `SYSDATE + 30` (30 days from now).
* **Line 37**: Performs a `FETCH` statement within the basic loop.
* **Line 43**: Evaluates the date difference `EndDate - SYSDATE`. Since date math in Oracle returns a fractional day value (representing elapsed hours/minutes), `CEIL()` is used to round up to the nearest full day.
* **Lines 46–52**: Formats output nicely using `TO_CHAR` for monetary amounts and date formatting, printing a complete business notification template.
* **Line 55**: Safely closes the cursor.

#### Expected Output
```text
============================================================
               LOAN REPAYMENT REMINDERS (DUE IN 30 DAYS)    
============================================================
Reminder #1
Customer Name : John Doe
Loan ID       : 101
Loan Amount   :   $50,000.00
Maturity Date : 2026-07-15
Days Remaining: 15 day(s)
Reminder Msg  : Dear John Doe, this is a friendly reminder 
                that your outstanding loan of   $50,000.00
                is due for full repayment on 2026-07-15.
                Please ensure adequate funds are available.
------------------------------------------------------------
Reminder #2
Customer Name : Robert Miller
Loan ID       : 103
Loan Amount   :   $30,000.00
Maturity Date : 2026-07-25
Days Remaining: 25 day(s)
Reminder Msg  : Dear Robert Miller, this is a friendly reminder 
                that your outstanding loan of   $30,000.00
                is due for full repayment on 2026-07-25.
                Please ensure adequate funds are available.
------------------------------------------------------------
Total reminders generated: 2
============================================================

PL/SQL procedure successfully completed.
```

#### Interview Questions
1. **How does Date Arithmetic work in Oracle SQL?**
   Oracle represents dates as integers, where `1` represents one full day. When you add/subtract values from dates (e.g. `SYSDATE + 30`), you are adding/subtracting days. Subtracting two dates yields the difference in days as a float.
2. **What is the difference between `TRUNC()`, `ROUND()`, and `CEIL()` when applied to date offsets?**
   * `TRUNC` cuts off the time part, returning the start of the day (midnight).
   * `ROUND` rounds to the nearest whole unit (noon changes target day).
   * `CEIL` always rounds fractions up to the next integer, ensuring that even if a loan is due in 14.2 days, it prints as "15 days remaining" to avoid understating the deadline.

#### Common Mistakes
* Hardcoding dates (e.g., comparing to `'30-JUL-26'`) which makes the script obsolete when run in the future. Using `SYSDATE` ensures the logic remains active forever.
* Subtracting `SYSDATE` from `EndDate` the wrong way: `SYSDATE - EndDate` returns a negative number if the loan matures in the future.

#### Oracle commands to execute
```sql
SET SERVEROUTPUT ON;
@scenario3_loan_reminder.sql;
```
