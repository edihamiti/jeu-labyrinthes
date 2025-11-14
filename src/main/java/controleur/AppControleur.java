package controleur;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import modele.Jeu;
import modele.Joueur;

import java.io.IOException;

/**
 * Contrôleur principal de l'application.
 * Gère la scène principale et la navigation entre les différentes vues.
 */
public class AppControleur extends Controleur {

    private final Scene menu;
    private Stage primaryStage;

    /**
     * Constructeur du contrôleur principal.
     *
     * @param jeu l'instance de Jeu à utiliser dans toute l'application
     */
    public AppControleur(Jeu jeu) throws IOException {
        this.jeu = jeu;

        FXMLLoader fxmlLoader = new FXMLLoader(AppControleur.class.getResource("/HomePage.fxml"));
        this.menu = new Scene(fxmlLoader.load(), 1400, 900);

        // Injecter Jeu et AppControleur dans le contrôleur de la page d'accueil
        Object controller = fxmlLoader.getController();
        if (controller instanceof Controleur) {
            ((Controleur) controller).setJeu(this.jeu);
            ((Controleur) controller).setAppControleur(this);
        }
    }

    /**
     * Charge une vue FXML et injecte automatiquement l'instance de Jeu dans le contrôleur.
     *
     * @param resource le chemin de la ressource FXML
     * @return le FXMLLoader chargé avec injection de Jeu
     * @throws IOException si le chargement de la vue échoue
     */
    public FXMLLoader loadFXMLWithJeu(String resource) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(resource));
        loader.load();
        Object controller = loader.getController();

        if (controller instanceof Controleur) {
            injectDependencies((Controleur) controller);
        }

        return loader;
    }

    /**
     * Injecte Jeu et AppControleur dans un contrôleur.
     * Pour standardiser l'injection de dépendances.
     *
     * @param controller le contrôleur dans lequel injecter les dépendances
     */
    private void injectDependencies(Controleur controller) {
        controller.setJeu(this.jeu);
        controller.setAppControleur(this);
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
    }

    public void MenuPrincipal() {
        primaryStage.setTitle("HomePage");
        primaryStage.setScene(this.menu);
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    public void resetGame() {
        jeu.setModeJeu(null);
        jeu.setDefiEnCours(null);
        jeu.setJoueur((Joueur) null);
    }

}
