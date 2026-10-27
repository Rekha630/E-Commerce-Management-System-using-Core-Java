package model;

public class Grocery extends Product {
    private final String weight;

    public Grocery(int id, String name, double price, int qty, String weight) {
        super(id, name, price, qty, "Grocery");
        this.weight = weight;
    }

    @Override
    public String getExtraInfo() { return "Weight: " + weight; }
}
