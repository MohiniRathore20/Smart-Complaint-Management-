package model;

public class Admin extends User {
    public Admin(String id, String name) {
        super(id, name, "Admin");
    }
    public void showDashboard() {
        System.out.println("Admin Dashboard");
    }
}
