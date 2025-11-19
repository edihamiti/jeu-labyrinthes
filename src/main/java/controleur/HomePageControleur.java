package controleur;

import boutique.GestionnaireBoutique;
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
import modele.Joueur;

import java.io.IOException;

/**
 * Contrôleur pour la page d'accueil de l'application.
 */
public class HomePageControleur extends Controleur {

    @FXML
    public Label nomMode;
    @FXML
    public Label descriptionMode;
    @FXML
    public Button button;
    @FXML
    public VBox contentPage;
    @FXML
    public Text modeLibreText;
    @FXML
    public Text modeProgressionText;
    @FXML
    public Button boutiqueButton;
    @FXML
    public Button inventaireButton;
    @FXML
    public Text boutiqueText;
    @FXML
    public Text inventaireText;

    private boolean modeProgression;


    /**
     * Initialise les composants de la page d'accueil.
     */
    @FXML
    public void initialize() {
        // Sélectionner par défaut le mode progression
        modeProgression();

        HBox.setHgrow(button, Priority.ALWAYS);
        button.setMaxWidth(Double.MAX_VALUE);

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
            if (modeProgression) {
                loader = new FXMLLoader(getClass().getResource("/ModeProgression.fxml"));
            } else {
                loader = new FXMLLoader(getClass().getResource("/ModeLibreParametres.fxml"));
            }
            Parent jeuView = loader.load();

            // Injecter les dépendances dans le contrôleur
            Object controller = loader.getController();
            if (controller instanceof Controleur) {
                ((Controleur) controller).setJeu(this.jeu);
                ((Controleur) controller).setAppControleur(this.appControleur);
            }

            Stage stage = (Stage) button.getScene().getWindow();

            Scene jeuScene = new Scene(jeuView, 1400, 900);

            stage.setScene(jeuScene);
            stage.setMaximized(true);
            stage.setTitle("Jeu des Labyrinthes");

            System.out.println("Jeu lancé !");

        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Ouvre la boutique.
     */
    public void boutique() {
        try {
            String id = "guest";
            Joueur j = jeu.getJoueur();
            if (j != null) {
                id = String.valueOf(j.getPseudo());
            }
            Stage stage = (Stage) boutiqueButton.getScene().getWindow();
            GestionnaireBoutique.getInstance().ouvrirBoutique(stage, id);
        } catch (Exception e) {
            System.err.println("Erreur lors de l'ouverture de la boutique : " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Ouvre l'inventaire des cosmétiques.
     */
    public void inventaire() {
        try {
            String id = "guest";
            Joueur j = jeu.getJoueur();
            if (j != null) {
                id = String.valueOf(j.getPseudo());
            }
            Stage stage = (Stage) inventaireButton.getScene().getWindow();
            GestionnaireBoutique.getInstance().ouvrirInventaire(stage, id);
        } catch (Exception e) {
            System.err.println("Erreur lors de l'ouverture de l'inventaire : " + e.getMessage());
            e.printStackTrace();
        }
    }
}
