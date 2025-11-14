package controleur;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
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
import modele.Leaderboard;
import modele.Sauvegarde;
import vue.ChargerProfileRendu;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

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

        Node label = chargerProfilButton.getChildren().get(0);
        chargerProfilButton.getChildren().clear();
        chargerProfilButton.getChildren().add(
                label
        );

        if (isChargerProfilActive) {
            HashMap<String, Joueur> profiles = saves.getJoueurs();

            chargerProfilButton.getChildren().add(ChargerProfileRendu.render(profiles, joueur -> {
                Jeu.getInstance().setJoueur(joueur);
                lancerJeu();
            }));
        }

    }

    public void leaderboard() {
        nomMode.setText("Leaderboard");
        descriptionMode.setText("Visualisez les meilleurs scores !");

        // cacher les autres boutons
        chargerProfilButton.setVisible(false);
        nouvellePartieButton.setVisible(false);

        // montrer la VBox du leaderboard
        profilsContainer.setVisible(true);

        // récupérer tous les joueurs triés
        Leaderboard leaderboard = new Leaderboard(Jeu.getInstance().getSauvegarde());
        List<Joueur> joueurs = leaderboard.getClassementComplet();

        profilsContainer.getChildren().clear();

        // entête
        HBox header = new HBox(10);
        header.setAlignment(Pos.CENTER_LEFT);
        Label rankHeader = new Label("Rang"); rankHeader.setPrefWidth(60);
        Label pseudoHeader = new Label("Pseudo"); pseudoHeader.setPrefWidth(240);
        Label scoreHeader = new Label("Score"); scoreHeader.setPrefWidth(120);
        header.getChildren().addAll(rankHeader, pseudoHeader, scoreHeader);
        profilsContainer.getChildren().add(header);

        // lignes du leaderboard
        int rang = 1;
        for (Joueur j : joueurs) {
            HBox row = new HBox(10);
            row.setAlignment(Pos.CENTER_LEFT);
            row.getChildren().addAll(
                    new Label(String.valueOf(rang)),
                    new Label(j.getPseudo()),
                    new Label(String.valueOf(j.getScore()))
            );
            profilsContainer.getChildren().add(row);
            rang++;
        }

        if (joueurs.isEmpty()) {
            profilsContainer.getChildren().add(new Label("Aucun joueur à afficher."));
        }
    }


}
