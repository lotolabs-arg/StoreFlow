package domain.stock;

import domain.common.BaseEntity;
import domain.common.DomainException;
import domain.common.Guard;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

/**
 * Represents a physical item in the inventory.
 * Manages stock levels, unit types, and pricing rules.
 */
@Entity
@Table(name = "products")
public class Product extends BaseEntity {

    private String name;
    private String description;

    private double stockQuantity;

    @Enumerated(EnumType.STRING)
    private UnitType unitType;

    protected Product() {

    }

    public Product(String name, UnitType unitType, double initialStock) {
        Guard.againstNullOrEmpty(name, "Product name");
        Guard.againstNull(unitType, "Unit Type");
        Guard.againstNegative(initialStock, "Initial stock");

        this.name = name;
        this.unitType = unitType;

        validateStockForUnitType(initialStock, unitType);
        this.stockQuantity = initialStock;
    }

    public void addStock(double quantity) {
        Guard.againstNegative(quantity, "Quantity to add");
        validateStockForUnitType(quantity, this.unitType);

        this.stockQuantity += quantity;
    }

    /**
     * Decreases the inventory count based on a sale or adjustment.
     * * @param quantity the amount to reduce
     * @throws DomainException if there is insufficient stock
     */
    public void reduceStock(double quantity) {
        Guard.againstNegative(quantity, "Quantity to reduce");
        validateStockForUnitType(quantity, this.unitType);

        if (this.stockQuantity < quantity) {
            throw new DomainException("Insufficient stock for product: " + this.name);
        }
        this.stockQuantity -= quantity;
    }

    /**
     * Ensures that UNIT types do not have fractional stock.
     */
    private void validateStockForUnitType(double quantity, UnitType type) {
        if (type == UnitType.UNIT) {
            Guard.againstFractional(quantity, "Stock for UNIT products");
        }
    }

    public String getName() {
        return name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public double getStockQuantity() {
        return stockQuantity;
    }

    public UnitType getUnitType() {
        return unitType;
    }
}
