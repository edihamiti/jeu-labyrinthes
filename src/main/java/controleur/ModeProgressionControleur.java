package controleur;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.HashMap;
import java.util.Map;

public class ModeProgressionControleur {

    @FXML
    private VBox etapesContainer;

    @FXML
    public void initialize() {
        String[] etapes = {"1", "2", "3"};

        Map<String, Image> difficultees = new HashMap<>();

        difficultees.put("Facile", new Image(getClass().getResourceAsStream("/img/difficultees/facile.png")));
        difficultees.put("Moyen", new Image(getClass().getResourceAsStream("/img/difficultees/moyen.png")));
        difficultees.put("Difficile", new Image(getClass().getResourceAsStream("/img/difficultees/difficile.png")));

        double prefWidth = 300;
        double prefHeight = 90;

        for (String step : etapes) {
            HBox stepCard = createStepCard(step, difficultees);
            stepCard.setPrefWidth(prefWidth);
            stepCard.setPrefHeight(prefHeight);
            etapesContainer.getChildren().add(stepCard);
        }


    }

    private HBox createStepCard(String step, Map<String, Image> difficultees) {
        HBox root = new HBox();
        root.getStyleClass().add("step-card");

        Text stepLabel = new Text("Étape " + step);
        stepLabel.getStyleClass().add("step-label");

        root.getChildren().add(stepLabel);

        Button lancerButton = new Button("Lancer");
        lancerButton.getStyleClass().add("button-filled");


        return root;
    }
}
