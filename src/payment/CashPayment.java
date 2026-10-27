package payment;

public class CashPayment extends Payment {
    @Override
    public boolean pay(double amount) {
        System.out.printf("Cash on Delivery selected. Pay Rs.%.2f when the order arrives.%n", amount);
        completed = true;   // payment commitment accepted
        return true;
    }

    @Override public String getMethodName() { return "Cash on Delivery"; }
}
