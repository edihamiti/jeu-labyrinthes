package vue.components.BetterButtons;

import javafx.scene.Node;
import javafx.scene.control.Button;

public class FilledButton extends Button {
    public FilledButton(Node graphic, Runnable action) {
        super();
        this.getStyleClass().addAll("button","filled");
        graphic.getStyleClass().addAll("button", "filled", "text");
        this.setGraphic(graphic);
        this.setOnAction(e -> action.run());
    }
}
