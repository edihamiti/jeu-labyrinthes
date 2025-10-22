package vue;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.Map;

public class EtapesRendu {
    public static VBox render(Map<String, Image> difficulties, String[] etapes) {
        VBox root = new VBox(15);
        root.setPrefWidth(VBox.USE_PREF_SIZE);
        root.setMaxWidth(VBox.USE_PREF_SIZE);

        for (String step : etapes) {
            root.getChildren().add(createStepCard(step, difficulties));
        }

        return root;
    }

    private static VBox createStepCard(String step, Map<String, Image> difficulties) {
        VBox root = new VBox(10);
        root.getStyleClass().add("step-card");

        Text label = new Text("Étape " + step);
        label.getStyleClass().add("step-label");
        root.getChildren().add(label);

        HBox difficultyContainer = new HBox(10);

        for (String difficulty : difficulties.keySet()) {
            difficultyContainer.getChildren().add(createDifficultyIcon(difficulties.get(difficulty), difficulty));
        }

        root.getChildren().add(difficultyContainer);

        return root;
    }

    private static Button createDifficultyIcon(Image image, String difficulty) {
        Button root = new Button();
        VBox graphics = new VBox(3);
        ImageView icon = new ImageView(image);
        icon.setFitHeight(50);
        icon.setFitWidth(50);
        graphics.getChildren().addAll(
                icon,
                new Text(difficulty)
        );
        root.setGraphic(graphics);
        return root;
    }


}
