package infrastructure.ui;

import application.interfaces.UserRepository;
import application.session.SessionContext;
import application.usecases.LoginUser;
import infrastructure.persistence.FakeUserRepository;
import infrastructure.ui.controller.LoginController;
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws IOException {

        UserRepository userRepository = new FakeUserRepository();
        SessionContext sessionContext = SessionContext.getInstance();
        LoginUser loginUserUseCase = new LoginUser(userRepository, sessionContext);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/login.fxml"));
        loader.setControllerFactory(param -> {
            if (param == LoginController.class) {
                return new LoginController(loginUserUseCase);
            }
            try {
                return param.getConstructor().newInstance();
            } catch (ReflectiveOperationException e) {
                throw new RuntimeException(e);
            }
        });

        Parent root = loader.load();
        Scene scene = new Scene(root);

        stage.setTitle("StoreFlow - Login");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
