package vue.visionsLabyrinthe;

import javafx.scene.layout.VBox;
import modele.Labyrinthe;
import vue.CarteMinimapRendu;
import vue.LabyrintheRendu;
import vue.Rendu;

public class VisionCarte implements VisionLabyrinthe {
    @Override
    public Rendu createRendu(Labyrinthe lab, VBox container) {
        // VUE_CARTE utilise LabyrintheRendu (vue complète)
        return new LabyrintheRendu(lab, container);
    }

    @Override
    public boolean requiresMinimap() {
        return true;
    }

    @Override
    public Rendu createMinimapRendu(Labyrinthe lab, VBox container) {
        return new CarteMinimapRendu(lab, container);
    }
}

