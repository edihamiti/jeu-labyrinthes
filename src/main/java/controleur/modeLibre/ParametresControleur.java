package controleur.modeLibre;

import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

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

    @FXML
    public void initialize() {
        largeurField.setText("" + largeur);
        hauteurField.setText("" + hauteur);
        pourcentageMursField.setText("" + pourcentageMurs);

        largeurField.setOnAction(this::onLargeurChange);
        hauteurField.setOnAction(this::onHauteurChange);
        pourcentageMursField.setOnAction(event -> onPourcentageChange((ActionEvent) event));
        pourcentageMursSlider.valueProperty().addListener(this::onPourcentageChangeFromSlider);;
    }

    public void onLargeurChange(ActionEvent event) {
        try {
            largeur = Integer.parseInt(this.largeurField.getText());
            updateLargeur(largeur);
        } catch (NumberFormatException e) {
            largeurField.setText("" + this.largeur);
        }
    }

    public void onHauteurChange(ActionEvent event) {
        try {
            hauteur = Integer.parseInt(this.hauteurField.getText());
            updateHauteur(hauteur);
        } catch (NumberFormatException e) {
            hauteurField.setText("" + this.hauteur);
        }
    }

    public void onPourcentageChangeFromSlider(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
        updatePourcentageMurs(newValue.doubleValue());
    }

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

    public void lancerModeLibre() {
        try {
            System.out.println("Lancement du mode libre");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Jeu.fxml"));
            Parent jeuView = loader.load();

            // TODO: Enregistrer les valeurs du formulaire quelque part??
            // faire un Jeu.getInstance().setParametres(largeur, hauteur, pourcentageMurs) ?? aucune idée besoin d'aide là dessus

            Stage stage = (Stage) validerButton.getScene().getWindow();

            Scene jeuScene = new Scene(jeuView, 1400, 900);

            stage.setScene(jeuScene);
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
