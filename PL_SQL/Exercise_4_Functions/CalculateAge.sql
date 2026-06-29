-- =========================================================
-- ORACLE PL/SQL EXERCISE 4: FUNCTIONS
-- FILE: CalculateAge.sql
-- PURPOSE: Implements a function to calculate a customer's
--          age in years based on their Date of Birth.
-- =========================================================

SET SERVEROUTPUT ON;

-- ---------------------------------------------------------
-- 1. Function Definition
-- ---------------------------------------------------------
CREATE OR REPLACE FUNCTION CalculateAge (
    p_dob IN DATE
) RETURN NUMBER IS
    v_age NUMBER;
BEGIN
    -- Handle NULL values safely as per requirements
    IF p_dob IS NULL THEN
        RETURN NULL;
    END IF;
    
    -- Calculate difference in months, divide by 12, and round down to nearest year
    v_age := FLOOR(MONTHS_BETWEEN(SYSDATE, p_dob) / 12);
    
    RETURN v_age;
EXCEPTION
    WHEN OTHERS THEN
        -- Handle unexpected exceptions gracefully
        RETURN NULL;
END CalculateAge;
/

-- ---------------------------------------------------------
-- 2. Sample Executions
-- ---------------------------------------------------------

-- Execution A: Querying the database table
PROMPT SELECT QUERY TEST:
SELECT CustomerID, Name, DOB, CalculateAge(DOB) AS Current_Age
FROM Customers;

-- Execution B: PL/SQL Block Test (including NULL test case)
PROMPT PL/SQL BLOCK TEST:
DECLARE
    v_dob1 DATE := TO_DATE('1995-08-25', 'YYYY-MM-DD');
    v_dob2 DATE := NULL;
    v_age1 NUMBER;
    v_age2 NUMBER;
BEGIN
    v_age1 := CalculateAge(v_dob1);
    v_age2 := CalculateAge(v_dob2);
    
    DBMS_OUTPUT.PUT_LINE('Test 1: DOB = 1995-08-25 -> Calculated Age = ' || NVL(TO_CHAR(v_age1), 'NULL'));
    DBMS_OUTPUT.PUT_LINE('Test 2: DOB = NULL       -> Calculated Age = ' || NVL(TO_CHAR(v_age2), 'NULL'));
END;
/
