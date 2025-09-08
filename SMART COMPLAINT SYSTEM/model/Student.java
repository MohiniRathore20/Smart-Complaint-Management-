package model;

public class Student extends User {
    public Student(String id, String name) {
        super(id, name, "Student");
    }
    public void showDashboard() {
        System.out.println("Student Dashboard");
    }
}