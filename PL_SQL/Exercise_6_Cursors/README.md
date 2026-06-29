# Exercise 6: Cursors - Reference Guide

## Objective
This exercise covers the use of explicit cursors in Oracle PL/SQL to process multiple records row-by-row. It covers declaration, execution steps (`OPEN`, `FETCH`, `CLOSE`), lock tracking (`FOR UPDATE`), and direct updates (`WHERE CURRENT OF`).

---

## Folder Structure
```text
PL_SQL/
└── Exercise_6_Cursors/
      ├── schema.sql
      ├── sample_data.sql
      ├── GenerateMonthlyStatements.sql
      ├── ApplyAnnualFee.sql
      ├── UpdateLoanInterestRates.sql
      └── README.md
```

---

## Concepts Used
* **Explicit Cursor**: Programmer-declared cursors that manage a multi-row query workspace.
* **Cursor Life Cycle**: Managed via `OPEN` (allocates resources and executes query), `FETCH` (loads current row), and `CLOSE` (releases locks/memory).
* **Cursor Attributes**: Checked cursor state using `%FOUND`, `%NOTFOUND`, `%ISOPEN`, and `%ROWCOUNT`.
* **Row Locking (`FOR UPDATE`)**: Prevents other sessions from modifying fetched rows during transaction steps.
* **WHERE CURRENT OF**: Directs updates to the physical `ROWID` pointed to by the active cursor record.

---

## Advantages of Explicit Cursors
1. **Multi-Row Processing**: Allows handling multiple query rows sequentially within PL/SQL.
2. **Resource Management**: Provides programmatic control over opening and closing to limit server resource consumption.
3. **Transaction Safety**: Binds locked rows directly to cursor loops, preventing transaction conflicts.

---

## File-by-File Documentation

### 1. schema.sql
Declares core relational tables (`Customers`, `Accounts`, `Transactions`, `Loans`, `Employees`).
* **Problem & Objective**: Setup tables with constraints.
* **Complete Code**: See [schema.sql](file:///c:/Users/anubh/OneDrive/Desktop/kranti/cognizant/myassests/-Cognizant-Java-FSE/PL_SQL/Exercise_6_Cursors/schema.sql).

---

### 2. sample_data.sql
Seeds bank customer, account, transaction, and loan records.
* **Complete Code**: See [sample_data.sql](file:///c:/Users/anubh/OneDrive/Desktop/kranti/cognizant/myassests/-Cognizant-Java-FSE/PL_SQL/Exercise_6_Cursors/sample_data.sql).

---

### 3. GenerateMonthlyStatements.sql

#### Problem Statement
Generate transaction statements for all accounts covering transactions performed in the current month.

#### Objective
Open an explicit cursor joining `Customers`, `Accounts`, and `Transactions` filtered by the current month, loop through records, print details, and track transaction counts.

#### Complete Oracle PL/SQL Code
```sql
DECLARE
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
        WHERE TRUNC(t.TransactionDate, 'MM') = TRUNC(SYSDATE, 'MM')
        ORDER BY c.Name, a.AccountID, t.TransactionDate;
        
    v_tx_rec c_monthly_statements%ROWTYPE;
    v_total_transactions NUMBER := 0;
BEGIN
    OPEN c_monthly_statements;
    LOOP
        FETCH c_monthly_statements INTO v_tx_rec;
        EXIT WHEN c_monthly_statements%NOTFOUND;
        
        v_total_transactions := v_total_transactions + 1;
        
        DBMS_OUTPUT.PUT_LINE('Record Number   : #' || v_total_transactions);
        DBMS_OUTPUT.PUT_LINE('Customer Name   : ' || v_tx_rec.CustomerName);
        DBMS_OUTPUT.PUT_LINE('Account ID      : ' || v_tx_rec.AccountID);
        DBMS_OUTPUT.PUT_LINE('Transaction ID  : ' || v_tx_rec.TransactionID);
        DBMS_OUTPUT.PUT_LINE('Amount          : ' || TO_CHAR(v_tx_rec.Amount, '$99,999.00'));
        DBMS_OUTPUT.PUT_LINE('------------------------------------------------------------');
    END LOOP;
    CLOSE c_monthly_statements;
    
    DBMS_OUTPUT.PUT_LINE('Total Report Transactions processed: ' || v_total_transactions);
END;
/
```

#### Detailed Line-by-Line Explanation
* **Lines 2–11**: Declares `c_monthly_statements` cursor. Employs `TRUNC(t.TransactionDate, 'MM') = TRUNC(SYSDATE, 'MM')` to retrieve records from only the current calendar month.
* **Line 16**: Opens the explicit cursor.
* **Line 19**: Fetches current row into `v_tx_rec`.
* **Line 20**: Evaluates `%NOTFOUND` attribute to exit when rows are exhausted.
* **Lines 24–28**: Displays formatted output using `DBMS_OUTPUT.PUT_LINE`.
* **Line 31**: Closes cursor.

#### Sample Execution Output
```text
============================================================
               MONTHLY TRANSACTION REPORT                   
               Month: JUNE      2026
============================================================
Record Number   : #1
Customer Name   : Jane Smith
Account ID      : 1002
Transaction ID  : 2003
Amount          :       $150.00
------------------------------------------------------------
Record Number   : #2
Customer Name   : John Doe
Account ID      : 1001
Transaction ID  : 2001
Amount          :       $500.00
------------------------------------------------------------
Total Report Transactions processed: 4
============================================================
```

#### Viva & Interview Questions
1. **What is a `%ROWTYPE` record variable?**
   A composite variable whose structure matches the schema returned by a cursor or table. It simplifies column variable declarations.
2. **What is the difference between explicit and implicit cursors?**
   Implicit cursors are created by Oracle automatically for all DML and single-row SELECT INTO statements. Explicit cursors are defined by the programmer for handling multi-row queries.

---

### 4. ApplyAnnualFee.sql

#### Problem Statement
Deduct a flat $50.00 annual maintenance fee from all customer accounts.

#### Objective
Use an explicit cursor with `FOR UPDATE OF` to lock accounts, deduct the fee, prevent negative balances, use `WHERE CURRENT OF` to update rows, print records, and commit.

#### Complete Oracle PL/SQL Code
```sql
DECLARE
    CURSOR c_accounts IS
        SELECT AccountID, Balance
        FROM Accounts
        FOR UPDATE OF Balance;
        
    v_acc_rec c_accounts%ROWTYPE;
    v_fee CONSTANT NUMBER(5, 2) := 50.00;
    v_new_balance NUMBER(15, 2);
    v_count NUMBER := 0;
BEGIN
    OPEN c_accounts;
    LOOP
        FETCH c_accounts INTO v_acc_rec;
        EXIT WHEN c_accounts%NOTFOUND;
        
        v_new_balance := v_acc_rec.Balance - v_fee;
        IF v_new_balance < 0 THEN
            v_new_balance := 0;
        END IF;
        
        UPDATE Accounts
        SET Balance = v_new_balance, LastUpdate = SYSDATE
        WHERE CURRENT OF c_accounts;
        
        v_count := v_count + 1;
        DBMS_OUTPUT.PUT_LINE('Account ID : ' || v_acc_rec.AccountID);
        DBMS_OUTPUT.PUT_LINE('Old Balance: ' || TO_CHAR(v_acc_rec.Balance, '$99,999.00'));
        DBMS_OUTPUT.PUT_LINE('New Balance: ' || TO_CHAR(v_new_balance, '$99,999.00'));
    END LOOP;
    CLOSE c_accounts;
    COMMIT;
END;
/
```

#### Detailed Line-by-Line Explanation
* **Line 4**: Cursor declared with `FOR UPDATE OF Balance`, locking the `Balance` column for all returned accounts.
* **Line 17**: Calculates fee deduction.
* **Line 23**: Applies update `WHERE CURRENT OF c_accounts` using Oracle's row identifier.
* **Line 31**: Commits the updates.

---

### 5. UpdateLoanInterestRates.sql

#### Problem Statement
Update the interest rates of active loans based on new policy guidelines.

#### Objective
Apply a 0.5% rate discount for loans above $40,000, and a 0.25% discount for smaller loans, using a cursor that joins `Loans` and `Customers`.

#### Complete Oracle PL/SQL Code
```sql
DECLARE
    CURSOR c_loans IS
        SELECT l.LoanID,
               l.LoanAmount,
               l.InterestRate AS OldRate,
               c.Name AS CustomerName
        FROM Loans l
        JOIN Customers c ON l.CustomerID = c.CustomerID
        FOR UPDATE OF l.InterestRate;
        
    v_loan_rec c_loans%ROWTYPE;
    v_new_rate NUMBER(5, 2);
    v_count NUMBER := 0;
BEGIN
    OPEN c_loans;
    LOOP
        FETCH c_loans INTO v_loan_rec;
        EXIT WHEN c_loans%NOTFOUND;
        
        IF v_loan_rec.LoanAmount > 40000.00 THEN
            v_new_rate := v_loan_rec.OldRate - 0.50;
        ELSE
            v_new_rate := v_loan_rec.OldRate - 0.25;
        END IF;
        
        IF v_new_rate < 0 THEN
            v_new_rate := 0.00;
        END IF;
        
        UPDATE Loans SET InterestRate = v_new_rate WHERE CURRENT OF c_loans;
        v_count := v_count + 1;
        DBMS_OUTPUT.PUT_LINE('Loan ID      : ' || v_loan_rec.LoanID);
        DBMS_OUTPUT.PUT_LINE('Old Rate     : ' || v_loan_rec.OldRate || '%');
        DBMS_OUTPUT.PUT_LINE('New Rate     : ' || v_new_rate || '%');
    END LOOP;
    CLOSE c_loans;
    COMMIT;
END;
/
```

#### Detailed Line-by-Line Explanation
* **Lines 2–8**: Declares cursor joining `Loans` and `Customers` with `FOR UPDATE OF l.InterestRate`.
* **Lines 20–24**: Implements discount rules: 0.5% rate reduction if `LoanAmount > 40000`, else 0.25%.
* **Line 30**: Performs update using `WHERE CURRENT OF c_loans`.
* **Line 38**: Commits updates.

---

## Conclusion
Explicit cursors provide granular control when processing multi-row transactions. Combining cursor attributes, locking mechanisms, and `WHERE CURRENT OF` statements enables secure and efficient batch processing.
