-- =========================================================
-- ORACLE PL/SQL EXERCISE 2: ERROR HANDLING
-- FILE: AddNewCustomer.sql
-- PURPOSE: Inserts a new customer record after validating that 
--          the CustomerID does not already exist, with strict
--          concurrency checks and error handling.
-- =========================================================

SET SERVEROUTPUT ON;

-- ---------------------------------------------------------
-- 1. Stored Procedure Definition
-- ---------------------------------------------------------
CREATE OR REPLACE PROCEDURE AddNewCustomer (
    p_cust_id IN NUMBER,
    p_name IN VARCHAR2,
    p_dob IN DATE,
    p_balance IN NUMBER
) IS
    -- Declarations
    v_exists NUMBER;
    
    -- Custom user-defined exception
    e_duplicate_customer EXCEPTION;

BEGIN
    DBMS_OUTPUT.PUT_LINE('AddNewCustomer initiated for ID: ' || p_cust_id);
    
    -- 1. Check whether CustomerID already exists
    SELECT COUNT(*)
    INTO v_exists
    FROM Customers
    WHERE CustomerID = p_cust_id;
    
    IF v_exists > 0 THEN
        RAISE e_duplicate_customer;
    END IF;
    
    -- 2. Insert customer details
    INSERT INTO Customers (CustomerID, Name, DOB, Balance, IsVIP, LastUpdate)
    VALUES (p_cust_id, p_name, p_dob, p_balance, 'FALSE', SYSDATE);
    
    -- Commit the transaction if the insertion is successful
    COMMIT;
    DBMS_OUTPUT.PUT_LINE('Customer Added Successfully');
    
EXCEPTION
    -- Handle user-defined duplicate customer exception
    WHEN e_duplicate_customer THEN
        ROLLBACK;
        DBMS_OUTPUT.PUT_LINE('Customer Already Exists');
        RAISE_APPLICATION_ERROR(-20007, 'Insertion Failed: Customer ID ' || p_cust_id || ' already exists in database.');
        
    -- Handle database index constraint violation (race condition backup)
    WHEN DUP_VAL_ON_INDEX THEN
        ROLLBACK;
        DBMS_OUTPUT.PUT_LINE('Customer Already Exists (Database Index Constraint)');
        RAISE_APPLICATION_ERROR(-20008, 'Insertion Failed: Unique key constraint violated on CustomerID.');
        
    -- Handle NO_DATA_FOUND (for compliance, although COUNT(*) always returns a row)
    WHEN NO_DATA_FOUND THEN
        ROLLBACK;
        DBMS_OUTPUT.PUT_LINE('No Data Found during query.');
        RAISE_APPLICATION_ERROR(-20009, 'Insertion Failed: Query returned no data.');
        
    -- Handle all other Oracle errors
    WHEN OTHERS THEN
        ROLLBACK;
        DBMS_OUTPUT.PUT_LINE('Error: ' || SQLERRM);
        RAISE_APPLICATION_ERROR(-20999, 'Insertion Failed: Oracle Database Error. Code: ' || SQLCODE || ' - ' || SQLERRM);
END AddNewCustomer;
/

-- ---------------------------------------------------------
-- 2. Anonymous Test Block (Demonstrates invocation)
-- ---------------------------------------------------------
DECLARE
    v_cust_id NUMBER := 5;
    v_name VARCHAR2(100) := 'Sarah Connor';
    v_dob DATE := TO_DATE('1984-11-26', 'YYYY-MM-DD');
    v_bal NUMBER := 3500.00;
BEGIN
    DBMS_OUTPUT.PUT_LINE('============================================================');
    DBMS_OUTPUT.PUT_LINE('TEST CASE 1: Valid Customer Insertion (Adding ID 5)');
    DBMS_OUTPUT.PUT_LINE('============================================================');
    AddNewCustomer(v_cust_id, v_name, v_dob, v_bal);
EXCEPTION
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('Test 1 Exception: ' || SQLERRM);
END;
/

DECLARE
    v_cust_id NUMBER := 1; -- Customer ID 1 already exists from sample_data.sql
    v_name VARCHAR2(100) := 'Dupe Customer';
    v_dob DATE := TO_DATE('1990-01-01', 'YYYY-MM-DD');
    v_bal NUMBER := 100.00;
BEGIN
    DBMS_OUTPUT.PUT_LINE('============================================================');
    DBMS_OUTPUT.PUT_LINE('TEST CASE 2: Invalid Customer Insertion (ID 1 Already Exists)');
    DBMS_OUTPUT.PUT_LINE('============================================================');
    AddNewCustomer(v_cust_id, v_name, v_dob, v_bal);
EXCEPTION
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('Test 2 Caught Exception: ' || SQLERRM);
END;
/
