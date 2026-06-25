public class AdapterPatternTest {

    public static void main(String[] args) {

        System.out.println("=============================================");
        System.out.println("   Adapter Pattern - Payment Processing      ");
        System.out.println("=============================================");
        System.out.println();

        PaymentProcessor paypal = new PayPalAdapter();
        paypal.processPayment(1000);

        System.out.println();

        PaymentProcessor stripe = new StripeAdapter();
        stripe.processPayment(2500);

        System.out.println();

        System.out.println("--- Processing Multiple Payments ---");

        PaymentProcessor[] processors = {
            new PayPalAdapter(),
            new StripeAdapter()
        };

        double[] amounts = { 500, 1500 };

        for (int i = 0; i < processors.length; i++) {
            processors[i].processPayment(amounts[i]);
        }

        System.out.println();
        System.out.println("=============================================");
        System.out.println("   All payments processed successfully!       ");
        System.out.println("=============================================");
    }
}
