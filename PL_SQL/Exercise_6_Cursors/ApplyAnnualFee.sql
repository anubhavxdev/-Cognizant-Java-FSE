-- =========================================================
-- ORACLE PL/SQL EXERCISE 6: CURSORS
-- FILE: ApplyAnnualFee.sql
-- PURPOSE: Deducts an annual maintenance fee ($50.00) from all
--          accounts, using an explicit cursor with WHERE CURRENT OF.
-- =========================================================

SET SERVEROUTPUT ON;

DECLARE
    -- 1. Explicit Cursor to lock accounts for transactional update
    CURSOR c_accounts IS
        SELECT AccountID, Balance
        FROM Accounts
        FOR UPDATE OF Balance;
        
    v_acc_rec c_accounts%ROWTYPE;
    
    -- Constant for the annual maintenance fee
    v_fee CONSTANT NUMBER(5, 2) := 50.00;
    
    v_new_balance NUMBER(15, 2);
    v_count NUMBER := 0;

BEGIN
    DBMS_OUTPUT.PUT_LINE('============================================================');
    DBMS_OUTPUT.PUT_LINE('          APPLYING ANNUAL MAINTENANCE FEE ($50.00)           ');
    DBMS_OUTPUT.PUT_LINE('============================================================');
    
    -- Open the explicit cursor
    OPEN c_accounts;
    
    -- Loop to fetch and process rows
    LOOP
        FETCH c_accounts INTO v_acc_rec;
        EXIT WHEN c_accounts%NOTFOUND;
        
        -- Calculate the new balance
        v_new_balance := v_acc_rec.Balance - v_fee;
        
        -- Business rule: Ensure balance does not drop below 0
        IF v_new_balance < 0 THEN
            v_new_balance := 0;
        END IF;
        
        -- Update the account balance using the WHERE CURRENT OF cursor pointer
        UPDATE Accounts
        SET Balance = v_new_balance,
            LastUpdate = SYSDATE
        WHERE CURRENT OF c_accounts;
        
        -- Increment count
        v_count := v_count + 1;
        
        -- Print account details
        DBMS_OUTPUT.PUT_LINE('Account ID : ' || v_acc_rec.AccountID);
        DBMS_OUTPUT.PUT_LINE('Old Balance: ' || TO_CHAR(v_acc_rec.Balance, '$99,999.00'));
        DBMS_OUTPUT.PUT_LINE('Fee Applied: ' || TO_CHAR(v_fee, '$99.00'));
        DBMS_OUTPUT.PUT_LINE('New Balance: ' || TO_CHAR(v_new_balance, '$99,999.00'));
        DBMS_OUTPUT.PUT_LINE('------------------------------------------------------------');
    END LOOP;
    
    -- Close the cursor
    CLOSE c_accounts;
    
    -- Commit the transaction to save changes permanently
    COMMIT;
    
    DBMS_OUTPUT.PUT_LINE('Annual fee deduction completed. Total accounts processed: ' || v_count);
    DBMS_OUTPUT.PUT_LINE('============================================================');

EXCEPTION
    WHEN OTHERS THEN
        -- Revert changes on error and close cursor if open
        IF c_accounts%ISOPEN THEN
            CLOSE c_accounts;
        END IF;
        ROLLBACK;
        DBMS_OUTPUT.PUT_LINE('ERROR: Process aborted due to database error. Rollback executed.');
        DBMS_OUTPUT.PUT_LINE('Error Message: ' || SQLERRM);
END;
/
