package application.usecases;

import application.dtos.UpdateProductDTO;
import application.interfaces.ProductRepository;
import domain.common.DomainException;
import domain.stock.Barcode;
import domain.stock.Product;
import domain.stock.UnitType;
import domain.users.User;
import domain.users.UserRole;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UpdateProductDetailsTest {

    private List<Product> db;
    private ProductRepository fakeRepo;
    private UpdateProductDetails useCase;
    private User admin;

    @BeforeEach
    void setUp() {
        db = new ArrayList<>();
        admin = new User("admin", "1234", UserRole.ADMIN);

        Product p1 = new Product("Coca", new Barcode("111"), UnitType.UNIT, BigDecimal.TEN, BigDecimal.TEN);

        db.add(p1);

        fakeRepo = new ProductRepository() {
            @Override
            public Product save(Product p) { return p; }
            @Override
            public Optional<Product> findById(String id) {
                return db.stream().filter(p -> p.getId().equals(id)).findFirst();
            }
            @Override
            public Optional<Product> findByBarcode(Barcode b) {
                return db.stream().filter(p -> p.getBarcode().equals(b)).findFirst();
            }
            @Override
            public List<Product> findAll() { return db; }
        };

        useCase = new UpdateProductDetails(fakeRepo);
    }

    @Test
    @DisplayName("Should update product successfully")
    void shouldUpdateProduct() {
        Product original = db.get(0);
        UpdateProductDTO dto = new UpdateProductDTO(
            original.getId(),
            "Coca Zero",
            "Sin azucar",
            "111",
            UnitType.UNIT,
            new BigDecimal("12.00")
        );

        useCase.execute(admin, dto);

        assertEquals("Coca Zero", original.getName());
        assertEquals(new BigDecimal("12.00"), original.getCost());
    }

    @Test
    @DisplayName("Should fail if new barcode belongs to another product")
    void shouldFail_IfBarcodeCollision() {
        db.add(new Product("Pepsi", new Barcode("222"), UnitType.UNIT, BigDecimal.TEN, BigDecimal.TEN));

        Product original = db.get(0);

        UpdateProductDTO dto = new UpdateProductDTO(
            original.getId(),
            "Coca", "Desc",
            "222",
            UnitType.UNIT, BigDecimal.TEN
        );

        assertThrows(DomainException.class, () -> useCase.execute(admin, dto));
    }
}
