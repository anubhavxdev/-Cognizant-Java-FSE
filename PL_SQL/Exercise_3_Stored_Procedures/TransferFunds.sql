-- =========================================================
-- ORACLE PL/SQL EXERCISE 3: STORED PROCEDURES
-- FILE: TransferFunds.sql
-- PURPOSE: Implements a database funds transfer stored procedure
--          with validations, row locking, transaction control,
--          and robust error handlers.
-- =========================================================

SET SERVEROUTPUT ON;

-- ---------------------------------------------------------
-- 1. Stored Procedure Definition
-- ---------------------------------------------------------
CREATE OR REPLACE PROCEDURE TransferFunds (
    p_source_acc_id IN NUMBER,
    p_dest_acc_id IN NUMBER,
    p_amount IN NUMBER
) IS
    -- Variable declarations
    v_source_balance NUMBER(15, 2);
    v_dest_balance NUMBER(15, 2);
    
    -- Custom business exceptions
    e_source_not_found EXCEPTION;
    e_dest_not_found EXCEPTION;
    e_insufficient_funds EXCEPTION;
    e_invalid_amount EXCEPTION;

BEGIN
    DBMS_OUTPUT.PUT_LINE('Transfer Started');
    DBMS_OUTPUT.PUT_LINE('Source: ' || p_source_acc_id || ' | Destination: ' || p_dest_acc_id);
    
    -- 1. Validate: Transfer amount must be positive
    IF p_amount <= 0 THEN
        RAISE e_invalid_amount;
    END IF;
    
    -- 2. Validate and Lock: Fetch Source Account Balance
    BEGIN
        SELECT Balance 
        INTO v_source_balance 
        FROM Accounts 
        WHERE AccountID = p_source_acc_id
        FOR UPDATE;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            RAISE e_source_not_found;
    END;
    
    -- 3. Validate and Lock: Fetch Destination Account Balance
    BEGIN
        SELECT Balance 
        INTO v_dest_balance 
        FROM Accounts 
        WHERE AccountID = p_dest_acc_id
        FOR UPDATE;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            RAISE e_dest_not_found;
    END;
    
    -- Display balances before modification
    DBMS_OUTPUT.PUT_LINE('Source Balance     : ' || TO_CHAR(v_source_balance, '$99,999.00'));
    DBMS_OUTPUT.PUT_LINE('Destination Balance: ' || TO_CHAR(v_dest_balance, '$99,999.00'));
    
    -- 4. Validate: Check sufficient funds in source account
    IF v_source_balance < p_amount THEN
        RAISE e_insufficient_funds;
    END IF;
    
    -- 5. Perform DML: Deduct from source account
    UPDATE Accounts
    SET Balance = Balance - p_amount,
        LastUpdate = SYSDATE
    WHERE AccountID = p_source_acc_id;
    
    -- 6. Perform DML: Add to destination account
    UPDATE Accounts
    SET Balance = Balance + p_amount,
        LastUpdate = SYSDATE
    WHERE AccountID = p_dest_acc_id;
    
    -- 7. Auditing: Record transactions
    INSERT INTO Transactions (TransactionID, AccountID, TransactionDate, Amount, TransactionType)
    VALUES (
        (SELECT NVL(MAX(TransactionID), 0) + 1 FROM Transactions),
        p_source_acc_id,
        SYSDATE,
        p_amount,
        'Withdrawal'
    );
    
    INSERT INTO Transactions (TransactionID, AccountID, TransactionDate, Amount, TransactionType)
    VALUES (
        (SELECT NVL(MAX(TransactionID), 0) + 1 FROM Transactions),
        p_dest_acc_id,
        SYSDATE,
        p_amount,
        'Deposit'
    );
    
    -- 8. Commit changes if transfer succeeded
    COMMIT;
    
    DBMS_OUTPUT.PUT_LINE('Amount Transferred : ' || TO_CHAR(p_amount, '$99,999.00'));
    DBMS_OUTPUT.PUT_LINE('Transfer Successful');
    DBMS_OUTPUT.PUT_LINE('------------------------------------------------------------');

EXCEPTION
    -- Custom handlers for business validation errors
    WHEN e_invalid_amount THEN
        ROLLBACK;
        DBMS_OUTPUT.PUT_LINE('Error: Transfer amount must be positive.');
        RAISE_APPLICATION_ERROR(-20011, 'Transfer Failed: Transfer amount must be positive.');
        
    WHEN e_source_not_found THEN
        ROLLBACK;
        DBMS_OUTPUT.PUT_LINE('Error: Source account ' || p_source_acc_id || ' does not exist.');
        RAISE_APPLICATION_ERROR(-20012, 'Transfer Failed: Source account does not exist.');
        
    WHEN e_dest_not_found THEN
        ROLLBACK;
        DBMS_OUTPUT.PUT_LINE('Error: Destination account ' || p_dest_acc_id || ' does not exist.');
        RAISE_APPLICATION_ERROR(-20013, 'Transfer Failed: Destination account does not exist.');
        
    WHEN e_insufficient_funds THEN
        ROLLBACK;
        DBMS_OUTPUT.PUT_LINE('Error: Insufficient balance in source account.');
        RAISE_APPLICATION_ERROR(-20014, 'Transfer Failed: Insufficient balance.');
        
    WHEN OTHERS THEN
        ROLLBACK;
        DBMS_OUTPUT.PUT_LINE('Error: ' || SQLERRM);
        RAISE_APPLICATION_ERROR(-20999, 'Transfer Failed: System error. Code: ' || SQLCODE || ' - ' || SQLERRM);
END TransferFunds;
/

-- ---------------------------------------------------------
-- 2. Test Block (Demonstrates invocation)
-- ---------------------------------------------------------
-- Test Case 1: Transfer $1,000 from Savings (1001) to Checking (1002) - Should succeed
BEGIN
    DBMS_OUTPUT.PUT_LINE('TEST CASE 1: Valid transfer of $1,000.00');
    TransferFunds(1001, 1002, 1000.00);
END;
/

-- Test Case 2: Transfer with insufficient funds ($200,000.00) - Should fail
BEGIN
    DBMS_OUTPUT.PUT_LINE('TEST CASE 2: Invalid transfer due to insufficient balance ($200,000.00)');
    TransferFunds(1001, 1002, 200000.00);
EXCEPTION
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('Caught Expected Exception: ' || SQLERRM);
END;
/

-- Test Case 3: Transfer to non-existent account (9999) - Should fail
BEGIN
    DBMS_OUTPUT.PUT_LINE('TEST CASE 3: Invalid transfer to non-existent destination account (9999)');
    TransferFunds(1001, 9999, 100.00);
EXCEPTION
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('Caught Expected Exception: ' || SQLERRM);
END;
/
