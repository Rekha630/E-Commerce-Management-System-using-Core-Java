package payment;

public class UpiPayment extends Payment {
    private final String upiId;

    public UpiPayment(String upiId) { this.upiId = upiId; }

    @Override
    public boolean pay(double amount) {
        if (upiId == null || !upiId.matches("[\\w.\\-]+@\\w+")) {
            System.out.println("Invalid UPI ID.");
            return false;
        }
        System.out.printf("Rs.%.2f paid using UPI (%s).%n", amount, upiId);
        completed = true;
        return true;
    }

    @Override public String getMethodName() { return "UPI"; }
}
