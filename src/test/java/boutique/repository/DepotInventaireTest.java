package boutique.repository;

import modele.boutique.InventaireJoueur;
import modele.boutique.IDepotInventaire;

import java.util.HashMap;
import java.util.Map;

public class DepotInventaireTest implements IDepotInventaire {

    private final Map<String, InventaireJoueur> inventaires = new HashMap<>();
    private final int scoreInitial;

    public DepotInventaireTest() {
        this(0);
    }

    public DepotInventaireTest(int scoreInitial) {
        this.scoreInitial = scoreInitial;
    }

    @Override
    public void sauvegarder(String idJoueur, InventaireJoueur inventaire) {
        inventaires.put(idJoueur, inventaire);
    }

    @Override
    public InventaireJoueur charger(String idJoueur) {
        return inventaires.computeIfAbsent(idJoueur, id -> new InventaireJoueur(scoreInitial));
    }

    public boolean aEteSauvegarde(String idJoueur) {
        return inventaires.containsKey(idJoueur);
    }

}

