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
                "mur_brique", "Mur en brique",
                TypeCosmetique.TEXTURE_MUR, 100, "textures/mur_brique.png"
        ));
        cosmetiques.add(new Cosmetique(
                "mur_pierre", "Mur en pierre",
                TypeCosmetique.TEXTURE_MUR, 150, "textures/mur_pierre.png"
        ));
        cosmetiques.add(new Cosmetique(
                "mur_bois", "Mur en bois",
                TypeCosmetique.TEXTURE_MUR, 120, "textures/mur_bois.png"
        ));
        cosmetiques.add(new Cosmetique(
                "joueur_rouge", "Joueur rouge",
                TypeCosmetique.TEXTURE_JOUEUR, 200, "textures/joueur_rouge.png"
        ));
        cosmetiques.add(new Cosmetique(
                "joueur_bleu", "Joueur bleu",
                TypeCosmetique.TEXTURE_JOUEUR, 200, "textures/joueur_bleu.png"
        ));
        cosmetiques.add(new Cosmetique(
                "joueur_vert", "Joueur vert",
                TypeCosmetique.TEXTURE_JOUEUR, 250, "textures/joueur_vert.png"
        ));
        cosmetiques.add(new Cosmetique(
                "sol_herbe", "Sol herbe",
                TypeCosmetique.TEXTURE_CHEMIN, 80, "textures/sol_herbe.png"
        ));
        cosmetiques.add(new Cosmetique(
                "sol_sable", "Sol sable",
                TypeCosmetique.TEXTURE_CHEMIN, 90, "textures/sol_sable.png"
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
