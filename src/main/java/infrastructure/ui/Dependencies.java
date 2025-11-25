package infrastructure.ui;

import application.usecases.LoginUser;
import application.usecases.RegisterProductEntry;

/**
 * Simple container for application dependencies (Use Cases).
 * Passed to the ViewNavigator to inject into Controllers.
 */
public class Dependencies {
    private final LoginUser loginUser;
    private final RegisterProductEntry registerProduct;

    public Dependencies(LoginUser loginUser, RegisterProductEntry registerProduct) {
        this.loginUser = loginUser;
        this.registerProduct = registerProduct;
    }

    public LoginUser getLoginUser() {
        return loginUser;
    }

    public RegisterProductEntry getRegisterProduct() {
        return registerProduct;
    }
}
