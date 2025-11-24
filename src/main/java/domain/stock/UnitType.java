package domain.stock;

/**
 * Defines the measurement unit for a product and its fractional capabilities.
 */
public enum UnitType {
    UNIT(false),
    KILOGRAM(true),
    METER(true),
    LITERS(true);

    private final boolean allowsFractions;

    UnitType(boolean allowsFractions) {
        this.allowsFractions = allowsFractions;
    }

    /**
     * Checks if this unit type supports decimal quantities.
     *
     * @return true if fractions are allowed, false otherwise.
     */
    public boolean allowsFractions() {
        return allowsFractions;
    }
}
