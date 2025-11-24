package infrastructure.ui.controller;

import application.usecases.LoginUser;
import domain.common.DomainException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    private static final Logger LOGGER = Logger.getLogger(LoginController.class.getName());

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    private final LoginUser loginUser;

    public LoginController(LoginUser loginUser) {
        this.loginUser = loginUser;
    }

    @FXML
    public void onLoginButtonClick() {
        try {
            String username = usernameField.getText();
            String password = passwordField.getText();

            loginUser.execute(username, password);

            showAlert(Alert.AlertType.INFORMATION, "Success", "Login successful!");

        } catch (DomainException e) {
            showAlert(Alert.AlertType.ERROR, "Login Failed", e.getMessage());
        } catch (RuntimeException e) {
            showAlert(Alert.AlertType.ERROR, "System Error", "An unexpected error occurred.");
            LOGGER.log(Level.SEVERE, "Unexpected error during login", e);
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
