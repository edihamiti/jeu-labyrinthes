package controleur;

import modele.defi.Defi;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import modele.Jeu;
import modele.Joueur;
import modele.ModeJeu;
import vue.EtapesRendu;
import vue.ParametresLabyrinthe;
import vue.Router;

import java.io.IOException;

/**
 * Contrôleur pour le mode progression du jeu de labyrinthe.
 */
public class ModeProgressionControleur extends Controleur {
    @FXML
    private VBox etapesContainer;
    @FXML
    private HBox pointsContainer;

    /**
     * Initialise les composants de la page de progression.
     */
    @FXML
    public void initialize() {
        // L'initialisation qui dépend de jeu est faite dans setJeu()
    }

    @Override
    public void setJeu(Jeu jeu) {
        super.setJeu(jeu);
        // Initialiser les éléments qui dépendent de jeu une fois qu'il est injecté
        if (jeu != null) {
            jeu.setModeJeu(ModeJeu.MODE_PROGRESSION);
            Joueur joueur = jeu.getJoueur();
            etapesContainer.getChildren().add(
                    EtapesRendu.render(joueur.getProgression(), defi -> {
                        System.out.println("Defi sélectionné : " + defi);
                        lancerModeProgression(defi);
                    })
            );
            EtapesRendu.renderPoints(jeu.getJoueur().getScore(), this.pointsContainer);
        }
    }

    /**
     * Lance le mode progression du jeu avec le défi sélectionné.
     */
    public void lancerModeProgression(Defi defi) {
        try {
            System.out.println("[DEBUG] Enregistrement du labyrinthe dans le Jeu");
            // Enregistrer le défi en cours
            jeu.setModeJeu(ModeJeu.MODE_PROGRESSION);
            jeu.setDefiEnCours(defi);
            jeu.setLabyrinthe(defi);

            System.out.println("[DEBUG] Lancement du mode progression");

            // Créer les paramètres du labyrinthe
            ParametresLabyrinthe params = new ParametresLabyrinthe(
                defi.largeur(), defi.hauteur(), defi.pourcentageMurs(),
                defi.distanceMin(), defi.typeLabyrinthe()
            );

            // Utiliser le Router pour naviguer vers le jeu et lui envoyer les paramètres
            Router.route("/Jeu.fxml", params);


            System.out.println("[\u001B[34mDEBUG\u001B[0m] Jeu lancé !");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void retour() {
        try {
            appControleur.resetGame();
            Router.back();
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Erreur lors du retour vers le menu principal");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }
}
