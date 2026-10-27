package payment;

public class CardPayment extends Payment {
    private final String cardNumber, holderName, cvv;

    public CardPayment(String cardNumber, String holderName, String cvv) {
        this.cardNumber = cardNumber;
        this.holderName = holderName;
        this.cvv = cvv;
    }

    @Override
    public boolean pay(double amount) {
        if (cardNumber == null || !cardNumber.matches("\\d{16}")) {
            System.out.println("Invalid card number (must be 16 digits).");
            return false;
        }
        if (cvv == null || !cvv.matches("\\d{3}")) {
            System.out.println("Invalid CVV (must be 3 digits).");
            return false;
        }
        System.out.printf("Rs.%.2f paid by card ending %s (%s).%n", amount,
                cardNumber.substring(12), holderName);
        completed = true;
        return true;
    }

    @Override public String getMethodName() { return "Card"; }
}
