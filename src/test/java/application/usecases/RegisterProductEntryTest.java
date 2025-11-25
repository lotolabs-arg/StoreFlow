package application.usecases;

import application.dtos.ProductEntryDTO;
import application.interfaces.ProductRepository;
import domain.common.DomainException;
import domain.stock.Barcode;
import domain.stock.Product;
import domain.stock.UnitType;
import domain.users.User;
import domain.users.UserRole;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RegisterProductEntryTest {

    private Product storedProduct;

    private final ProductRepository fakeRepo = new ProductRepository() {
        @Override
        public Product save(Product product) {
            storedProduct = product;
            return product;
        }
        @Override
        public Optional<Product> findById(String id) { return Optional.empty(); }

        @Override
        public Optional<Product> findByBarcode(Barcode barcode) {
            if (storedProduct != null && storedProduct.getBarcode().equals(barcode)) {
                return Optional.of(storedProduct);
            }
            return Optional.empty();
        }

        @Override
        public List<Product> findAll() {
            return List.of();
        }
    };

    private final RegisterProductEntry useCase = new RegisterProductEntry(fakeRepo);
    private final User seller = new User("vendedor", "1234", UserRole.SELLER);

    @Test
    @DisplayName("Should create new product successfully with all fields")
    void shouldCreateNewProduct() {
        ProductEntryDTO dto = new ProductEntryDTO(
            "Coca", "Deliciosa", "111", UnitType.UNIT, new BigDecimal("10"), new BigDecimal("100")
        );

        assertDoesNotThrow(() -> useCase.execute(seller, dto));

        assertNotNull(storedProduct);
        assertEquals("Coca", storedProduct.getName());
        assertEquals("Deliciosa", storedProduct.getDescription());
        assertEquals(new BigDecimal("100"), storedProduct.getCost());
    }

    @Test
    @DisplayName("Should fail when Barcode is missing")
    void shouldFail_WhenBarcodeIsMissing() {
        ProductEntryDTO dto = new ProductEntryDTO(
            "Coca", "Desc", "", UnitType.UNIT, BigDecimal.TEN, BigDecimal.TEN
        );

        DomainException e = assertThrows(DomainException.class, () -> useCase.execute(seller, dto));
        assertEquals("Barcode is required.", e.getMessage());
    }

    @Test
    @DisplayName("Should fail when Name is missing for new product")
    void shouldFail_WhenNameIsMissing() {
        ProductEntryDTO dto = new ProductEntryDTO(
            "", "Desc", "123", UnitType.UNIT, BigDecimal.TEN, BigDecimal.TEN
        );

        DomainException e = assertThrows(DomainException.class, () -> useCase.execute(seller, dto));
        assertEquals("Product name is required for new products.", e.getMessage());
    }

    @Test
    @DisplayName("Should fail when Description is missing for new product")
    void shouldFail_WhenDescriptionIsMissing() {
        ProductEntryDTO dto = new ProductEntryDTO(
            "Coca", "", "123", UnitType.UNIT, BigDecimal.TEN, BigDecimal.TEN
        );

        DomainException e = assertThrows(DomainException.class, () -> useCase.execute(seller, dto));
        assertEquals("Product description is required for new products.", e.getMessage());
    }

    @Test
    @DisplayName("Should fail when Cost is missing (null)")
    void shouldFail_WhenCostIsMissing() {
        ProductEntryDTO dto = new ProductEntryDTO(
            "Coca", "Desc", "123", UnitType.UNIT, BigDecimal.TEN, null
        );

        DomainException e = assertThrows(DomainException.class, () -> useCase.execute(seller, dto));
        assertEquals("Cost is required.", e.getMessage());
    }

    @Test
    @DisplayName("Should fail when UnitType is missing (null)")
    void shouldFail_WhenUnitTypeIsMissing() {
        ProductEntryDTO dto = new ProductEntryDTO(
            "Coca", "Desc", "123", null, BigDecimal.TEN, BigDecimal.TEN
        );

        DomainException e = assertThrows(DomainException.class, () -> useCase.execute(seller, dto));
        assertEquals("Unit type is required.", e.getMessage());
    }

    @Test
    @DisplayName("Should fail when Quantity is missing (null)")
    void shouldFail_WhenQuantityIsMissing() {
        ProductEntryDTO dto = new ProductEntryDTO(
            "Coca", "Desc", "123", UnitType.UNIT, null, BigDecimal.TEN
        );

        DomainException e = assertThrows(DomainException.class, () -> useCase.execute(seller, dto));
        assertEquals("Initial stock is required.", e.getMessage());
    }
}
