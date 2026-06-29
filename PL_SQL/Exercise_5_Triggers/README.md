# Exercise 5: Triggers - Reference Guide

## Objective
This exercise covers the creation and validation of Oracle database triggers to enforce automated schema actions (such as timestamp logging, transaction auditing, and business-rule validation constraints).

---

## Folder Structure
```text
PL_SQL/
└── Exercise_5_Triggers/
      ├── schema.sql
      ├── sample_data.sql
      ├── UpdateCustomerLastModified.sql
      ├── LogTransaction.sql
      ├── CheckTransactionRules.sql
      └── README.md
```

---

## Concepts Used
* **Database Triggers**: Stored PL/SQL programs compiled on the server and fired automatically by the database in response to specific events (DML/DDL).
* **Trigger Timing**: Defined `BEFORE` and `AFTER` timings to intercept database operations.
* **Row-Level Triggers**: Used `FOR EACH ROW` to inspect and modify column values on individual rows using `:new` and `:old` binders.
* **Audit Trails**: Built custom tables and triggers to capture user and session footprints during transactions.
* **Integrity Validation**: Bypassed database checks using custom procedural exceptions in triggers to raise ORA-20000+ errors.

---

## Advantages of Triggers
1. **Automated Auditing**: Capture data alterations without modifying client-side application queries.
2. **Security & Validation**: Prevent invalid DML operations at the database layer.
3. **Data Integrity**: Automatically update timestamps and calculated fields.

---

## File-by-File Documentation

### 1. schema.sql
Declares core tables and the `AuditLog` table to support transaction log auditing.
* **Problem & Objective**: Setup schemas and sequences for trigger auditing.
* **Complete Code**: See [schema.sql](file:///c:/Users/anubh/OneDrive/Desktop/kranti/cognizant/myassests/-Cognizant-Java-FSE/PL_SQL/Exercise_5_Triggers/schema.sql).

---

### 2. sample_data.sql
Seeds bank customer and account records.
* **Complete Code**: See [sample_data.sql](file:///c:/Users/anubh/OneDrive/Desktop/kranti/cognizant/myassests/-Cognizant-Java-FSE/PL_SQL/Exercise_5_Triggers/sample_data.sql).

---

### 3. UpdateCustomerLastModified.sql

#### Problem Statement
Automatically keep track of when a customer's record was last changed by updating the `LastUpdate` timestamp.

#### Objective
Create a `BEFORE UPDATE FOR EACH ROW` trigger on the `Customers` table to set `LastUpdate = SYSDATE` automatically.

#### Complete Oracle PL/SQL Code
```sql
CREATE OR REPLACE TRIGGER UpdateCustomerLastModified
BEFORE UPDATE ON Customers
FOR EACH ROW
BEGIN
    :new.LastUpdate := SYSDATE;
END;
/
```

#### Detailed Line-by-Line Explanation
* **Line 1**: Defines a trigger named `UpdateCustomerLastModified`.
* **Line 2**: Tells the database to fire before any `UPDATE` query targets the `Customers` table.
* **Line 3**: Specifying `FOR EACH ROW` configures it as a row-level trigger, exposing the `:new` and `:old` binders.
* **Line 5**: Sets the incoming row's `LastUpdate` to `SYSDATE` (current database time) before it is committed.

#### Sample Execution
```sql
UPDATE Customers SET Name = 'John H. Doe' WHERE CustomerID = 1;
SELECT LastUpdate FROM Customers WHERE CustomerID = 1;
```

#### Viva & Interview Questions
1. **What is the difference between `:new` and `:old`?**
   - `:old` contains values before the update.
   - `:new` contains values proposed by the update.
2. **Can you modify `:new` fields in an `AFTER UPDATE` trigger?**
   No. `:new` values can be modified only in `BEFORE` triggers, since the data is already written to the database in `AFTER` triggers.

---

### 4. LogTransaction.sql

#### Problem Statement
Maintain an audit trail of all transactions processed in the bank.

#### Objective
Implement an `AFTER INSERT FOR EACH ROW` trigger on the `Transactions` table to write audit details to the `AuditLog` table using sequence numbers, built-in stamps, and active session user details.

#### Complete Oracle PL/SQL Code
```sql
CREATE OR REPLACE TRIGGER LogTransaction
AFTER INSERT ON Transactions
FOR EACH ROW
BEGIN
    INSERT INTO AuditLog (AuditID, TransactionID, Action, ActionDate, ActionUser)
    VALUES (
        seq_audit_log.NEXTVAL, 
        :new.TransactionID, 
        'TRANSACTION RECORDED: ' || :new.TransactionType, 
        SYSDATE, 
        USER
    );
END;
/
```

#### Detailed Line-by-Line Explanation
* **Line 2**: Fired after any transaction record is successfully added.
* **Line 6**: Automatically queries the next sequence number `seq_audit_log.NEXTVAL`.
* **Line 7**: Captures the newly generated Transaction ID via `:new.TransactionID`.
* **Line 9**: Uses built-in system variables `SYSDATE` and `USER` to log the date and execution user.

#### Expected Output
Inserting a transaction yields:
```text
Audit record created automatically in AuditLog table.
```

---

### 5. CheckTransactionRules.sql

#### Problem Statement
Verify that transactions follow safety rules (withdrawals cannot exceed balances and deposits must be positive).

#### Objective
Write a `BEFORE INSERT FOR EACH ROW` trigger on `Transactions` to query balances, check conditions, and reject invalid statements using `RAISE_APPLICATION_ERROR`.

#### Complete Oracle PL/SQL Code
```sql
CREATE OR REPLACE TRIGGER CheckTransactionRules
BEFORE INSERT ON Transactions
FOR EACH ROW
DECLARE
    v_balance NUMBER(15, 2);
BEGIN
    IF :new.Amount <= 0 THEN
        RAISE_APPLICATION_ERROR(
            -20021, 
            'Transaction Failed: Transaction amount must be positive.'
        );
    END IF;
    
    IF :new.TransactionType = 'Withdrawal' THEN
        SELECT Balance INTO v_balance FROM Accounts WHERE AccountID = :new.AccountID;
        
        IF :new.Amount > v_balance THEN
            RAISE_APPLICATION_ERROR(
                -20022, 
                'Transaction Failed: Insufficient funds. Available: ' || TO_CHAR(v_balance, '$99,999.00')
            );
        END IF;
    END IF;
END;
/
```

#### Detailed Line-by-Line Explanation
* **Line 11**: Raises ORA-20021 if transaction amount is zero or negative.
* **Line 19**: Queries balance of Account ID being modified.
* **Line 22–27**: Compares withdrawal request against balance; throws ORA-20022 if it exceeds it.

#### Sample Input / Output
* **Input**: Withdrawal of $10,000 from account with $5,000.
* **Output**: `ORA-20022: Transaction Failed: Insufficient funds.`

---

## Conclusion
Database triggers are powerful for implementing auditing, validation, and auto-modification of rows. Ensuring that triggers handle exceptions, use row-level variables, and validate boundary rules helps secure the database layer.
