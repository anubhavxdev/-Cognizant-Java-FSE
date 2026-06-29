-- =========================================================
-- ORACLE PL/SQL EXERCISE 4: FUNCTIONS
-- FILE: sample_data.sql
-- PURPOSE: Populates banking tables with realistic test data.
--          Uses TO_DATE to be independent of NLS settings.
-- =========================================================

-- 1. Clear Existing Data (Clean slate, ordered by dependencies)
DELETE FROM Transactions;
DELETE FROM Accounts;
DELETE FROM Loans;
DELETE FROM Customers;
DELETE FROM Employees;

-- 2. Insert into Customers Table
INSERT INTO Customers (CustomerID, Name, DOB, Balance, IsVIP, LastUpdate)
VALUES (1, 'John Doe', TO_DATE('1955-05-15', 'YYYY-MM-DD'), 5000.00, 'FALSE', SYSDATE - 10);

INSERT INTO Customers (CustomerID, Name, DOB, Balance, IsVIP, LastUpdate)
VALUES (2, 'Jane Smith', TO_DATE('1980-07-20', 'YYYY-MM-DD'), 1000.00, 'FALSE', SYSDATE - 5);

INSERT INTO Customers (CustomerID, Name, DOB, Balance, IsVIP, LastUpdate)
VALUES (3, 'Robert Miller', TO_DATE('1950-11-10', 'YYYY-MM-DD'), 25000.00, 'TRUE', SYSDATE - 15);

-- 3. Insert into Accounts Table
INSERT INTO Accounts (AccountID, CustomerID, AccountType, Balance, LastUpdate)
VALUES (1001, 1, 'Savings', 5000.00, SYSDATE - 10);

INSERT INTO Accounts (AccountID, CustomerID, AccountType, Balance, LastUpdate)
VALUES (1002, 2, 'Checking', 1000.00, SYSDATE - 5);

INSERT INTO Accounts (AccountID, CustomerID, AccountType, Balance, LastUpdate)
VALUES (1003, 3, 'Savings', 25000.00, SYSDATE - 15);

-- 4. Insert into Loans Table
-- Loan 101: 50,000 at 8.5% for 6 years
INSERT INTO Loans (LoanID, CustomerID, LoanAmount, InterestRate, StartDate, EndDate)
VALUES (101, 1, 50000.00, 8.50, TO_DATE('2020-01-01', 'YYYY-MM-DD'), TO_DATE('2026-01-01', 'YYYY-MM-DD'));

-- Loan 102: 20,000 at 7.2% for 5 years
INSERT INTO Loans (LoanID, CustomerID, LoanAmount, InterestRate, StartDate, EndDate)
VALUES (102, 2, 20000.00, 7.20, TO_DATE('2022-05-01', 'YYYY-MM-DD'), TO_DATE('2027-05-01', 'YYYY-MM-DD'));

-- 5. Insert into Employees Table
INSERT INTO Employees (EmployeeID, Name, Position, Salary, Department, HireDate)
VALUES (301, 'Alice Johnson', 'Manager', 75000.00, 'IT', TO_DATE('2015-06-15', 'YYYY-MM-DD'));

INSERT INTO Employees (EmployeeID, Name, Position, Salary, Department, HireDate)
VALUES (302, 'Bob Brown', 'Analyst', 50000.00, 'IT', TO_DATE('2018-09-10', 'YYYY-MM-DD'));

-- Commit Transaction
COMMIT;
DBMS_OUTPUT.PUT_LINE('Sample data for Functions exercise inserted successfully.');
/
