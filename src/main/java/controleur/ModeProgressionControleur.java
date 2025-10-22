package controleur;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import modele.*;
import vue.EtapesRendu;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ModeProgressionControleur {

    private Labyrinthe labyrinthe;
    private final Sauvegarde sauvegarde = new Sauvegarde();
    private Joueur joueur;

    @FXML
    public Button validerForm;
    @FXML
    public TextField pseudoTextField;
    @FXML
    private VBox etapesContainer;

    @FXML
    public void initialize() {
        sauvegarde.chargerJoueurs();
        String[] etapes = {"1", "2", "3"};

        Map<String, Image> difficultees = new HashMap<>();

        difficultees.put("Facile", new Image(getClass().getResourceAsStream("/img/difficultees/facile.png")));
        difficultees.put("Moyen", new Image(getClass().getResourceAsStream("/img/difficultees/moyen.png")));
        difficultees.put("Difficile", new Image(getClass().getResourceAsStream("/img/difficultees/difficile.png")));

        etapesContainer.getChildren().add(EtapesRendu.render(difficultees, etapes));
    }

    public void initJoueur() throws PseudoException {
        String pseudo = pseudoTextField.getText().trim();
        this.joueur = sauvegarde.getJoueurParPseudo(pseudo) != null ? sauvegarde.getJoueurParPseudo(pseudo) : new Joueur(pseudo);
        System.out.println("[DEBUG] init joueur" + this.joueur.toString());
    }

    public void validerForm(ActionEvent actionEvent) throws PseudoException {
        initJoueur();
        this.labyrinthe = new Labyrinthe(Defi.DIFFICILE1);
        lancerModeLibre();
    }

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
