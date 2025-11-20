package modele;

import modele.joueursRepositories.JoueurRepository;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Leaderboard {

    private final JoueurRepository sauvegarde;

    public Leaderboard(JoueurRepository sauvegarde) {
        this.sauvegarde = sauvegarde;
    }

    public List<Joueur> getClassementComplet() {
        return sauvegarde.getAllJoueurs()
                .stream()
                .sorted(Comparator.comparingInt(Joueur::getScore).reversed())
                .collect(Collectors.toList());
    }
}
