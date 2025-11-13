package vue.visionsLabyrinthe;

import javafx.scene.layout.VBox;
import modele.Labyrinthe;
import vue.LabyrintheRendu;
import vue.MiniMapRendu;
import vue.Rendu;

public class VisionLibre implements VisionLabyrinthe {
    @Override
    public Rendu createRendu(Labyrinthe lab, VBox container) {
        return new LabyrintheRendu(lab, container);
    }

    @Override
    public boolean requiresMinimap() {
        return false;
    }

    @Override
    public Rendu createMinimapRendu(Labyrinthe lab, VBox container) {
        return new MiniMapRendu(lab, container);
    }
}
