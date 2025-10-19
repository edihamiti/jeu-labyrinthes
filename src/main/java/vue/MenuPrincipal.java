package vue;

import controleur.MenuPrincipalControleur;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import modele.Jeu;
import modele.ModeJeu;
import vue.components.Navbar;
import vue.components.ProgressMenu;
import vue.components.Spacer;

public class MenuPrincipal extends Stage implements LabyrinthesObservateur {
    Jeu jeu;
    HBox root;
    Navbar navbar;
    ScrollPane contenu;
    ProgressMenu progressMenu;

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
        this.navbar = new Navbar(this);
        root.getChildren().add(this.navbar);

        this.contenu = new ScrollPane();
        this.contenu.getStyleClass().add("contenu");
        root.getChildren().add(this.contenu);

        this.contenu.setFitToWidth(true);
        this.contenu.setFitToHeight(true);
        HBox.setHgrow(this.contenu, Priority.ALWAYS);

        this.progressMenu = new ProgressMenu();

        this.contenu.setContent(this.progressMenu);

        this.show();
    }

    @Override
    public void update(Jeu j) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Navbar getNavbar(){return this.navbar;}
    public ScrollPane getContenu(){return this.contenu;}
    public void setContenu(Node node){this.contenu.setContent(node);}
    public void setProgressTab() {
        this.contenu.setContent(this.progressMenu);
    }
}
