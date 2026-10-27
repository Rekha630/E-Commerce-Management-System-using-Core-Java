package service;

import exception.InsufficientStockException;
import model.*;
import payment.Payment;

import java.util.ArrayList;
import java.util.List;

public class OrderService {
    private final List<Order> orders = new ArrayList<>();
    private final PaymentService paymentService;

    public OrderService(PaymentService paymentService) { this.paymentService = paymentService; }

    public Order placeOrder(Customer customer, Payment payment) throws InsufficientStockException {
        Cart cart = customer.getCart();
        if (cart.isEmpty()) throw new IllegalStateException("Cart is empty. Cannot checkout.");

        List<CartItem> items = cart.getItems();
        for (CartItem i : items)       // re-check stock
            if (i.getQuantity() > i.getProduct().getQuantity())
                throw new InsufficientStockException("Not enough stock for " + i.getProduct().getName());

        double amount = cart.getFinalAmount();
        if (!paymentService.process(payment, amount))
            throw new IllegalStateException("Payment failed. Order not created.");

        Order order = new Order(customer, items, amount, payment);
        order.confirm();
        for (CartItem i : items) i.getProduct().reduceStock(i.getQuantity());   // rule 10
        orders.add(order);
        cart.clear();
        return order;
    }

    public Order findById(int id) {
        for (Order o : orders) if (o.getOrderId() == id) return o;
        return null;
    }

    public List<Order> getOrdersByCustomer(Customer c) {
        List<Order> result = new ArrayList<>();
        for (Order o : orders) if (o.getCustomer() == c) result.add(o);
        return result;
    }

    public List<Order> getAllOrders() { return new ArrayList<>(orders); }

    public void cancel(Order o) {
        o.cancel();
        for (CartItem i : o.getItems()) i.getProduct().increaseStock(i.getQuantity());   // restock
    }
}
