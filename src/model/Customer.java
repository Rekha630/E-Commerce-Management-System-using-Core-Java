package model;

public class Customer extends User {
    private static int counter = 1000;      // static member: shared ID generator
    private String address;
    private final Cart cart = new Cart();

    public Customer(String name, String email, String password, String address) {
        super(++counter, name, email, password, "CUSTOMER");
        this.address = address;
    }

    public String getAddress() { return address; }
    public Cart getCart()      { return cart; }

    @Override
    public String getProfile() {
        return "Customer ID: " + getUserId() + " | " + getName() + " | " + getEmail() + " | " + address;
    }
}
