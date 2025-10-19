package vue.components;

import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class Navbar extends VBox {
    public Navbar() {
        super();
        this.getStyleClass().add("navbar");
        this.getChildren().add(
                new Text("Le jeu des Labyrinthes")
        );
    }
}
