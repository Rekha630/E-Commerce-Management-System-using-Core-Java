package model;

/** Parent class holding common user details (Inheritance + Encapsulation). */
public abstract class User {
    private final int userId;
    private String name;
    private final String email;
    private String password;
    private final String role;

    protected User(int userId, String name, String email, String password, String role) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public int getUserId()    { return userId; }
    public String getName()   { return name; }
    public String getEmail()  { return email; }
    public String getRole()   { return role; }
    public void setName(String name) { this.name = name; }
    public boolean checkPassword(String input) { return password.equals(input); }

    /** Each user type shows its own details (Polymorphism). */
    public abstract String getProfile();
}
