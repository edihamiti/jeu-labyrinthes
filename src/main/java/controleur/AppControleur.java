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
public class AppControleur {

    private static AppControleur instance;
    private final Scene menu;
    private Stage primaryStage;

    /**
     * Constructeur privé pour le singleton.
     */
    private AppControleur() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(AppControleur.class.getResource("/HomePage.fxml"));
        this.menu = new Scene(fxmlLoader.load(), 1400, 900);
    }

    /**
     * Méthode pour obtenir l'instance unique du contrôleur.
     *
     * @return instance unique de AppControleur
     * @throws IOException si le chargement de la vue échoue
     */
    public static AppControleur getInstance() throws IOException {
        if (instance == null) {
            instance = new AppControleur();
        }
        return instance;
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
        Jeu.getInstance().setModeJeu(null);
        Jeu.getInstance().setDefiEnCours(null);
        Jeu.getInstance().setJoueur((Joueur) null);
        System.out.println("[DEBUG] Jeu reset");
    }

}
