package controleur;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

/**
 * Contrôleur pour le pop-up d'information sur les commandes de jeu.
 */
public class PopupCommandesControleur {

    /**
     * Gère les événements clavier sur le pop-up.
     * Ferme le pop-up si la touche Entrée est pressée.
     */
    @FXML
    private void handleKeyPress(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            fermerPopup(event);
        }
    }

    /**
     * Ferme le pop-up lorsque l'utilisateur clique sur le bouton ou appuie sur Entrée.
     */
    @FXML
    private void fermerPopup(javafx.event.Event event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}

