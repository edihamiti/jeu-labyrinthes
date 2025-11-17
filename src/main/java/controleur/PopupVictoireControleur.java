package controleur;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class PopupVictoireControleur {

    @FXML
    private VBox popupRoot;

    @FXML
    private Label tempsLabel;

    @FXML
    private Label scoreLabel;

    @FXML
    private Label deplacementLabel;

    private Runnable onRejouer;
    private Runnable onRetourMenu;

    public void setTemps(String temps) {
        tempsLabel.setText("Temps : " + temps);
        tempsLabel.setVisible(true);
        tempsLabel.setManaged(true);
    }

    public void setScore(String score) {
        scoreLabel.setText("Score : " + score);
        scoreLabel.setVisible(true);
        scoreLabel.setManaged(true);
    }

    public void setDeplacement(String deplacement) {
        deplacementLabel.setText("Déplacements : " + deplacement);
        deplacementLabel.setVisible(true);
        deplacementLabel.setManaged(true);
    }

    public void setRejouer(Runnable action) {
        this.onRejouer = action;
    }

    public void setRetourMenu(Runnable action) {
        this.onRetourMenu = action;
    }

    @FXML
    private void rejouer() {
        fermerPopup();
        if (onRejouer != null) {
            onRejouer.run();
        }
    }

    @FXML
    private void retourMenu() {
        fermerPopup();
        if (onRetourMenu != null) {
            onRetourMenu.run();
        }
    }

    @FXML
    private void handleKeyPress(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            rejouer();
        }
    }

    private void fermerPopup() {
        Stage stage = (Stage) popupRoot.getScene().getWindow();
        stage.close();
    }
}

