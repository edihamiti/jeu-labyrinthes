package modele;


/**
 * Exception levée lorsqu'un pseudo de joueur est invalide.
 * Elle peut être déclenchée si le pseudo est vide, trop long,
 * ou contient des caractères non autorisés.
 */
public class PseudoException extends Exception {


    /**
     * Construit une nouvelle exception avec un message descriptif.
     *
     * @param message le message expliquant la cause de l'exception
     */
    public PseudoException(String message) {
        super(message);
    }
}
