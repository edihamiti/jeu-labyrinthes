package controleur;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;


public class HomePageControleur {

    @FXML
    public Label nomMode;
    public Label descriptionMode;
    public Button chargerProfilButton;
    public Button nouvellePartieButton;
    public VBox contentPage;
    public Text modeLibreText;
    public Text modeProgressionText;
    private boolean modeProgression;

    // Constructeur par défaut (obligatoire pour JavaFX)
    public HomePageControleur() {
    }

    // Méthode d'initialisation appelée automatiquement après le chargement du FXML
    @FXML
    public void initialize() {
        modeProgression = true;

        HBox.setHgrow(chargerProfilButton, Priority.ALWAYS);
        HBox.setHgrow(nouvellePartieButton, Priority.ALWAYS);
        nouvellePartieButton.setMaxWidth(Double.MAX_VALUE);
        chargerProfilButton.setMaxWidth(Double.MAX_VALUE);

        Rectangle clip = new Rectangle();
        clip.setArcWidth(40);
        clip.setArcHeight(40);

        // Lier le width et la hauteur du clip aux dimensions du VBox
        clip.widthProperty().bind(contentPage.widthProperty());
        clip.heightProperty().bind(contentPage.heightProperty());

        contentPage.setClip(clip);
    }

    public void modeProgression() {
        nomMode.setText("Mode progression");
        descriptionMode.setText("Complète des labyrinthes pour débloquer de nouveaux niveaux !");
        chargerProfilButton.setVisible(true);
        modeProgressionText.getStyleClass().add("selected");
        modeLibreText.getStyleClass().removeAll("selected");
        modeProgression = true;
    }

    public void modeLibre() {
        nomMode.setText("Mode libre");
        descriptionMode.setText("Entrainez vous à l’infini dans le mode libre !");
        chargerProfilButton.setVisible(false);
        chargerProfilButton.maxWidth(0);
        modeLibreText.getStyleClass().add("selected");
        modeProgressionText.getStyleClass().removeAll("selected");
        modeProgression = false;
    }

    public void quit() {
        System.exit(0);
    }

    public void lancerJeu() {
        try {
            FXMLLoader loader;
            if (modeProgression) {
                loader = new FXMLLoader(getClass().getResource("/Jeu.fxml"));
            } else {
                loader = new FXMLLoader(getClass().getResource("/ModeLibreParametres.fxml"));
            }
            Parent jeuView = loader.load();

            Stage stage = (Stage) nouvellePartieButton.getScene().getWindow();

            Scene jeuScene = new Scene(jeuView, 1400, 900);

            stage.setScene(jeuScene);
            stage.setMaximized(true);
            stage.setTitle("Jeu des Labyrinthes");

            System.out.println("Jeu lancé !");

        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
