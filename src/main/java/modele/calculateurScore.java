package modele;

import java.time.Duration;

public class calculateurScore {
    public static int calculerScore(Defi defi, Duration duration) {
        int score = 0;
        // Logique de calcul du score en fonction du défi et de la durée
        return score;
    }


    private int calculerScore(long minutes, long secondes) {
        if (Jeu.getInstance().getJoueur() != null && Jeu.getInstance().getDefiEnCours() != null) {
            int pointsBase = Jeu.getInstance().getDefiEnCours().getPoints();
            long tempsTotal = minutes * 60 + secondes;

            if (tempsTotal > 10) {
                long tempsSupplementaire = tempsTotal - 10;
                int penalite = (int) (tempsSupplementaire / 10);
                return Math.max(0, pointsBase - penalite);
            }
            return pointsBase;
        }
        return 0;
    }
}
