package vue.visionsLabyrinthe;

import javafx.scene.layout.VBox;
import modele.Labyrinthe;
import vue.LabyrintheRendu;
import vue.Rendu;

public class VisionCarte implements VisionLabyrinthe {
    @Override
    public Rendu createRendu(Labyrinthe lab, VBox container) {
        // VUE_CARTE utilise aussi LabyrintheRendu (vue complète)
        return new LabyrintheRendu(lab, container);
    }

    @Override
    public boolean requiresMinimap() {
        return false;
    }
}

