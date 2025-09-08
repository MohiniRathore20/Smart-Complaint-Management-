package model;

public abstract class User {
    protected String id;
    protected String name;
    protected String role;

    public User(String id, String name, String role) {
        this.id = id;
        this.name = name;
        this.role = role;
    }

    public abstract void showDashboard();
}
