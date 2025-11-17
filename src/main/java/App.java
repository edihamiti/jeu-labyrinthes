import controleur.AppControleur;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        stage.setTitle("Le jeu des Labyrinthes");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("img/sortie.png")));

        AppControleur.getInstance().setPrimaryStage(stage);
        AppControleur.getInstance().MenuPrincipal();

    }

    public static void main(String[] args) {
        launch(args);
    }
}