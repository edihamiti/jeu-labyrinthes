package vue.visionsLabyrinthe;

import javafx.scene.layout.VBox;
import modele.Jeu;
import modele.Labyrinthe;
import vue.LabyrintheRendu;
import vue.MiniMapRendu;
import vue.Rendu;

public class VisionLibre implements VisionLabyrinthe {
    @Override
    public Rendu createRendu(Labyrinthe lab, VBox container, Jeu jeu) {
        return new LabyrintheRendu(lab, container, jeu);
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
