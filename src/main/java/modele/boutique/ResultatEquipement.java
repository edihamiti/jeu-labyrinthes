package modele.boutique;

/**
 * Représente le résultat d'une tentative d'équipement de cosmétique.
 * Encapsule le statut de succès et un message descriptif.
 */
public class ResultatEquipement {

    private final boolean succes;
    private final String message;

    private ResultatEquipement(boolean succes, String message) {
        this.succes = succes;
        this.message = message;
    }

    /**
     * Crée un résultat d'équipement réussi.
     *
     * @return le résultat d'équipement réussi
     */
    public static ResultatEquipement reussi() {
        return new ResultatEquipement(true, "Cosmétique équipé avec succès");
    }

    /**
     * Crée un résultat d'équipement échoué car le cosmétique n'est pas possédé.
     *
     * @return le résultat d'équipement échoué
     */
    public static ResultatEquipement nonPossede() {
        return new ResultatEquipement(false, "Vous ne possédez pas ce cosmétique");
    }

    /**
     * Crée un résultat d'équipement échoué car le cosmétique est introuvable.
     *
     * @return le résultat d'équipement échoué
     */
    public static ResultatEquipement cosmetiqueIntrouvable() {
        return new ResultatEquipement(false, "Cosmétique introuvable");
    }

    /**
     * @return true si l'équipement a réussi, false sinon
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

}
