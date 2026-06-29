-- =========================================================
-- ORACLE PL/SQL EXERCISE 6: CURSORS
-- FILE: GenerateMonthlyStatements.sql
-- PURPOSE: Generates monthly statements for all customers with
--          transactions in the current month using an explicit cursor.
-- =========================================================

SET SERVEROUTPUT ON;

DECLARE
    -- Explicit Cursor to retrieve transaction details for the current month
    CURSOR c_monthly_statements IS
        SELECT c.Name AS CustomerName,
               a.AccountID,
               t.TransactionID,
               t.TransactionDate,
               t.TransactionType,
               t.Amount
        FROM Customers c
        JOIN Accounts a ON c.CustomerID = a.CustomerID
        JOIN Transactions t ON a.AccountID = t.AccountID
        -- Filter for transactions in the current month and year
        WHERE TRUNC(t.TransactionDate, 'MM') = TRUNC(SYSDATE, 'MM')
        ORDER BY c.Name, a.AccountID, t.TransactionDate;
        
    -- Record variable to hold cursor details
    v_tx_rec c_monthly_statements%ROWTYPE;
    
    -- Counter to keep track of total report transactions
    v_total_transactions NUMBER := 0;

BEGIN
    DBMS_OUTPUT.PUT_LINE('============================================================');
    DBMS_OUTPUT.PUT_LINE('               MONTHLY TRANSACTION REPORT                   ');
    DBMS_OUTPUT.PUT_LINE('               Month: ' || TO_CHAR(SYSDATE, 'MONTH YYYY'));
    DBMS_OUTPUT.PUT_LINE('============================================================');
    
    -- 1. Open the explicit cursor
    OPEN c_monthly_statements;
    
    -- 2. Loop to fetch rows
    LOOP
        -- Fetch the current row into the record variable
        FETCH c_monthly_statements INTO v_tx_rec;
        
        -- Exit condition
        EXIT WHEN c_monthly_statements%NOTFOUND;
        
        -- Increment the transaction counter
        v_total_transactions := v_total_transactions + 1;
        
        -- Print transaction details
        DBMS_OUTPUT.PUT_LINE('Record Number   : #' || v_total_transactions);
        DBMS_OUTPUT.PUT_LINE('Customer Name   : ' || v_tx_rec.CustomerName);
        DBMS_OUTPUT.PUT_LINE('Account ID      : ' || v_tx_rec.AccountID);
        DBMS_OUTPUT.PUT_LINE('Transaction ID  : ' || v_tx_rec.TransactionID);
        DBMS_OUTPUT.PUT_LINE('Date & Time     : ' || TO_CHAR(v_tx_rec.TransactionDate, 'YYYY-MM-DD HH24:MI:SS'));
        DBMS_OUTPUT.PUT_LINE('Type            : ' || v_tx_rec.TransactionType);
        DBMS_OUTPUT.PUT_LINE('Amount          : ' || TO_CHAR(v_tx_rec.Amount, '$99,999.00'));
        DBMS_OUTPUT.PUT_LINE('------------------------------------------------------------');
    END LOOP;
    
    -- 3. Close the cursor
    CLOSE c_monthly_statements;
    
    -- Print summary total transactions
    DBMS_OUTPUT.PUT_LINE('Total Report Transactions processed: ' || v_total_transactions);
    DBMS_OUTPUT.PUT_LINE('============================================================');

EXCEPTION
    WHEN OTHERS THEN
        -- Handle unexpected exceptions safely
        IF c_monthly_statements%ISOPEN THEN
            CLOSE c_monthly_statements;
        END IF;
        DBMS_OUTPUT.PUT_LINE('ERROR: Failed to generate monthly statements.');
        DBMS_OUTPUT.PUT_LINE('Error Message: ' || SQLERRM);
END;
/
