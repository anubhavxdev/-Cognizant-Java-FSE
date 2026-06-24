public class ObserverPatternTest {

    public static void main(String[] args) {

        System.out.println("=============================================");
        System.out.println("   Observer Pattern - Stock Market Monitor    ");
        System.out.println("=============================================");

        StockMarket stockMarket = new StockMarket();

        Observer mobileApp = new MobileApp();
        Observer webApp = new WebApp();

        System.out.println("\n--- Registering Observers ---");
        stockMarket.registerObserver(mobileApp);
        System.out.println("MobileApp registered.");
        stockMarket.registerObserver(webApp);
        System.out.println("WebApp registered.");

        System.out.println("\n--- Stock Price Change 1 ---");
        stockMarket.setStockPrice("AAPL", 200.5);

        System.out.println("\n--- Stock Price Change 2 ---");
        stockMarket.setStockPrice("GOOGL", 2800.75);

        System.out.println("\n--- Removing WebApp Observer ---");
        stockMarket.removeObserver(webApp);
        System.out.println("WebApp removed from observers.");

        System.out.println("\n--- Stock Price Change 3 (WebApp removed) ---");
        stockMarket.setStockPrice("AAPL", 210.0);

        System.out.println("\n=============================================");
        System.out.println("   Stock monitoring completed successfully!   ");
        System.out.println("=============================================");
    }
}
