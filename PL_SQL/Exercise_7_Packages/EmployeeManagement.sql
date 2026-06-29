-- =========================================================
-- ORACLE PL/SQL EXERCISE 7: PACKAGES
-- FILE: EmployeeManagement.sql
-- PURPOSE: Implements an Employee Management package containing
--          procedures to hire and update employee records,
--          and a function to compute their annual salary.
-- =========================================================

SET SERVEROUTPUT ON;

-- ---------------------------------------------------------
-- 1. Package Specification
-- ---------------------------------------------------------
CREATE OR REPLACE PACKAGE EmployeeManagement IS

    -- Procedure to insert a new employee record
    PROCEDURE HireEmployee(
        p_emp_id IN NUMBER,
        p_name IN VARCHAR2,
        p_position IN VARCHAR2,
        p_salary IN NUMBER,
        p_dept IN VARCHAR2,
        p_hire_date IN DATE
    );
    
    -- Procedure to update employee profile and compensation
    PROCEDURE UpdateEmployee(
        p_emp_id IN NUMBER,
        p_name IN VARCHAR2,
        p_position IN VARCHAR2,
        p_salary IN NUMBER,
        p_dept IN VARCHAR2
    );
    
    -- Function to calculate annual salary (Monthly Salary * 12)
    FUNCTION CalculateAnnualSalary(
        p_emp_id IN NUMBER
    ) RETURN NUMBER;

END EmployeeManagement;
/

-- ---------------------------------------------------------
-- 2. Package Body
-- ---------------------------------------------------------
CREATE OR REPLACE PACKAGE BODY EmployeeManagement IS

    -- Hire a new employee
    PROCEDURE HireEmployee(
        p_emp_id IN NUMBER,
        p_name IN VARCHAR2,
        p_position IN VARCHAR2,
        p_salary IN NUMBER,
        p_dept IN VARCHAR2,
        p_hire_date IN DATE
    ) IS
    BEGIN
        INSERT INTO Employees (EmployeeID, Name, Position, Salary, Department, HireDate)
        VALUES (p_emp_id, p_name, p_position, p_salary, p_dept, p_hire_date);
        
        -- Commit changes
        COMMIT;
        DBMS_OUTPUT.PUT_LINE('HireEmployee Success: Hired ' || p_name || ' (ID: ' || p_emp_id || ')');
    EXCEPTION
        WHEN DUP_VAL_ON_INDEX THEN
            ROLLBACK;
            RAISE_APPLICATION_ERROR(-20081, 'HireEmployee Failed: Employee ID ' || p_emp_id || ' already exists.');
        WHEN OTHERS THEN
            ROLLBACK;
            RAISE_APPLICATION_ERROR(-20999, 'HireEmployee Failed: Database error: ' || SQLERRM);
    END HireEmployee;
    
    -- Update employee details
    PROCEDURE UpdateEmployee(
        p_emp_id IN NUMBER,
        p_name IN VARCHAR2,
        p_position IN VARCHAR2,
        p_salary IN NUMBER,
        p_dept IN VARCHAR2
    ) IS
        v_exists NUMBER;
    BEGIN
        -- Validate existence
        SELECT COUNT(*)
        INTO v_exists
        FROM Employees
        WHERE EmployeeID = p_emp_id;
        
        IF v_exists = 0 THEN
            RAISE_APPLICATION_ERROR(-20082, 'UpdateEmployee Failed: Employee ID ' || p_emp_id || ' does not exist.');
        END IF;
        
        -- Update values
        UPDATE Employees
        SET Name = p_name,
            Position = p_position,
            Salary = p_salary,
            Department = p_dept
        WHERE EmployeeID = p_emp_id;
        
        -- Commit changes
        COMMIT;
        DBMS_OUTPUT.PUT_LINE('UpdateEmployee Success: Modified Employee ID: ' || p_emp_id);
    EXCEPTION
        WHEN OTHERS THEN
            ROLLBACK;
            RAISE;
    END UpdateEmployee;
    
    -- Calculate annual salary
    FUNCTION CalculateAnnualSalary(
        p_emp_id IN NUMBER
    ) RETURN NUMBER IS
        v_monthly_salary NUMBER(15, 2);
    BEGIN
        SELECT Salary
        INTO v_monthly_salary
        FROM Employees
        WHERE EmployeeID = p_emp_id;
        
        -- Annual salary = Monthly Salary * 12 months
        RETURN v_monthly_salary * 12;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            RAISE_APPLICATION_ERROR(-20083, 'CalculateAnnualSalary Failed: Employee ID ' || p_emp_id || ' not found.');
    END CalculateAnnualSalary;

END EmployeeManagement;
/

-- ---------------------------------------------------------
-- 3. Sample Execution Block
-- ---------------------------------------------------------
PROMPT TESTING EMPLOYEE MANAGEMENT PACKAGE:
DECLARE
    v_annual_salary NUMBER;
BEGIN
    DBMS_OUTPUT.PUT_LINE('============================================================');
    DBMS_OUTPUT.PUT_LINE('Step 1: Hiring a new employee (ID: 302, Bob Brown)');
    DBMS_OUTPUT.PUT_LINE('============================================================');
    EmployeeManagement.HireEmployee(302, 'Bob Brown', 'Analyst', 5000.00, 'IT', TO_DATE('2018-09-10', 'YYYY-MM-DD'));
    
    DBMS_OUTPUT.PUT_LINE('============================================================');
    DBMS_OUTPUT.PUT_LINE('Step 2: Calculating employee annual salary');
    DBMS_OUTPUT.PUT_LINE('============================================================');
    v_annual_salary := EmployeeManagement.CalculateAnnualSalary(302);
    DBMS_OUTPUT.PUT_LINE('Bob Brown Annual Salary: ' || TO_CHAR(v_annual_salary, '$999,999.00'));
    
    DBMS_OUTPUT.PUT_LINE('============================================================');
    DBMS_OUTPUT.PUT_LINE('Step 3: Updating employee role and salary');
    DBMS_OUTPUT.PUT_LINE('============================================================');
    EmployeeManagement.UpdateEmployee(302, 'Bob Brown', 'Senior Analyst', 6000.00, 'IT');
    
    v_annual_salary := EmployeeManagement.CalculateAnnualSalary(302);
    DBMS_OUTPUT.PUT_LINE('Updated Annual Salary  : ' || TO_CHAR(v_annual_salary, '$999,999.00'));
    DBMS_OUTPUT.PUT_LINE('============================================================');
EXCEPTION
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('Caught Exception: ' || SQLERRM);
END;
/
