package boutique.repository;

import boutique.modele.InventaireJoueur;

/**
 * Interface définissant le contrat pour la persistance des inventaires des joueurs.
 * Permet de sauvegarder et charger les inventaires.
 */
public interface IDepotInventaire {

    /**
     * Sauvegarde l'inventaire d'un joueur.
     *
     * @param idJoueur l'identifiant du joueur
     * @param inventaire l'inventaire à sauvegarder
     */
    void sauvegarder(String idJoueur, InventaireJoueur inventaire);

    /**
     * Charge l'inventaire d'un joueur.
     * Si l'inventaire n'existe pas, retourne un inventaire vide avec un score initial.
     *
     * @param idJoueur l'identifiant du joueur
     * @return l'inventaire du joueur
     */
    InventaireJoueur charger(String idJoueur);

}
