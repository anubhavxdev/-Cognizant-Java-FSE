public class StrategyPatternTest {

    public static void main(String[] args) {

        System.out.println("=============================================");
        System.out.println("   Strategy Pattern - Payment System          ");
        System.out.println("=============================================");

        System.out.println("\n--- Case 1: Credit Card Payment ---");

        PaymentContext context = new PaymentContext(new CreditCardPayment());
        context.executePayment(5000);

        System.out.println("\n--- Case 2: Switch to PayPal (Runtime) ---");

        context.setPaymentStrategy(new PayPalPayment());
        context.executePayment(3000);

        System.out.println("\n--- Case 3: Multiple Strategy Switches ---");

        context.setPaymentStrategy(new CreditCardPayment());
        context.executePayment(1500);

        context.setPaymentStrategy(new PayPalPayment());
        context.executePayment(2000);

        System.out.println("\n--- Case 4: Different Users, Different Strategies ---");

        PaymentContext user1 = new PaymentContext(new CreditCardPayment());
        PaymentContext user2 = new PaymentContext(new PayPalPayment());

        System.out.print("User 1: ");
        user1.executePayment(7500);

        System.out.print("User 2: ");
        user2.executePayment(4200);

        System.out.println("\n=============================================");
        System.out.println("   All payments processed successfully!       ");
        System.out.println("=============================================");
    }
}
