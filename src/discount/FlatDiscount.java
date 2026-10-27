package discount;

public class FlatDiscount implements Discount {
    private final double off;
    private final double minAmount;

    public FlatDiscount(double off, double minAmount) { this.off = off; this.minAmount = minAmount; }

    @Override public double calculate(double amount) { return amount >= minAmount ? off : 0; }
    @Override public String getDescription()         { return "Flat Rs." + off; }
}
