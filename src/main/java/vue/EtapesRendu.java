package vue;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import modele.Defi;
import modele.Joueur;

import java.util.*;

/**
 * Classe responsable du rendu des étapes avec leurs difficultés.
 */
public class EtapesRendu {
    private static Map<Defi, Boolean> progression;
    private static Image imgFacile = new Image(Objects.requireNonNull(EtapesRendu.class.getResourceAsStream("/img/difficultees/facile.png")));
    private static Image imgMoyen = new Image(Objects.requireNonNull(EtapesRendu.class.getResourceAsStream("/img/difficultees/moyen.png")));
    private static Image imgDifficile = new Image(Objects.requireNonNull(EtapesRendu.class.getResourceAsStream("/img/difficultees/difficile.png")));
    private static Image imgCheckmark = new Image(Objects.requireNonNull(EtapesRendu.class.getResourceAsStream("/img/checkmark.png")));


    /**
     * Rend les étapes avec leurs difficultés sous forme de VBox.
     *
     * @param progress Une Map<Defi, Boolean> représentant la progression des défis.
     * @return VBox contenant les étapes rendues.
     */
    public static VBox render(Map<Defi, Boolean> progress) {
        VBox root = new VBox(15);
        root.setPrefWidth(VBox.USE_PREF_SIZE);
        root.setMaxWidth(VBox.USE_PREF_SIZE);
        System.out.println(progress);
        progression = progress;

        Map<Integer, List<Defi>> defisByEtape = new HashMap<>();
        for (Defi defi : Defi.values()) {
            defisByEtape.computeIfAbsent(defi.getEtape(), k -> new ArrayList<>()).add(defi);
        }

        for (int etape = 1; etape <= 3; etape++) {
            List<Defi> defis = defisByEtape.get(etape);
            if (defis != null && !defis.isEmpty()) {
                VBox stepCard = createStepCard(String.valueOf(etape), defis);
                root.getChildren().add(stepCard);
            }
        }

        return root;
    }

    /**
     * Crée une carte d'étape avec ses difficultés.
     *
     * @param step        L'étape à rendre.
     * @param defis TODO: Description de 'defis'.
     * @return VBox représentant la carte d'étape.
     */
    private static VBox createStepCard(String step, List<Defi> defis) {
        VBox root = new VBox(10);
        root.getStyleClass().add("step-card");

        Text label = new Text("Étape " + step);
        label.getStyleClass().add("step-label");
        root.getChildren().add(label);

        HBox difficultyContainer = new HBox(10);

        for (Defi defi : defis) {
            Image image = getImageForDefi(defi);
            String difficulty = getDifficultyLabel(defi);
            boolean isDone = progression.getOrDefault(defi, false);
            difficultyContainer.getChildren().add(createDifficultyIcon(image, difficulty, isDone));
        }

        root.getChildren().add(difficultyContainer);

        return root;
    }

    private static Image getImageForDefi(Defi defi) {
        String name = defi.name();
        if (name.startsWith("FACILE")) return imgFacile;
        if (name.startsWith("MOYEN")) return imgMoyen;
        return imgDifficile;
    }

    private static String getDifficultyLabel(Defi defi) {
        String name = defi.name();
        if (name.startsWith("FACILE")) return "Facile";
        if (name.startsWith("MOYEN")) return "Moyen";
        return "Difficile";
    }


    /**
     * Crée une icône de difficulté avec une image et un label.
     *
     * @param image      L'image représentant la difficulté.
     * @param difficulty Le label de la difficulté.
     * @return Button représentant l'icône de difficulté.
     */
    private static Button createDifficultyIcon(Image image, String difficulty, boolean isDone) {
        Button root = new Button();
        VBox graphics = new VBox(3);

        StackPane sp = new StackPane();
        sp.setAlignment(javafx.geometry.Pos.CENTER);

        ImageView icon = new ImageView(image);
        icon.setFitHeight(50);
        icon.setFitWidth(50);

        ImageView doneIcon = new ImageView(imgCheckmark);
        doneIcon.setFitHeight(30);
        doneIcon.setFitWidth(30);

        sp.getChildren().add(icon);
        if (isDone) sp.getChildren().add(doneIcon);

        Text label = new Text(difficulty);
        label.getStyleClass().add("defi-button-label");
        graphics.getChildren().addAll(
                sp,
                label
        );
        graphics.setAlignment(javafx.geometry.Pos.TOP_CENTER);

        root.setGraphic(graphics);
        root.setPadding(new javafx.geometry.Insets(8, 8, 5, 8));
        root.getStyleClass().add("defi-button");
        return root;
    }


}
