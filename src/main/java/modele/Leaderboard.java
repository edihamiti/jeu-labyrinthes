package modele;

import modele.joueursRepositories.JoueurRepository;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Classe représentant le classement des joueurs.
 * Elle permet d'obtenir la liste des joueurs triés par score.
 */
public class Leaderboard {

    private final JoueurRepository sauvegarde;


    /**
     * Construit un Leaderboard à partir d'un dépôt de joueurs.
     *
     * @param sauvegarde le dépôt contenant les joueurs
     */
    public Leaderboard(JoueurRepository sauvegarde) {
        this.sauvegarde = sauvegarde;
    }

    /**
     * Retourne la liste des joueurs triés par score décroissant.
     *
     * @return une liste de joueurs ordonnée du meilleur au moins bon score
     */
    public List<Joueur> getClassementComplet() {
        return sauvegarde.getAllJoueurs()
                .stream()
                .sorted(Comparator.comparingInt(Joueur::getScore).reversed())
                .collect(Collectors.toList());
    }
}
