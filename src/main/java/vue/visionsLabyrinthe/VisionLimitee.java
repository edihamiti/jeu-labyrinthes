package vue.visionsLabyrinthe;

import javafx.scene.layout.VBox;
import modele.Jeu;
import modele.Labyrinthe;
import vue.LimiteeRendu;
import vue.MiniMapRendu;
import vue.Rendu;

public class VisionLimitee implements VisionLabyrinthe {
    private final int porteeVision;

    /**
     * Constructeur avec portée de vision par défaut (2).
     */
    public VisionLimitee() {
        this(2);
    }

    /**
     * Constructeur avec portée de vision personnalisée.
     *
     * @param porteeVision La portée de vision du joueur.
     */
    public VisionLimitee(int porteeVision) {
        this.porteeVision = porteeVision;
    }

    @Override
    public Rendu createRendu(Labyrinthe lab, VBox container, Jeu jeu) {
        return new LimiteeRendu(lab, container, porteeVision, jeu);
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
