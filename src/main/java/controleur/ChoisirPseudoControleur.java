package controleur;

import controleur.AppControleur;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import modele.PseudoException;

import java.io.IOException;

public class ChoisirPseudoControleur {
    @FXML
    public TextField pseudoField;
    @FXML
    public Button startButton;
    public String pseudo = "";

    @FXML
    public void initialize() {
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

    public void lancerModeLibre() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModeProgression.fxml"));

            Parent progressionView = loader.load();
            controleur.ModeProgressionControleur jeuControleur = loader.getController();

            try {
                modele.Joueur joueur = new modele.Joueur(this.pseudo);
                System.out.println(joueur);
            } catch (PseudoException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur de pseudo");
                alert.setHeaderText("Le pseudo est invalide !");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
                return;
            }

            Stage stage = (Stage) startButton.getScene().getWindow();
            Scene jeuScene = new Scene(progressionView, 1400, 900);
            stage.setMaximized(true); // pourquoi ça maximize pas???? TODO: Faire en sorte que ça maximize
            stage.setScene(jeuScene);

            // TODO: Set le joueur dans le mode progression?
            // Je regarderais comment vous avez fait pour les paramètres du mode libre mais j'ai pas trop le temps pour l'instant

            System.out.println("Jeu lancé !");
        } catch (IOException e) {
            System.err.println("Erreur lors du lancement du mode progression !");
            System.err.println(e.getMessage());
            System.err.println("Voici les valeurs du formulaire : ");
            System.err.println("\tPseudo : " + pseudo);
        }
    }
}
