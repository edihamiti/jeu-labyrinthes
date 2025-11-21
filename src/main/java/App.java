import controleur.AppControleur;
import controleur.ChoisirPseudoControleur;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import modele.Jeu;
import vue.Router;

import java.io.IOException;
import java.util.Objects;

public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        // Créer l'instance unique de Jeu pour toute l'application
        Jeu jeu = new Jeu();
        AppControleur appControleur = new AppControleur(jeu);

        // Initialiser le Router avec les dépendances globales
        Router.initialize(stage, appControleur, jeu);

        stage.setTitle("Le jeu des Labyrinthes");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("img/sortie.png")));

        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/ChoisirPseudo.fxml")));
        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();

        ChoisirPseudoControleur controleur = loader.getController();
        controleur.setJeu(jeu);
        controleur.setAppControleur(appControleur);
    }

    public static void main(String[] args) {
        launch(args);
    }
}