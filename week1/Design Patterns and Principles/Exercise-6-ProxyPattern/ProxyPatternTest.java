public class ProxyPatternTest {

    public static void main(String[] args) {

        System.out.println("=============================================");
        System.out.println("      Proxy Pattern - Image Viewer            ");
        System.out.println("=============================================");
        System.out.println();

        System.out.println("--- Creating ProxyImage (no loading yet) ---");
        Image image = new ProxyImage("sample.jpg");
        System.out.println("ProxyImage created. Image NOT loaded yet.\n");

        System.out.println("--- First call to display() ---");
        image.display();
        System.out.println();

        System.out.println("--- Second call to display() (cached) ---");
        image.display();
        System.out.println();

        System.out.println("--- Another ProxyImage ---");
        Image anotherImage = new ProxyImage("wallpaper.png");
        System.out.println("ProxyImage created for wallpaper.png.\n");

        System.out.println("--- First call to display() ---");
        anotherImage.display();
        System.out.println();

        System.out.println("--- Second call to display() (cached) ---");
        anotherImage.display();

        System.out.println("\n=============================================");
        System.out.println("      All images displayed successfully!      ");
        System.out.println("=============================================");
    }
}
