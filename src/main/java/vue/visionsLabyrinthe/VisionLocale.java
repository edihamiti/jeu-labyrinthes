package vue.visionsLabyrinthe;

import javafx.scene.layout.VBox;
import modele.Jeu;
import modele.Labyrinthe;
import vue.LocaleRendu;
import vue.MiniMapRendu;
import vue.Rendu;

public class VisionLocale implements VisionLabyrinthe {
    @Override
    public Rendu createRendu(Labyrinthe lab, VBox container, Jeu jeu) {
        return new LocaleRendu(lab, container);
    }

    @Override
    public boolean requiresMinimap() {
        return true;
    }

    @Override
    public Rendu createMinimapRendu(Labyrinthe lab, VBox container) {
        return new MiniMapRendu(lab, container);
    }
}
