import controleur.AppControleur;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        AppControleur.getInstance().setPrimaryStage(stage);
        AppControleur.getInstance().MenuPrincipal();
    }

    public static void main(String[] args) {
        launch(args);
    }
}