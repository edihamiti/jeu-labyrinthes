package controleur;

import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import modele.Jeu;
import modele.PseudoException;

import java.io.IOException;

/**
 * Contrôleur pour la sélection du pseudo du joueur.
 */
public class ChoisirPseudoControleur {
    @FXML
    public TextField pseudoField;
    @FXML
    public Button startButton;
    public String pseudo = "";

    /**
     * Initialise le contrôleur et configure les observables d'événements.
     */
    @FXML
    public void initialize() {
        pseudoField.setOnAction(e -> lancerModeProgression());
        pseudoField.setText(pseudo);
        pseudoField.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (newValue.trim().length() > 15) {
                pseudoField.setText(oldValue);
            } else {
                pseudoField.setText(newValue.trim());
                this.pseudo = newValue.trim();
            }
        });
    }

    /**
     * Lance le mode progression du jeu avec le pseudo sélectionné.
     */
    public void lancerModeProgression() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModeProgression.fxml"));

            Jeu.getInstance().setJoueur(pseudo);
            System.out.println("[\u001B[34mDEBUG\u001B[0m] Joueur initialisé dans le modèle Jeu");

            Parent progressionView = loader.load();
            controleur.ModeProgressionControleur jeuControleur = loader.getController();


            Stage stage = (Stage) startButton.getScene().getWindow();
            Scene jeuScene = new Scene(progressionView, 1400, 900);
            stage.setScene(jeuScene);
            stage.setMaximized(true);

            System.out.println("Jeu lancé !");
        } catch (IOException e) {
            System.err.println("Erreur lors du lancement du mode progression !");
            System.err.println(e.getMessage());
            System.err.println("Voici les valeurs du formulaire : ");
            System.err.println("\tPseudo : " + pseudo);
            System.err.println("Trace :");
            e.printStackTrace();
        } catch (PseudoException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur avec le pseudo");
            alert.setHeaderText("Votre pseudo est invalide !");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }
}
