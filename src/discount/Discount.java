package discount;

/** Interface: common behaviour for all discount types. */
public interface Discount {
    double calculate(double amount);
    String getDescription();
}
