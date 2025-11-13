package modele;

import java.time.Duration;

public class CalculateurScore {
    public static int calculerScore(Defi defi, Duration duration) {
        if (defi != null) {
            int pointsBase = defi.getPoints();
            long tempsTotal = duration.toMinutes() * 60 + duration.getSeconds();

            if (tempsTotal > 10) {
                long tempsSupplementaire = tempsTotal - 10;
                int penalite = (int) (tempsSupplementaire / 10);
                return Math.max(0, pointsBase - penalite);
            }
            return pointsBase;
        }
        System.out.println("Pas de défi :(");
        return 0;
    }
}
