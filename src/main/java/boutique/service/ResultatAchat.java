package boutique.service;

/**
 * Représente le résultat d'une tentative d'achat de cosmétique.
 * Encapsule le statut de succès, un message descriptif et le nouveau score si l'achat a réussi.
 */
public class ResultatAchat {

    private final boolean succes;
    private final String message;
    private final Integer nouveauScore;

    private ResultatAchat(boolean succes, String message, Integer nouveauScore) {
        this.succes = succes;
        this.message = message;
        this.nouveauScore = nouveauScore;
    }

    /**
     * Crée un résultat d'achat réussi.
     *
     * @param nouveauScore le score du joueur après l'achat
     * @return le résultat d'achat réussi
     */
    public static ResultatAchat reussi(int nouveauScore) {
        return new ResultatAchat(true, "Achat réussi", nouveauScore);
    }

    /**
     * Crée un résultat d'achat échoué pour cause de score insuffisant.
     *
     * @param prixRequis  le prix du cosmétique
     * @param scoreActuel le score actuel du joueur
     * @return le résultat d'achat échoué avec un message explicatif
     */
    public static ResultatAchat scoreInsuffisant(int prixRequis, int scoreActuel) {
        return new ResultatAchat(false, String.format("Score insuffisant ! Il vous faut %d points (vous avez %d)", prixRequis, scoreActuel), null);
    }

    /**
     * Crée un résultat d'achat échoué si le cosmétique est déjà possédé.
     *
     * @return le résultat d'achat échoué
     */
    public static ResultatAchat dejaPossede() {
        return new ResultatAchat(false, "Vous possédez déjà ce cosmétique", null);
    }

    /**
     * Crée un résultat d'achat échoué si le cosmétique est introuvable.
     *
     * @return le résultat d'achat échoué
     */
    public static ResultatAchat cosmetiqueIntrouvable() {
        return new ResultatAchat(false, "Cosmétique introuvable", null);
    }

    /**
     * @return true si l'achat a réussi, false sinon
     */
    public boolean estReussi() {
        return succes;
    }

    /**
     * @return le message descriptif du résultat
     */
    public String getMessage() {
        return message;
    }

    /**
     * @return le nouveau score du joueur si l'achat a réussi, null sinon
     */
    public Integer getNouveauScore() {
        return nouveauScore;
    }

}
