import controleur.AppControleur;
import javafx.application.Application;
import javafx.stage.Stage;
import modele.Jeu;

import java.io.IOException;

public class App extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // Créer l'instance unique de Jeu pour toute l'application
        Jeu jeu = new Jeu();

        stage.setTitle("Le jeu des Labyrinthes");
        stage.getIcons().add(new javafx.scene.image.Image(getClass().getResourceAsStream("img/sortie.png")));

        // Initialiser AppControleur avec l'instance de Jeu
        AppControleur.getInstance(jeu).setPrimaryStage(stage);
        AppControleur.getInstance().MenuPrincipal();

    }

    public static void main(String[] args) {
        launch(args);
    }
}