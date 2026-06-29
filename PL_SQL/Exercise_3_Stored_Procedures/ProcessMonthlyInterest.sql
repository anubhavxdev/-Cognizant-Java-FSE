-- =========================================================
-- ORACLE PL/SQL EXERCISE 3: STORED PROCEDURES
-- FILE: ProcessMonthlyInterest.sql
-- PURPOSE: Calculates and applies monthly interest to all savings 
--          accounts, using a Cursor FOR Loop and explicit cursor.
-- =========================================================

SET SERVEROUTPUT ON;

-- ---------------------------------------------------------
-- 1. Stored Procedure Definition
-- ---------------------------------------------------------
CREATE OR REPLACE PROCEDURE ProcessMonthlyInterest (
    p_interest_rate IN NUMBER DEFAULT 1.0
) IS
    -- Explicit Cursor declared with FOR UPDATE to lock savings records during transaction
    CURSOR c_savings_accounts IS
        SELECT AccountID, CustomerID, Balance
        FROM Accounts
        WHERE AccountType = 'Savings'
        FOR UPDATE;
        
    -- Variables for monthly interest calculation
    v_interest_amount NUMBER(15, 2);
    v_new_balance NUMBER(15, 2);
    v_processed_count NUMBER := 0;

BEGIN
    DBMS_OUTPUT.PUT_LINE('============================================================');
    DBMS_OUTPUT.PUT_LINE('STARTING MONTHLY INTEREST PROCESSING');
    DBMS_OUTPUT.PUT_LINE('Annual Interest Rate Parameter: ' || TO_CHAR(p_interest_rate, '99.99') || '%');
    DBMS_OUTPUT.PUT_LINE('============================================================');

    -- 2. Cursor FOR Loop: automatically opens the cursor, fetches, and closes it
    FOR r_acc IN c_savings_accounts LOOP
        
        -- Monthly interest calculation: (Balance * (AnnualRate / 12 months)) / 100
        v_interest_amount := ROUND(r_acc.Balance * (p_interest_rate / 12) / 100, 2);
        v_new_balance     := r_acc.Balance + v_interest_amount;
        
        -- Update the account record directly using WHERE CURRENT OF
        UPDATE Accounts
        SET Balance = v_new_balance,
            LastUpdate = SYSDATE
        WHERE CURRENT OF c_savings_accounts;
        
        -- Increment processed count
        v_processed_count := v_processed_count + 1;
        
        -- Print individual account details
        DBMS_OUTPUT.PUT_LINE('Account ID     : ' || r_acc.AccountID);
        DBMS_OUTPUT.PUT_LINE('Customer ID    : ' || r_acc.CustomerID);
        DBMS_OUTPUT.PUT_LINE('Old Balance    : ' || TO_CHAR(r_acc.Balance, '$99,999.00'));
        DBMS_OUTPUT.PUT_LINE('Interest Added : ' || TO_CHAR(v_interest_amount, '$99,999.00'));
        DBMS_OUTPUT.PUT_LINE('New Balance    : ' || TO_CHAR(v_new_balance, '$99,999.00'));
        DBMS_OUTPUT.PUT_LINE('------------------------------------------------------------');
        
    END LOOP;

    -- 3. Commit the transaction to save all interest updates permanently
    COMMIT;
    
    DBMS_OUTPUT.PUT_LINE('Total Savings Accounts Processed: ' || v_processed_count);
    DBMS_OUTPUT.PUT_LINE('Monthly Interest Processing Completed');
    DBMS_OUTPUT.PUT_LINE('============================================================');

EXCEPTION
    WHEN OTHERS THEN
        -- Revert changes and rollback if an error occurs
        ROLLBACK;
        DBMS_OUTPUT.PUT_LINE('ERROR: Failed to complete monthly interest processing.');
        DBMS_OUTPUT.PUT_LINE('Error Message: ' || SQLERRM);
END ProcessMonthlyInterest;
/

-- ---------------------------------------------------------
-- 2. Test Block (Demonstrates invocation)
-- ---------------------------------------------------------
-- Test Case 1: Running with default rate (1%)
BEGIN
    DBMS_OUTPUT.PUT_LINE('TEST CASE 1: Running with DEFAULT interest rate (1%)');
    ProcessMonthlyInterest;
END;
/

-- Test Case 2: Running with a custom annual rate (6%)
BEGIN
    DBMS_OUTPUT.PUT_LINE('TEST CASE 2: Running with CUSTOM interest rate (6.0%)');
    ProcessMonthlyInterest(6.0);
END;
/
