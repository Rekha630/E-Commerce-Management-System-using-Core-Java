package service;

import payment.Payment;

public class PaymentService {
    public boolean process(Payment payment, double amount) { return payment.pay(amount); }
}
