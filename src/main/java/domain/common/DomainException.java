package domain.common;

/**
 * Custom runtime exception representing a violation of a business rule.
 * Example: "Insufficient stock", "Invalid price", etc.
 */
public class DomainException extends RuntimeException {

    public DomainException(String message) {
        super(message);
    }
}
