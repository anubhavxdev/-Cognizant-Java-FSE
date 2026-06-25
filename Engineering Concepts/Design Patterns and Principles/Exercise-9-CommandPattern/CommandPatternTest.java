public class CommandPatternTest {

    public static void main(String[] args) {

        System.out.println("=============================================");
        System.out.println("   Command Pattern - Home Automation System   ");
        System.out.println("=============================================");

        Light livingRoomLight = new Light();

        Command lightOn = new LightOnCommand(livingRoomLight);
        Command lightOff = new LightOffCommand(livingRoomLight);

        RemoteControl remote = new RemoteControl();

        System.out.println("\n--- Pressing Button: Light ON ---");
        remote.setCommand(lightOn);
        remote.pressButton();

        System.out.println("\n--- Pressing Button: Light OFF ---");
        remote.setCommand(lightOff);
        remote.pressButton();

        System.out.println("\n--- Replaying Commands ---");
        remote.setCommand(lightOn);
        remote.pressButton();
        remote.setCommand(lightOff);
        remote.pressButton();

        System.out.println("\n=============================================");
        System.out.println("   Home automation executed successfully!     ");
        System.out.println("=============================================");
    }
}
