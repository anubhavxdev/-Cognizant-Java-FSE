-- =========================================================
-- ORACLE PL/SQL EXERCISE 6: CURSORS
-- FILE: UpdateLoanInterestRates.sql
-- PURPOSE: Updates interest rates on all active loans according 
--          to the new policy using an explicit cursor with row-level locking.
-- =========================================================

SET SERVEROUTPUT ON;

DECLARE
    -- 1. Explicit Cursor to fetch active loans and associate them with customer names
    -- Locks the InterestRate column of the Loans table for updates
    CURSOR c_loans IS
        SELECT l.LoanID,
               l.LoanAmount,
               l.InterestRate AS OldRate,
               c.Name AS CustomerName
        FROM Loans l
        JOIN Customers c ON l.CustomerID = c.CustomerID
        FOR UPDATE OF l.InterestRate;
        
    -- Record variable to hold cursor data
    v_loan_rec c_loans%ROWTYPE;
    
    -- Variable to hold new interest rate
    v_new_rate NUMBER(5, 2);
    v_count NUMBER := 0;

BEGIN
    DBMS_OUTPUT.PUT_LINE('============================================================');
    DBMS_OUTPUT.PUT_LINE('          UPDATING LOAN INTEREST RATES (POLICY CHANGE)      ');
    DBMS_OUTPUT.PUT_LINE('============================================================');
    
    -- Open the explicit cursor
    OPEN c_loans;
    
    -- Loop to fetch and update records
    LOOP
        FETCH c_loans INTO v_loan_rec;
        EXIT WHEN c_loans%NOTFOUND;
        
        -- POLICY RULES:
        -- - High-value loans (LoanAmount > $40,000) receive a 0.50% interest rate reduction.
        -- - Smaller loans (LoanAmount <= $40,000) receive a 0.25% interest rate reduction.
        IF v_loan_rec.LoanAmount > 40000.00 THEN
            v_new_rate := v_loan_rec.OldRate - 0.50;
        ELSE
            v_new_rate := v_loan_rec.OldRate - 0.25;
        END IF;
        
        -- Safeguard: Ensure interest rate cannot be negative
        IF v_new_rate < 0 THEN
            v_new_rate := 0.00;
        END IF;
        
        -- Update the Loans table row currently pointed to by the cursor
        UPDATE Loans
        SET InterestRate = v_new_rate
        WHERE CURRENT OF c_loans;
        
        -- Increment processed count
        v_count := v_count + 1;
        
        -- Display loan details
        DBMS_OUTPUT.PUT_LINE('Loan ID      : ' || v_loan_rec.LoanID);
        DBMS_OUTPUT.PUT_LINE('Customer Name: ' || v_loan_rec.CustomerName);
        DBMS_OUTPUT.PUT_LINE('Loan Amount  : ' || TO_CHAR(v_loan_rec.LoanAmount, '$999,999.00'));
        DBMS_OUTPUT.PUT_LINE('Old Rate     : ' || TO_CHAR(v_loan_rec.OldRate, '99.99') || '%');
        DBMS_OUTPUT.PUT_LINE('New Rate     : ' || TO_CHAR(v_new_rate, '99.99') || '%');
        DBMS_OUTPUT.PUT_LINE('------------------------------------------------------------');
    END LOOP;
    
    -- Close the cursor
    CLOSE c_loans;
    
    -- Commit the transaction to save changes permanently
    COMMIT;
    
    DBMS_OUTPUT.PUT_LINE('Loan interest rates updated successfully. Total records: ' || v_count);
    DBMS_OUTPUT.PUT_LINE('============================================================');

EXCEPTION
    WHEN OTHERS THEN
        -- Revert changes on error and close cursor if open
        IF c_loans%ISOPEN THEN
            CLOSE c_loans;
        END IF;
        ROLLBACK;
        DBMS_OUTPUT.PUT_LINE('ERROR: Loan interest rate update aborted. Rollback executed.');
        DBMS_OUTPUT.PUT_LINE('Error Message: ' || SQLERRM);
END;
/
