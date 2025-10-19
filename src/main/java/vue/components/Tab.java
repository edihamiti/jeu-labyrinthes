package vue.components;

import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class Tab extends Button {
    boolean selected;
    Text text;
    Rectangle rectangle;
    Runnable onAction;

    public Tab(String text, Runnable onAction) {
        super();
        this.getStyleClass().add("tab");
        this.selected = false;
        this.onAction = onAction;

        this.rectangle = rectangle();
        this.text = text(text);

        HBox content = new HBox();
        content.setSpacing(10);
        content.getChildren().addAll(
                this.rectangle,
                this.text
        );

        this.setGraphic(content);
        this.setOnAction(e -> {
            this.setSelected(true);
            onAction.run();
        });
    }

    private Rectangle rectangle() {
        Rectangle rect = new Rectangle();
        rect.getStyleClass().add("tabs-rectangle");
        rect.setWidth(4);
        rect.setHeight(30);
        return rect;
    }

    private Text text(String string) {
        Text text = new Text(string);
        text.getStyleClass().add("tabs-text");
        return text;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
        if (selected) {
            this.getStyleClass().add("selected");
            this.rectangle.getStyleClass().add("selected");
            this.text.getStyleClass().add("selected");
        } else {
            this.getStyleClass().removeAll("selected");
            this.rectangle.getStyleClass().removeAll("selected");
            this.text.getStyleClass().removeAll("selected");
        }
    }

    public void deselect() {this.setSelected(false);}
    public void select() {this.setSelected(true);}
    public boolean isSelected() {return this.selected;}
}
