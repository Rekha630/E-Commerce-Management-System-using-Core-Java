package model;

/** Abstract parent for all product types. */
public abstract class Product {
    private final int productId;
    private String name;
    private double price;
    private int quantity;
    private final String category;

    protected Product(int productId, String name, double price, int quantity, String category) {
        if (price < 0)    throw new IllegalArgumentException("Price cannot be negative.");
        if (quantity < 0) throw new IllegalArgumentException("Quantity cannot be negative.");
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.category = category;
    }

    public int getProductId()   { return productId; }
    public String getName()     { return name; }
    public double getPrice()    { return price; }
    public int getQuantity()    { return quantity; }
    public String getCategory() { return category; }

    public void setName(String name) { this.name = name; }

    public void setPrice(double price) {
        if (price < 0) throw new IllegalArgumentException("Price cannot be negative.");
        this.price = price;
    }

    public void setQuantity(int quantity) {
        if (quantity < 0) throw new IllegalArgumentException("Quantity cannot be negative.");
        this.quantity = quantity;
    }

    public void reduceStock(int qty)   { setQuantity(quantity - qty); }
    public void increaseStock(int qty) { setQuantity(quantity + qty); }

    /** Category specific information (Polymorphism). */
    public abstract String getExtraInfo();

    @Override
    public String toString() {
        return String.format("%-5d %-15s %-12s Rs.%-10.2f %-6d %s",
                productId, name, category, price, quantity, getExtraInfo());
    }
}
