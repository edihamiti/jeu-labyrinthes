package controleur.modeLibre;

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

/**
 * Contrôleur pour les paramètres du mode libre du jeu de labyrinthe.
 */
public class ParametresControleur {
    public TextField largeurField;
    public TextField hauteurField;
    public TextField pourcentageMursField;
    public Button validerButton;
    public Slider pourcentageMursSlider;
    public int largeur = 5;
    public int hauteur = 5;
    public double pourcentageMurs = 50.0;
    private final int LARGEUR_MIN = 1;
    private final int LARGEUR_MAX = 30;
    private final int HAUTEUR_MIN = 1;
    private final int HAUTEUR_MAX = 30;
    private final double POURCENTAGE_MIN = 0.0;
    private final double POURCENTAGE_MAX = 100.0;

    // Constructeur par défaut
    public ParametresControleur() {
    }

    /**
     * Initialise les composants du formulaire et configure les observables d'événements.
     */
    @FXML
    public void initialize() {
        largeurField.setText("" + largeur);
        hauteurField.setText("" + hauteur);
        pourcentageMursField.setText("" + pourcentageMurs);

        largeurField.setOnAction((e) -> {
            onLargeurChange();
        });
        largeurField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            onLargeurChange();
        });
        hauteurField.setOnAction((e) -> {
            onHauteurChange();
        });
        hauteurField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            onHauteurChange();
        });
        pourcentageMursField.setOnAction(event -> onPourcentageChange((ActionEvent) event));
        pourcentageMursSlider.valueProperty().addListener(this::onPourcentageChangeFromSlider);
        ;
    }

    /**
     * Gère le changement de la largeur du labyrinthe.
     */
    public void onLargeurChange() {
        try {
            largeur = Integer.parseInt(this.largeurField.getText());
            updateLargeur(largeur);
        } catch (NumberFormatException e) {
            largeurField.setText("" + this.largeur);
        }
    }

    /**
     * Gère le changement de la hauteur du labyrinthe.
     */
    public void onHauteurChange() {
        try {
            hauteur = Integer.parseInt(this.hauteurField.getText());
            updateHauteur(hauteur);
        } catch (NumberFormatException e) {
            hauteurField.setText("" + this.hauteur);
        }
    }

    /**
     * Gère le changement du pourcentage de murs via le slider.
     */
    public void onPourcentageChangeFromSlider(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
        updatePourcentageMurs(newValue.doubleValue());
    }

    /**
     * Gère le changement du pourcentage de murs via le champ de texte.
     */
    public void onPourcentageChange(ActionEvent event) {
        try {
            pourcentageMurs = Double.parseDouble(this.pourcentageMursField.getText());
            updatePourcentageMurs(pourcentageMurs);
        } catch (NumberFormatException e) {
            pourcentageMursField.setText(this.pourcentageMurs + "");
        }
    }

    private void updateLargeur(int largeur) {
        this.largeur = Math.max(largeur, LARGEUR_MIN);
        this.largeur = Math.min(this.largeur, LARGEUR_MAX);
        largeurField.setText("" + this.largeur);
    }

    private void updateHauteur(int hauteur) {
        this.hauteur = Math.max(hauteur, HAUTEUR_MIN);
        this.hauteur = Math.min(this.hauteur, HAUTEUR_MAX);
        hauteurField.setText("" + this.hauteur);
    }

    private void updatePourcentageMurs(double pourcentageMurs) {
        this.pourcentageMurs = Math.max(pourcentageMurs, POURCENTAGE_MIN);
        this.pourcentageMurs = Math.min(this.pourcentageMurs, POURCENTAGE_MAX);
        pourcentageMursField.setText("" + this.pourcentageMurs);
        pourcentageMursSlider.setValue(this.pourcentageMurs);
    }

    /**
     * Gère le clic sur le bouton de retour vers le menu principal.
     */
    public void retourClicked() {
        try {
            AppControleur.getInstance().MenuPrincipal();
        } catch (IOException e) {
            System.err.println("Erreur lors de la retour vers le menu principal !");
            System.err.println(e.getMessage());

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur !");
            alert.setHeaderText("Erreur lors de la retour vers le menu principal !");
            alert.setContentText("Details : " + e.getMessage());
            alert.showAndWait();

            System.exit(1);
        }
    }

    /**
     * Valide le formulaire et lance le mode libre.
     *
     * @throws PseudoException si le pseudo est invalide
     */
    public void lancerModeLibre() throws PseudoException {
        try {
            System.out.println("Lancement du mode libre avec les valeurs suivantes :");
            System.out.println("\tLargeur : " + largeur);
            System.out.println("\tHauteur : " + hauteur);
            System.out.println("\tPourcentageMurs : " + pourcentageMurs);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Jeu.fxml"));

            Parent jeuView = loader.load();
            controleur.JeuControleur jeuControleur = loader.getController();

            modele.Joueur joueur = new modele.Joueur("ModeLibre");

            // TODO: Enregistrer les valeurs du formulaire quelque part??
            // faire un Jeu.getInstance().setParametres(largeur, hauteur, pourcentageMurs) ?? aucune idée besoin d'aide là dessus
            jeuControleur.setParametresLab(largeur, hauteur, pourcentageMurs);
            //normalement ça devrait marcher comme ça

            Stage stage = (Stage) validerButton.getScene().getWindow();

            Scene jeuScene = new Scene(jeuView, 1400, 900);
            stage.setScene(jeuScene);
            stage.setMaximized(true);
            stage.setTitle("Jeu des Labyrinthes - Mode Libre");

            System.out.println("Jeu lancé !");
        } catch (IOException e) {
            System.err.println("Erreur lors du lancement du mode libre !");
            System.err.println(e.getMessage());
            System.err.println("Voici les valeurs du formulaire : ");
            System.err.println("\tLargeur : " + largeur + "\n\tHauteur : " + hauteur + "\n\tPourcentageMurs : " + pourcentageMurs);
            System.err.println("Trace :");
            e.printStackTrace();
        }
    }
}
