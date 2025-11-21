package vue.visionsLabyrinthe;

import javafx.scene.layout.VBox;
import modele.Jeu;
import modele.Labyrinthe;
import vue.AveugleRendu;
import vue.MiniMapRendu;
import vue.Rendu;

public class VisionAveugle implements VisionLabyrinthe {
    @Override
    public Rendu createRendu(Labyrinthe lab, VBox container, Jeu jeu) {
        return new AveugleRendu(lab, container, jeu);
    }

    @Override
    public boolean requiresMinimap() {
        return false;
    }

    @Override
    public Rendu createMinimapRendu(Labyrinthe lab, VBox container, Jeu jeu) {
        return new MiniMapRendu(lab, container, jeu);
    }
}
