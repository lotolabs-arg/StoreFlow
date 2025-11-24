package domain.stock;

import domain.common.DomainException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    @Test
    @DisplayName("Should create product with correct attributes")
    void shouldCreateProductWithAttributes() {
        Product coca = new Product("Coca", "779123456", UnitType.UNIT, 10.0, 100.0);

        assertNotNull(coca.getId());
        assertEquals("779123456", coca.getBarcode());
        assertEquals(100.0, coca.getCost());
        assertEquals(10.0, coca.getStockQuantity());
    }

    @Test
    @DisplayName("Should fail when creating product without barcode")
    void shouldFail_WhenBarcodeIsMissing() {
        assertThrows(DomainException.class, () -> {
            new Product("Coca", "", UnitType.UNIT, 10.0, 100.0);
        });
    }

    @Test
    @DisplayName("Should update cost automatically on restock")
    void shouldUpdateCostOnRestock() {
        Product coca = new Product("Coca", "111222", UnitType.UNIT, 5.0, 100.0);
        assertEquals(100.0, coca.getCost());

        coca.restock(10.0, 150.0);

        assertEquals(15.0, coca.getStockQuantity());
        assertEquals(150.0, coca.getCost());
    }

    @Test
    @DisplayName("Should throw exception when initial stock is decimal for UNIT type")
    void shouldThrowException_When_InitialStockIsDecimalForUnitType() {
        assertThrows(DomainException.class, () -> {
            new Product("Coca", "123", UnitType.UNIT, 1.5, 100.0);
        }, "Should throw DomainException because UNIT cannot have fractional stock");
    }

    @Test
    @DisplayName("Should throw exception when adding decimal stock to UNIT product")
    void shouldThrowException_When_AddingDecimalStockToUnitProduct() {
        Product coca = new Product("Coca", "123", UnitType.UNIT, 5.0, 50.0);

        assertThrows(DomainException.class, () -> {
            coca.restock(0.5, coca.getCost());
        });
    }

    @Test
    @DisplayName("Should reduce stock correctly")
    void shouldReduceStock_Correctly() {
        Product tela = new Product("Tela", "999", UnitType.METER, 10.0, 500.0);
        tela.reduceStock(1.5);
        assertEquals(8.5, tela.getStockQuantity());
    }

    @Test
    @DisplayName("Should throw exception if reducing more stock than available")
    void shouldThrowException_If_ReducingMoreThanAvailable() {
        Product tela = new Product("Tela", "999", UnitType.METER, 2.0, 500.0);

        assertThrows(DomainException.class, () -> {
            tela.reduceStock(2.1);
        }, "Should fail because stock would become negative");
    }
}
