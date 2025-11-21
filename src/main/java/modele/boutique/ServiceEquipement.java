package modele.boutique;

import java.util.Optional;

/**
 * Implémentation du service d'équipement de cosmétiques.
 * Gère l'équipement des cosmétiques possédés et la récupération des textures actives.
 */
public class ServiceEquipement implements IServiceEquipement {

    private final IDepotCosmetique depotCosmetique;
    private final IDepotInventaire depotInventaire;

    /**
     * Construit un service d'équipement avec les dépôts nécessaires.
     *
     * @param depotCosmetique le dépôt des cosmétiques disponibles
     * @param depotInventaire le dépôt des inventaires des joueurs
     */
    public ServiceEquipement(IDepotCosmetique depotCosmetique, IDepotInventaire depotInventaire) {
        this.depotCosmetique = depotCosmetique;
        this.depotInventaire = depotInventaire;
    }


    /**
     * Équipe un cosmétique pour un joueur.
     *
     * <p>Les étapes de validation sont :
     * 1. Vérifier que le joueur possède le cosmétique.
     * 2. Vérifier que le cosmétique existe dans le dépôt.
     * 3. Équiper le cosmétique pour le type correspondant (mur, joueur, chemin, sortie).
     * 4. Sauvegarder l'inventaire mis à jour.
     *
     * @param idJoueur l'identifiant du joueur
     * @param idCosmetique l'identifiant du cosmétique à équiper
     * @return un objet {@link ResultatEquipement} représentant le succès ou l'échec de l'opération
     */
    @Override
    public ResultatEquipement equiperCosmetique(String idJoueur, String idCosmetique) {
        InventaireJoueur inventaire = depotInventaire.charger(idJoueur);

        if (!inventaire.possedeCosmetique(idCosmetique)) {
            return ResultatEquipement.nonPossede();
        }

        Optional<Cosmetique> cosmetiqueOpt = depotCosmetique.obtenirParId(idCosmetique);
        if (cosmetiqueOpt.isEmpty()) {
            return ResultatEquipement.cosmetiqueIntrouvable();
        }

        Cosmetique cosmetique = cosmetiqueOpt.get();

        inventaire.equiperCosmetique(cosmetique.type(), idCosmetique);

        depotInventaire.sauvegarder(idJoueur, inventaire);

        return ResultatEquipement.reussi();
    }


    /**
     * Obtient le chemin de la texture actuellement équipée pour un type de cosmétique donné.
     *
     * <p>Si aucun cosmétique n'est équipé pour ce type, retourne la texture par défaut.
     *
     * @param idJoueur l'identifiant du joueur
     * @param type le type de cosmétique (mur, chemin, joueur, sortie)
     * @return le chemin du fichier de texture à utiliser
     */
    @Override
    public String obtenirTextureEquipee(String idJoueur, TypeCosmetique type) {
        InventaireJoueur inventaire = depotInventaire.charger(idJoueur);
        String idCosmetique = inventaire.getCosmetiqueEquipe(type);

        if (idCosmetique == null) {
            return obtenirTextureParDefaut(type);
        }

        return depotCosmetique.obtenirParId(idCosmetique)
                .map(Cosmetique::cheminTexture)
                .orElse(obtenirTextureParDefaut(type));
    }


    /**
     * Fournit le chemin de la texture par défaut pour un type de cosmétique.
     *
     * @param type le type de cosmétique
     * @return le chemin vers la texture par défaut
     */
    private String obtenirTextureParDefaut(TypeCosmetique type) {
        return "/textures/default/" + type.name().toLowerCase() + ".png";
    }
}
