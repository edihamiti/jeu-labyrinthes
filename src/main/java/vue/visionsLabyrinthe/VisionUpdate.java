package vue.visionsLabyrinthe;

import javafx.scene.layout.VBox;
import modele.Labyrinthe;
import vue.MiniMapRendu;
import vue.Rendu;
import vue.UpdateRendu;

/**
 * Stratégie de vision pour la carte qui se met à jour progressivement.
 * Utilisée pour l'exploration progressive avec minimap.
 */
public class VisionUpdate implements VisionLabyrinthe {
    private final int porteeVision;

    /**
     * Constructeur avec portée de vision par défaut (2).
     */
    public VisionUpdate() {
        this(2);
    }

    /**
     * Constructeur avec portée de vision personnalisée.
     *
     * @param porteeVision La portée de vision du joueur.
     */
    public VisionUpdate(int porteeVision) {
        this.porteeVision = porteeVision;
    }

    @Override
    public Rendu createRendu(Labyrinthe lab, VBox container) {
        return new UpdateRendu(lab, container, porteeVision);
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

