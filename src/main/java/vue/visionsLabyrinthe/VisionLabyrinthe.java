package vue.visionsLabyrinthe;

import javafx.scene.layout.VBox;
import modele.Labyrinthe;
import vue.Rendu;

public interface VisionLabyrinthe {
    Rendu createRendu(Labyrinthe lab, VBox container);
    boolean requiresMinimap();
}
