package vue.components;

import controleur.MenuPrincipalControleur;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import vue.MenuPrincipal;
import vue.utils.Colors;

public class Navbar extends VBox {
    MenuPrincipal menuPrincipal;

    public Navbar(MenuPrincipal menuPrincipal) {
        super();
        this.menuPrincipal = menuPrincipal;
        this.getStyleClass().add("navbar");
        this.getChildren().addAll(
                gameLogo(),
                tabs()
        );
    }

    private Node tabs() {
        VBox container = new VBox();
        container.getStyleClass().add("tabs-container");
        container.setSpacing(10);
        TabsGroup tg = new TabsGroup();

        Tab progressTab = new Tab("Mode Progression", () -> {
            System.out.println("Mode Progression clicked");
            this.menuPrincipal.setProgressTab();
        });

        Tab libreTab = new Tab("Mode Libre", () -> {
            System.out.println("Mode Libre clicked");
        });

        tg.addTab(progressTab);
        tg.addTab(libreTab);

        container.getChildren().addAll(
                tg
        );

        return container;
    }

    private Node gameLogo() {
        VBox logo = new VBox();
        logo.setSpacing(-18);

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
