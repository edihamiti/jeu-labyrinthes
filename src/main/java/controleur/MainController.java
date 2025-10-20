package controleur;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.io.IOException;


public class MainController {

    @FXML
    public ScrollPane content;
    // Constructeur par défaut (obligatoire pour JavaFX)
    public MainController() {
    }

    // Méthode d'initialisation appelée automatiquement après le chargement du FXML
    @FXML
    public void initialize() throws IOException {
        FXMLLoader homepageLoader = new FXMLLoader(getClass().getResource("/HomePage.fxml"));
        content.setFitToWidth(true);
        content.setFitToHeight(true);
        content.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        HBox.setHgrow(content, Priority.ALWAYS);
        VBox.setVgrow(content, Priority.ALWAYS);

        content.setContent(homepageLoader.load());
    }
}
