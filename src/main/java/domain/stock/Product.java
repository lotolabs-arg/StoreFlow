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
 * Manages stock levels, identification (barcode) and replacement cost.
 */
@Entity
@Table(name = "products")
public class Product extends BaseEntity {

    private String name;
    private String barcode;
    private String description;

    private double stockQuantity;
    private double cost;

    @Enumerated(EnumType.STRING)
    private UnitType unitType;

    protected Product() {
    }

    public Product(String name, String barcode, UnitType unitType, double initialStock, double cost) {
        Guard.againstNullOrEmpty(name, "Product name");
        Guard.againstNullOrEmpty(barcode, "Barcode"); // Validación obligatoria
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

    public void restock(double quantityIn, double newEntryCost) {
        Guard.againstZeroOrNegative(quantityIn, "Quantity to add");
        Guard.againstZeroOrNegative(newEntryCost, "New Entry Cost");
        validateStockForUnitType(quantityIn, this.unitType);

        this.cost = newEntryCost;
        this.stockQuantity += quantityIn;
    }

    public void reduceStock(double quantity) {
        Guard.againstNegative(quantity, "Quantity to reduce");
        validateStockForUnitType(quantity, this.unitType);

        if (this.stockQuantity < quantity) {
            throw new DomainException("Insufficient stock for product: " + this.name);
        }
        this.stockQuantity -= quantity;
    }

    private void validateStockForUnitType(double quantity, UnitType type) {
        if (type == UnitType.UNIT) {
            Guard.againstFractional(quantity, "Stock for UNIT products");
        }
    }

    public String getName() {
        return name;
    }

    public String getBarcode() {
        return barcode;
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

    public double getCost() {
        return cost;
    }
}
