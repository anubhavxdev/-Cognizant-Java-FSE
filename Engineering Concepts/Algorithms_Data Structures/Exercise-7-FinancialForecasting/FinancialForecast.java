/**
 * Utility class to calculate financial forecasts.
 * Demonstrates the use of recursion for exponential growth calculations.
 */
public class FinancialForecast {

    /**
     * Recursively predicts the future value of an investment.
     * Formula applied recursively: Future Value = Current Value * (1 + Growth Rate)
     *
     * @param currentValue the initial or current value of the investment
     * @param growthRate   the annual growth rate expressed as a decimal (e.g., 0.10 for 10%)
     * @param years        the number of years to project the forecast
     * @return the predicted future value after the specified number of years
     */
    public double predictFutureValue(double currentValue, double growthRate, int years) {
        // Base Case: If years is 0 or negative, return the currentValue as is.
        // This stops the recursive descent.
        if (years <= 0) {
            return currentValue;
        }

        // Calculate the value at the end of the current year:
        double nextValue = currentValue * (1 + growthRate);

        // Recursive Case: project the future value for the remaining years
        // We pass the updated value (nextValue) as the current value and decrement years by 1.
        return predictFutureValue(nextValue, growthRate, years - 1);
    }
}
