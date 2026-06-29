-- =========================================================
-- ORACLE PL/SQL EXERCISE 1: CONTROL STRUCTURES
-- FILE: scenario2_vip_customers.sql
-- PURPOSE: Classify bank customers as VIP (IsVIP = 'TRUE') if
--          their balance exceeds $10,000, else 'FALSE'.
-- =========================================================

-- Enable server output to display messages in SQL Developer / SQL*Plus
SET SERVEROUTPUT ON;

-- ---------------------------------------------------------
-- 1. Alter Customers Table (Dynamic check to ensure re-runnability)
-- ---------------------------------------------------------
DECLARE
    v_col_count NUMBER;
BEGIN
    -- Check if IsVIP column already exists in Customers table
    SELECT COUNT(*)
    INTO v_col_count
    FROM USER_TAB_COLS
    WHERE TABLE_NAME = 'CUSTOMERS' AND COLUMN_NAME = 'ISVIP';
    
    -- If the column does not exist, add it
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
        
    -- Record variable to hold cursor data
    v_cust_rec c_customers%ROWTYPE;
    
    -- Variable to hold VIP status
    v_vip_status VARCHAR2(5);
    v_processed_count NUMBER := 0;

BEGIN
    DBMS_OUTPUT.PUT_LINE('------------------------------------------------------------');
    DBMS_OUTPUT.PUT_LINE('STARTING CUSTOMER VIP STATUS UPDATE');
    DBMS_OUTPUT.PUT_LINE('------------------------------------------------------------');
    
    -- Open the explicit cursor
    OPEN c_customers;
    
    -- Loop through customer records
    LOOP
        -- Fetch row into the record variable
        FETCH c_customers INTO v_cust_rec;
        
        -- Exit condition when no more rows exist
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
        
        -- Increment the counter
        v_processed_count := v_processed_count + 1;
        
        -- Print customer VIP details
        DBMS_OUTPUT.PUT_LINE('Customer ID: ' || v_cust_rec.CustomerID);
        DBMS_OUTPUT.PUT_LINE('Name       : ' || v_cust_rec.Name);
        DBMS_OUTPUT.PUT_LINE('Balance    : ' || TO_CHAR(v_cust_rec.Balance, '$99,999.00'));
        DBMS_OUTPUT.PUT_LINE('VIP Status : ' || v_vip_status);
        DBMS_OUTPUT.PUT_LINE('------------------------------------------------------------');
    END LOOP;
    
    -- Close the cursor
    CLOSE c_customers;
    
    -- Commit the transaction to finalize updates
    COMMIT;
    
    DBMS_OUTPUT.PUT_LINE('VIP Classification completed. Total records processed: ' || v_processed_count);
    DBMS_OUTPUT.PUT_LINE('------------------------------------------------------------');

EXCEPTION
    WHEN OTHERS THEN
        -- Handle unexpected errors gracefully and rollback transactions
        IF c_customers%ISOPEN THEN
            CLOSE c_customers;
        END IF;
        ROLLBACK;
        DBMS_OUTPUT.PUT_LINE('ERROR: An unexpected error occurred. transaction rolled back.');
        DBMS_OUTPUT.PUT_LINE('Error Message: ' || SQLERRM);
END;
/
