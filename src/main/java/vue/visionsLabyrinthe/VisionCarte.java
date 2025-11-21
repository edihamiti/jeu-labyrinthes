package vue.visionsLabyrinthe;

import javafx.scene.layout.VBox;
import modele.Jeu;
import modele.Labyrinthe;
import vue.LocaleRendu;
import vue.Rendu;
import vue.UpdateRendu;

/**
 * Vision pour l'étape 6 : vue locale + carte progressive.
 * - Vue principale : LocaleRendu (vue locale autour du joueur)
 * - Minimap : UpdateRendu (carte qui se révèle progressivement, sans la sortie)
 */
public class VisionCarte implements VisionLabyrinthe {
    private final int porteeVision;

    /**
     * Constructeur avec portée de vision par défaut (2).
     */
    public VisionCarte() {
        this(2);
    }

    /**
     * Constructeur avec portée de vision personnalisée.
     *
     * @param porteeVision La portée de vision du joueur.
     */
    public VisionCarte(int porteeVision) {
        this.porteeVision = porteeVision;
    }

    @Override
    public Rendu createRendu(Labyrinthe lab, VBox container, Jeu jeu) {
        // Vue principale = Vue locale (montre seulement la zone autour du joueur)
        return new LocaleRendu(lab, container, porteeVision, jeu);
    }

    @Override
    public boolean requiresMinimap() {
        return true;
    }

    @Override
    public Rendu createMinimapRendu(Labyrinthe lab, VBox container, Jeu jeu) {
        // Minimap = Carte progressive (se révèle au fur et à mesure, sans la sortie)
        return new UpdateRendu(lab, container, porteeVision, jeu);
    }
}

