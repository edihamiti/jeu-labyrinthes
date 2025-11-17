package modele;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe représentant le mode de jeu "Progression".
 */
public class ModeProgression {
    private List<Etape> etapes = new ArrayList<>();

    /**
     * Constructeur pour le mode de jeu "Progression".
     * Initialise les étapes avec les défis correspondants.
     */
    public ModeProgression() {
        /*for (Defi defi : Defi.values()) {
            etapes.add(new Etape(defi, 0));
        }
        etapes.add(new Etape(Defi.FACILE1, 1));
        etapes.add(new Etape(Defi.FACILE2, 2));
        etapes.add(new Etape(Defi.FACILE3, 3));
        etapes.add(new Etape(Defi.MOYEN1, 4));
        etapes.add(new Etape(Defi.MOYEN2, 5));
        etapes.add(new Etape(Defi.MOYEN3, 6));
        etapes.add(new Etape(Defi.DIFFICILE1, 7));
        etapes.add(new Etape(Defi.DIFFICILE2, 8));
        etapes.add(new Etape(Defi.DIFFICILE3, 9));*/
    }

    public List<Etape> getEtapes() {
        return etapes;
    }

    public Etape getEtape(int numero) {
        for (Etape etape : etapes) {
            if (etape.getNumero() == numero) {
                return etape;
            }
        }
        return null;
    }

    public void jouer() {
//TODO
    }
}
