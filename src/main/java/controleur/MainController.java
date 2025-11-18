package controleur;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.io.IOException;

/**
 * Contrôleur pour la page principale de l'application.
 * TODO: Voir si cette classe est vraiment utile
 */
public class MainController extends Controleur {

    @FXML
    public ScrollPane content;

    public MainController() {
    }

    /**
     * Initialise les composants de la page principale.
     */
    @FXML
    public void initialize() throws IOException {
        FXMLLoader homepageLoader = new FXMLLoader(getClass().getResource("/HomePage.fxml"));
        content.setFitToWidth(true);
        content.setFitToHeight(true);
        content.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        HBox.setHgrow(content, Priority.ALWAYS);
        VBox.setVgrow(content, Priority.ALWAYS);

        content.setContent(homepageLoader.load());

        // Injecter les dépendances dans le contrôleur
        Object controller = homepageLoader.getController();
        if (controller instanceof Controleur) {
            ((Controleur) controller).setJeu(this.jeu);
            ((Controleur) controller).setAppControleur(this.appControleur);
        }
    }
}
