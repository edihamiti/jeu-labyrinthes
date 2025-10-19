package vue;

import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import modele.Jeu;
import vue.components.Navbar;

public class MenuPrincipal extends Stage implements LabyrinthesObservateur {
    Jeu jeu;
    HBox root;

    public MenuPrincipal(Jeu jeu) {
        this.jeu = jeu;
        this.jeu.addObservateur(this);
        this.setTitle("Le Jeu des Labyrinthes");
        HBox root = new HBox();
        this.root = root;
        Scene scene = new Scene(root, 1400, 900);
        this.setScene(scene);
        this.setFullScreen(true);

        var cssResource = getClass().getResource("/vue/styles.css");
        if (cssResource != null) {
            String cssPath = cssResource.toExternalForm();
            root.getStylesheets().add(cssPath);
        }
        root.getStyleClass().add("menu-principal");
        root.getChildren().add(new Navbar());

        this.show();
    }

    @Override
    public void update(Jeu j) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
