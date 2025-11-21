package modele.boutique;

/**
 * Service gérant les achats de cosmétiques dans la boutique.
 * Responsable de la validation et de l'exécution des transactions d'achat.
 */
public interface IServiceAchat {

    /**
     * Tente d'acheter un cosmétique pour un joueur.
     * Vérifie que le joueur a suffisamment de points et ne possède pas déjà le cosmétique.
     *
     * @param pseudoJoueur l'identifiant du joueur
     * @param idCosmetique l'identifiant du cosmétique à acheter
     * @return le résultat de la tentative d'achat
     */
    ResultatAchat acheterCosmetique(String pseudoJoueur, String idCosmetique);

}
