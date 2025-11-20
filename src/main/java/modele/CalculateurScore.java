package modele;

import modele.defi.Defi;

public class CalculateurScore {


    public static int calculerScore(Defi defi, int nombreDeplacements) {
        if (defi == null) {
            return 0;
        }

        int pointsBase = defi.points();
        int etape = defi.etape();

        //étapes 1, 2 et 3
        if (etape <= 3) {
            return pointsBase;
        }

        //étapes 4, 5 et 6
        int deplacementsOptimal = (int) (defi.distanceMin() * 1.5);

        if (nombreDeplacements > deplacementsOptimal) {
            int deplacementsSupplementaires = nombreDeplacements - deplacementsOptimal;
            int penalite = deplacementsSupplementaires / 2;// 1 point de pénalité par 2 déplacements supplémentaires
            return Math.max(0, pointsBase - penalite);
        }
        return pointsBase;
    }
}
