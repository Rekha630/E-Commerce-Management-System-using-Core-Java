package exception;

/** Thrown when a requested product does not exist. */
public class ProductNotFoundException extends Exception {
    public ProductNotFoundException(String message) { super(message); }
}
