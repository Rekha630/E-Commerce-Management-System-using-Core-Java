package model;

public class Electronics extends Product {
    private final int warrantyMonths;

    public Electronics(int id, String name, double price, int qty, int warrantyMonths) {
        super(id, name, price, qty, "Electronics");
        this.warrantyMonths = warrantyMonths;
    }

    @Override
    public String getExtraInfo() { return "Warranty: " + warrantyMonths + " months"; }
}
