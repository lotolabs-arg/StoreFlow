package application.usecases;

import application.dtos.UpdateProductDTO;
import application.interfaces.ProductRepository;
import domain.common.DomainException;
import domain.stock.Barcode;
import domain.stock.Product;
import domain.users.User;
import java.util.Optional;

public class UpdateProductDetails {

    private final ProductRepository repository;

    public UpdateProductDetails(ProductRepository repository) {
        this.repository = repository;
    }

    /**
     * Executes the product update process.
     *
     * @param actor   the user performing the action (used for auditing/permissions context)
     * @param request the DTO containing product details (barcode, cost, quantity, etc.)
     * @throws DomainException if the uuid is missing
     */
    public void execute(User actor, UpdateProductDTO request) {
        Product product = repository.findById(request.id())
            .orElseThrow(() -> new DomainException("Product not found."));

        Barcode newBarcode = new Barcode(request.barcode());

        if (!product.getBarcode().equals(newBarcode)) {
            Optional<Product> collision = repository.findByBarcode(newBarcode);
            if (collision.isPresent()) {
                throw new DomainException("Barcode already exists in another product.");
            }
        }

        product.updateDetails(
            request.name(),
            request.description(),
            newBarcode,
            request.unitType(),
            request.cost()
        );

        repository.save(product);
    }
}
