package infrastructure.ui;

import application.session.SessionContext;
import infrastructure.ui.controller.LoginController;
import infrastructure.ui.controller.MainLayoutController;
import infrastructure.ui.controller.ProductController;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 * Handles navigation between different scenes/views in the application.
 * Acts as the central point for dependency injection into Controllers.
 */
public class ViewNavigator {

    private final Stage mainStage;
    private final Dependencies dependencies;

    public ViewNavigator(Stage mainStage, Dependencies dependencies) {
        this.mainStage = mainStage;
        this.dependencies = dependencies;
    }

    public void showLogin() {
        loadScene("/view/login.fxml", "StoreFlow - Login");
    }

    public void showMainLayout() {
        loadScene("/view/main_layout.fxml", "StoreFlow - Gestión");
        mainStage.centerOnScreen();
        mainStage.setMaximized(true);
    }

    private void loadScene(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            loader.setControllerFactory(createControllerFactory());

            Parent root = loader.load();
            Scene scene = new Scene(root);

            mainStage.setTitle(title);
            mainStage.setScene(scene);
            mainStage.show();

        } catch (IOException | RuntimeException e) {
            throw new RuntimeException("Failed to load view: " + fxmlPath, e);
        }
    }

    private Callback<Class<?>, Object> createControllerFactory() {
        return param -> {
            if (param == LoginController.class) {
                return new LoginController(dependencies.getLoginUser(), this);
            }
            if (param == ProductController.class) {
                return new ProductController(dependencies.getRegisterProduct(), SessionContext.getInstance());
            }
            if (param == MainLayoutController.class) {
                return new MainLayoutController();
            }

            try {
                return param.getConstructor().newInstance();
            } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
                     InvocationTargetException e) {
                throw new RuntimeException("Could not instantiate controller: " + param.getName(), e);
            }
        };
    }
}
