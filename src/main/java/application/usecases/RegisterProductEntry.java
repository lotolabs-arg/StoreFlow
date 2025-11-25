package application.usecases;

import application.dtos.ProductEntryDTO;
import application.interfaces.ProductRepository;
import domain.common.DomainException;
import domain.stock.Barcode;
import domain.stock.Product;
import domain.users.User;
import java.util.Optional;

public class RegisterProductEntry {

    private final ProductRepository productRepository;

    public RegisterProductEntry(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * Executes the product entry process (Upsert logic).
     * <p>
     * If the product exists (identified by barcode), it restocks the inventory and updates the replacement cost.
     * If the product does not exist, it validates the mandatory data and creates a new product record.
     * </p>
     *
     * @param actor   the user performing the action (used for auditing/permissions context)
     * @param request the DTO containing product details (barcode, cost, quantity, etc.)
     * @throws DomainException if the barcode is missing or if creating a new product with incomplete data
     */
    public void execute(User actor, ProductEntryDTO request) {
        if (request.barcode() == null || request.barcode().isBlank()) {
            throw new DomainException("Barcode is required.");
        }

        Barcode barcode = new Barcode(request.barcode());
        Optional<Product> existingProduct = productRepository.findByBarcode(barcode);

        if (existingProduct.isPresent()) {
            Product product = existingProduct.get();
            product.restock(request.quantity(), request.cost());
            productRepository.save(product);
        } else {
            validateNewProductData(request);

            Product newProduct = new Product(
                request.name(),
                barcode,
                request.unitType(),
                request.quantity(),
                request.cost()
            );

            newProduct.setDescription(request.description());

            productRepository.save(newProduct);
        }
    }

    private void validateNewProductData(ProductEntryDTO request) {
        if (request.name() == null || request.name().isBlank()) {
            throw new DomainException("Product name is required for new products.");
        }
        if (request.description() == null || request.description().isBlank()) {
            throw new DomainException("Product description is required for new products.");
        }
        if (request.unitType() == null) {
            throw new DomainException("Unit type is required.");
        }
        if (request.cost() == null) {
            throw new DomainException("Cost is required.");
        }
        if (request.quantity() == null) {
            throw new DomainException("Initial stock is required.");
        }
    }
}
