package vue.visionsLabyrinthe;

import javafx.scene.layout.VBox;
import modele.Labyrinthe;
import vue.Rendu;

public class VisionLocale implements VisionLabyrinthe {
    @Override
    public Rendu createRendu(Labyrinthe lab, VBox container) {
        return null;
    }

    @Override
    public boolean requiresMinimap() {
        return false;
    }
}
