package service;

import model.Admin;
import model.Customer;

import java.util.HashMap;
import java.util.Map;

public class CustomerService {
    private final Map<String, Customer> customers = new HashMap<>();   // key = email (unique)
    private final Admin admin = new Admin(1, "Administrator", "admin@shop.com", "admin123");

    public Customer register(String name, String email, String password, String address) {
        email = email.toLowerCase();
        if (customers.containsKey(email)) throw new IllegalArgumentException("Email already registered.");
        if (password.length() < 4) throw new IllegalArgumentException("Password must be at least 4 characters.");
        Customer c = new Customer(name, email, password, address);
        customers.put(email, c);
        return c;
    }

    public Customer login(String email, String password) {
        Customer c = customers.get(email.toLowerCase());
        return (c != null && c.checkPassword(password)) ? c : null;
    }

    public Admin loginAdmin(String email, String password) {
        return (admin.getEmail().equalsIgnoreCase(email) && admin.checkPassword(password)) ? admin : null;
    }
}
