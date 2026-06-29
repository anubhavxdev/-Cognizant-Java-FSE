-- =========================================================
-- ORACLE PL/SQL EXERCISE 2: ERROR HANDLING
-- FILE: UpdateSalary.sql
-- PURPOSE: Updates an employee's salary by a given percentage
--          with validation, error handling, and transaction controls.
-- =========================================================

SET SERVEROUTPUT ON;

-- ---------------------------------------------------------
-- 1. Stored Procedure Definition
-- ---------------------------------------------------------
CREATE OR REPLACE PROCEDURE UpdateSalary (
    p_emp_id IN NUMBER,
    p_percentage IN NUMBER
) IS
    -- Declarations
    v_old_salary NUMBER(15, 2);
    v_new_salary NUMBER(15, 2);
    v_emp_name VARCHAR2(100);
    v_position VARCHAR2(50);
    v_dept VARCHAR2(50);
    
    -- Custom business exceptions
    e_invalid_percentage EXCEPTION;

BEGIN
    DBMS_OUTPUT.PUT_LINE('Salary Update process initiated for Employee ID: ' || p_emp_id);
    
    -- Validation: Percentage increase must be non-negative
    IF p_percentage < 0 THEN
        RAISE e_invalid_percentage;
    END IF;
    
    -- Fetch old salary and details.
    -- If EmployeeID does not exist, Oracle raises NO_DATA_FOUND automatically.
    SELECT Name, Position, Salary, Department
    INTO v_emp_name, v_position, v_old_salary, v_dept
    FROM Employees
    WHERE EmployeeID = p_emp_id;
    
    -- Calculate the new salary
    v_new_salary := v_old_salary * (1 + (p_percentage / 100));
    
    -- Perform DML: Update Employee Salary
    UPDATE Employees
    SET Salary = v_new_salary
    WHERE EmployeeID = p_emp_id;
    
    -- Commit the changes to make them permanent
    COMMIT;
    
    -- Print old and updated details
    DBMS_OUTPUT.PUT_LINE('------------------------------------------------------------');
    DBMS_OUTPUT.PUT_LINE('Employee Details:');
    DBMS_OUTPUT.PUT_LINE('Name          : ' || v_emp_name);
    DBMS_OUTPUT.PUT_LINE('Position      : ' || v_position);
    DBMS_OUTPUT.PUT_LINE('Department    : ' || v_dept);
    DBMS_OUTPUT.PUT_LINE('Old Salary    : ' || TO_CHAR(v_old_salary, '$999,999.00'));
    DBMS_OUTPUT.PUT_LINE('New Salary    : ' || TO_CHAR(v_new_salary, '$999,999.00'));
    DBMS_OUTPUT.PUT_LINE('Salary successfully updated by ' || p_percentage || '%.');
    DBMS_OUTPUT.PUT_LINE('------------------------------------------------------------');

EXCEPTION
    -- Handle case where employee does not exist
    WHEN NO_DATA_FOUND THEN
        ROLLBACK;
        RAISE_APPLICATION_ERROR(-20005, 'Salary Update Failed: Employee with ID ' || p_emp_id || ' does not exist.');
        
    -- Handle business exception for invalid percentage
    WHEN e_invalid_percentage THEN
        ROLLBACK;
        RAISE_APPLICATION_ERROR(-20006, 'Salary Update Failed: Percentage increase cannot be negative.');
        
    -- Handle all other system errors
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE_APPLICATION_ERROR(-20999, 'Salary Update Failed: System error. Code: ' || SQLCODE || ' - ' || SQLERRM);
END UpdateSalary;
/

-- ---------------------------------------------------------
-- 2. Anonymous Test Block (Demonstrates invocation)
-- ---------------------------------------------------------
DECLARE
    v_emp_id NUMBER := 301;
    v_pct NUMBER := 10; -- 10% increase
BEGIN
    DBMS_OUTPUT.PUT_LINE('============================================================');
    DBMS_OUTPUT.PUT_LINE('TEST CASE 1: Valid Salary Update (10% raise for employee 301)');
    DBMS_OUTPUT.PUT_LINE('============================================================');
    UpdateSalary(v_emp_id, v_pct);
EXCEPTION
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('Test 1 Exception: ' || SQLERRM);
END;
/

DECLARE
    v_emp_id NUMBER := 999; -- Non-existent employee ID
    v_pct NUMBER := 5;
BEGIN
    DBMS_OUTPUT.PUT_LINE('============================================================');
    DBMS_OUTPUT.PUT_LINE('TEST CASE 2: Invalid Salary Update (Employee ID does not exist)');
    DBMS_OUTPUT.PUT_LINE('============================================================');
    UpdateSalary(v_emp_id, v_pct);
EXCEPTION
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('Test 2 Caught Exception: ' || SQLERRM);
END;
/
