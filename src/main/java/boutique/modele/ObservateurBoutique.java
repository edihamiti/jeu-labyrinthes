package boutique.modele;

/**
 * Interface pour observer les événements de la boutique.
 * Permet d'être notifié des achats, équipements et changements de score.
 */
public interface ObservateurBoutique {

    /**
     * Appelé lorsqu'un achat est réussi.
     *
     * @param cosmetique le cosmétique acheté
     */
    void surAchatReussi(Cosmetique cosmetique);

    /**
     * Appelé lorsqu'un équipement est réussi.
     *
     * @param cosmetique le cosmétique équipé
     */
    void surEquipementReussi(Cosmetique cosmetique);

    /**
     * Appelé lorsqu'une erreur survient.
     *
     * @param message le message d'erreur
     */
    void surErreur(String message);

    /**
     * Appelé lorsque le score du joueur change.
     *
     * @param nouveauScore le nouveau score du joueur
     */
    void surChangementScore(int nouveauScore);

}
