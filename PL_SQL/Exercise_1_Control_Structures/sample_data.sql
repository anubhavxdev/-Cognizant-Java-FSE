-- =========================================================
-- ORACLE PL/SQL EXERCISE 1: CONTROL STRUCTURES
-- FILE: sample_data.sql
-- PURPOSE: Populates banking tables with realistic test data.
--          Uses TO_DATE to be independent of NLS settings.
-- =========================================================

-- ---------------------------------------------------------
-- 1. Clear Existing Data (Clean slate, ordered by dependencies)
-- ---------------------------------------------------------
DELETE FROM Transactions;
DELETE FROM Accounts;
DELETE FROM Loans;
DELETE FROM Customers;
DELETE FROM Employees;

-- ---------------------------------------------------------
-- 2. Insert into Customers Table
-- ---------------------------------------------------------
-- Note: Current year in context is 2026.
-- Customer 1: Born 1955 (Age 71), Balance > 10,000 (VIP candidate)
INSERT INTO Customers (CustomerID, Name, DOB, Balance, LastUpdate)
VALUES (1, 'John Doe', TO_DATE('1955-05-15', 'YYYY-MM-DD'), 12000.00, SYSDATE - 10);

-- Customer 2: Born 1980 (Age 46), Balance < 10,000 (Non-VIP candidate)
INSERT INTO Customers (CustomerID, Name, DOB, Balance, LastUpdate)
VALUES (2, 'Jane Smith', TO_DATE('1980-07-20', 'YYYY-MM-DD'), 8500.00, SYSDATE - 5);

-- Customer 3: Born 1950 (Age 76), Balance > 10,000 (VIP candidate)
INSERT INTO Customers (CustomerID, Name, DOB, Balance, LastUpdate)
VALUES (3, 'Robert Miller', TO_DATE('1950-11-10', 'YYYY-MM-DD'), 25000.00, SYSDATE - 15);

-- Customer 4: Born 1995 (Age 31), Balance < 10,000 (Non-VIP candidate)
INSERT INTO Customers (CustomerID, Name, DOB, Balance, LastUpdate)
VALUES (4, 'Emily Davis', TO_DATE('1995-03-12', 'YYYY-MM-DD'), 4000.00, SYSDATE - 2);

-- ---------------------------------------------------------
-- 3. Insert into Accounts Table
-- ---------------------------------------------------------
INSERT INTO Accounts (AccountID, CustomerID, AccountType, Balance, LastUpdate)
VALUES (1001, 1, 'Savings', 12000.00, SYSDATE - 10);

INSERT INTO Accounts (AccountID, CustomerID, AccountType, Balance, LastUpdate)
VALUES (1002, 2, 'Checking', 8500.00, SYSDATE - 5);

INSERT INTO Accounts (AccountID, CustomerID, AccountType, Balance, LastUpdate)
VALUES (1003, 3, 'Savings', 25000.00, SYSDATE - 15);

INSERT INTO Accounts (AccountID, CustomerID, AccountType, Balance, LastUpdate)
VALUES (1004, 4, 'Checking', 4000.00, SYSDATE - 2);

-- ---------------------------------------------------------
-- 4. Insert into Transactions Table
-- ---------------------------------------------------------
INSERT INTO Transactions (TransactionID, AccountID, TransactionDate, Amount, TransactionType)
VALUES (2001, 1001, TO_DATE('2026-06-15', 'YYYY-MM-DD'), 1000.00, 'Deposit');

INSERT INTO Transactions (TransactionID, AccountID, TransactionDate, Amount, TransactionType)
VALUES (2002, 1002, TO_DATE('2026-06-20', 'YYYY-MM-DD'), 500.00, 'Withdrawal');

INSERT INTO Transactions (TransactionID, AccountID, TransactionDate, Amount, TransactionType)
VALUES (2003, 1003, TO_DATE('2026-06-10', 'YYYY-MM-DD'), 5000.00, 'Deposit');

INSERT INTO Transactions (TransactionID, AccountID, TransactionDate, Amount, TransactionType)
VALUES (2004, 1004, TO_DATE('2026-06-25', 'YYYY-MM-DD'), 200.00, 'Deposit');

-- ---------------------------------------------------------
-- 5. Insert into Loans Table
-- ---------------------------------------------------------
-- Note: Current date is June 30, 2026.
-- Loan 101: Belongs to John (Age 71 > 60), Interest 8.5%, due July 15, 2026 (within 30 days)
INSERT INTO Loans (LoanID, CustomerID, LoanAmount, InterestRate, StartDate, EndDate)
VALUES (101, 1, 50000.00, 8.50, TO_DATE('2020-01-01', 'YYYY-MM-DD'), TO_DATE('2026-07-15', 'YYYY-MM-DD'));

-- Loan 102: Belongs to Jane (Age 46 < 60), Interest 7.2%, due May 1, 2027 (long-term)
INSERT INTO Loans (LoanID, CustomerID, LoanAmount, InterestRate, StartDate, EndDate)
VALUES (102, 2, 20000.00, 7.20, TO_DATE('2022-05-01', 'YYYY-MM-DD'), TO_DATE('2027-05-01', 'YYYY-MM-DD'));

-- Loan 103: Belongs to Robert (Age 76 > 60), Interest 6.8%, due July 25, 2026 (within 30 days)
INSERT INTO Loans (LoanID, CustomerID, LoanAmount, InterestRate, StartDate, EndDate)
VALUES (103, 3, 30000.00, 6.80, TO_DATE('2021-10-10', 'YYYY-MM-DD'), TO_DATE('2026-07-25', 'YYYY-MM-DD'));

-- ---------------------------------------------------------
-- 6. Insert into Employees Table
-- ---------------------------------------------------------
INSERT INTO Employees (EmployeeID, Name, Position, Salary, Department, HireDate)
VALUES (301, 'Alice Johnson', 'Manager', 75000.00, 'IT', TO_DATE('2015-06-15', 'YYYY-MM-DD'));

INSERT INTO Employees (EmployeeID, Name, Position, Salary, Department, HireDate)
VALUES (302, 'Bob Brown', 'Analyst', 50000.00, 'Finance', TO_DATE('2018-09-10', 'YYYY-MM-DD'));

INSERT INTO Employees (EmployeeID, Name, Position, Salary, Department, HireDate)
VALUES (303, 'Charlie Green', 'Developer', 60000.00, 'IT', TO_DATE('2021-02-01', 'YYYY-MM-DD'));

-- ---------------------------------------------------------
-- 7. Commit Transaction
-- ---------------------------------------------------------
COMMIT;
DBMS_OUTPUT.PUT_LINE('Sample data inserted and committed successfully.');
/
