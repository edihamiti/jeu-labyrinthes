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

    private static AppControleur instance;
    private final Scene menu;
    private Stage primaryStage;

    /**
     * Constructeur privé pour le singleton.
     *
     * @param jeu l'instance unique de Jeu à utiliser dans toute l'application
     */
    private AppControleur(Jeu jeu) throws IOException {
        // Utiliser l'instance de Jeu passée en paramètre
        this.jeu = jeu;

        FXMLLoader fxmlLoader = new FXMLLoader(AppControleur.class.getResource("/HomePage.fxml"));
        this.menu = new Scene(fxmlLoader.load(), 1400, 900);

        // Injecter Jeu dans le contrôleur de la page d'accueil
        Object controller = fxmlLoader.getController();
        if (controller instanceof Controleur) {
            ((Controleur) controller).setJeu(this.jeu);
        }
    }

    /**
     * Méthode pour obtenir l'instance unique du contrôleur.
     *
     * @param jeu l'instance de Jeu à utiliser (utilisée uniquement lors de la première création)
     * @return instance unique de AppControleur
     * @throws IOException si le chargement de la vue échoue
     */
    public static AppControleur getInstance(Jeu jeu) throws IOException {
        if (instance == null) {
            instance = new AppControleur(jeu);
        }
        return instance;
    }

    /**
     * Méthode pour obtenir l'instance unique du contrôleur (si déjà créée).
     *
     * @return instance unique de AppControleur
     * @throws IllegalStateException si l'instance n'a pas encore été créée
     */
    public static AppControleur getInstance() {
        if (instance == null) {
            throw new IllegalStateException("AppControleur n'a pas encore été initialisé avec une instance de Jeu");
        }
        return instance;
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
            ((Controleur) controller).setJeu(this.jeu);
        }
        return loader;
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
