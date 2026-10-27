package exception;

/** Thrown when requested quantity is greater than available stock. */
public class InsufficientStockException extends Exception {
    public InsufficientStockException(String message) { super(message); }
}
