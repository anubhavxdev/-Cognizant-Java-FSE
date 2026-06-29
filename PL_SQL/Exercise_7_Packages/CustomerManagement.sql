-- =========================================================
-- ORACLE PL/SQL EXERCISE 7: PACKAGES
-- FILE: CustomerManagement.sql
-- PURPOSE: Implements a Customer Management package containing
--          procedures to add and update customer details,
--          and a function to retrieve customer balances.
-- =========================================================

SET SERVEROUTPUT ON;

-- ---------------------------------------------------------
-- 1. Package Specification
-- ---------------------------------------------------------
CREATE OR REPLACE PACKAGE CustomerManagement IS

    -- Procedure to add a new customer to the database
    PROCEDURE AddCustomer(
        p_cust_id IN NUMBER,
        p_name IN VARCHAR2,
        p_dob IN DATE,
        p_balance IN NUMBER
    );
    
    -- Procedure to update customer name and balance
    PROCEDURE UpdateCustomer(
        p_cust_id IN NUMBER,
        p_name IN VARCHAR2,
        p_balance IN NUMBER
    );
    
    -- Function to fetch a customer's balance
    FUNCTION GetCustomerBalance(
        p_cust_id IN NUMBER
    ) RETURN NUMBER;

END CustomerManagement;
/

-- ---------------------------------------------------------
-- 2. Package Body
-- ---------------------------------------------------------
CREATE OR REPLACE PACKAGE BODY CustomerManagement IS

    -- Add a new customer
    PROCEDURE AddCustomer(
        p_cust_id IN NUMBER,
        p_name IN VARCHAR2,
        p_dob IN DATE,
        p_balance IN NUMBER
    ) IS
    BEGIN
        INSERT INTO Customers (CustomerID, Name, DOB, Balance, IsVIP, LastUpdate)
        VALUES (p_cust_id, p_name, p_dob, p_balance, 'FALSE', SYSDATE);
        
        -- Commit changes upon successful insertion
        COMMIT;
        DBMS_OUTPUT.PUT_LINE('AddCustomer Success: Registered ' || p_name || ' (ID: ' || p_cust_id || ')');
    EXCEPTION
        WHEN DUP_VAL_ON_INDEX THEN
            ROLLBACK;
            RAISE_APPLICATION_ERROR(-20071, 'AddCustomer Failed: Customer ID ' || p_cust_id || ' already exists.');
        WHEN OTHERS THEN
            ROLLBACK;
            RAISE_APPLICATION_ERROR(-20999, 'AddCustomer Failed: Database error: ' || SQLERRM);
    END AddCustomer;
    
    -- Update customer details
    PROCEDURE UpdateCustomer(
        p_cust_id IN NUMBER,
        p_name IN VARCHAR2,
        p_balance IN NUMBER
    ) IS
        v_exists NUMBER;
    BEGIN
        -- Verify existence first
        SELECT COUNT(*)
        INTO v_exists
        FROM Customers
        WHERE CustomerID = p_cust_id;
        
        IF v_exists = 0 THEN
            RAISE_APPLICATION_ERROR(-20072, 'UpdateCustomer Failed: Customer ID ' || p_cust_id || ' does not exist.');
        END IF;
        
        -- Update statement
        UPDATE Customers
        SET Name = p_name,
            Balance = p_balance,
            LastUpdate = SYSDATE
        WHERE CustomerID = p_cust_id;
        
        -- Commit updates
        COMMIT;
        DBMS_OUTPUT.PUT_LINE('UpdateCustomer Success: Modified Customer ID: ' || p_cust_id);
    EXCEPTION
        WHEN OTHERS THEN
            ROLLBACK;
            RAISE;
    END UpdateCustomer;
    
    -- Get customer balance
    FUNCTION GetCustomerBalance(
        p_cust_id IN NUMBER
    ) RETURN NUMBER IS
        v_balance NUMBER(15, 2);
    BEGIN
        SELECT Balance
        INTO v_balance
        FROM Customers
        WHERE CustomerID = p_cust_id;
        
        RETURN v_balance;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            RAISE_APPLICATION_ERROR(-20073, 'GetCustomerBalance Failed: Customer ID ' || p_cust_id || ' not found.');
    END GetCustomerBalance;

END CustomerManagement;
/

-- ---------------------------------------------------------
-- 3. Sample Execution Block
-- ---------------------------------------------------------
PROMPT TESTING CUSTOMER MANAGEMENT PACKAGE:
DECLARE
    v_balance NUMBER;
BEGIN
    DBMS_OUTPUT.PUT_LINE('============================================================');
    DBMS_OUTPUT.PUT_LINE('Step 1: Adding a new customer (ID: 3, Robert Miller)');
    DBMS_OUTPUT.PUT_LINE('============================================================');
    CustomerManagement.AddCustomer(3, 'Robert Miller', TO_DATE('1975-11-10', 'YYYY-MM-DD'), 25000.00);
    
    DBMS_OUTPUT.PUT_LINE('============================================================');
    DBMS_OUTPUT.PUT_LINE('Step 2: Fetching customer balance');
    DBMS_OUTPUT.PUT_LINE('============================================================');
    v_balance := CustomerManagement.GetCustomerBalance(3);
    DBMS_OUTPUT.PUT_LINE('Robert Miller Balance: ' || TO_CHAR(v_balance, '$99,999.00'));
    
    DBMS_OUTPUT.PUT_LINE('============================================================');
    DBMS_OUTPUT.PUT_LINE('Step 3: Updating customer balance and name');
    DBMS_OUTPUT.PUT_LINE('============================================================');
    CustomerManagement.UpdateCustomer(3, 'Robert A. Miller', 26500.00);
    
    v_balance := CustomerManagement.GetCustomerBalance(3);
    DBMS_OUTPUT.PUT_LINE('Updated Balance      : ' || TO_CHAR(v_balance, '$99,999.00'));
    DBMS_OUTPUT.PUT_LINE('============================================================');
EXCEPTION
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('Caught Exception: ' || SQLERRM);
END;
/
