-- =========================================================
-- ORACLE PL/SQL EXERCISE 4: FUNCTIONS
-- FILE: CalculateMonthlyInstallment.sql
-- PURPOSE: Implements a function to calculate the monthly loan
--          equated installment (EMI) using the standard formula.
-- =========================================================

SET SERVEROUTPUT ON;

-- ---------------------------------------------------------
-- 1. Function Definition & Formula Explanation
-- ---------------------------------------------------------
-- FORMULA EXPLANATION:
-- The Equated Monthly Installment (EMI) is calculated as:
--   EMI = [P x r x (1+r)^n] / [(1+r)^n - 1]
-- Where:
--   P = Principal Loan Amount (p_loan_amount)
--   r = Monthly Interest Rate = (Annual Interest Rate / 12 months) / 100
--   n = Number of monthly installments = Duration in years * 12 (v_total_months)
--
-- Boundary condition: If annual interest rate is 0%, the formula simplifies to:
--   EMI = P / n
-- ---------------------------------------------------------
CREATE OR REPLACE FUNCTION CalculateMonthlyInstallment (
    p_loan_amount IN NUMBER,
    p_interest_rate IN NUMBER,
    p_duration_years IN NUMBER
) RETURN NUMBER IS
    v_monthly_rate NUMBER;
    v_total_months NUMBER;
    v_emi NUMBER;
BEGIN
    -- Validate inputs
    IF p_loan_amount IS NULL OR p_interest_rate IS NULL OR p_duration_years IS NULL THEN
        RETURN 0;
    END IF;
    
    IF p_loan_amount <= 0 OR p_duration_years <= 0 OR p_interest_rate < 0 THEN
        RETURN 0;
    END IF;
    
    -- Calculate total duration in months
    v_total_months := p_duration_years * 12;
    
    -- Handle 0% interest rate case to prevent division-by-zero error
    IF p_interest_rate = 0 THEN
        v_emi := p_loan_amount / v_total_months;
    ELSE
        -- Convert annual percentage rate to monthly decimal rate
        v_monthly_rate := (p_interest_rate / 12) / 100;
        
        -- Apply the standard EMI formula
        v_emi := (p_loan_amount * v_monthly_rate * POWER(1 + v_monthly_rate, v_total_months)) /
                 (POWER(1 + v_monthly_rate, v_total_months) - 1);
    END IF;
    
    -- Return monthly installment rounded to 2 decimal places
    RETURN ROUND(v_emi, 2);
    
EXCEPTION
    WHEN OTHERS THEN
        RETURN 0;
END CalculateMonthlyInstallment;
/

-- ---------------------------------------------------------
-- 2. Sample Executions
-- ---------------------------------------------------------

-- Execution A: Querying the database table
PROMPT SELECT QUERY TEST (Loans Table EMI Calculations):
SELECT LoanID, CustomerID, LoanAmount, InterestRate, 
       CalculateMonthlyInstallment(LoanAmount, InterestRate, 6) AS Estimated_Monthly_EMI_6_Years
FROM Loans;

-- Execution B: PL/SQL Block Test (various inputs)
PROMPT PL/SQL BLOCK TEST:
DECLARE
    v_emi1 NUMBER;
    v_emi2 NUMBER;
BEGIN
    -- Loan 1: $10,000 at 12% annual rate for 1 year (12 installments)
    -- Expected: 10000 * 0.01 * (1.01)^12 / ((1.01)^12 - 1) = $888.49
    v_emi1 := CalculateMonthlyInstallment(10000, 12.0, 1);
    
    -- Loan 2: $12,000 at 0% annual rate for 1 year (Interest-free)
    -- Expected: 12000 / 12 = $1000.00
    v_emi2 := CalculateMonthlyInstallment(12000, 0.0, 1);
    
    DBMS_OUTPUT.PUT_LINE('Test 1: $10,000 Loan, 12% APR, 1 Year  -> Monthly EMI = ' || TO_CHAR(v_emi1, '$99,999.00'));
    DBMS_OUTPUT.PUT_LINE('Test 2: $12,000 Loan, 0% APR, 1 Year   -> Monthly EMI = ' || TO_CHAR(v_emi2, '$99,999.00'));
END;
/
