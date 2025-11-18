package vue;

import modele.defi.Defi;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import modele.Joueur;

import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * Classe responsable du rendu du contenu du bouton de chargement de profils.
 */
public class ChargerProfileRendu {
    private static Map<Defi, Boolean> progression;
    private static final Image imgFacile = new Image(Objects.requireNonNull(ChargerProfileRendu.class.getResourceAsStream("/img/difficultees/facile.png")));
    private static final Image imgMoyen = new Image(Objects.requireNonNull(ChargerProfileRendu.class.getResourceAsStream("/img/difficultees/moyen.png")));
    private static final Image imgDifficile = new Image(Objects.requireNonNull(ChargerProfileRendu.class.getResourceAsStream("/img/difficultees/difficile.png")));
    private static final Image imgCheckmark = new Image(Objects.requireNonNull(ChargerProfileRendu.class.getResourceAsStream("/img/checkmark.png")));


    /**
     * Rend une VBox avec des boutons pour charger les joueurs.
     *
     * @param profiles Une {@code Map<String, Joueur>} représentant les profils disponibles.
     * @param action   Une action {@code Consumer<Joueur>} à exécuter lors du clic sur un bouton de profil.
     * @return VBox contenant les boutons pour charger les joueurs.
     */
    public static VBox render(Map<String, Joueur> profiles, Consumer<Joueur> action) {
        VBox root = new VBox(10);
        root.setMaxWidth(Double.MAX_VALUE);
        root.setAlignment(javafx.geometry.Pos.TOP_CENTER);

        if (profiles.isEmpty()) {
            Label pasDeProfile = new Label("Pas de profils disponible");
            pasDeProfile.getStyleClass().add("form-small-text");
            pasDeProfile.setStyle("-fx-text-fill: -color-5;");
            root.getChildren().add(pasDeProfile);
            return root;
        }

        for (String name : profiles.keySet()) {
            Joueur joueur = profiles.get(name);
            Button button = new Button(name);
            button.setMaxWidth(Double.MAX_VALUE);
            button.getStyleClass().add("charger-profile-button");
            button.setStyle("-fx-text-fill: -color-5");
            button.setOnAction(event -> {
                action.accept(joueur);
            });
            root.getChildren().add(button);
        }

        return root;
    }
}
