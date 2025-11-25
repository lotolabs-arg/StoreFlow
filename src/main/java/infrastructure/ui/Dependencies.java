package infrastructure.ui;

import application.interfaces.ProductRepository;
import application.usecases.LoginUser;
import application.usecases.RegisterProductEntry;
import application.usecases.UpdateProductDetails;

public class Dependencies {
    private final LoginUser loginUser;
    private final RegisterProductEntry registerProduct;
    private final UpdateProductDetails updateProductDetails;
    private final ProductRepository productRepository;

    public Dependencies(LoginUser loginUser,
                        RegisterProductEntry registerProduct,
                        UpdateProductDetails updateProductDetails,
                        ProductRepository productRepository) {
        this.loginUser = loginUser;
        this.registerProduct = registerProduct;
        this.updateProductDetails = updateProductDetails;
        this.productRepository = productRepository;
    }

    public LoginUser getLoginUser() { return loginUser; }
    public RegisterProductEntry getRegisterProduct() { return registerProduct; }
    public UpdateProductDetails getUpdateProductDetails() { return updateProductDetails; }
    public ProductRepository getProductRepository() { return productRepository; }
}
