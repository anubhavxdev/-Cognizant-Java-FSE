-- =========================================================
-- ORACLE PL/SQL EXERCISE 6: CURSORS
-- FILE: schema.sql
-- PURPOSE: Create core relational database schema tables
--          with Primary Keys, Foreign Keys, and Constraints.
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
         -- Catch and ignore if table doesn't exist
         NULL;
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

-- Commit structure updates
COMMIT;
