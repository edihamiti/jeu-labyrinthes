package vue.components.BetterButtons;

import javafx.scene.Node;
import javafx.scene.control.Button;

public class OutlinedButton extends Button {
    public OutlinedButton(Node graphic, Runnable action) {
        super();
        this.getStyleClass().addAll("button","outlined");
        graphic.getStyleClass().addAll("button", "outlined", "text");
        this.setGraphic(graphic);
        this.setOnAction(e -> action.run());
    }
}
