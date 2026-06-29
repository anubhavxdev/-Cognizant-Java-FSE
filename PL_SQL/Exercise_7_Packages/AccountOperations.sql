-- =========================================================
-- ORACLE PL/SQL EXERCISE 7: PACKAGES
-- FILE: AccountOperations.sql
-- PURPOSE: Implements an Account Operations package containing
--          procedures to open and close customer accounts,
--          and a function to compute total balances across accounts.
-- =========================================================

SET SERVEROUTPUT ON;

-- ---------------------------------------------------------
-- 1. Package Specification
-- ---------------------------------------------------------
CREATE OR REPLACE PACKAGE AccountOperations IS

    -- Procedure to open a new bank account
    PROCEDURE OpenAccount(
        p_acc_id IN NUMBER,
        p_cust_id IN NUMBER,
        p_acc_type IN VARCHAR2,
        p_balance IN NUMBER
    );
    
    -- Procedure to close a bank account (DML Delete)
    PROCEDURE CloseAccount(
        p_acc_id IN NUMBER
    );
    
    -- Function to calculate sum of balances across all accounts for a customer
    FUNCTION GetTotalBalance(
        p_cust_id IN NUMBER
    ) RETURN NUMBER;

END AccountOperations;
/

-- ---------------------------------------------------------
-- 2. Package Body
-- ---------------------------------------------------------
CREATE OR REPLACE PACKAGE BODY AccountOperations IS

    -- Open a new account
    PROCEDURE OpenAccount(
        p_acc_id IN NUMBER,
        p_cust_id IN NUMBER,
        p_acc_type IN VARCHAR2,
        p_balance IN NUMBER
    ) IS
        v_cust_exists NUMBER;
    BEGIN
        -- Check if Customer exists
        SELECT COUNT(*) 
        INTO v_cust_exists 
        FROM Customers 
        WHERE CustomerID = p_cust_id;
        
        IF v_cust_exists = 0 THEN
            RAISE_APPLICATION_ERROR(-20091, 'OpenAccount Failed: Customer ID ' || p_cust_id || ' does not exist.');
        END IF;
        
        -- Insert new account record
        INSERT INTO Accounts (AccountID, CustomerID, AccountType, Balance, LastUpdate)
        VALUES (p_acc_id, p_cust_id, p_acc_type, p_balance, SYSDATE);
        
        -- Commit changes
        COMMIT;
        DBMS_OUTPUT.PUT_LINE('OpenAccount Success: Account ID ' || p_acc_id || ' opened for Customer ID ' || p_cust_id);
    EXCEPTION
        WHEN DUP_VAL_ON_INDEX THEN
            ROLLBACK;
            RAISE_APPLICATION_ERROR(-20092, 'OpenAccount Failed: Account ID ' || p_acc_id || ' already exists.');
        WHEN OTHERS THEN
            ROLLBACK;
            RAISE;
    END OpenAccount;
    
    -- Close a bank account
    PROCEDURE CloseAccount(
        p_acc_id IN NUMBER
    ) IS
        v_exists NUMBER;
    BEGIN
        -- Check if Account exists
        SELECT COUNT(*) 
        INTO v_exists 
        FROM Accounts 
        WHERE AccountID = p_acc_id;
        
        IF v_exists = 0 THEN
            RAISE_APPLICATION_ERROR(-20093, 'CloseAccount Failed: Account ID ' || p_acc_id || ' does not exist.');
        END IF;
        
        -- Delete the account
        DELETE FROM Accounts 
        WHERE AccountID = p_acc_id;
        
        -- Commit changes
        COMMIT;
        DBMS_OUTPUT.PUT_LINE('CloseAccount Success: Account ID ' || p_acc_id || ' closed and deleted.');
    EXCEPTION
        WHEN OTHERS THEN
            ROLLBACK;
            RAISE;
    END CloseAccount;
    
    -- Calculate total balance for a customer
    FUNCTION GetTotalBalance(
        p_cust_id IN NUMBER
    ) RETURN NUMBER IS
        v_total_balance NUMBER(15, 2);
        v_cust_exists NUMBER;
    BEGIN
        -- Check customer existence
        SELECT COUNT(*) 
        INTO v_cust_exists 
        FROM Customers 
        WHERE CustomerID = p_cust_id;
        
        IF v_cust_exists = 0 THEN
            RAISE_APPLICATION_ERROR(-20094, 'GetTotalBalance Failed: Customer ID ' || p_cust_id || ' does not exist.');
        END IF;
        
        -- Fetch sum of balances across accounts
        SELECT SUM(Balance)
        INTO v_total_balance
        FROM Accounts
        WHERE CustomerID = p_cust_id;
        
        -- Return 0 if SUM returns NULL
        RETURN NVL(v_total_balance, 0.00);
    END GetTotalBalance;

END AccountOperations;
/

-- ---------------------------------------------------------
-- 3. Sample Execution Block
-- ---------------------------------------------------------
PROMPT TESTING ACCOUNT OPERATIONS PACKAGE:
DECLARE
    v_total_bal NUMBER;
BEGIN
    -- Customer ID 1 has Account 1001 (Balance $5,000)
    DBMS_OUTPUT.PUT_LINE('============================================================');
    DBMS_OUTPUT.PUT_LINE('Step 1: Opening a second account (1004) for Customer 1 ($2,500.00)');
    DBMS_OUTPUT.PUT_LINE('============================================================');
    AccountOperations.OpenAccount(1004, 1, 'Savings', 2500.00);
    
    DBMS_OUTPUT.PUT_LINE('============================================================');
    DBMS_OUTPUT.PUT_LINE('Step 2: Calculating customer total balance across all accounts');
    DBMS_OUTPUT.PUT_LINE('============================================================');
    v_total_bal := AccountOperations.GetTotalBalance(1);
    DBMS_OUTPUT.PUT_LINE('Customer 1 Total Balance: ' || TO_CHAR(v_total_bal, '$99,999.00') || ' (Expected: $7,500.00)');
    
    DBMS_OUTPUT.PUT_LINE('============================================================');
    DBMS_OUTPUT.PUT_LINE('Step 3: Closing Account 1004');
    DBMS_OUTPUT.PUT_LINE('============================================================');
    AccountOperations.CloseAccount(1004);
    
    v_total_bal := AccountOperations.GetTotalBalance(1);
    DBMS_OUTPUT.PUT_LINE('Customer 1 Total Balance: ' || TO_CHAR(v_total_bal, '$99,999.00') || ' (Expected: $5,000.00)');
    DBMS_OUTPUT.PUT_LINE('============================================================');
EXCEPTION
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('Caught Exception: ' || SQLERRM);
END;
/
