package vue;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import modele.Joueur;

import java.util.*;
import java.util.function.Consumer;

/**
 * Classe responsable du rendu des profils des joueurs.
 */
public class ProfilsRendu {

    /**
     * Rend les profils des joueurs.
     *
     * @param joueurs Une {@code Map<String, Joueur>} représentant la progression des défis.
     * @param action L'action a éffectuer lors du clic sur un bouton de profil.
     * @return VBox contenant les étapes rendues.
     */
    public static VBox render(Map<String, Joueur> joueurs, Consumer<Joueur> action) {
        VBox root = new VBox(15);
        HBox.setHgrow(root, Priority.ALWAYS);
        root.setMaxWidth(Double.MAX_VALUE);
        root.setStyle("-fx-background-color: -color-1;");

        for (Joueur joueur : joueurs.values()) {
            root.getChildren().add(
                joueurCard(joueur, action)
            );
        }

        return root;
    }

    private static Node joueurCard(Joueur joueur, Consumer<Joueur> action) {
        HBox root = new HBox(10);
        root.setAlignment(Pos.BOTTOM_LEFT);
        root.setPadding(new Insets(10));
        root.getStyleClass().add("joueur-card");
        root.setMaxWidth(Double.MAX_VALUE);


        VBox infos = new VBox(10);

        HBox pointsContainer = new HBox(10);
        pointsContainer.setMaxHeight(40);
        EtapesRendu.renderPoints(joueur.getScore(), pointsContainer);

        Text pseudo = new Text(joueur.getPseudo());
        pseudo.getStyleClass().add("pseudo-text");


        infos.getChildren().addAll(
                pseudo,
                pointsContainer
        );

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button button = new Button("→");
        button.getStyleClass().add("button-outlined");
        button.setOnAction(event -> {action.accept(joueur);});


        root.getChildren().addAll(
                //TODO :  Mettre l'image du joueur du joueur
                infos,
                spacer,
                button
        );

        root.setOnMouseClicked(event -> {
            action.accept(joueur);
        });

        return root;
    }
}