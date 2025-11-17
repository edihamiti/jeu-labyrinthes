package modele.defi;

import modele.TypeLabyrinthe;
import modele.Vision;

public record Defi(
        String name,
        int etape,
        int largeur,
        int hauteur,
        double pourcentageMurs,
        int points,
        Vision vision,
        int distanceMin,
        TypeLabyrinthe typeLabyrinthe,
        int portee
) {

    public Defi {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Le nom ne peut pas être vide");
        }
        if (etape <= 0) {
            throw new IllegalArgumentException("L'étape doit être positive");
        }
        if (largeur <= 0 || hauteur <= 0) {
            throw new IllegalArgumentException("Largeur et hauteur doivent être positives");
        }
        if (pourcentageMurs < 0 || pourcentageMurs > 100) {
            throw new IllegalArgumentException("Pourcentage doit être entre 0 et 100");
        }
        if (points < 0) {
            throw new IllegalArgumentException("Points ne peut pas être négatif");
        }
        if (distanceMin < 0) {
            throw new IllegalArgumentException("Distance minimale ne peut pas être négative");
        }
        if (portee < 0) {
            throw new IllegalArgumentException("La portée ne peut pas être négative");
        }
    }

}
