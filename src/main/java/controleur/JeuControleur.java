package controleur;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class JeuControleur {
    public Button retourMenu;

    public void pauseJeu(ActionEvent actionEvent) {
    }

    public void recommencerJeu(ActionEvent actionEvent) {
    }

    public void retourMenu(ActionEvent actionEvent) throws IOException {
        AppControleur.getInstance().MenuPrincipal();
    }
}
