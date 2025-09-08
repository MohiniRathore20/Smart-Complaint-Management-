package model;

public class Department extends User {
    public Department(String id, String name) {
        super(id, name, "Department");
    }
    public void showDashboard() {
        System.out.println("Department Dashboard");
    }
}
