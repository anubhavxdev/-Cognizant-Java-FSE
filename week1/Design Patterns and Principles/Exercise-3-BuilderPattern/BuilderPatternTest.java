public class BuilderPatternTest {

    public static void main(String[] args) {

        System.out.println("=============================================");
        System.out.println("   Builder Pattern - Computer Configurations  ");
        System.out.println("=============================================");

        Computer gamingPC = new Computer.Builder()
                .setCPU("Intel i9")
                .setRAM("32GB")
                .setStorage("1TB SSD")
                .setGPU("RTX 4080")
                .setOperatingSystem("Windows 11")
                .build();

        System.out.println("\n--- Gaming PC ---");
        System.out.println(gamingPC);

        Computer officePC = new Computer.Builder()
                .setCPU("Intel i5")
                .setRAM("8GB")
                .setStorage("512GB SSD")
                .setOperatingSystem("Windows 11")
                .build();

        System.out.println("\n--- Office PC ---");
        System.out.println(officePC);

        Computer developerPC = new Computer.Builder()
                .setCPU("AMD Ryzen 7")
                .setRAM("16GB")
                .setStorage("1TB SSD")
                .setOperatingSystem("Ubuntu Linux")
                .build();

        System.out.println("\n--- Developer PC ---");
        System.out.println(developerPC);

        System.out.println("\n=============================================");
        System.out.println("   All configurations created successfully!   ");
        System.out.println("=============================================");
    }
}
