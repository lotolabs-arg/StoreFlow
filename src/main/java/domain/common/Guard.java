package domain.common;

import java.math.BigDecimal;

/**
 * Static utility class for defensive programming and input validation.
 */
public final class Guard {

    private static final double SUSPICIOUS_MARGIN_THRESHOLD = 10.0;
    private static final int PERCENTAGE_MULTIPLIER = 100;

    private Guard() {
    }

    public static void againstNull(Object value, String fieldName) {
        if (value == null) {
            throw new DomainException(fieldName + " cannot be null.");
        }
    }

    public static void againstNullOrEmpty(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new DomainException(fieldName + " cannot be null or empty.");
        }
    }

    public static void againstNegative(int value, String fieldName) {
        if (value < 0) {
            throw new DomainException(fieldName + " cannot be negative.");
        }
    }

    public static void againstNegative(BigDecimal value, String fieldName) {
        if (value == null || value.compareTo(BigDecimal.ZERO) < 0) {
            throw new DomainException(fieldName + " cannot be negative.");
        }
    }

    public static void againstZeroOrNegative(BigDecimal value, String fieldName) {
        if (value == null || value.compareTo(BigDecimal.ZERO) <= 0) {
            throw new DomainException(fieldName + " must be greater than zero.");
        }
    }

    public static void againstFractional(BigDecimal value, String fieldName) {
        if (value != null && value.remainder(BigDecimal.ONE).compareTo(BigDecimal.ZERO) != 0) {
            throw new DomainException(fieldName + " must be a whole number.");
        }
    }

    public static void againstUnrealisticProfitMargin(BigDecimal value, String fieldName) {
        if (value != null && value.compareTo(BigDecimal.valueOf(SUSPICIOUS_MARGIN_THRESHOLD)) > 0) {
            throw new DomainException(fieldName
                + " is suspiciously high (" + value + "). "
                + "Did you mean " + (value.divide(BigDecimal.valueOf(PERCENTAGE_MULTIPLIER)))
                + "? (Values are in decimals: 0.50 = 50%)");
        }
    }

    public static void againstNegative(double value, String fieldName) {
        if (value < 0.0) {
            throw new DomainException(fieldName + " cannot be negative.");
        }
    }
}
