package domain.stock;

import domain.common.DomainException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    @Test
    @DisplayName("Should throw exception when initial stock is decimal for UNIT type")
    void shouldThrowException_When_InitialStockIsDecimalForUnitType() {
        assertThrows(DomainException.class, () -> {
            new Product("Coca Cola", UnitType.UNIT, 1.5);
        }, "Should throw DomainException because UNIT cannot have fractional stock");
    }

    @Test
    @DisplayName("Should create product successfully when stock is integer for UNIT type")
    void shouldCreateProduct_When_StockIsIntegerForUnitType() {
        Product product = new Product("Coca Cola", UnitType.UNIT, 10.0);

        assertNotNull(product);
        assertEquals(10.0, product.getStockQuantity());
    }

    @Test
    @DisplayName("Should allow decimal stock for KILOGRAM type")
    void shouldAllowDecimalStock_For_KilogramType() {
        Product pan = new Product("Pan Francés", UnitType.KILOGRAM, 10.5);

        assertEquals(10.5, pan.getStockQuantity());
    }

    @Test
    @DisplayName("Should allow adding decimal stock to KILOGRAM product")
    void shouldAddDecimalStock_To_KilogramProduct() {
        Product pan = new Product("Pan Francés", UnitType.KILOGRAM, 10.0);

        pan.addStock(0.250);

        assertEquals(10.250, pan.getStockQuantity());
    }

    @Test
    @DisplayName("Should throw exception when adding decimal stock to UNIT product")
    void shouldThrowException_When_AddingDecimalStockToUnitProduct() {
        Product coca = new Product("Coca Cola", UnitType.UNIT, 5.0);

        assertThrows(DomainException.class, () -> {
            coca.addStock(0.5);
        });
    }

    @Test
    @DisplayName("Should reduce stock correctly")
    void shouldReduceStock_Correctly() {
        Product tela = new Product("Tela", UnitType.METER, 10.0);

        tela.reduceStock(1.5);

        assertEquals(8.5, tela.getStockQuantity());
    }

    @Test
    @DisplayName("Should throw exception if reducing more stock than available")
    void shouldThrowException_If_ReducingMoreThanAvailable() {
        Product tela = new Product("Tela", UnitType.METER, 2.0);

        assertThrows(DomainException.class, () -> {
            tela.reduceStock(2.1);
        }, "Should fail because stock would become negative");
    }
}
