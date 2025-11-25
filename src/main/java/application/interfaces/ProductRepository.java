package application.interfaces;

import domain.stock.Barcode;
import domain.stock.Product;
import java.util.Optional;

public interface ProductRepository {

    Product save(Product product);

    Optional<Product> findById(String id);

    Optional<Product> findByBarcode(Barcode barcode);
}
