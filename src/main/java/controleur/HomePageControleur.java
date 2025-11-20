package controleur;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
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
    public Button nouvellePartieButton;
    public VBox contentPage;
    public Text modeLibreText;
    public Text modeProgressionText;
    public Text leaderboardText;
    private boolean modeProgression;
    private boolean isChargerProfilActive;
    private Sauvegarde saves;
    public VBox leaderboardContainer;

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
        leaderboardText.getStyleClass().removeAll("selected");
        modeProgressionText.getStyleClass().add("selected");
        modeLibreText.getStyleClass().removeAll("selected");
        modeProgression = true;
        nouvellePartieButton.setVisible(true);
        leaderboardContainer.setVisible(false);
        leaderboardContainer.setManaged(false);
    }

    /**
     * Sélectionne le mode libre et met à jour l'interface en conséquence.
     */
    public void modeLibre() {
        nomMode.setText("Mode libre");
        descriptionMode.setText("Entrainez vous à l’infini dans le mode libre !");
        chargerProfilButton.setVisible(false);
        leaderboardText.getStyleClass().removeAll("selected");
        chargerProfilButton.maxWidth(0);
        modeLibreText.getStyleClass().add("selected");
        modeProgressionText.getStyleClass().removeAll("selected");
        modeProgression = false;
        nouvellePartieButton.setVisible(true);
        leaderboardContainer.setVisible(false);
        leaderboardContainer.setManaged(false);

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

        afficherLeaderboard();
    }

    private void afficherLeaderboard() {
        // Centrer la zone entière
        leaderboardText.getStyleClass().add("selected");
        modeProgressionText.getStyleClass().removeAll("selected");
        modeLibreText.getStyleClass().removeAll("selected");
        contentPage.setAlignment(Pos.CENTER);
        leaderboardContainer.setVisible(true);
        leaderboardContainer.setManaged(true);
        leaderboardContainer.getChildren().clear();
        leaderboardContainer.setAlignment(Pos.CENTER);
        leaderboardContainer.setFillWidth(false);

        Leaderboard leaderboard = new Leaderboard(Jeu.getInstance().getSauvegarde());
        List<Joueur> joueurs = leaderboard.getClassementComplet();

        // --- ENTÊTE ---
        HBox header = new HBox(10);
        header.getStyleClass().add("leaderboard-header");
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(10));

        Label rankHeader = new Label("Rang");
        Label pseudoHeader = new Label("Pseudo");
        Label scoreHeader = new Label("Score");

        // Permettre aux labels de s'étirer et centrer leur texte
        rankHeader.setPrefWidth(60);
        rankHeader.setMaxWidth(Double.MAX_VALUE);
        rankHeader.setAlignment(Pos.CENTER);
        HBox.setHgrow(rankHeader, Priority.ALWAYS);

        pseudoHeader.setPrefWidth(240);
        pseudoHeader.setMaxWidth(Double.MAX_VALUE);
        pseudoHeader.setAlignment(Pos.CENTER);
        HBox.setHgrow(pseudoHeader, Priority.ALWAYS);

        scoreHeader.setPrefWidth(120);
        scoreHeader.setMaxWidth(Double.MAX_VALUE);
        scoreHeader.setAlignment(Pos.CENTER);
        HBox.setHgrow(scoreHeader, Priority.ALWAYS);

        header.getChildren().addAll(rankHeader, pseudoHeader, scoreHeader);
        leaderboardContainer.getChildren().add(header);

        // --- LIGNES ---
        int rang = 1;
        for (Joueur j : joueurs) {
            HBox row = new HBox(10);
            row.getStyleClass().add("leaderboard-row");
            row.setAlignment(Pos.CENTER);
            row.setPadding(new Insets(8));
            row.setMaxWidth(800); // optionnel : limite la largeur du tableau pour un rendu centré

            Label rankLabel = new Label(String.valueOf(rang));
            rankLabel.setPrefWidth(60);
            rankLabel.setMaxWidth(Double.MAX_VALUE);
            rankLabel.setAlignment(Pos.CENTER);
            HBox.setHgrow(rankLabel, Priority.ALWAYS);

            Label pseudoLabel = new Label(j.getPseudo());
            pseudoLabel.setPrefWidth(240);
            pseudoLabel.setMaxWidth(Double.MAX_VALUE);
            pseudoLabel.setAlignment(Pos.CENTER);
            HBox.setHgrow(pseudoLabel, Priority.ALWAYS);

            Label scoreLabel = new Label(String.valueOf(j.getScore()));
            scoreLabel.setPrefWidth(120);
            scoreLabel.setMaxWidth(Double.MAX_VALUE);
            scoreLabel.setAlignment(Pos.CENTER);
            HBox.setHgrow(scoreLabel, Priority.ALWAYS);

            row.getChildren().addAll(rankLabel, pseudoLabel, scoreLabel);
            leaderboardContainer.getChildren().add(row);
            rang++;
        }

        if (joueurs.isEmpty()) {
            Label vide = new Label("Aucun joueur enregistré.");
            vide.getStyleClass().add("empty-message");
            // Centrer le message vide aussi
            VBox wrapper = new VBox(vide);
            wrapper.setAlignment(Pos.CENTER);
            leaderboardContainer.getChildren().add(wrapper);
        }
    }
}
