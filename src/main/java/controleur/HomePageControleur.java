package controleur;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
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
import modele.Jeu;
import modele.Joueur;
import modele.Sauvegarde;

import java.io.IOException;
import java.util.HashMap;

/**
 * Contrôleur pour la page d'accueil de l'application.
 */
public class HomePageControleur {

    @FXML
    public Label nomMode;
    public Label descriptionMode;
    public VBox chargerProfilButton;
    public VBox profilsContainer;
    public Button nouvellePartieButton;
    public VBox contentPage;
    public Text modeLibreText;
    public Text modeProgressionText;
    private boolean modeProgression;
    private boolean isChargerProfilActive;
    private Sauvegarde saves;

    // Constructeur par défaut (obligatoire pour JavaFX)
    public HomePageControleur() {
    }


    // Méthode d'initialisation appelée automatiquement après le chargement du FXML
    /**
     * Initialise les composants de la page d'accueil.
     */
    @FXML
    public void initialize() {
        modeProgression = true;
        isChargerProfilActive = false;
        saves = Jeu.getInstance().getSauvegarde();
        saves.chargerJoueurs();

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

    /**
     * Sélectionne le mode progression et met à jour l'interface en conséquence.
     */
    public void modeProgression() {
        nomMode.setText("Mode progression");
        descriptionMode.setText("Complète des labyrinthes pour débloquer de nouveaux niveaux !");
        chargerProfilButton.setVisible(true);
        modeProgressionText.getStyleClass().add("selected");
        modeLibreText.getStyleClass().removeAll("selected");
        modeProgression = true;
    }

    /**
     * Sélectionne le mode libre et met à jour l'interface en conséquence.
     */
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

    /**
     * Lance le jeu en fonction du mode sélectionné.
     */
    public void lancerJeu() {
        try {
            FXMLLoader loader;
            if (modeProgression && Jeu.getInstance().getJoueur() == null) {
                loader = new FXMLLoader(getClass().getResource("/ChoisirPseudo.fxml"));
            } else if (modeProgression) {
                loader = new FXMLLoader(getClass().getResource("/ModeProgression.fxml"));
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

    public void chargerProfil() {
        isChargerProfilActive = !isChargerProfilActive;

        if (isChargerProfilActive) {
            Node label = chargerProfilButton.getChildren().get(0);
            chargerProfilButton.getChildren().clear();
            chargerProfilButton.getChildren().add(
                    label
            );

            HashMap<String, Joueur> profiles = saves.getJoueurs();

            for (String name : profiles.keySet()) {
                Joueur joueur = profiles.get(name);
                Button button = new Button(name);
                button.setMaxWidth(Double.MAX_VALUE);
                button.getStyleClass().add("charger-profile-button");
                button.setOnAction(event -> {
                    Jeu.getInstance().setJoueur(joueur);
                    lancerJeu();
                });
                chargerProfilButton.getChildren().add(button);
            }
        } else {
            Node label = chargerProfilButton.getChildren().get(0);
            chargerProfilButton.getChildren().clear();
            chargerProfilButton.getChildren().add(
                    label
            );
        }

    }

}
