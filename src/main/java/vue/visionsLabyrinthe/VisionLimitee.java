package vue.visionsLabyrinthe;

import javafx.scene.layout.VBox;
import modele.Labyrinthe;
import vue.LimiteeRendu;
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
}
