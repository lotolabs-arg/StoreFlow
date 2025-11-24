package infrastructure.ui;

import java.io.IOException;
import java.net.URL;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * JavaFX App Entry Point.
 */
public class MainApp extends Application {

    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 600;

    @Override
    public void start(Stage stage) throws IOException {
        URL fxmlUrl = getClass().getResource("/view/main_layout.fxml");

        if (fxmlUrl == null) {
            throw new IOException("FXML file not found at /view/main_layout.fxml");
        }

        FXMLLoader loader = new FXMLLoader(fxmlUrl);
        Parent root = loader.load();

        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);

        stage.setTitle("StoreFlow - Point of Sale");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
