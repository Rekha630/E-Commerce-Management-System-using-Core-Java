package model;

import payment.Payment;

import java.util.List;

public class Order {
    private static int counter = 5000;
    private final int orderId;
    private final Customer customer;
    private final List<CartItem> items;
    private final double totalAmount;
    private final Payment payment;
    private OrderStatus status = OrderStatus.CREATED;

    public Order(Customer customer, List<CartItem> items, double totalAmount, Payment payment) {
        this.orderId = ++counter;
        this.customer = customer;
        this.items = items;
        this.totalAmount = totalAmount;
        this.payment = payment;
    }

    public int getOrderId()          { return orderId; }
    public Customer getCustomer()    { return customer; }
    public List<CartItem> getItems() { return items; }
    public double getTotalAmount()   { return totalAmount; }
    public OrderStatus getStatus()   { return status; }

    // ---- status transitions (enforce business rules 9, 11, 12) ----
    public void confirm() {
        if (status != OrderStatus.CREATED) throw new IllegalStateException("Only a CREATED order can be confirmed.");
        if (!payment.isCompleted()) throw new IllegalStateException("Payment must be completed before confirming.");
        status = OrderStatus.CONFIRMED;
    }

    public void ship() {
        if (status == OrderStatus.CANCELLED) throw new IllegalStateException("A cancelled order cannot be shipped.");
        if (status != OrderStatus.CONFIRMED) throw new IllegalStateException("Only a CONFIRMED order can be shipped.");
        status = OrderStatus.SHIPPED;
    }

    public void deliver() {
        if (status != OrderStatus.SHIPPED) throw new IllegalStateException("Only a SHIPPED order can be delivered.");
        status = OrderStatus.DELIVERED;
    }

    public void cancel() {
        if (status == OrderStatus.DELIVERED)
            throw new IllegalStateException("An order that is already delivered cannot be cancelled.");
        if (status == OrderStatus.CANCELLED) throw new IllegalStateException("Order is already cancelled.");
        status = OrderStatus.CANCELLED;
    }

    public void display() {
        System.out.println("Order ID : " + orderId + " | Customer: " + customer.getName()
                + " | Status: " + status);
        for (CartItem i : items)
            System.out.printf("   %s x %d = Rs.%.2f%n", i.getProduct().getName(), i.getQuantity(), i.getTotal());
        System.out.printf("   Total: Rs.%.2f | Payment: %s%n", totalAmount, payment.getMethodName());
    }
}
