package vue.components;

import javafx.scene.Node;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import vue.utils.Colors;

public class Navbar extends VBox {
    public Navbar() {
        super();
        this.getStyleClass().add("navbar");
        this.getChildren().add(
                gameLogo()
        );
    }

    private Node gameLogo() {
        VBox logo = new VBox();

        Text firstLine = new Text("Le jeu des");
        firstLine.getStyleClass().add("logo");
        firstLine.getStyleClass().add("first-line");
        firstLine.setFill(Paint.valueOf(Colors.COLOR_6.getHexCode()));

        Text secondLine = new Text("Labyrinthes");
        secondLine.getStyleClass().add("logo");
        secondLine.getStyleClass().add("second-line");
        secondLine.setFill(Paint.valueOf(Colors.COLOR_6.getHexCode()));

        logo.getChildren().addAll(firstLine, secondLine);

        return logo;
    }
}
