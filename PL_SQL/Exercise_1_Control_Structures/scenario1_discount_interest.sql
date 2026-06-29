-- =========================================================
-- ORACLE PL/SQL EXERCISE 1: CONTROL STRUCTURES
-- FILE: scenario1_discount_interest.sql
-- PURPOSE: Apply a 1% interest rate discount to loans of senior 
--          citizens (customers aged above 60 years).
-- =========================================================

-- Enable server output to display messages in SQL Developer / SQL*Plus
SET SERVEROUTPUT ON;

DECLARE
    -- 1. Explicit Cursor to fetch customers and their associated loans
    CURSOR c_customer_loans IS
        SELECT c.CustomerID,
               c.Name AS CustomerName,
               c.DOB,
               l.LoanID,
               l.InterestRate AS OldInterestRate
        FROM Customers c
        JOIN Loans l ON c.CustomerID = l.CustomerID;
        
    -- Record variable to hold cursor data
    v_loan_rec c_customer_loans%ROWTYPE;
    
    -- Variables for calculations
    v_age NUMBER;
    v_new_interest_rate NUMBER(5,2);
    v_updated_count NUMBER := 0;

BEGIN
    DBMS_OUTPUT.PUT_LINE('------------------------------------------------------------');
    DBMS_OUTPUT.PUT_LINE('STARTING INTEREST RATE UPDATE FOR SENIOR CITIZENS (> 60 YEARS)');
    DBMS_OUTPUT.PUT_LINE('------------------------------------------------------------');

    -- 2. Open the explicit cursor
    OPEN c_customer_loans;
    
    -- 3. Loop through each record fetched by the cursor
    LOOP
        -- Fetch current row into record variable
        FETCH c_customer_loans INTO v_loan_rec;
        
        -- Exit loop when no more records are found
        EXIT WHEN c_customer_loans%NOTFOUND;
        
        -- 4. Calculate customer age in years using MONTHS_BETWEEN
        v_age := FLOOR(MONTHS_BETWEEN(SYSDATE, v_loan_rec.DOB) / 12);
        
        -- 5. Conditional check: Verify if the customer is above 60 years old
        IF v_age > 60 THEN
            -- Calculate new interest rate (reduce by 1%)
            v_new_interest_rate := v_loan_rec.OldInterestRate - 1;
            
            -- Ensure interest rate does not drop below 0%
            IF v_new_interest_rate < 0 THEN
                v_new_interest_rate := 0;
            END IF;
            
            -- 6. Update the Loans table with the new interest rate
            UPDATE Loans
            SET InterestRate = v_new_interest_rate
            WHERE LoanID = v_loan_rec.LoanID;
            
            -- Increment update counter
            v_updated_count := v_updated_count + 1;
            
            -- 7. Print customer and interest rate details
            DBMS_OUTPUT.PUT_LINE('Customer Name    : ' || v_loan_rec.CustomerName);
            DBMS_OUTPUT.PUT_LINE('Current Age      : ' || v_age || ' years');
            DBMS_OUTPUT.PUT_LINE('Old Interest Rate: ' || TO_CHAR(v_loan_rec.OldInterestRate, '99.99') || '%');
            DBMS_OUTPUT.PUT_LINE('New Interest Rate: ' || TO_CHAR(v_new_interest_rate, '99.99') || '%');
            DBMS_OUTPUT.PUT_LINE('Status           : Interest Updated');
            DBMS_OUTPUT.PUT_LINE('------------------------------------------------------------');
        END IF;
    END LOOP;
    
    -- 8. Close the cursor to free system resources
    CLOSE c_customer_loans;
    
    -- Commit the changes to make them permanent
    COMMIT;
    
    -- Print summary
    DBMS_OUTPUT.PUT_LINE('Process completed. Total loans updated: ' || v_updated_count);
    DBMS_OUTPUT.PUT_LINE('------------------------------------------------------------');

EXCEPTION
    WHEN OTHERS THEN
        -- Handle unexpected errors gracefully
        IF c_customer_loans%ISOPEN THEN
            CLOSE c_customer_loans;
        END IF;
        ROLLBACK;
        DBMS_OUTPUT.PUT_LINE('ERROR: An unexpected error occurred. transaction rolled back.');
        DBMS_OUTPUT.PUT_LINE('Error Message: ' || SQLERRM);
END;
/
