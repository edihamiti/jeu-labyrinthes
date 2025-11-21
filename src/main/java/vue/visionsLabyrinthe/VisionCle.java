package vue.visionsLabyrinthe;

import javafx.scene.layout.VBox;
import modele.Labyrinthe;
import vue.CleRendu;
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

    @Override
    public Rendu createRendu(Labyrinthe lab, VBox container) {
        return new CleRendu(lab, container, porteeVision);
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
