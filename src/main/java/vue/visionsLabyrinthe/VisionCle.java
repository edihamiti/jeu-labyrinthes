package vue.visionsLabyrinthe;

import javafx.scene.layout.VBox;
import modele.Jeu;
import modele.Labyrinthe;
import vue.CleRendu;
import vue.LabyrintheRendu;
import vue.MiniMapRendu;
import vue.Rendu;

public class VisionCle implements VisionLabyrinthe{

    private final int porteeVision;

    /**
     * Constructeur avec portée de vision par défaut (2).
     */
    public VisionCle() {
        this(2);
    }

    /**
     * Constructeur avec portée de vision personnalisée.
     *
     * @param porteeVision La portée de vision du joueur.
     */
    public VisionCle(int porteeVision) {
        this.porteeVision = porteeVision;
    }

    public Rendu createRendu(Labyrinthe lab, VBox container, Jeu jeu) {
        return new CleRendu(lab, container,3, jeu);
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
