package boutique.service;

import boutique.modele.Cosmetique;
import boutique.modele.InventaireJoueur;
import boutique.modele.TypeCosmetique;
import boutique.repository.IDepotCosmetique;
import boutique.repository.IDepotInventaire;

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

    private String obtenirTextureParDefaut(TypeCosmetique type) {
        return "/textures/default/" + type.name().toLowerCase() + ".png";
    }
}
