package infrastructure.ui;

import application.interfaces.ProductRepository;
import application.interfaces.UserRepository;
import application.session.SessionContext;
import application.usecases.LoginUser;
import application.usecases.RegisterProductEntry;
import infrastructure.persistence.SqliteProductRepository;
import infrastructure.persistence.SqliteUserRepository;
import infrastructure.persistence.seed.DatabaseSeeder;
import java.io.IOException;
import javafx.application.Application;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        UserRepository userRepository = new SqliteUserRepository();
        ProductRepository productRepository = new SqliteProductRepository();
        new DatabaseSeeder(userRepository).seed();

        SessionContext sessionContext = SessionContext.getInstance();
        LoginUser loginUser = new LoginUser(userRepository, sessionContext);
        RegisterProductEntry registerProduct = new RegisterProductEntry(productRepository);

        Dependencies dependencies = new Dependencies(loginUser, registerProduct);

        ViewNavigator navigator = new ViewNavigator(stage, dependencies);
        navigator.showLogin();
    }

    public static void main(String[] args) {
        launch();
    }
}
