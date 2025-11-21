package controleur;

import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import modele.Jeu;
import modele.Joueur;
import modele.PseudoException;
import vue.ProfilsRendu;
import vue.Router;

import java.io.IOException;

/**
 * Contrôleur pour la sélection du pseudo du joueur.
 */
public class ChoisirPseudoControleur extends Controleur {
    @FXML
    public TextField pseudoField;
    @FXML
    public Button startButton;
    public String pseudo = "";
    @FXML
    public ScrollPane conteneurProfils;

    /**
     * Initialise le contrôleur et configure les observables d'événements.
     */
    @FXML
    public void initialize() {
        conteneurProfils.setFitToWidth(true);
        pseudoField.setOnAction(_ -> lancerJeu());
        pseudoField.setText(pseudo);
        pseudoField.textProperty().addListener((ObservableValue<? extends String> _, String oldValue, String newValue) -> {
            if (newValue.trim().length() > 15) {
                pseudoField.setText(oldValue);
            } else {
                pseudoField.setText(newValue.trim());
                this.pseudo = newValue.trim();
            }
        });
    }

    @Override
    public void setJeu(Jeu jeu) {
        super.setJeu(jeu);
        chargerJoueurs();
    }

    /**
     * Cette méthode permet de charger les joueurs existants dans le jeu et ensuite affiche la liste.
     */
    private void chargerJoueurs() {
        if (jeu != null) {
            jeu.getSauvegarde().chargerJoueurs();
            conteneurProfils.setContent(ProfilsRendu.render(jeu.getSauvegarde().getJoueurs(), (Joueur joueur) -> {
                System.out.printf("Connexion de %s%n", joueur.getPseudo());
                choisirJouer(joueur);
            }));
        }
    }

    /**
     * Lance le mode progression du jeu avec le pseudo sélectionné.
     */
    public void lancerJeu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/HomePage.fxml"));

            jeu.setJoueur(pseudo);
            System.out.println("[\u001B[34mDEBUG\u001B[0m] Joueur initialisé dans le modèle Jeu");

//            Parent homepageView = loader.load();
//            HomePageControleur controleur = loader.getController();
//            controleur.setJeu(this.jeu);
//            controleur.setAppControleur(this.appControleur);
//
//            Stage stage = (Stage) startButton.getScene().getWindow();
//            Scene jeuScene = new Scene(homepageView, 1400, 900);
//            stage.setScene(jeuScene);
//            stage.setMaximized(true);

            Router.route("/HomePage.fxml");

            System.out.println("Jeu lancé !");
        } catch (IOException e) {
            System.err.println("Erreur lors du lancement du mode progression !");
            System.err.println(e.getMessage());
            System.err.println("Voici les valeurs du formulaire : ");
            System.err.println("\tPseudo : " + pseudo);
            System.err.println("Trace :");
            e.printStackTrace();
        } catch (PseudoException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur avec le pseudo");
            alert.setHeaderText("Votre pseudo est invalide !");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    public void choisirJouer(Joueur joueur) {
        this.pseudo = joueur.getPseudo();
        lancerJeu();
    }
}
