public class StudentController {

    private Student model;
    private StudentView view;

    public StudentController(Student model, StudentView view) {
        this.model = model;
        this.view = view;
    }

    public String getStudentId() {
        return model.getId();
    }

    public String getStudentName() {
        return model.getName();
    }

    public String getStudentGrade() {
        return model.getGrade();
    }

    public void updateId(String id) {
        model.setId(id);
    }

    public void updateName(String name) {
        model.setName(name);
    }

    public void updateGrade(String grade) {
        model.setGrade(grade);
    }

    public void displayStudent() {
        view.displayStudentDetails(
            model.getId(),
            model.getName(),
            model.getGrade()
        );
    }
}
