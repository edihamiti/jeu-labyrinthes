package vue.components;

import javafx.geometry.Orientation;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Text;

public class ProgressMenu extends StackPane {

    public ProgressMenu() {
        super();

        // Couche 1 : Region avec le background
        Region imageLayer = new Region();
        Image backgroundImage = new Image(getClass().getResourceAsStream("/vue/img/progress-background.png"));
        BackgroundImage bgImage = new BackgroundImage(
            backgroundImage,
            BackgroundRepeat.NO_REPEAT,
            BackgroundRepeat.NO_REPEAT,
            BackgroundPosition.CENTER,
            new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, false, true)
        );
        imageLayer.setBackground(new Background(bgImage));

        // Couche 2 : Region avec le gradietn
        Region gradientLayer = new Region();
        LinearGradient gradient = new LinearGradient(
            0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
            new Stop(0, Color.rgb(36, 74, 35, 0.50)),
            new Stop(0.5, Color.rgb(36, 74, 35, 0.00)),
            new Stop(1, Color.rgb(36, 74, 35, 0.50))
        );
        gradientLayer.setBackground(new Background(new BackgroundFill(gradient, null, null)));

        // Couche : VBox avec le contenu
        VBox contentBox = new VBox();
        contentBox.getStyleClass().add("home-tab");
        contentBox.setBackground(Background.EMPTY);
        contentBox.setPrefHeight(Double.MAX_VALUE);

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

        contentBox.getChildren().addAll(
                header,
                new Spacer(Orientation.VERTICAL),
                buttonsContainer 
        );


        // Empilement des couches : image, dégradé, contenu
        this.getChildren().addAll(imageLayer, gradientLayer, contentBox);
    }
}
