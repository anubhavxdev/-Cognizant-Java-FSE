-- =========================================================
-- ORACLE PL/SQL EXERCISE 5: TRIGGERS
-- FILE: sample_data.sql
-- PURPOSE: Populates banking tables with realistic test data.
--          Uses TO_DATE to be independent of NLS settings.
-- =========================================================

-- 1. Clear Existing Data (Clean slate, ordered by dependencies)
DELETE FROM AuditLog;
DELETE FROM Transactions;
DELETE FROM Accounts;
DELETE FROM Loans;
DELETE FROM Customers;
DELETE FROM Employees;

-- 2. Insert into Customers Table
INSERT INTO Customers (CustomerID, Name, DOB, Balance, IsVIP, LastUpdate)
VALUES (1, 'John Doe', TO_DATE('1985-05-15', 'YYYY-MM-DD'), 5000.00, 'FALSE', SYSDATE - 10);

INSERT INTO Customers (CustomerID, Name, DOB, Balance, IsVIP, LastUpdate)
VALUES (2, 'Jane Smith', TO_DATE('1990-07-20', 'YYYY-MM-DD'), 1000.00, 'FALSE', SYSDATE - 5);

-- 3. Insert into Accounts Table
INSERT INTO Accounts (AccountID, CustomerID, AccountType, Balance, LastUpdate)
VALUES (1001, 1, 'Savings', 5000.00, SYSDATE - 10);

INSERT INTO Accounts (AccountID, CustomerID, AccountType, Balance, LastUpdate)
VALUES (1002, 2, 'Checking', 1000.00, SYSDATE - 5);

-- Commit Transaction
COMMIT;
DBMS_OUTPUT.PUT_LINE('Sample data for Triggers exercise inserted successfully.');
/
