package domain.stock;

import domain.common.BaseEntity;
import domain.common.DomainException;
import domain.common.Guard;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import java.math.BigDecimal;

/**
 * Represents a physical item in the inventory.
 * Uses precise arithmetic for financial and stock calculations.
 */
@Entity
@Table(name = "products")
public class Product extends BaseEntity {

    private String name;
    private String description;

    @Embedded
    private Barcode barcode;

    private BigDecimal stockQuantity;
    private BigDecimal cost;

    @Enumerated(EnumType.STRING)
    private UnitType unitType;

    protected Product() {
    }

    public Product(String name, Barcode barcode, UnitType unitType, BigDecimal initialStock, BigDecimal cost) {
        Guard.againstNullOrEmpty(name, "Product name");
        Guard.againstNull(barcode, "Barcode");
        Guard.againstNull(unitType, "Unit Type");
        Guard.againstNegative(initialStock, "Initial stock");
        Guard.againstNegative(cost, "Cost");

        this.name = name;
        this.barcode = barcode;
        this.unitType = unitType;
        this.cost = cost;

        validateStockForUnitType(initialStock, unitType);
        this.stockQuantity = initialStock;
    }

    public void restock(BigDecimal quantityIn, BigDecimal newEntryCost) {
        Guard.againstZeroOrNegative(quantityIn, "Quantity to add");
        Guard.againstZeroOrNegative(newEntryCost, "New Entry Cost");
        validateStockForUnitType(quantityIn, this.unitType);

        this.cost = newEntryCost;
        this.stockQuantity = this.stockQuantity.add(quantityIn);
    }

    public void reduceStock(BigDecimal quantity) {
        Guard.againstNegative(quantity, "Quantity to reduce");
        validateStockForUnitType(quantity, this.unitType);

        if (this.stockQuantity.compareTo(quantity) < 0) {
            throw new DomainException("Insufficient stock for product: " + this.name);
        }
        this.stockQuantity = this.stockQuantity.subtract(quantity);
    }

    private void validateStockForUnitType(BigDecimal quantity, UnitType type) {
        if (!type.allowsFractions()) {
            Guard.againstFractional(quantity, "Stock for UNIT products");
        }
    }

    public String getName() {
        return name;
    }

    public Barcode getBarcode() {
        return barcode;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getStockQuantity() {
        return stockQuantity;
    }

    public UnitType getUnitType() {
        return unitType;
    }

    public BigDecimal getCost() {
        return cost;
    }
}
