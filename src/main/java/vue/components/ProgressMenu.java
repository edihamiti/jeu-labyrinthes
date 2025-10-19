package vue.components;

import javafx.geometry.Orientation;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class ProgressMenu extends VBox {

    public ProgressMenu() {
        super();

        // Couche 1 : Region avec le background
        Image backgroundImage = new Image(getClass().getResourceAsStream("/vue/img/progress-background.png"));
        BackgroundImage bgImage = new BackgroundImage(
            backgroundImage,
            BackgroundRepeat.NO_REPEAT,
            BackgroundRepeat.NO_REPEAT,
            BackgroundPosition.CENTER,
            new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, false, true)
        );
        this.setBackground(new Background(bgImage));
        this.getStyleClass().add("home-tab");
        this.setPrefHeight(Double.MAX_VALUE);

        VBox header = new VBox();
        Text title = new Text("Mode progression");
        title.getStyleClass().add("title");

        Text subtitle = new Text("Complète des labyrinthes pour débloquer de nouveaux niveaux !");
        subtitle.getStyleClass().add("subtitle");

        header.getChildren().addAll(
                title,
                subtitle
        );

        HBox buttonsContainer = new HBox();
        buttonsContainer.setSpacing(10);
        buttonsContainer.getStyleClass().add("home-tab");
        buttonsContainer.getStyleClass().add("buttons-container");

        this.getChildren().addAll(
                header,
                new Spacer(Orientation.VERTICAL),
                buttonsContainer
        );

        // Important : créer un clip rectangulaire avec coins arrondis pour le VBox
        Rectangle clip = new Rectangle();
        clip.widthProperty().bind(this.widthProperty());
        clip.heightProperty().bind(this.heightProperty());
        clip.setArcWidth(36); // 2 * rayon
        clip.setArcHeight(36);
        this.setClip(clip);
    }
}
