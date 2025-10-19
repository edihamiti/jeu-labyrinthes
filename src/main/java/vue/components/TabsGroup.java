package vue.components;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class TabsGroup extends VBox {
    private List<Tab> tabs;

    public TabsGroup() {
        super();
        this.tabs = new ArrayList<>();
        this.setSpacing(5);
    }

    public void addTab(Tab tab) {
        this.tabs.add(tab);
        this.getChildren().add(tab);

        EventHandler<ActionEvent> originalAction = tab.getOnAction();
        tab.setOnAction(e -> {
            selectTab(tab);
            if (originalAction != null) {
                originalAction.handle(e);
            }
        });

        if (this.tabs.size() == 1) {
            selectTab(tab);
        }
    }

    public void selectTab(Tab tab) {
        this.tabs.forEach(Tab::deselect);
        tab.select();
    }

    public void removeTab(Tab tab) {
        this.tabs.remove(tab);
        this.getChildren().remove(tab);
    }

    public List<Tab> getTabs() {
        return tabs;
    }

    public Tab getSelectedTab() {
        return tabs.stream().filter(Tab::isSelected).findFirst().orElse(null);
    }
}