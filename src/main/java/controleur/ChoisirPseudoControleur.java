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
                jeuControleur.setJoueur(joueur); // TODO: Au lieu de stocker le joueur dans le controleur, le stocker dans le modele
                System.out.println(joueur);
            } catch (PseudoException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur de pseudo");
                alert.setHeaderText("Le pseudo est invalide !");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
                return;
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText("Une erreur est survenue !");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }

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
            System.err.println("Trace : ");
            e.printStackTrace();
        }
    }
}
