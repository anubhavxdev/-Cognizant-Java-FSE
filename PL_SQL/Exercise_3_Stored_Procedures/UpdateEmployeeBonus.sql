-- =========================================================
-- ORACLE PL/SQL EXERCISE 3: STORED PROCEDURES
-- FILE: UpdateEmployeeBonus.sql
-- PURPOSE: Updates salaries of employees within a specific 
--          department by adding a bonus percentage, printing details.
-- =========================================================

SET SERVEROUTPUT ON;

-- ---------------------------------------------------------
-- 1. Stored Procedure Definition
-- ---------------------------------------------------------
CREATE OR REPLACE PROCEDURE UpdateEmployeeBonus (
    p_dept_name IN VARCHAR2,
    p_bonus_pct IN NUMBER
) IS
    -- Cursor declared with parameter reference to fetch matching employees
    CURSOR c_dept_employees IS
        SELECT EmployeeID, Name, Department, Salary
        FROM Employees
        WHERE Department = p_dept_name
        FOR UPDATE OF Salary;
        
    -- Variables for calculations
    v_bonus_amount NUMBER(15, 2);
    v_new_salary NUMBER(15, 2);
    v_processed_count NUMBER := 0;
    
    -- Custom business exception
    e_invalid_bonus EXCEPTION;

BEGIN
    DBMS_OUTPUT.PUT_LINE('============================================================');
    DBMS_OUTPUT.PUT_LINE('STARTING EMPLOYEE BONUS PROCESS');
    DBMS_OUTPUT.PUT_LINE('Target Department : ' || p_dept_name);
    DBMS_OUTPUT.PUT_LINE('Bonus Percentage  : ' || TO_CHAR(p_bonus_pct, '99.99') || '%');
    DBMS_OUTPUT.PUT_LINE('============================================================');

    -- Validate: Bonus percentage cannot be negative
    IF p_bonus_pct < 0 THEN
        RAISE e_invalid_bonus;
    END IF;

    -- Cursor FOR Loop to process departmental updates
    FOR r_emp IN c_dept_employees LOOP
        
        -- Calculate bonus and new salary
        v_bonus_amount := ROUND(r_emp.Salary * (p_bonus_pct / 100), 2);
        v_new_salary   := r_emp.Salary + v_bonus_amount;
        
        -- Update the salary in the database using WHERE CURRENT OF
        UPDATE Employees
        SET Salary = v_new_salary
        WHERE CURRENT OF c_dept_employees;
        
        -- Increment processed count
        v_processed_count := v_processed_count + 1;
        
        -- Print individual details (Old salary, bonus, new salary)
        DBMS_OUTPUT.PUT_LINE('Employee ID   : ' || r_emp.EmployeeID);
        DBMS_OUTPUT.PUT_LINE('Employee Name : ' || r_emp.Name);
        DBMS_OUTPUT.PUT_LINE('Department    : ' || r_emp.Department);
        DBMS_OUTPUT.PUT_LINE('Old Salary    : ' || TO_CHAR(r_emp.Salary, '$999,999.00'));
        DBMS_OUTPUT.PUT_LINE('Bonus Amount  : ' || TO_CHAR(v_bonus_amount, '$999,999.00'));
        DBMS_OUTPUT.PUT_LINE('New Salary    : ' || TO_CHAR(v_new_salary, '$999,999.00'));
        DBMS_OUTPUT.PUT_LINE('------------------------------------------------------------');
        
    END LOOP;

    -- Commit transaction to finalize changes
    COMMIT;
    
    DBMS_OUTPUT.PUT_LINE('Total Employees Updated: ' || v_processed_count);
    IF v_processed_count > 0 THEN
        DBMS_OUTPUT.PUT_LINE('Bonus Updated Successfully');
    ELSE
        DBMS_OUTPUT.PUT_LINE('No employees found in department: ' || p_dept_name);
    END IF;
    DBMS_OUTPUT.PUT_LINE('============================================================');

EXCEPTION
    WHEN e_invalid_bonus THEN
        ROLLBACK;
        DBMS_OUTPUT.PUT_LINE('ERROR: Bonus percentage cannot be negative.');
        
    WHEN OTHERS THEN
        ROLLBACK;
        DBMS_OUTPUT.PUT_LINE('ERROR: Failed to update employee bonus details.');
        DBMS_OUTPUT.PUT_LINE('Error Message: ' || SQLERRM);
END UpdateEmployeeBonus;
/

-- ---------------------------------------------------------
-- 2. Test Block (Demonstrates invocation)
-- ---------------------------------------------------------
-- Test Case 1: Apply 10% bonus to IT department (Alice & Bob)
BEGIN
    DBMS_OUTPUT.PUT_LINE('TEST CASE 1: Applying 10% bonus to the "IT" Department');
    UpdateEmployeeBonus('IT', 10);
END;
/

-- Test Case 2: Apply 5% bonus to HR department (Charlie)
BEGIN
    DBMS_OUTPUT.PUT_LINE('TEST CASE 2: Applying 5% bonus to the "HR" Department');
    UpdateEmployeeBonus('HR', 5);
END;
/

-- Test Case 3: Run with negative bonus percentage (Should catch validation error)
BEGIN
    DBMS_OUTPUT.PUT_LINE('TEST CASE 3: Attempting invalid negative bonus percentage');
    UpdateEmployeeBonus('IT', -5);
END;
/
