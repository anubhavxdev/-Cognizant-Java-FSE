-- =========================================================
-- ORACLE PL/SQL EXERCISE 2: ERROR HANDLING
-- FILE: SafeTransferFunds.sql
-- PURPOSE: Implements a safe money transfer procedure between
--          two accounts, incorporating validations, custom
--          exceptions, transaction control, and auditing.
-- =========================================================

SET SERVEROUTPUT ON;

-- ---------------------------------------------------------
-- 1. Stored Procedure Definition
-- ---------------------------------------------------------
CREATE OR REPLACE PROCEDURE SafeTransferFunds (
    p_source_acc_id IN NUMBER,
    p_dest_acc_id IN NUMBER,
    p_amount IN NUMBER
) IS
    -- Declarations (Equivalent to DECLARE block in anonymous PL/SQL)
    v_source_balance NUMBER(15, 2);
    v_dest_balance NUMBER(15, 2);
    v_source_exists NUMBER;
    v_dest_exists NUMBER;
    
    -- User-defined exceptions for business validation
    e_invalid_amount EXCEPTION;
    e_source_not_found EXCEPTION;
    e_dest_not_found EXCEPTION;
    e_insufficient_funds EXCEPTION;

BEGIN
    DBMS_OUTPUT.PUT_LINE('Transfer Started');
    DBMS_OUTPUT.PUT_LINE('Source Account ID: ' || p_source_acc_id || ' | Destination Account ID: ' || p_dest_acc_id);
    DBMS_OUTPUT.PUT_LINE('Transfer Amount  : ' || TO_CHAR(p_amount, '$99,999.00'));
    
    -- Validate: Transfer amount must be positive
    IF p_amount <= 0 THEN
        RAISE e_invalid_amount;
    END IF;
    
    -- Validate: Check if Source Account exists
    SELECT COUNT(*) 
    INTO v_source_exists 
    FROM Accounts 
    WHERE AccountID = p_source_acc_id;
    
    IF v_source_exists = 0 THEN
        RAISE e_source_not_found;
    END IF;
    
    -- Validate: Check if Destination Account exists
    SELECT COUNT(*) 
    INTO v_dest_exists 
    FROM Accounts 
    WHERE AccountID = p_dest_acc_id;
    
    IF v_dest_exists = 0 THEN
        RAISE e_dest_not_found;
    END IF;
    
    -- Fetch balances and lock the rows for update to prevent race conditions (concurrency control)
    SELECT Balance INTO v_source_balance 
    FROM Accounts 
    WHERE AccountID = p_source_acc_id 
    FOR UPDATE;
    
    SELECT Balance INTO v_dest_balance 
    FROM Accounts 
    WHERE AccountID = p_dest_acc_id 
    FOR UPDATE;
    
    DBMS_OUTPUT.PUT_LINE('Source Balance     : ' || TO_CHAR(v_source_balance, '$99,999.00'));
    DBMS_OUTPUT.PUT_LINE('Destination Balance: ' || TO_CHAR(v_dest_balance, '$99,999.00'));
    
    -- Validate: Check if Source Account has sufficient balance
    IF v_source_balance < p_amount THEN
        RAISE e_insufficient_funds;
    END IF;
    
    -- Perform DML: Deduct from source account
    UPDATE Accounts
    SET Balance = Balance - p_amount,
        LastUpdate = SYSDATE
    WHERE AccountID = p_source_acc_id;
    
    -- Perform DML: Add to destination account
    UPDATE Accounts
    SET Balance = Balance + p_amount,
        LastUpdate = SYSDATE
    WHERE AccountID = p_dest_acc_id;
    
    -- Auditing: Record Transaction for Source Account (Withdrawal)
    INSERT INTO Transactions (TransactionID, AccountID, TransactionDate, Amount, TransactionType)
    VALUES (
        (SELECT NVL(MAX(TransactionID), 0) + 1 FROM Transactions),
        p_source_acc_id,
        SYSDATE,
        p_amount,
        'Withdrawal'
    );
    
    -- Auditing: Record Transaction for Destination Account (Deposit)
    INSERT INTO Transactions (TransactionID, AccountID, TransactionDate, Amount, TransactionType)
    VALUES (
        (SELECT NVL(MAX(TransactionID), 0) + 1 FROM Transactions),
        p_dest_acc_id,
        SYSDATE,
        p_amount,
        'Deposit'
    );
    
    -- Commit the transaction to save changes permanently
    COMMIT;
    DBMS_OUTPUT.PUT_LINE('Transfer Successful');
    
EXCEPTION
    -- Custom exception handlers utilizing RAISE_APPLICATION_ERROR
    WHEN e_invalid_amount THEN
        ROLLBACK;
        RAISE_APPLICATION_ERROR(-20001, 'Transfer Failed: Transfer amount must be positive.');
        
    WHEN e_source_not_found THEN
        ROLLBACK;
        RAISE_APPLICATION_ERROR(-20002, 'Transfer Failed: Source account ' || p_source_acc_id || ' does not exist.');
        
    WHEN e_dest_not_found THEN
        ROLLBACK;
        RAISE_APPLICATION_ERROR(-20003, 'Transfer Failed: Destination account ' || p_dest_acc_id || ' does not exist.');
        
    WHEN e_insufficient_funds THEN
        ROLLBACK;
        RAISE_APPLICATION_ERROR(-20004, 'Transfer Failed: Insufficient funds. Source balance is ' 
            || TO_CHAR(v_source_balance, '$99,999.00') || ', requested transfer: ' || TO_CHAR(p_amount, '$99,999.00'));
            
    WHEN OTHERS THEN
        -- Handle unexpected system/Oracle errors
        ROLLBACK;
        RAISE_APPLICATION_ERROR(-20999, 'Transfer Failed: System error occurred. Code: ' || SQLCODE || ' - ' || SQLERRM);
END SafeTransferFunds;
/

-- ---------------------------------------------------------
-- 3. Anonymous Test Block using DECLARE (Demonstrates invocation)
-- ---------------------------------------------------------
DECLARE
    v_src_acc NUMBER := 1001;
    v_dst_acc NUMBER := 1002;
    v_tx_amount NUMBER := 500.00;
BEGIN
    DBMS_OUTPUT.PUT_LINE('============================================================');
    DBMS_OUTPUT.PUT_LINE('TEST CASE 1: Valid Funds Transfer of $500.00');
    DBMS_OUTPUT.PUT_LINE('============================================================');
    SafeTransferFunds(v_src_acc, v_dst_acc, v_tx_amount);
EXCEPTION
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('Test 1 Exception: ' || SQLERRM);
END;
/

DECLARE
    v_src_acc NUMBER := 1001;
    v_dst_acc NUMBER := 1002;
    v_tx_amount NUMBER := 100000.00; -- Excess amount to trigger insufficient funds
BEGIN
    DBMS_OUTPUT.PUT_LINE('============================================================');
    DBMS_OUTPUT.PUT_LINE('TEST CASE 2: Invalid Funds Transfer (Insufficient Balance)');
    DBMS_OUTPUT.PUT_LINE('============================================================');
    SafeTransferFunds(v_src_acc, v_dst_acc, v_tx_amount);
EXCEPTION
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('Test 2 Caught Exception: ' || SQLERRM);
END;
/
