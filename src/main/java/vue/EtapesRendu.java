package vue;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.Map;

/**
 * Classe responsable du rendu des étapes avec leurs difficultés.
 */
public class EtapesRendu {

    /**
     * Rend les étapes avec leurs difficultés sous forme de VBox.
     *
     * @param difficulties Map des difficultés avec leurs images associées.
     * @param etapes       Tableau des étapes à rendre.
     * @return VBox contenant les étapes rendues.
     */
    public static VBox render(Map<String, Image> difficulties, String[] etapes) {
        VBox root = new VBox(15);
        root.setPrefWidth(VBox.USE_PREF_SIZE);
        root.setMaxWidth(VBox.USE_PREF_SIZE);

        for (String step : etapes) {
            root.getChildren().add(createStepCard(step, difficulties));
        }

        return root;
    }

    /**
     * Crée une carte d'étape avec ses difficultés.
     *
     * @param step        L'étape à rendre.
     * @param difficulties Map des difficultés avec leurs images associées.
     * @return VBox représentant la carte d'étape.
     */
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

    /**
     * Crée une icône de difficulté avec une image et un label.
     *
     * @param image      L'image représentant la difficulté.
     * @param difficulty Le label de la difficulté.
     * @return Button représentant l'icône de difficulté.
     */
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
