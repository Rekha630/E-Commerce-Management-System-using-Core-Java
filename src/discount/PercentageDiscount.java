package discount;

public class PercentageDiscount implements Discount {
    private final double percent;

    public PercentageDiscount(double percent) { this.percent = percent; }

    @Override public double calculate(double amount) { return amount * percent / 100.0; }
    @Override public String getDescription()         { return percent + "%"; }
}
