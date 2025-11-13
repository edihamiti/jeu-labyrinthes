package vue.visionsLabyrinthe;

import javafx.scene.layout.VBox;
import modele.Labyrinthe;
import vue.LimiteeRendu;
import vue.MiniMapRendu;
import vue.Rendu;

public class VisionLimitee implements VisionLabyrinthe {
    @Override
    public Rendu createRendu(Labyrinthe lab, VBox container) {
        return new LimiteeRendu(lab, container);
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
