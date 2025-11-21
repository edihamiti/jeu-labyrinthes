package modele;

import modele.defi.Defi;


/**
 * Classe utilitaire pour calculer le score obtenu par un joueur
 * en fonction d'un défi et du nombre de déplacements effectués.
 */
public class CalculateurScore {


    /**
     * Calcule le score obtenu pour un défi donné et un nombre de déplacements.
     *
     * La logique est la suivante :
     * - Si le défi est null, le score est 0.
     * - Pour les étapes 1, 2 et 3, le score correspond aux points de base du défi.
     * - Pour les étapes 4, 5 et 6 :
     *      - On calcule un nombre de déplacements optimal (1,5 × distance minimale du défi).
     *      - Chaque 2 déplacements supplémentaires au-delà de ce nombre réduit le score d'1 point.
     *      - Le score minimum est 0.
     *
     * @param defi le défi réalisé
     * @param nombreDeplacements le nombre de déplacements effectués par le joueur
     * @return le score calculé pour ce défi
     */
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
