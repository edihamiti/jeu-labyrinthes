package controleur;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import modele.Defi;
import modele.Jeu;
import modele.Joueur;
import modele.ModeJeu;
import vue.EtapesRendu;

import java.io.IOException;

/**
 * Contrôleur pour le mode progression du jeu de labyrinthe.
 */
public class ModeProgressionControleur {
    @FXML
    private VBox etapesContainer;
    @FXML
    private HBox pointsContainer;

    /**
     * Initialise le contrôleur et configure les étapes du mode progression.
     */
    @FXML
    public void initialize() {
        Jeu.getInstance().setModeJeu(ModeJeu.MODE_PROGRESSION);
        Joueur joueur = Jeu.getInstance().getJoueur();
        etapesContainer.getChildren().add(
                EtapesRendu.render(joueur.getProgression(), defi -> {
                    System.out.println("Defi sélectionné : " + defi);
                    lancerModeProgression(defi);
                })
        );
        EtapesRendu.renderPoints(Jeu.getInstance().getJoueur().getScore(), this.pointsContainer);
    }

    /**
     * Lance le mode progression du jeu avec le défi sélectionné.
     */
    public void lancerModeProgression(Defi defi) {
        try {
            System.out.println("[DEBUG] Enregistrement du labyrinthe dans le Jeu");
            // Enregistrer le défi en cours
            Jeu jeu = Jeu.getInstance();
            jeu.setDefiEnCours(defi);
            jeu.setLabyrinthe(defi);

            System.out.println("[DEBUG] Lancement du mode progression");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Jeu.fxml"));
            Parent jeuView = loader.load();

            JeuControleur jeuControleur = loader.getController();
            if (jeuControleur != null) {
                jeuControleur.setParametresLab(defi.getLargeur(), defi.getHauteur(), defi.getPourcentageMurs());
            }

            Stage stage = (Stage) etapesContainer.getScene().getWindow();
            Scene jeuScene = new Scene(jeuView, 1400, 900);
            stage.setScene(jeuScene);
            stage.setMaximized(true);
            stage.setTitle("Jeu des Labyrinthes - Mode Progression");

            System.out.println("[\u001B[34mDEBUG\u001B[0m] Jeu lancé !");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void retour() {
        System.out.println("Retour");
    }
}
