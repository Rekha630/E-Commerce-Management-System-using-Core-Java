package main;

import exception.InsufficientStockException;
import exception.ProductNotFoundException;
import model.*;
import payment.*;
import service.*;

import java.util.List;
import java.util.Scanner;

/** Entry point: console menus for Customer and Admin. */
public class ECommerceApplication {

    private static final Scanner sc = new Scanner(System.in);
    private static final ProductService productService = new ProductService();
    private static final CustomerService customerService = new CustomerService();
    private static final CartService cartService = new CartService(productService);
    private static final PaymentService paymentService = new PaymentService();
    private static final OrderService orderService = new OrderService(paymentService);

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n====================================");
            System.out.println("   E-COMMERCE MANAGEMENT SYSTEM");
            System.out.println("====================================");
            System.out.println("1. Register\n2. Customer Login\n3. Admin Login\n4. Exit");
            int choice = readInt("Enter your choice: ");
            switch (choice) {
                case 1: register(); break;
                case 2: customerLogin(); break;
                case 3: adminLogin(); break;
                case 4: System.out.println("Thank you for shopping. Goodbye!"); return;
                default: System.out.println("Invalid choice.");
            }
        }
    }

    // ---------------- input helpers ----------------
    private static String readLine(String prompt) {
        System.out.print(prompt);
        return sc.hasNextLine() ? sc.nextLine().trim() : "";
    }

    private static int readInt(String prompt) {
        while (true) {
            String s = readLine(prompt);
            try { return Integer.parseInt(s); }
            catch (NumberFormatException e) { System.out.println("Please enter a valid whole number."); }
            if (!sc.hasNextLine()) return -1;   // stop on end of input
        }
    }

    private static double readDouble(String prompt) {
        while (true) {
            String s = readLine(prompt);
            try { return Double.parseDouble(s); }
            catch (NumberFormatException e) { System.out.println("Please enter a valid number."); }
            if (!sc.hasNextLine()) return -1;
        }
    }

    // ---------------- registration / login ----------------
    private static void register() {
        String name = readLine("Name: ");
        String email = readLine("Email: ");
        String password = readLine("Password (min 4 chars): ");
        String address = readLine("Address: ");
        try {
            Customer c = customerService.register(name, email, password, address);
            System.out.println("Registration successful! Your Customer ID is " + c.getUserId());
        } catch (IllegalArgumentException e) {
            System.out.println("Registration failed: " + e.getMessage());
        }
    }

    private static void customerLogin() {
        String email = readLine("Email: ");
        String password = readLine("Password: ");
        Customer c = customerService.login(email, password);
        if (c == null) { System.out.println("Invalid email or password."); return; }
        System.out.println("Welcome, " + c.getName() + "!");
        customerMenu(c);
    }

    private static void adminLogin() {
        String email = readLine("Admin email: ");
        String password = readLine("Password: ");
        Admin a = customerService.loginAdmin(email, password);
        if (a == null) { System.out.println("Invalid admin credentials."); return; }
        System.out.println("Welcome, " + a.getName() + "!");
        adminMenu();
    }

    // ---------------- customer module ----------------
    private static void customerMenu(Customer c) {
        while (true) {
            System.out.println("\n====================================");
            System.out.println("CUSTOMER MENU");
            System.out.println("====================================");
            System.out.println("1. View Products\n2. Search Product\n3. Add Product to Cart\n4. View Cart");
            System.out.println("5. Remove Product\n6. Update Quantity\n7. Apply Discount\n8. Checkout");
            System.out.println("9. View Orders\n10. Cancel Order\n11. Logout");
            int choice = readInt("Enter your choice: ");
            Cart cart = c.getCart();
            try {
                switch (choice) {
                    case 1: productService.display(productService.getAllProducts()); break;
                    case 2: productService.display(productService.search(readLine("Search keyword: "))); break;
                    case 3: {
                        int id = readInt("Product ID: ");
                        int qty = readInt("Quantity: ");
                        cartService.add(cart, id, qty);
                        System.out.println("Added to cart.");
                        break;
                    }
                    case 4: cart.display(); break;
                    case 5: cartService.remove(cart, readInt("Product ID to remove: ")); System.out.println("Removed."); break;
                    case 6: {
                        int id = readInt("Product ID: ");
                        int qty = readInt("New quantity (0 to remove): ");
                        cartService.updateQuantity(cart, id, qty);
                        System.out.println("Cart updated.");
                        break;
                    }
                    case 7: applyDiscount(cart); break;
                    case 8: checkout(c); break;
                    case 9: viewOrders(orderService.getOrdersByCustomer(c)); break;
                    case 10: cancelOrder(c); break;
                    case 11: System.out.println("Logged out."); return;
                    default: System.out.println("Invalid choice.");
                }
            } catch (ProductNotFoundException | InsufficientStockException e) {
                System.out.println("Error: " + e.getMessage());
            } catch (IllegalArgumentException | IllegalStateException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private static void applyDiscount(Cart cart) {
        if (cart.isEmpty()) { System.out.println("Cart is empty."); return; }
        System.out.println("Available coupons: SAVE10 (10% off), FLAT500 (Rs.500 off on Rs.2000+)");
        if (cartService.applyCoupon(cart, readLine("Enter coupon code: "))) {
            System.out.printf("Discount applied: Rs.%.2f%n", cart.getDiscountAmount());
            cart.display();
        } else System.out.println("Invalid coupon code.");
    }

    private static void checkout(Customer c) throws InsufficientStockException {
        Cart cart = c.getCart();
        if (cart.isEmpty()) { System.out.println("Your cart is empty. Cannot checkout."); return; }
        cart.display();
        System.out.println("\nSelect payment method:\n1. UPI\n2. Card\n3. Cash on Delivery");
        Payment payment;
        switch (readInt("Enter your choice: ")) {
            case 1: payment = new UpiPayment(readLine("UPI ID: ")); break;
            case 2: {
                String no = readLine("Card number (16 digits): ");
                String holder = readLine("Card holder name: ");
                String cvv = readLine("CVV (3 digits): ");
                payment = new CardPayment(no, holder, cvv);
                break;
            }
            case 3: payment = new CashPayment(); break;
            default: System.out.println("Invalid payment option."); return;
        }
        Order order = orderService.placeOrder(c, payment);
        System.out.println("\nPayment successful! Order placed.");
        order.display();
    }

    private static void viewOrders(List<Order> orders) {
        if (orders.isEmpty()) { System.out.println("No orders found."); return; }
        for (Order o : orders) { o.display(); System.out.println(); }
    }

    private static void cancelOrder(Customer c) {
        Order o = orderService.findById(readInt("Order ID to cancel: "));
        if (o == null || o.getCustomer() != c) { System.out.println("Order not found."); return; }
        orderService.cancel(o);
        System.out.println("Order " + o.getOrderId() + " cancelled. Stock restored.");
    }

    // ---------------- admin module ----------------
    private static void adminMenu() {
        while (true) {
            System.out.println("\n====================================");
            System.out.println("ADMIN MENU");
            System.out.println("====================================");
            System.out.println("1. Add Product\n2. View Products\n3. Search Product\n4. Update Product");
            System.out.println("5. Update Stock\n6. Remove Product\n7. View Orders\n8. Update Order Status\n9. Logout");
            int choice = readInt("Enter your choice: ");
            try {
                switch (choice) {
                    case 1: addProduct(); break;
                    case 2: productService.display(productService.getAllProducts()); break;
                    case 3: productService.display(productService.search(readLine("Search keyword: "))); break;
                    case 4: updateProduct(); break;
                    case 5: {
                        int id = readInt("Product ID: ");
                        productService.updateStock(id, readInt("New stock quantity: "));
                        System.out.println("Stock updated.");
                        break;
                    }
                    case 6: productService.removeProduct(readInt("Product ID to remove: ")); System.out.println("Product removed."); break;
                    case 7: viewOrders(orderService.getAllOrders()); break;
                    case 8: updateOrderStatus(); break;
                    case 9: System.out.println("Logged out."); return;
                    default: System.out.println("Invalid choice.");
                }
            } catch (ProductNotFoundException e) {
                System.out.println("Error: " + e.getMessage());
            } catch (IllegalArgumentException | IllegalStateException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private static void addProduct() {
        System.out.println("Category: 1. Electronics  2. Clothing  3. Grocery");
        int cat = readInt("Enter category: ");
        int id = readInt("Product ID: ");
        String name = readLine("Name: ");
        double price = readDouble("Price: ");
        int qty = readInt("Stock quantity: ");
        Product p;
        switch (cat) {
            case 1: p = new Electronics(id, name, price, qty, readInt("Warranty (months): ")); break;
            case 2: p = new Clothing(id, name, price, qty, readLine("Size: ")); break;
            case 3: p = new Grocery(id, name, price, qty, readLine("Weight (e.g. 1 kg): ")); break;
            default: System.out.println("Invalid category."); return;
        }
        productService.addProduct(p);
        System.out.println("Product added successfully.");
    }

    private static void updateProduct() throws ProductNotFoundException {
        int id = readInt("Product ID: ");
        Product p = productService.getProduct(id);
        System.out.println("Current: " + p);
        System.out.println("1. Change name\n2. Change price");
        int ch = readInt("Enter your choice: ");
        if (ch == 1) productService.updateName(id, readLine("New name: "));
        else if (ch == 2) productService.updatePrice(id, readDouble("New price: "));
        else { System.out.println("Invalid choice."); return; }
        System.out.println("Product updated.");
    }

    private static void updateOrderStatus() {
        Order o = orderService.findById(readInt("Order ID: "));
        if (o == null) { System.out.println("Order not found."); return; }
        System.out.println("Current status: " + o.getStatus());
        System.out.println("1. Ship\n2. Deliver\n3. Cancel");
        switch (readInt("Enter your choice: ")) {
            case 1: o.ship(); break;
            case 2: o.deliver(); break;
            case 3: orderService.cancel(o); break;
            default: System.out.println("Invalid choice."); return;
        }
        System.out.println("Order status is now " + o.getStatus());
    }
}
