package domain.stock;

import domain.common.Guard;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

/**
 * Value Object representing a Product Barcode.
 * Ensures integrity and format of the identifier.
 */
@Embeddable
public class Barcode implements Serializable {

    private String value;

    protected Barcode() {
    }

    public Barcode(String value) {
        Guard.againstNullOrEmpty(value, "Barcode value");
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Barcode barcode = (Barcode) o;
        return Objects.equals(value, barcode.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
