package controleur;

import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import modele.Jeu;
import modele.ModeJeu;
import modele.PseudoException;
import modele.TypeLabyrinthe;

import java.io.IOException;

/**
 * Contrôleur pour les paramètres du mode libre du jeu de labyrinthe.
 */
public class ParametresControleur extends Controleur {
    private final double POURCENTAGE_MIN = 0.0;
    private final double POURCENTAGE_MAX = 100.0;
    public Spinner<Integer> largeurField;
    public Spinner<Integer> hauteurField;
    public TextField pourcentageMursField;
    public Button validerButton;
    public Slider pourcentageMursSlider;
    public ComboBox<String> typeLabyrintheField;
    public Spinner<Integer> distanceMinSpinner;
    public int largeur = 5;
    public int hauteur = 5;
    public double pourcentageMurs = 50.0;
    public int distanceMin = 1;
    public TypeLabyrinthe typeLabyrinthe = TypeLabyrinthe.ALEATOIRE;

    /**
     * Initialise les composants du formulaire et configure les observables d'événements.
     */
    @FXML
    public void initialize() {
        validerButton.getParent().sceneProperty().addListener((observableValue, scene, t1) -> {
            t1.addEventFilter(javafx.scene.input.KeyEvent.KEY_PRESSED, keyEvent -> {
                if (keyEvent.getCode() == KeyCode.ENTER) {
                    try {
                        lancerModeLibre();
                    } catch (PseudoException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        });
        largeurField.getValueFactory().setValue(largeur);
        hauteurField.getValueFactory().setValue(hauteur);
        pourcentageMursField.setText("" + pourcentageMurs);

        for (TypeLabyrinthe typeLabyrinthe : TypeLabyrinthe.values()) {
            typeLabyrintheField.getItems().add(typeLabyrinthe.getNom());
            if (typeLabyrinthe == this.typeLabyrinthe) {
                typeLabyrintheField.getSelectionModel().select(typeLabyrinthe.getNom());
            }
        }

        largeurField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            this.largeur = this.largeurField.getValue();
        });

        hauteurField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            this.hauteur = this.hauteurField.getValue();
        });
        pourcentageMursField.setOnAction(event -> onPourcentageChange((ActionEvent) event));
        pourcentageMursSlider.setValue(pourcentageMurs);
        pourcentageMursSlider.valueProperty().addListener(this::onPourcentageChangeFromSlider);

        distanceMinSpinner.getValueFactory().setValue(distanceMin);
        distanceMinSpinner.focusedProperty().addListener((obs, oldVal, newVal) -> {
            this.distanceMin = this.distanceMinSpinner.getValue();
        });
        updateAvailableFields();
    }

    @Override
    public void setJeu(Jeu jeu) {
        super.setJeu(jeu);
        // Initialiser le mode de jeu une fois que jeu est injecté
        if (jeu != null) {
            jeu.setModeJeu(ModeJeu.MODE_LIBRE);
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

    private void updatePourcentageMurs(double pourcentageMurs) {
        this.pourcentageMurs = Math.max(pourcentageMurs, POURCENTAGE_MIN);
        this.pourcentageMurs = Math.min(this.pourcentageMurs, POURCENTAGE_MAX);
        pourcentageMursField.setText("" + this.pourcentageMurs);
        pourcentageMursSlider.setValue(this.pourcentageMurs);
    }

    public void onTypeLabyrintheChange() {
        String typeLabyrinthe = typeLabyrintheField.getValue();
        System.out.println("Type de labyrinthe sélectionné : " + typeLabyrinthe);
        switch (typeLabyrinthe) {
            case ("Aléatoire"):
                this.typeLabyrinthe = TypeLabyrinthe.ALEATOIRE;
                break;
            case ("Parfait"):
                this.typeLabyrinthe = TypeLabyrinthe.PARFAIT;
                break;
            default:
                throw new IllegalArgumentException("Type de labyrinthe inconnu !");
        }
        this.updateAvailableFields();
    }

    /**
     * Gère la mise à jour des fields lors de la modification du type de labyrinthe.
     */
    private void updateAvailableFields() {
        switch (this.typeLabyrinthe) {
            case PARFAIT -> {
                pourcentageMursField.setDisable(true);
                pourcentageMursSlider.setDisable(true);
                pourcentageMurs = 100.0;

                distanceMinSpinner.setDisable(false);
            }
            case ALEATOIRE -> {
                pourcentageMursField.setDisable(false);
                pourcentageMursSlider.setDisable(false);
                pourcentageMurs = pourcentageMursSlider.getValue();

                distanceMinSpinner.setDisable(true);
            }
        }
    }


    /**
     * Gère le clic sur le bouton de retour vers le menu principal.
     */
    public void retourClicked() {
        appControleur.MenuPrincipal();
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
            System.out.println("\tTypeLabyrinthe : " + typeLabyrinthe);

            int distanceMinEffective;
            if (typeLabyrinthe == TypeLabyrinthe.PARFAIT) {
                distanceMinEffective = distanceMin;
            } else {
                distanceMinEffective = 1;
            }
            System.out.println("\tDistanceMin : " + distanceMinEffective);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Jeu.fxml"));
            Parent jeuView = loader.load();

            controleur.JeuControleur jeuControleur = loader.getController();

            // Injecter les dépendances dans le contrôleur
            if (jeuControleur != null) {
                jeuControleur.setJeu(this.jeu);
                jeuControleur.setAppControleur(this.appControleur);
            }

            // Appeler setParametresLab APRÈS le chargement du FXML
            jeuControleur.setParametresLab(largeur, hauteur, pourcentageMurs, distanceMinEffective, typeLabyrinthe);

            Stage stage = (Stage) validerButton.getScene().getWindow();
            Scene jeuScene = new Scene(jeuView, 1400, 900);
            stage.setScene(jeuScene);
            stage.setMaximized(true);
            stage.setTitle("Jeu des Labyrinthes - Mode Libre");
        } catch (IOException e) {
            System.err.println("Erreur lors du lancement du mode libre !");
            System.err.println(e.getMessage());
            System.err.println("Voici les valeurs du formulaire : ");
            System.err.println("\tLargeur : " + largeur + "\n\tHauteur : " + hauteur + "\n\tPourcentageMurs : " + pourcentageMurs + "\n\tTypeLabyrinthe : " + typeLabyrinthe + "\n\tDistanceMin : " + distanceMin);
            System.err.println("Trace :");
            e.printStackTrace();
        }
    }
}
