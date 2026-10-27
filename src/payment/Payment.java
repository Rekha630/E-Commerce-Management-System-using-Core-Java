package payment;

/** Abstract payment: same operation, different implementations. */
public abstract class Payment {
    protected boolean completed;

    public abstract boolean pay(double amount);
    public abstract String getMethodName();
    public boolean isCompleted() { return completed; }
}
