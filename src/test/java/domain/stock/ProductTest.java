package domain.stock;

import domain.common.DomainException;
import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    @Test
    @DisplayName("Should create product with correct attributes")
    void shouldCreateProductWithAttributes() {
        Product coca = new Product(
            "Coca",
            new Barcode("779123456"),
            UnitType.UNIT,
            new BigDecimal("10.0"),
            new BigDecimal("100.00")
        );

        assertNotNull(coca.getId());
        assertEquals(new Barcode("779123456"), coca.getBarcode());
        assertEquals(new BigDecimal("100.00"), coca.getCost());
        assertEquals(new BigDecimal("10.0"), coca.getStockQuantity());
    }

    @Test
    @DisplayName("Should fail when creating product without barcode")
    void shouldFail_WhenBarcodeIsMissing() {
        assertThrows(DomainException.class, () -> {
            new Product(
                "Coca",
                new Barcode(""),
                UnitType.UNIT,
                new BigDecimal("10.0"),
                new BigDecimal("100.00")
            );
        });
    }

    @Test
    @DisplayName("Should update cost automatically on restock")
    void shouldUpdateCostOnRestock() {
        Product coca = new Product(
            "Coca",
            new Barcode("111222"),
            UnitType.UNIT,
            new BigDecimal("5.0"),
            new BigDecimal("100.00")
        );

        assertEquals(new BigDecimal("100.00"), coca.getCost());

        coca.restock(new BigDecimal("10.0"), new BigDecimal("150.00"));

        assertEquals(new BigDecimal("15.0"), coca.getStockQuantity());
        assertEquals(new BigDecimal("150.00"), coca.getCost());
    }

    @Test
    @DisplayName("Should throw exception when initial stock is decimal for UNIT type")
    void shouldThrowException_When_InitialStockIsDecimalForUnitType() {
        assertThrows(DomainException.class, () -> {
            new Product(
                "Coca",
                new Barcode("123"),
                UnitType.UNIT,
                new BigDecimal("1.5"),
                new BigDecimal("100.00")
            );
        }, "Should throw DomainException because UNIT cannot have fractional stock");
    }

    @Test
    @DisplayName("Should throw exception when adding decimal stock to UNIT product")
    void shouldThrowException_When_AddingDecimalStockToUnitProduct() {
        Product coca = new Product(
            "Coca",
            new Barcode("123"),
            UnitType.UNIT,
            new BigDecimal("5.0"),
            new BigDecimal("50.00")
        );

        assertThrows(DomainException.class, () -> {
            coca.restock(new BigDecimal("0.5"), coca.getCost());
        });
    }

    @Test
    @DisplayName("Should reduce stock correctly")
    void shouldReduceStock_Correctly() {
        Product tela = new Product(
            "Tela",
            new Barcode("999"),
            UnitType.FRACTION,
            new BigDecimal("10.0"),
            new BigDecimal("500.00")
        );

        tela.reduceStock(new BigDecimal("1.5"));

        assertEquals(new BigDecimal("8.5"), tela.getStockQuantity());
    }

    @Test
    @DisplayName("Should throw exception if reducing more stock than available")
    void shouldThrowException_If_ReducingMoreThanAvailable() {
        Product tela = new Product(
            "Tela",
            new Barcode("999"),
            UnitType.FRACTION,
            new BigDecimal("2.0"),
            new BigDecimal("500.00")
        );

        assertThrows(DomainException.class, () -> {
            tela.reduceStock(new BigDecimal("2.1"));
        }, "Should fail because stock would become negative");
    }

    @Test
    @DisplayName("Should update product details successfully")
    void shouldUpdateProductDetails() {
        Product product = new Product("Coca", new Barcode("111"),
            UnitType.UNIT, BigDecimal.TEN, BigDecimal.TEN);
        Barcode newBarcode = new Barcode("222");

        product.updateDetails(
            "Coca Zero",
            "Sin azúcar",
            newBarcode,
            UnitType.UNIT,
            new BigDecimal("120.00")
        );

        assertEquals("Coca Zero", product.getName());
        assertEquals("Sin azúcar", product.getDescription());
        assertEquals(newBarcode, product.getBarcode());
        assertEquals(new BigDecimal("120.00"), product.getCost());
    }

    @Test
    @DisplayName("Should fail when changing type to UNIT if stock has decimals")
    void shouldFail_WhenChangingToUnitWithDecimalStock() {
        Product pan = new Product("Pan", new Barcode("333"), UnitType.FRACTION,
            new BigDecimal("1.5"), BigDecimal.TEN);

        assertThrows(DomainException.class, () -> {
            pan.updateDetails(
                pan.getName(),
                pan.getDescription(),
                pan.getBarcode(),
                UnitType.UNIT,
                pan.getCost()
            );
        }, "Should prevent changing to UNIT if current stock is fractional");
    }

    @Test
    @DisplayName("Should adjust stock manually")
    void shouldAdjustStockManually() {
        Product product = new Product("Coca", new Barcode("111"), UnitType.UNIT,
            BigDecimal.TEN, BigDecimal.TEN);

        product.adjustStock(new BigDecimal("5.0"));

        assertEquals(new BigDecimal("5.0"), product.getStockQuantity());
    }

    @Test
    @DisplayName("Should fail when adjusting stock to negative")
    void shouldFail_WhenAdjustingStockToNegative() {
        Product product = new Product("Coca", new Barcode("111"), UnitType.UNIT,
            BigDecimal.TEN, BigDecimal.TEN);

        assertThrows(DomainException.class, () -> {
            product.adjustStock(new BigDecimal("-1.0"));
        });
    }
}
