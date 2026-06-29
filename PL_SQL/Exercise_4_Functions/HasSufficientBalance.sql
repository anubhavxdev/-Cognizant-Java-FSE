-- =========================================================
-- ORACLE PL/SQL EXERCISE 4: FUNCTIONS
-- FILE: HasSufficientBalance.sql
-- PURPOSE: Implements a function to check if an account contains
--          a balance greater than or equal to a specified amount.
-- =========================================================

SET SERVEROUTPUT ON;

-- ---------------------------------------------------------
-- 1. Function Definition
-- ---------------------------------------------------------
-- NOTE: This function returns a PL/SQL BOOLEAN. Oracle SQL does
-- not support BOOLEAN in SELECT queries (e.g. in SELECT from DUAL).
-- Therefore, this function must be invoked inside PL/SQL blocks.
-- ---------------------------------------------------------
CREATE OR REPLACE FUNCTION HasSufficientBalance (
    p_account_id IN NUMBER,
    p_amount IN NUMBER
) RETURN BOOLEAN IS
    v_balance NUMBER(15, 2);
BEGIN
    -- Rule validation: negative request amounts are invalid
    IF p_amount < 0 THEN
        RETURN FALSE;
    END IF;
    
    -- Query the balance using SELECT INTO
    SELECT Balance
    INTO v_balance
    FROM Accounts
    WHERE AccountID = p_account_id;
    
    -- Evaluate balance condition
    IF v_balance >= p_amount THEN
        RETURN TRUE;
    ELSE
        RETURN FALSE;
    END IF;
    
EXCEPTION
    -- Catch exception if the Account ID is not found in database
    WHEN NO_DATA_FOUND THEN
        DBMS_OUTPUT.PUT_LINE('Warning: Account ID ' || p_account_id || ' does not exist.');
        RETURN FALSE;
        
    -- Catch all other unexpected failures
    WHEN OTHERS THEN
        RETURN FALSE;
END HasSufficientBalance;
/

-- ---------------------------------------------------------
-- 2. Sample Executions (PL/SQL Blocks)
-- ---------------------------------------------------------

PROMPT PL/SQL BLOCK TEST:

-- Test Case 1: Checking account 1001 (Balance $5,000) for a withdrawal of $3,000 (Sufficient)
DECLARE
    v_acc NUMBER := 1001;
    v_amt NUMBER := 3000.00;
BEGIN
    IF HasSufficientBalance(v_acc, v_amt) THEN
        DBMS_OUTPUT.PUT_LINE('Test 1 Passed: Account ' || v_acc || ' has sufficient funds for ' || TO_CHAR(v_amt, '$99,999.00'));
    ELSE
        DBMS_OUTPUT.PUT_LINE('Test 1 Failed: Account ' || v_acc || ' lacks funds for ' || TO_CHAR(v_amt, '$99,999.00'));
    END IF;
END;
/

-- Test Case 2: Checking account 1002 (Balance $1,000) for a withdrawal of $8,000 (Insufficient)
DECLARE
    v_acc NUMBER := 1002;
    v_amt NUMBER := 8000.00;
BEGIN
    IF HasSufficientBalance(v_acc, v_amt) THEN
        DBMS_OUTPUT.PUT_LINE('Test 2 Failed: Account ' || v_acc || ' has sufficient funds for ' || TO_CHAR(v_amt, '$99,999.00'));
    ELSE
        DBMS_OUTPUT.PUT_LINE('Test 2 Passed: Account ' || v_acc || ' correctly reported insufficient funds for ' || TO_CHAR(v_amt, '$99,999.00'));
    END IF;
END;
/

-- Test Case 3: Checking non-existent account ID 9999
DECLARE
    v_acc NUMBER := 9999;
    v_amt NUMBER := 100.00;
BEGIN
    IF HasSufficientBalance(v_acc, v_amt) THEN
        DBMS_OUTPUT.PUT_LINE('Test 3 Failed: Account ' || v_acc || ' reported sufficient funds.');
    ELSE
        DBMS_OUTPUT.PUT_LINE('Test 3 Passed: Non-existent account ' || v_acc || ' correctly evaluated to FALSE.');
    END IF;
END;
/
