package vue;

import modele.defi.Defi;
import modele.defi.repository.DefiJson;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.*;
import java.util.function.Consumer;

/**
 * Classe responsable du rendu des étapes avec leurs difficultés.
 */
public class EtapesRendu {
    private static final Image imgFacile = new Image(Objects.requireNonNull(EtapesRendu.class.getResourceAsStream("/img/difficultees/facile.png")));
    private static final Image imgMoyen = new Image(Objects.requireNonNull(EtapesRendu.class.getResourceAsStream("/img/difficultees/moyen.png")));
    private static final Image imgDifficile = new Image(Objects.requireNonNull(EtapesRendu.class.getResourceAsStream("/img/difficultees/difficile.png")));
    private static final Image imgCheckmark = new Image(Objects.requireNonNull(EtapesRendu.class.getResourceAsStream("/img/checkmark.png")));
    private static final Image imgPoints = new Image(Objects.requireNonNull(EtapesRendu.class.getResourceAsStream("/img/points.png")));
    private static final int DEFI_PAR_ETAPES = 3;
    private static final int NOMBRE_ETAPES = new DefiJson().charger().getDefisRepo().size() / DEFI_PAR_ETAPES;
    private static Map<Defi, Boolean> progression;

    /**
     * Rend les étapes avec leurs difficultés sous forme de VBox.
     *
     * @param progress Une {@code Map<Defi, Boolean>} représentant la progression des défis.
     * @return VBox contenant les étapes rendues.
     */
    public static VBox render(Map<Defi, Boolean> progress, Consumer<Defi> action) {
        VBox root = new VBox(15);
        root.setPrefWidth(VBox.USE_PREF_SIZE);
        root.setMaxWidth(VBox.USE_PREF_SIZE);
        System.out.println(progress);
        progression = progress;

        Map<Integer, List<Defi>> defisByEtape = new HashMap<>();
        for (Defi defi : new DefiJson().charger().getDefisRepo()) {
            defisByEtape.computeIfAbsent(defi.etape(), k -> new ArrayList<>()).add(defi);
        }

        for (int etape = 1; etape <= NOMBRE_ETAPES; etape++) {
            List<Defi> defis = defisByEtape.get(etape);
            if (defis != null && !defis.isEmpty()) {
                boolean isUnlocked = isStepUnlocked(etape, defisByEtape);
                VBox stepCard = createStepCard(String.valueOf(etape), defis, action, isUnlocked);
                root.getChildren().add(stepCard);
            }
        }

        return root;
    }

    public static HBox renderPoints(int nbPoints, HBox container) {
        container.getChildren().clear();
        container.setSpacing(10);
        container.setAlignment(Pos.CENTER_LEFT);
        container.getStyleClass().add("points-container");

        ImageView icon = new ImageView(imgPoints);
        icon.setFitHeight(18);
        icon.setFitWidth(16);

        Label label = new Label(nbPoints + " points");
        label.getStyleClass().add("points-text");

        container.getChildren().addAll(icon, label);

        return container;
    }

    /**
     * Vérifie si une étape est déverrouillée.
     * L'étape 1 est toujours déverrouillée.
     * Les autres étapes nécessitent qu'au moins un défi de l'étape précédente soit complété.
     *
     * @param etape        L'étape à vérifier.
     * @param defisByEtape Map des défis groupés par étape.
     * @return true si l'étape est déverrouillée, false sinon.
     */
    private static boolean isStepUnlocked(int etape, Map<Integer, List<Defi>> defisByEtape) {
        if (etape == 1) return true;

        List<Defi> previousStepDefis = defisByEtape.get(etape - 1);
        if (previousStepDefis == null) return true;

        return previousStepDefis.stream()
                .anyMatch(defi -> progression.getOrDefault(defi, false));
    }

    /**
     * Crée une carte d'étape avec ses difficultés.
     *
     * @param step       L'étape à rendre.
     * @param defis      Les défis de cette étape.
     * @param action     Le callback à exécuter lors du clic.
     * @param isUnlocked Indique si l'étape est déverrouillée.
     * @return VBox représentant la carte d'étape.
     */
    private static VBox createStepCard(String step, List<Defi> defis, Consumer<Defi> action, boolean isUnlocked) {
        VBox root = new VBox(10);
        root.getStyleClass().add("step-card");

        if (!isUnlocked) {
            root.getStyleClass().add("step-card-locked");
        }

        Text label = new Text("Étape " + step);
        label.getStyleClass().add("step-label");
        root.getChildren().add(label);

        HBox difficultyContainer = new HBox(10);

        for (Defi defi : defis) {
            Image image = getImageForDefi(defi);
            String difficulty = getDifficultyLabel(defi);
            boolean isDone = progression.getOrDefault(defi, false);
            Button bt = createDifficultyIcon(image, difficulty, isDone, isUnlocked);

            if (isUnlocked) {
                bt.setOnAction(e -> action.accept(defi));
            } else {
                bt.setDisable(true);
            }

            difficultyContainer.getChildren().add(bt);
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
     * @param isDone     Indique si le défi est complété.
     * @param isUnlocked Indique si le défi est déverrouillé.
     * @return Button représentant l'icône de difficulté.
     */
    private static Button createDifficultyIcon(Image image, String difficulty, boolean isDone, boolean isUnlocked) {
        Button root = new Button();
        VBox graphics = new VBox(3);

        StackPane sp = new StackPane();
        sp.setAlignment(javafx.geometry.Pos.CENTER);

        ImageView icon = new ImageView(image);
        icon.setFitHeight(50);
        icon.setFitWidth(50);

        if (!isUnlocked) {
            icon.setDisable(true);
            //icon.setEffect(new ColorAdjust(0, -1, 0.2, 0));
        }

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
        root.setPadding(new Insets(8, 8, 5, 8));
        root.getStyleClass().add("defi-button");

        if (!isUnlocked) {
            root.getStyleClass().add("defi-button-locked");
        }

        return root;
    }
}