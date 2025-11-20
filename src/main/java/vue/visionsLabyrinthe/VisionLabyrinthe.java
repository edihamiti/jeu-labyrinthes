package vue.visionsLabyrinthe;

import javafx.scene.layout.VBox;
import modele.Jeu;
import modele.Labyrinthe;
import vue.Rendu;

public interface VisionLabyrinthe {
    Rendu createRendu(Labyrinthe lab, VBox container, Jeu jeu);

    boolean requiresMinimap();

    Rendu createMinimapRendu(Labyrinthe lab, VBox container);
}
