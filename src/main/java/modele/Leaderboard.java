package modele;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Classe représentant un leaderboard basé sur les joueurs enregistrés dans Sauvegarde.
 */
public class Leaderboard {

    private final Sauvegarde sauvegarde;

    /**
     * Constructeur du leaderboard.
     *
     * @param sauvegarde la sauvegarde contenant les joueurs
     */
    public Leaderboard(Sauvegarde sauvegarde) {
        this.sauvegarde = sauvegarde;
    }

    /**
     * Retourne tous les joueurs triés du meilleur au moins bon.
     *
     * @return liste triée des joueurs
     */
    public List<Joueur> getClassementComplet() {
        return sauvegarde.getJoueurs()
                .values()
                .stream()
                .sorted(Comparator.comparingInt(Joueur::getScore).reversed())
                .collect(Collectors.toList());
    }
}
