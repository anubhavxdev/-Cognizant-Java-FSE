-- =========================================================
-- ORACLE PL/SQL EXERCISE 5: TRIGGERS
-- FILE: UpdateCustomerLastModified.sql
-- PURPOSE: Implements a BEFORE UPDATE trigger on the Customers table
--          to automatically set LastUpdate to SYSDATE on modification.
-- =========================================================

SET SERVEROUTPUT ON;

-- ---------------------------------------------------------
-- 1. Trigger Definition
-- ---------------------------------------------------------
CREATE OR REPLACE TRIGGER UpdateCustomerLastModified
BEFORE UPDATE ON Customers
FOR EACH ROW
BEGIN
    -- Automatically set LastUpdate to current system timestamp
    -- :new represents the new state of the row being updated
    :new.LastUpdate := SYSDATE;
END;
/

-- ---------------------------------------------------------
-- 2. Sample Execution & Test
-- ---------------------------------------------------------
PROMPT TESTING UPDATE TRIGGER:

-- Step A: Show current customer state
PROMPT Current Customer State before update:
SELECT CustomerID, Name, LastUpdate 
FROM Customers 
WHERE CustomerID = 1;

-- Wait a short duration (or just execute the update) to ensure a visible difference in timestamp
PROMPT Executing Update Statement on John Doe...
UPDATE Customers
SET Name = 'John H. Doe'
WHERE CustomerID = 1;

-- Step B: Show customer state after update to verify the trigger fired
PROMPT Current Customer State after update (LastUpdate should match current time):
SELECT CustomerID, Name, LastUpdate 
FROM Customers 
WHERE CustomerID = 1;

-- Rollback test change to keep data clean
ROLLBACK;
