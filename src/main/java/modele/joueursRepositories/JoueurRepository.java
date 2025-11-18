package modele.joueursRepositories;

import modele.Joueur;
import modele.PseudoException;

import java.util.Map;

public interface JoueurRepository {
    /**
     * Récupère un joueur en fonction de son pseudo.
     *
     * @param pseudo le pseudo du joueur à rechercher
     * @return l'objet Joueur correspondant au pseudo spécifié
     * @throws PseudoException si le pseudo est invalide
     */
    Joueur getJoueurParPseudo(String pseudo) throws PseudoException;

    /**
     * Récupère l'ensemble des joueurs enregistrés.
     *
     * @return une map contenant les joueurs, où la clé est le pseudo du joueur
     *         et la valeur est l'objet Joueur correspondant
     */
    Map<String, Joueur> getJoueurs();

    /**
     * Supprime un joueur de la sauvegarde.
     *
     * @param joueur le joueur à supprimer
     */
    void removeJoueur(Joueur joueur);

    /**
     * Supprime un joueur de la sauvegarde.
     *
     * @param pseudo le pseudo du joueur à supprimer
     */
    void removeJoueur(String pseudo);

    /**
     * Ajoute un joueur à la sauvegarde.
     *
     * @param joueur le joueur à ajouter
     */
    void addJoueur(Joueur joueur);

    /**
     * Sauvegarde l'état actuel des joueurs.
     *
     */
    void sauvegarder();

    /**
     * Charge les joueurs dans le repository.
     *
     */
    void chargerJoueurs();
}
