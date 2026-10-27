package model;

public class Clothing extends Product {
    private final String size;

    public Clothing(int id, String name, double price, int qty, String size) {
        super(id, name, price, qty, "Clothing");
        this.size = size;
    }

    @Override
    public String getExtraInfo() { return "Size: " + size; }
}
