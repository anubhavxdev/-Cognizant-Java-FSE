# Exercise 7: Packages - Reference Guide

## Objective
This exercise covers the design, declaration, compilation, and execution of Oracle PL/SQL Packages. It showcases the division of Package Specifications (public interface declarations) and Package Bodies (private implementations and business code).

---

## Folder Structure
```text
PL_SQL/
└── Exercise_7_Packages/
      ├── schema.sql
      ├── sample_data.sql
      ├── CustomerManagement.sql
      ├── EmployeeManagement.sql
      ├── AccountOperations.sql
      └── README.md
```

---

## Concepts Used
* **Package Specification**: The public interface of a package declaring procedures, functions, variables, constants, cursors, and exceptions.
* **Package Body**: The implementation details of the package containing the actual code for procedures and functions.
* **Encapsulation**: Grouping related logical interfaces together under a single namespace (e.g. `CustomerManagement`).
* **Information Hiding**: Public declarations in the spec are visible to calling programs, while private subprograms declared only inside the body are invisible.

---

## Advantages of Packages
1. **Modularity**: Grouping logically related subprograms together simplifies development and makes the codebase easier to manage.
2. **Information Hiding**: By exposing only the specification, you protect the underlying logic and database details from external users.
3. **Performance**: When a package subprogram is first invoked, Oracle loads the entire package into database memory (SGA), speeding up subsequent executions.

---

## File-by-File Documentation

### 1. schema.sql
Declares core relational tables (`Customers`, `Accounts`, `Transactions`, `Loans`, `Employees`).
* **Problem & Objective**: Setup tables with constraints.
* **Complete Code**: See [schema.sql](file:///c:/Users/anubh/OneDrive/Desktop/kranti/cognizant/myassests/-Cognizant-Java-FSE/PL_SQL/Exercise_7_Packages/schema.sql).

---

### 2. sample_data.sql
Seeds bank customer, account, and employee records.
* **Complete Code**: See [sample_data.sql](file:///c:/Users/anubh/OneDrive/Desktop/kranti/cognizant/myassests/-Cognizant-Java-FSE/PL_SQL/Exercise_7_Packages/sample_data.sql).

---

### 3. CustomerManagement.sql

#### Problem Statement
Design a package `CustomerManagement` containing methods to add a customer, update a customer's details, and query a customer's balance.

#### Objective
Create the package specification, implement the logic in the body with transaction checks, and execute tests.

#### Complete Oracle PL/SQL Code
```sql
CREATE OR REPLACE PACKAGE CustomerManagement IS
    PROCEDURE AddCustomer(p_cust_id IN NUMBER, p_name IN VARCHAR2, p_dob IN DATE, p_balance IN NUMBER);
    PROCEDURE UpdateCustomer(p_cust_id IN NUMBER, p_name IN VARCHAR2, p_balance IN NUMBER);
    FUNCTION GetCustomerBalance(p_cust_id IN NUMBER) RETURN NUMBER;
END CustomerManagement;
/

CREATE OR REPLACE PACKAGE BODY CustomerManagement IS
    PROCEDURE AddCustomer(p_cust_id IN NUMBER, p_name IN VARCHAR2, p_dob IN DATE, p_balance IN NUMBER) IS
    BEGIN
        INSERT INTO Customers (CustomerID, Name, DOB, Balance, IsVIP, LastUpdate)
        VALUES (p_cust_id, p_name, p_dob, p_balance, 'FALSE', SYSDATE);
        COMMIT;
    EXCEPTION
        WHEN DUP_VAL_ON_INDEX THEN
            ROLLBACK;
            RAISE_APPLICATION_ERROR(-20071, 'AddCustomer Failed: Customer ID already exists.');
        WHEN OTHERS THEN
            ROLLBACK;
            RAISE;
    END AddCustomer;
    
    PROCEDURE UpdateCustomer(p_cust_id IN NUMBER, p_name IN VARCHAR2, p_balance IN NUMBER) IS
        v_exists NUMBER;
    BEGIN
        SELECT COUNT(*) INTO v_exists FROM Customers WHERE CustomerID = p_cust_id;
        IF v_exists = 0 THEN
            RAISE_APPLICATION_ERROR(-20072, 'UpdateCustomer Failed: Customer ID not found.');
        END IF;
        UPDATE Customers SET Name = p_name, Balance = p_balance, LastUpdate = SYSDATE WHERE CustomerID = p_cust_id;
        COMMIT;
    END UpdateCustomer;
    
    FUNCTION GetCustomerBalance(p_cust_id IN NUMBER) RETURN NUMBER IS
        v_balance NUMBER(15, 2);
    BEGIN
        SELECT Balance INTO v_balance FROM Customers WHERE CustomerID = p_cust_id;
        RETURN v_balance;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            RAISE_APPLICATION_ERROR(-20073, 'GetCustomerBalance Failed: Customer ID not found.');
    END GetCustomerBalance;
END CustomerManagement;
/
```

#### Detailed Line-by-Line Explanation
* **Lines 1–5**: Declares the package specification with two procedures (`AddCustomer`, `UpdateCustomer`) and one function (`GetCustomerBalance`).
* **Lines 10–23**: Body implementation of `AddCustomer` with error handling for duplicate keys (`DUP_VAL_ON_INDEX`) and rollback controls.
* **Lines 25–34**: Body implementation of `UpdateCustomer` checking customer existence before executing updates.
* **Lines 36–46**: Body implementation of `GetCustomerBalance` returning customer balances.

#### Sample Execution
```sql
DECLARE
    v_bal NUMBER;
BEGIN
    CustomerManagement.AddCustomer(3, 'Robert Miller', TO_DATE('1975-11-10', 'YYYY-MM-DD'), 25000.00);
    v_bal := CustomerManagement.GetCustomerBalance(3);
    DBMS_OUTPUT.PUT_LINE('Robert Balance: ' || v_bal);
END;
/
```

---

### 4. EmployeeManagement.sql

#### Problem Statement
Design a package `EmployeeManagement` containing methods to hire a new employee, update an employee's profile, and compute an employee's annual salary.

#### Objective
Create specification and body implementing hiring inserts, departmental updates, and a calculation function returning monthly salary scaled by 12.

#### Complete Oracle PL/SQL Code
```sql
CREATE OR REPLACE PACKAGE EmployeeManagement IS
    PROCEDURE HireEmployee(p_emp_id IN NUMBER, p_name IN VARCHAR2, p_position IN VARCHAR2, p_salary IN NUMBER, p_dept IN VARCHAR2, p_hire_date IN DATE);
    PROCEDURE UpdateEmployee(p_emp_id IN NUMBER, p_name IN VARCHAR2, p_position IN VARCHAR2, p_salary IN NUMBER, p_dept IN VARCHAR2);
    FUNCTION CalculateAnnualSalary(p_emp_id IN NUMBER) RETURN NUMBER;
END EmployeeManagement;
/

CREATE OR REPLACE PACKAGE BODY EmployeeManagement IS
    PROCEDURE HireEmployee(p_emp_id IN NUMBER, p_name IN VARCHAR2, p_position IN VARCHAR2, p_salary IN NUMBER, p_dept IN VARCHAR2, p_hire_date IN DATE) IS
    BEGIN
        INSERT INTO Employees (EmployeeID, Name, Position, Salary, Department, HireDate)
        VALUES (p_emp_id, p_name, p_position, p_salary, p_dept, p_hire_date);
        COMMIT;
    EXCEPTION
        WHEN DUP_VAL_ON_INDEX THEN
            ROLLBACK;
            RAISE_APPLICATION_ERROR(-20081, 'HireEmployee Failed: Employee ID already exists.');
    END HireEmployee;
    
    PROCEDURE UpdateEmployee(p_emp_id IN NUMBER, p_name IN VARCHAR2, p_position IN VARCHAR2, p_salary IN NUMBER, p_dept IN VARCHAR2) IS
        v_exists NUMBER;
    BEGIN
        SELECT COUNT(*) INTO v_exists FROM Employees WHERE EmployeeID = p_emp_id;
        IF v_exists = 0 THEN
            RAISE_APPLICATION_ERROR(-20082, 'UpdateEmployee Failed: Employee ID not found.');
        END IF;
        UPDATE Employees SET Name = p_name, Position = p_position, Salary = p_salary, Department = p_dept WHERE EmployeeID = p_emp_id;
        COMMIT;
    END UpdateEmployee;
    
    FUNCTION CalculateAnnualSalary(p_emp_id IN NUMBER) RETURN NUMBER IS
        v_monthly_salary NUMBER(15, 2);
    BEGIN
        SELECT Salary INTO v_monthly_salary FROM Employees WHERE EmployeeID = p_emp_id;
        RETURN v_monthly_salary * 12;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            RAISE_APPLICATION_ERROR(-20083, 'CalculateAnnualSalary Failed: Employee ID not found.');
    END CalculateAnnualSalary;
END EmployeeManagement;
/
```

---

### 5. AccountOperations.sql

#### Problem Statement
Design a package `AccountOperations` containing methods to open a new account, close a bank account, and sum total balances for a customer.

#### Objective
Implement account opening inserts, account closing deletions, and aggregate functions summing account balances for a customer.

#### Complete Oracle PL/SQL Code
```sql
CREATE OR REPLACE PACKAGE AccountOperations IS
    PROCEDURE OpenAccount(p_acc_id IN NUMBER, p_cust_id IN NUMBER, p_acc_type IN VARCHAR2, p_balance IN NUMBER);
    PROCEDURE CloseAccount(p_acc_id IN NUMBER);
    FUNCTION GetTotalBalance(p_cust_id IN NUMBER) RETURN NUMBER;
END AccountOperations;
/

CREATE OR REPLACE PACKAGE BODY AccountOperations IS
    PROCEDURE OpenAccount(p_acc_id IN NUMBER, p_cust_id IN NUMBER, p_acc_type IN VARCHAR2, p_balance IN NUMBER) IS
        v_cust_exists NUMBER;
    BEGIN
        SELECT COUNT(*) INTO v_cust_exists FROM Customers WHERE CustomerID = p_cust_id;
        IF v_cust_exists = 0 THEN
            RAISE_APPLICATION_ERROR(-20091, 'OpenAccount Failed: Customer ID not found.');
        END IF;
        INSERT INTO Accounts (AccountID, CustomerID, AccountType, Balance, LastUpdate)
        VALUES (p_acc_id, p_cust_id, p_acc_type, p_balance, SYSDATE);
        COMMIT;
    EXCEPTION
        WHEN DUP_VAL_ON_INDEX THEN
            ROLLBACK;
            RAISE_APPLICATION_ERROR(-20092, 'OpenAccount Failed: Account ID already exists.');
    END OpenAccount;
    
    PROCEDURE CloseAccount(p_acc_id IN NUMBER) IS
        v_exists NUMBER;
    BEGIN
        SELECT COUNT(*) INTO v_exists FROM Accounts WHERE AccountID = p_acc_id;
        IF v_exists = 0 THEN
            RAISE_APPLICATION_ERROR(-20093, 'CloseAccount Failed: Account ID does not exist.');
        END IF;
        DELETE FROM Accounts WHERE AccountID = p_acc_id;
        COMMIT;
    END CloseAccount;
    
    FUNCTION GetTotalBalance(p_cust_id IN NUMBER) RETURN NUMBER IS
        v_total_balance NUMBER(15, 2);
        v_cust_exists NUMBER;
    BEGIN
        SELECT COUNT(*) INTO v_cust_exists FROM Customers WHERE CustomerID = p_cust_id;
        IF v_cust_exists = 0 THEN
            RAISE_APPLICATION_ERROR(-20094, 'GetTotalBalance Failed: Customer ID does not exist.');
        END IF;
        SELECT SUM(Balance) INTO v_total_balance FROM Accounts WHERE CustomerID = p_cust_id;
        RETURN NVL(v_total_balance, 0.00);
    END GetTotalBalance;
END AccountOperations;
/
```

---

## Viva & Interview Questions
1. **Can a Package Specification exist without a Package Body?**
   Yes. If the package specification only contains variables, constants, exceptions, or types (without subprogram declarations), a package body is not needed.
2. **What is package overloading?**
   Defining multiple procedures or functions inside a package with the same name but different parameter types or parameter counts.

---

## Conclusion
Packages organize PL/SQL code logically, improve security through encapsulation, and optimize execution performance.
