package model;

public class Faculty extends User {
    public Faculty(String id, String name) {
        super(id, name, "Faculty");
    }
    public void showDashboard() {
        System.out.println("Faculty Dashboard");
    }
}
