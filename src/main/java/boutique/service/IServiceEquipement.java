package boutique.service;

import boutique.modele.TypeCosmetique;

/**
 * Service gérant l'équipement de cosmétiques par les joueurs.
 * Responsable de l'équipement et de la récupération des textures équipées.
 */
public interface IServiceEquipement {

    /**
     * Tente d'équiper un cosmétique pour un joueur.
     * Vérifie que le joueur possède le cosmétique avant de l'équiper.
     *
     * @param idJoueur l'identifiant du joueur
     * @param idCosmetique l'identifiant du cosmétique à équiper
     * @return le résultat de la tentative d'équipement
     */
    ResultatEquipement equiperCosmetique(String idJoueur, String idCosmetique);

    /**
     * Récupère le chemin de la texture équipée pour un type de cosmétique donné.
     * Retourne la texture par défaut si aucune texture n'est équipée.
     *
     * @param idJoueur l'identifiant du joueur
     * @param type le type de cosmétique
     * @return le chemin vers la texture équipée ou la texture par défaut
     */
    String obtenirTextureEquipee(String idJoueur, TypeCosmetique type);

}
