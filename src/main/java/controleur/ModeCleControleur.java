package controleur;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import modele.ModeJeu;
import modele.TypeLabyrinthe;

/**
 * Contrôleur pour le mode Chasse à la Clé.
 */
public class ModeCleControleur extends Controleur {

    @FXML
    private Button btnFacile;
    @FXML
    private Button btnMoyen;
    @FXML
    private Button btnDifficile;
    @FXML
    private Button btnRetour;

    @FXML
    public void initialize() {
        // Initialisation si nécessaire
    }

    @Override
    public void setJeu(modele.Jeu jeu) {
        super.setJeu(jeu);
        if (jeu != null) {
            jeu.setModeJeu(ModeJeu.MODE_LIBRE);
        }
    }

    /**
     * Lance le mode facile
     */
    @FXML
    public void lancerFacile() {
        lancerModeCle(10, 10, 4, btnFacile);
    }

    /**
     * Lance le mode moyen
     */
    @FXML
    public void lancerMoyen() {
        lancerModeCle(15, 15, 3, btnMoyen);
    }

    /**
     * Lance le mode difficile
     */
    @FXML
    public void lancerDifficile() {
        lancerModeCle(20, 20, 2, btnDifficile);
    }

    /**
     * Lance le jeu avec les paramètres donnés
     */
    private void lancerModeCle(int largeur, int hauteur, int porteeVision, Button sourceButton) {
        try {
            System.out.println("[DEBUG] Lancement du mode Clé");
            System.out.println("\tLargeur : " + largeur);
            System.out.println("\tHauteur : " + hauteur);
            System.out.println("\tPortée vision : " + porteeVision);

            // Charger la vue du jeu
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Jeu.fxml"));
            Parent jeuView = loader.load();

            JeuControleur jeuControleur = loader.getController();

            // Injecter les dépendances
            jeuControleur.setJeu(this.jeu);
            jeuControleur.setAppControleur(this.appControleur);

            // Configurer le labyrinthe avec le mode clé activé (génération de base, sans distanceMin)
            jeuControleur.setParametresLabAvecCle(largeur, hauteur, 100, porteeVision);

            // Changer la scène
            Stage stage = (Stage) sourceButton.getScene().getWindow();
            Scene scene = new Scene(jeuView, 1400, 900);
            stage.setScene(scene);
            stage.setMaximized(true);
            stage.setTitle("Jeu des Labyrinthes - Mode Clé");

            System.out.println("[DEBUG] Mode Clé lancé avec succès !");

        } catch (Exception e) {
            System.err.println("Erreur lors du lancement du jeu : " + e.getMessage());
            e.printStackTrace();

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Erreur lors du lancement du mode Clé");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    /**
     * Gère le clic sur le bouton de retour vers le menu principal.
     */
    @FXML
    public void retourClicked() {
        try {
            appControleur.resetGame();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/HomePage.fxml"));
            Parent menuView = loader.load();
            Controleur controleur = loader.getController();
            controleur.setAppControleur(this.appControleur);
            controleur.setJeu(this.jeu);
            Scene scene = new Scene(menuView, 1400, 900);

            Stage stage = (Stage) btnRetour.getScene().getWindow();
            stage.setScene(scene);
            stage.setMaximized(true);
            stage.setTitle("Jeu des Labyrinthes");

        } catch (Exception e) {
            System.err.println("Erreur lors du retour : " + e.getMessage());
            e.printStackTrace();

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Erreur lors du retour vers le menu principal");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }
}
