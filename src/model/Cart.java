package model;

import discount.Discount;
import exception.InsufficientStockException;
import exception.ProductNotFoundException;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Cart {
    private final Map<Integer, CartItem> items = new LinkedHashMap<>();
    private Discount discount;

    public void addItem(Product p, int qty) throws InsufficientStockException {
        if (qty <= 0) throw new IllegalArgumentException("Quantity must be greater than zero.");
        if (p.getQuantity() == 0)
            throw new InsufficientStockException(p.getName() + " is out of stock.");

        CartItem existing = items.get(p.getProductId());
        int newQty = qty + (existing == null ? 0 : existing.getQuantity());
        if (newQty > p.getQuantity())
            throw new InsufficientStockException("Only " + p.getQuantity() + " unit(s) of "
                    + p.getName() + " available.");

        if (existing == null) items.put(p.getProductId(), new CartItem(p, qty));
        else existing.setQuantity(newQty);
    }

    public void removeItem(int productId) throws ProductNotFoundException {
        if (items.remove(productId) == null)
            throw new ProductNotFoundException("Product " + productId + " is not in your cart.");
    }

    public void updateQuantity(int productId, int qty)
            throws ProductNotFoundException, InsufficientStockException {
        CartItem item = items.get(productId);
        if (item == null)
            throw new ProductNotFoundException("Product " + productId + " is not in your cart.");
        if (qty < 0) throw new IllegalArgumentException("Quantity cannot be negative.");
        if (qty == 0) { items.remove(productId); return; }
        if (qty > item.getProduct().getQuantity())
            throw new InsufficientStockException("Only " + item.getProduct().getQuantity()
                    + " unit(s) available.");
        item.setQuantity(qty);
    }

    public double getTotal() {
        double total = 0;
        for (CartItem i : items.values()) total += i.getTotal();
        return total;
    }

    public void setDiscount(Discount d) { this.discount = d; }
    public double getDiscountAmount()   { return discount == null ? 0 : discount.calculate(getTotal()); }
    public double getFinalAmount()      { return getTotal() - getDiscountAmount(); }
    public boolean isEmpty()            { return items.isEmpty(); }
    public List<CartItem> getItems()    { return new ArrayList<>(items.values()); }

    public void clear() { items.clear(); discount = null; }

    public void display() {
        if (items.isEmpty()) { System.out.println("Your cart is empty."); return; }
        System.out.println("------------------------------------------------------");
        for (CartItem i : items.values()) {
            System.out.printf("[%d] %-14s %d x Rs.%.2f = Rs.%.2f%n", i.getProduct().getProductId(),
                    i.getProduct().getName(), i.getQuantity(), i.getPrice(), i.getTotal());
        }
        System.out.println("------------------------------------------------------");
        System.out.printf("Total           : Rs.%.2f%n", getTotal());
        if (discount != null) {
            System.out.printf("Discount (%s): Rs.%.2f%n", discount.getDescription(), getDiscountAmount());
            System.out.printf("Final Amount    : Rs.%.2f%n", getFinalAmount());
        }
    }
}
