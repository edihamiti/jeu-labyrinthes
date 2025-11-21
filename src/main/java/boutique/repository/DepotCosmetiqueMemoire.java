package boutique.repository;

import boutique.modele.Cosmetique;
import boutique.modele.TypeCosmetique;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implémentation en mémoire du dépôt de cosmétiques.
 * Stocke les cosmétiques dans une liste et les initialise au démarrage.
 */
public class DepotCosmetiqueMemoire implements IDepotCosmetique {

    private final List<Cosmetique> cosmetiques;

    /**
     * Construit le dépôt et initialise la liste des cosmétiques disponibles.
     */
    public DepotCosmetiqueMemoire() {
        this.cosmetiques = new ArrayList<>();
        initialiserCosmetiques();
    }

    private void initialiserCosmetiques() {
        cosmetiques.add(new Cosmetique(
                "default_mur", "Mur de feuille",
                TypeCosmetique.TEXTURE_MUR, 0, "/textures/default/texture_mur.png"
        ));
        cosmetiques.add(new Cosmetique(
                "default_chemin", "Chemin de forêt",
                TypeCosmetique.TEXTURE_CHEMIN, 0, "/textures/default/texture_chemin.png"
        ));
        cosmetiques.add(new Cosmetique(
                "default_sortie", "Sortie de forêt",
                TypeCosmetique.TEXTURE_SORTIE, 0, "/textures/default/texture_sortie.png"
        ));
        cosmetiques.add(new Cosmetique(
                "default_joueur", "Pion",
                TypeCosmetique.TEXTURE_JOUEUR, 0, "/textures/default/texture_joueur.png"
        ));
        cosmetiques.add(new Cosmetique(
                "mur_brique", "Mur en brique",
                TypeCosmetique.TEXTURE_MUR, 100, "/textures/mur_brique.png"
        ));
        cosmetiques.add(new Cosmetique(
                "chemin_stone", "Chemin en pierre",
                TypeCosmetique.TEXTURE_CHEMIN, 100, "/textures/chemin_stone.png"
        ));
        cosmetiques.add(new Cosmetique(
                "sortie_stone", "Sortie en pierre",
                TypeCosmetique.TEXTURE_SORTIE, 100, "/textures/sortie_stone.png"
        ));
        cosmetiques.add(new Cosmetique(
                "joueur_homme", "Joueur homme",
                TypeCosmetique.TEXTURE_JOUEUR, 100, "/textures/joueur_homme.png"
        ));
        cosmetiques.add(new Cosmetique(
                "mur_noel", "Mur de Noël",
                TypeCosmetique.TEXTURE_MUR, 200, "/textures/mur_noel.png"
        ));
        cosmetiques.add(new Cosmetique(
                "chemin_noel", "Chemin de Noël",
                TypeCosmetique.TEXTURE_CHEMIN, 200, "/textures/chemin_noel.png"
        ));
        cosmetiques.add(new Cosmetique(
                "joueur_noel", "Père Noël",
                TypeCosmetique.TEXTURE_JOUEUR, 200, "/textures/joueur_noel.png"
        ));
        cosmetiques.add(new Cosmetique(
                "sortie_noel", "Sapin de Noël",
                TypeCosmetique.TEXTURE_SORTIE, 200, "/textures/sortie_noel.png"
        ));
    }

    @Override
    public List<Cosmetique> obtenirTous() {
        return new ArrayList<>(cosmetiques);
    }

    @Override
    public List<Cosmetique> obtenirParType(TypeCosmetique type) {
        return cosmetiques.stream()
                .filter(c -> c.type() == type)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Cosmetique> obtenirParId(String id) {
        return cosmetiques.stream()
                .filter(c -> c.id().equals(id))
                .findFirst();
    }
}
