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
    /**
     * Constructeur du contrôleur principal.
     *
     * @param jeu l'instance de Jeu à utiliser dans toute l'application
     */
    public AppControleur(Jeu jeu) throws IOException {
        this.jeu = jeu;
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

    public void resetGame() {
        jeu.setModeJeu(null);
        jeu.setDefiEnCours(null);
    }
}
