public class MVCPatternTest {

    public static void main(String[] args) {

        System.out.println("=============================================");
        System.out.println("    MVC Pattern - Student Management System   ");
        System.out.println("=============================================");

        Student model = new Student("101", "John", "A");

        StudentView view = new StudentView();

        StudentController controller = new StudentController(model, view);

        System.out.println("\n--- Initial Student Details ---");
        controller.displayStudent();

        System.out.println("\n--- Updating Student Data ---");
        controller.updateName("Jane");
        System.out.println("Name updated to: " + controller.getStudentName());

        controller.updateGrade("A+");
        System.out.println("Grade updated to: " + controller.getStudentGrade());

        controller.updateId("202");
        System.out.println("ID updated to: " + controller.getStudentId());

        System.out.println("\n--- Updated Student Details ---");
        controller.displayStudent();

        System.out.println("\n--- Another Update ---");
        controller.updateName("Alice");
        controller.updateGrade("B+");
        controller.displayStudent();

        System.out.println("\n=============================================");
        System.out.println("    MVC operations completed successfully!    ");
        System.out.println("=============================================");
    }
}
