package controleur;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import modele.*;
import vue.EtapesRendu;
import java.io.IOException;

/**
 * Contrôleur pour le mode progression du jeu de labyrinthe.
 */
public class ModeProgressionControleur {
    @FXML
    private VBox etapesContainer;

    /**
     * Initialise le contrôleur et configure les étapes du mode progression.
     */
    @FXML
    public void initialize() {
        Joueur joueur = Jeu.getInstance().getJoueur();
        var progression = joueur.getProgression();
        progression.forEach((defi, termine) -> progression.put(defi, true));
        etapesContainer.getChildren().add(
                EtapesRendu.render(joueur.getProgression(), defi -> {
                    System.out.println("Defi sélectionné : " + defi);
                    lancerModeLibre(defi);
                })
        );
    }

    /**
     * Lance le mode progression du jeu avec le défi sélectionné.
     */
    public void lancerModeLibre(Defi defi) {
        try {
            System.out.println("[DEBUG] Enregistrement du labyrinthe dans le Jeu");
            // Enregistrer le défi en cours
            Jeu jeu = Jeu.getInstance();
            jeu.setDefiEnCours(defi);
            jeu.setLabyrinthe(defi);

            System.out.println("[DEBUG] Lancement du mode progression");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Jeu.fxml"));
            Parent jeuView = loader.load();


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
}
