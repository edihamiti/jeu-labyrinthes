package controleur;

import javafx.fxml.FXML;
import javafx.scene.control.Label;


public class HomePageControleur {

    @FXML
    public Label nomMode;
    public Label descriptionMode;

    // Constructeur par défaut (obligatoire pour JavaFX)
    public HomePageControleur() {
    }

    // Méthode d'initialisation appelée automatiquement après le chargement du FXML
    @FXML
    public void initialize() {
    }

    public void modeProgression(){
        nomMode.setText("Mode progression");
        descriptionMode.setText("Complète des labyrinthes pour débloquer de nouveaux niveaux");
    }

    public void modeLibre() {
        nomMode.setText("Mode libre");
        descriptionMode.setText("Vous êtes dans le mode libre");
    }
}
