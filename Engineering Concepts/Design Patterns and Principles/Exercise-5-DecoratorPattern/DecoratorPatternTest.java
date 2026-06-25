public class DecoratorPatternTest {

    public static void main(String[] args) {

        System.out.println("=============================================");
        System.out.println("   Decorator Pattern - Notification System    ");
        System.out.println("=============================================");

        System.out.println("\n--- Case 1: Email Only ---");

        Notifier emailNotifier = new EmailNotifier();
        emailNotifier.send("Server Down!");

        System.out.println("\n--- Case 2: Email + SMS ---");

        Notifier smsNotifier = new SMSNotifierDecorator(
                new EmailNotifier()
        );
        smsNotifier.send("Server Down!");

        System.out.println("\n--- Case 3: Email + SMS + Slack ---");

        Notifier multiChannelNotifier = new SlackNotifierDecorator(
                new SMSNotifierDecorator(
                        new EmailNotifier()
                )
        );
        multiChannelNotifier.send("Server Down!");

        System.out.println("\n=============================================");
        System.out.println("   All notifications sent successfully!       ");
        System.out.println("=============================================");
    }
}
