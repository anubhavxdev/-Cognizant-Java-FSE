-- =========================================================
-- ORACLE PL/SQL EXERCISE 5: TRIGGERS
-- FILE: LogTransaction.sql
-- PURPOSE: Implements an AFTER INSERT trigger on the Transactions table
--          to log transaction execution records to the AuditLog table.
-- =========================================================

SET SERVEROUTPUT ON;

-- ---------------------------------------------------------
-- 1. Trigger Definition
-- ---------------------------------------------------------
CREATE OR REPLACE TRIGGER LogTransaction
AFTER INSERT ON Transactions
FOR EACH ROW
BEGIN
    -- Log the newly inserted Transaction into the AuditLog table.
    -- Uses the seq_audit_log sequence for the PK,
    -- :new.TransactionID to capture the inserted transaction reference,
    -- and system variables for the action description, timestamp, and active user.
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

-- ---------------------------------------------------------
-- 2. Sample Execution & Test
-- ---------------------------------------------------------
PROMPT TESTING AUDIT LOGGER TRIGGER:

-- Step A: Show current AuditLog state (Should be empty initially)
PROMPT Current entries in AuditLog:
SELECT * FROM AuditLog;

-- Step B: Insert a new Transaction to fire the trigger
PROMPT Inserting transaction into Transactions table for Account 1001...
INSERT INTO Transactions (TransactionID, AccountID, TransactionDate, Amount, TransactionType)
VALUES (501, 1001, SYSDATE, 250.00, 'Deposit');

-- Step C: Verify AuditLog (Should show the logged entry)
PROMPT Checking AuditLog entries (Audit record should be created automatically):
SELECT AuditID, TransactionID, Action, ActionDate, ActionUser 
FROM AuditLog;

-- Rollback transaction to keep data clean
ROLLBACK;
