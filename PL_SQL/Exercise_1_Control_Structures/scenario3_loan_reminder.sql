-- =========================================================
-- ORACLE PL/SQL EXERCISE 1: CONTROL STRUCTURES
-- FILE: scenario3_loan_reminder.sql
-- PURPOSE: Find loans due within the next 30 days and print
--          a professional reminder message for each.
-- =========================================================

-- Enable server output to display messages in SQL Developer / SQL*Plus
SET SERVEROUTPUT ON;

DECLARE
    -- 1. Explicit Cursor to fetch customers and loans ending in the next 30 days
    -- Joined Customers and Loans on CustomerID
    CURSOR c_due_loans IS
        SELECT c.Name AS CustomerName,
               l.LoanID,
               l.LoanAmount,
               l.EndDate
        FROM Customers c
        JOIN Loans l ON c.CustomerID = l.CustomerID
        WHERE l.EndDate BETWEEN SYSDATE AND (SYSDATE + 30);
        
    -- Record variable to hold cursor data
    v_loan_rec c_due_loans%ROWTYPE;
    
    -- Variable for calculated remaining days
    v_remaining_days NUMBER;
    v_count NUMBER := 0;

BEGIN
    DBMS_OUTPUT.PUT_LINE('============================================================');
    DBMS_OUTPUT.PUT_LINE('               LOAN REPAYMENT REMINDERS (DUE IN 30 DAYS)    ');
    DBMS_OUTPUT.PUT_LINE('============================================================');
    
    -- 2. Open the explicit cursor
    OPEN c_due_loans;
    
    -- 3. Loop through the records
    LOOP
        -- Fetch current row into record variable
        FETCH c_due_loans INTO v_loan_rec;
        
        -- Exit condition
        EXIT WHEN c_due_loans%NOTFOUND;
        
        -- Increment found count
        v_count := v_count + 1;
        
        -- 4. Calculate remaining days using date arithmetic (EndDate - SYSDATE)
        -- Using CEIL to round up to the nearest whole day
        v_remaining_days := CEIL(v_loan_rec.EndDate - SYSDATE);
        
        -- 5. Print a professional reminder message
        DBMS_OUTPUT.PUT_LINE('Reminder #' || v_count);
        DBMS_OUTPUT.PUT_LINE('Customer Name : ' || v_loan_rec.CustomerName);
        DBMS_OUTPUT.PUT_LINE('Loan ID       : ' || v_loan_rec.LoanID);
        DBMS_OUTPUT.PUT_LINE('Loan Amount   : ' || TO_CHAR(v_loan_rec.LoanAmount, '$999,999.00'));
        DBMS_OUTPUT.PUT_LINE('Maturity Date : ' || TO_CHAR(v_loan_rec.EndDate, 'YYYY-MM-DD'));
        DBMS_OUTPUT.PUT_LINE('Days Remaining: ' || v_remaining_days || ' day(s)');
        DBMS_OUTPUT.PUT_LINE('Reminder Msg  : Dear ' || v_loan_rec.CustomerName || ', this is a friendly reminder ');
        DBMS_OUTPUT.PUT_LINE('                that your outstanding loan of ' || TO_CHAR(v_loan_rec.LoanAmount, '$999,999.00'));
        DBMS_OUTPUT.PUT_LINE('                is due for full repayment on ' || TO_CHAR(v_loan_rec.EndDate, 'YYYY-MM-DD') || '.');
        DBMS_OUTPUT.PUT_LINE('                Please ensure adequate funds are available.');
        DBMS_OUTPUT.PUT_LINE('------------------------------------------------------------');
    END LOOP;
    
    -- 6. Close the cursor
    CLOSE c_due_loans;
    
    -- Print summary
    IF v_count = 0 THEN
        DBMS_OUTPUT.PUT_LINE('No loans are due within the next 30 days.');
    ELSE
        DBMS_OUTPUT.PUT_LINE('Total reminders generated: ' || v_count);
    END IF;
    DBMS_OUTPUT.PUT_LINE('============================================================');

EXCEPTION
    WHEN OTHERS THEN
        -- Handle exceptions gracefully
        IF c_due_loans%ISOPEN THEN
            CLOSE c_due_loans;
        END IF;
        DBMS_OUTPUT.PUT_LINE('ERROR: An error occurred while generating loan reminders.');
        DBMS_OUTPUT.PUT_LINE('Error Message: ' || SQLERRM);
END;
/
