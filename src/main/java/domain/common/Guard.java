package domain.common;

/**
 * Static utility class for defensive programming and input validation.
 * <p>
 * Used to enforce business rules and preconditions (Guard Clauses).
 * If a validation fails, a {@link DomainException} is thrown.
 * </p>
 */
public final class Guard {

    private Guard() {

    }

    /**
     * Validates that the provided object is not null.
     *
     * @param value     the object to check
     * @param fieldName the name of the field (for the error message)
     * @throws DomainException if the value is null
     */
    public static void againstNull(Object value, String fieldName) {
        if (value == null) {
            throw new DomainException(fieldName + " cannot be null.");
        }
    }

    /**
     * Validates that a string is not null and not empty (including whitespace).
     *
     * @param value     the string to check
     * @param fieldName the name of the field (for the error message)
     * @throws DomainException if the string is null or blank
     */
    public static void againstNullOrEmpty(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new DomainException(fieldName + " cannot be null or empty.");
        }
    }

    /**
     * Validates that an integer value is not negative.
     *
     * @param value     the number to check
     * @param fieldName the name of the field (for the error message)
     * @throws DomainException if the value is less than zero
     */
    public static void againstNegative(int value, String fieldName) {
        if (value < 0) {
            throw new DomainException(fieldName + " cannot be negative.");
        }
    }

    /**
     * Validates that a double value is not negative.
     *
     * @param value     the number to check
     * @param fieldName the name of the field (for the error message)
     * @throws DomainException if the value is less than zero
     */
    public static void againstNegative(double value, String fieldName) {
        if (value < 0.0) {
            throw new DomainException(fieldName + " cannot be negative.");
        }
    }

    /**
     * Validates that a value is strictly positive (greater than zero).
     *
     * @param value     the number to check
     * @param fieldName the name of the field (for the error message)
     * @throws DomainException if the value is zero or negative
     */
    public static void againstZeroOrNegative(double value, String fieldName) {
        if (value <= 0.0) {
            throw new DomainException(fieldName + " must be greater than zero.");
        }
    }

    /**
     * Validates that a double value represents a whole number (no decimals).
     * Used for Unit types that cannot be fractioned (e.g. "1.5 Bottles").
     *
     * @param value     the number to check
     * @param fieldName the name of the field (for the error message)
     * @throws DomainException if the value has decimals
     */
    public static void againstFractional(double value, String fieldName) {
        if (value % 1 != 0) {
            throw new DomainException(fieldName + " must be a whole number.");
        }
    }
}
