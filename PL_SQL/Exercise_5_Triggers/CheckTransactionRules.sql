-- =========================================================
-- ORACLE PL/SQL EXERCISE 5: TRIGGERS
-- FILE: CheckTransactionRules.sql
-- PURPOSE: Implements a BEFORE INSERT trigger on Transactions table
--          to enforce banking rules (positive transaction amount
--          and sufficient balance check for withdrawals).
-- =========================================================

SET SERVEROUTPUT ON;

-- ---------------------------------------------------------
-- 1. Trigger Definition
-- ---------------------------------------------------------
CREATE OR REPLACE TRIGGER CheckTransactionRules
BEFORE INSERT ON Transactions
FOR EACH ROW
DECLARE
    v_balance NUMBER(15, 2);
BEGIN
    -- Rule 1: Deposits and Withdrawals must be positive values
    IF :new.Amount <= 0 THEN
        RAISE_APPLICATION_ERROR(
            -20021, 
            'Transaction Failed: Transaction amount must be positive. Amount entered: ' 
            || TO_CHAR(:new.Amount, '$99,999.00')
        );
    END IF;
    
    -- Rule 2: Withdrawals cannot exceed account balance
    IF :new.TransactionType = 'Withdrawal' THEN
        -- Query the active balance from Accounts table
        SELECT Balance 
        INTO v_balance 
        FROM Accounts 
        WHERE AccountID = :new.AccountID;
        
        -- Raise error if amount exceeds available balance
        IF :new.Amount > v_balance THEN
            RAISE_APPLICATION_ERROR(
                -20022, 
                'Transaction Failed: Insufficient funds in Account ' || :new.AccountID || '. ' ||
                'Available balance: ' || TO_CHAR(v_balance, '$99,999.00') || ', ' ||
                'Requested withdrawal: ' || TO_CHAR(:new.Amount, '$99,999.00')
            );
        END IF;
    END IF;
EXCEPTION
    WHEN NO_DATA_FOUND THEN
        -- Handle case where AccountID is invalid/doesn't exist
        RAISE_APPLICATION_ERROR(-20023, 'Transaction Failed: Account ID ' || :new.AccountID || ' does not exist.');
    WHEN OTHERS THEN
        -- Propagate other application errors or handle system issues
        RAISE;
END;
/

-- ---------------------------------------------------------
-- 2. Sample Executions & Test Block
-- ---------------------------------------------------------
PROMPT TESTING TRANSACTION RULES TRIGGER:

-- Test Case 1: Attempting withdrawal exceeding balance ($10,000 from Account 1001 with balance $5,000)
DECLARE
BEGIN
    DBMS_OUTPUT.PUT_LINE('============================================================');
    DBMS_OUTPUT.PUT_LINE('Test 1: Attempting Overdraft ($10,000.00 withdrawal)');
    DBMS_OUTPUT.PUT_LINE('============================================================');
    INSERT INTO Transactions (TransactionID, AccountID, TransactionDate, Amount, TransactionType)
    VALUES (901, 1001, SYSDATE, 10000.00, 'Withdrawal');
EXCEPTION
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('Test 1 Caught Exception (Expected): ' || SQLERRM);
END;
/

-- Test Case 2: Attempting negative deposit (-$100.00)
DECLARE
BEGIN
    DBMS_OUTPUT.PUT_LINE('============================================================');
    DBMS_OUTPUT.PUT_LINE('Test 2: Attempting Negative Deposit (-$100.00)');
    DBMS_OUTPUT.PUT_LINE('============================================================');
    INSERT INTO Transactions (TransactionID, AccountID, TransactionDate, Amount, TransactionType)
    VALUES (902, 1001, SYSDATE, -100.00, 'Deposit');
EXCEPTION
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('Test 2 Caught Exception (Expected): ' || SQLERRM);
END;
/

-- Test Case 3: Attempting valid deposit (should succeed)
DECLARE
BEGIN
    DBMS_OUTPUT.PUT_LINE('============================================================');
    DBMS_OUTPUT.PUT_LINE('Test 3: Attempting Valid Deposit ($200.00)');
    DBMS_OUTPUT.PUT_LINE('============================================================');
    INSERT INTO Transactions (TransactionID, AccountID, TransactionDate, Amount, TransactionType)
    VALUES (903, 1001, SYSDATE, 200.00, 'Deposit');
    DBMS_OUTPUT.PUT_LINE('Test 3 Success: Transaction inserted successfully.');
    ROLLBACK; -- Keep data clean
EXCEPTION
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('Test 3 Failed: ' || SQLERRM);
END;
/
