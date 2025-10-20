package controleur;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;


public class HomePageControleur {

    @FXML
    public Label nomMode;
    public Label descriptionMode;
    public Button chargerProfilButton;
    public Button nouvellePartieButton;
    public Text progressModeText;
    public Text libreModeText;
    public VBox contentPage;
    private boolean modeProgression;

    // Constructeur par défaut (obligatoire pour JavaFX)
    public HomePageControleur() {
    }

    // Méthode d'initialisation appelée automatiquement après le chargement du FXML
    @FXML
    public void initialize() {
        modeProgression = true;

        Rectangle clip = new Rectangle();
        clip.setArcWidth(40);
        clip.setArcHeight(40);

        // Lier le width et la hauteur du clip aux dimensions du VBox
        clip.widthProperty().bind(contentPage.widthProperty());
        clip.heightProperty().bind(contentPage.heightProperty());

        contentPage.setClip(clip);
    }

    public void modeProgression(){
        nomMode.setText("Mode progression");
        descriptionMode.setText("Complète des labyrinthes pour débloquer de nouveaux niveaux");
        chargerProfilButton.setVisible(true);
        modeProgression = true;
    }

    public void modeLibre() {
        nomMode.setText("Mode libre");
        descriptionMode.setText("Vous êtes dans le mode libre");
        chargerProfilButton.setVisible(false);
        chargerProfilButton.maxWidth(0);
        modeProgression = false;
    }
}
