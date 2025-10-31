package boutique.service;

import boutique.modele.Cosmetique;
import boutique.modele.InventaireJoueur;
import boutique.repository.IDepotCosmetique;
import boutique.repository.IDepotInventaire;

import java.util.Optional;

/**
 * Implémentation du service d'achat de cosmétiques.
 * Gère la logique métier des achats : vérification du score, possession, et mise à jour de l'inventaire.
 */
public class ServiceAchat implements IServiceAchat {

    private final IDepotCosmetique depotCosmetique;
    private final IDepotInventaire depotInventaire;

    /**
     * Construit un service d'achat avec les dépôts nécessaires.
     *
     * @param depotCosmetique le dépôt des cosmétiques disponibles
     * @param depotInventaire le dépôt des inventaires des joueurs
     */
    public ServiceAchat(IDepotCosmetique depotCosmetique, IDepotInventaire depotInventaire) {
        this.depotCosmetique = depotCosmetique;
        this.depotInventaire = depotInventaire;
    }

    @Override
    public ResultatAchat acheterCosmetique(String pseudoJoueur, String idCosmetique) {
        InventaireJoueur inventaire = depotInventaire.charger(pseudoJoueur);

        Optional<Cosmetique> cosmetiqueOpt = depotCosmetique.obtenirParId(idCosmetique);
        if (cosmetiqueOpt.isEmpty()) {
            return ResultatAchat.cosmetiqueIntrouvable();
        }

        Cosmetique cosmetique = cosmetiqueOpt.get();

        if (inventaire.possedeCosmetique(idCosmetique)) {
            return ResultatAchat.dejaPossede();
        }

        if (inventaire.getScore() < cosmetique.prix()) {
            return ResultatAchat.scoreInsuffisant(
                    cosmetique.prix(),
                    inventaire.getScore()
            );
        }

        inventaire.setScore(inventaire.getScore() - cosmetique.prix());
        inventaire.ajouterCosmetique(idCosmetique);

        depotInventaire.sauvegarder(pseudoJoueur, inventaire);

        return ResultatAchat.reussi(inventaire.getScore());
    }

}
