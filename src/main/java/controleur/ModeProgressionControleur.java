package controleur;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import modele.*;
import vue.EtapesRendu;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Contrôleur pour le mode progression du jeu de labyrinthe.
 */
public class ModeProgressionControleur {

    private Labyrinthe labyrinthe;
    private final Sauvegarde sauvegarde = new Sauvegarde();
    private Joueur joueur;

    @FXML
    public Button validerForm;

    @FXML
    private VBox etapesContainer;

    /**
     * Initialise le contrôleur et configure les étapes du mode progression.
     */
    @FXML
    public void initialize() {
        String[] etapes = {"1", "2", "3"};

        this.joueur = Jeu.getInstance().getJoueur();
        var progression = joueur.getProgression();
        progression.forEach((defi, termine) -> progression.put(defi, true));
        etapesContainer.getChildren().add(EtapesRendu.render(joueur.getProgression()));
        Map<String, Image> difficultees = new HashMap<>();
    }

    /*public void initJoueur() throws PseudoException {
        String pseudo = pseudoTextField.getText().trim();
        this.joueur = sauvegarde.getJoueurParPseudo(pseudo) != null ? sauvegarde.getJoueurParPseudo(pseudo) : new Joueur(pseudo);
        System.out.println("[DEBUG] init joueur" + this.joueur.toString());
    }*/

    /**
     * Valide le formulaire et lance le mode libre.
     *
     * @param actionEvent l'événement d'action déclenché
     * @throws PseudoException si le pseudo est invalide
     */
    public void validerForm(ActionEvent actionEvent) throws PseudoException {
        this.labyrinthe = new Labyrinthe(Defi.DIFFICILE1);
        lancerModeLibre();
    }

    /**
     * Lance le mode libre du jeu.
     */
    public void lancerModeLibre() {
        try {
            System.out.println("[DEBUG] Lancement du mode progression");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Jeu.fxml"));
            Parent jeuView = loader.load();

            // TODO: Enregistrer les valeurs du formulaire quelque part??
            // faire un Jeu.getInstance().setParametres(largeur, hauteur, pourcentageMurs) ?? aucune idée besoin d'aide là dessus

            Stage stage = (Stage) validerForm.getScene().getWindow();

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
