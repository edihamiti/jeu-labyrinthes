package modele.defi;

import modele.TypeLabyrinthe;
import modele.Vision;


/**
 * Représente un défi dans le jeu de labyrinthe.
 *
 * Chaque défi contient des informations sur :
 * - son nom et son étape
 * - les dimensions du labyrinthe
 * - le pourcentage de murs
 * - les points attribués
 * - la vision disponible pour le joueur
 * - la distance minimale pour certains types de labyrinthe
 * - le type de labyrinthe
 * - la portée (portée de vision ou autre paramètre spécifique)
 *
 * Toutes les valeurs sont validées à la création pour garantir que le défi
 * est correct et cohérent.
 *
 * Exemple d'utilisation :
 * Defi d = new Defi("Défi 1", 1, 20, 20, 30.0, 100, Vision.TOTAL, 10, TypeLabyrinthe.ALEATOIRE, 5);
 */
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


    /**
     * Constructeur compact du record.
     *
     * Valide les paramètres :
     * - name ne peut pas être null ou vide
     * - etape doit être positive
     * - largeur et hauteur doivent être positives
     * - pourcentageMurs doit être compris entre 0 et 100
     * - points, distanceMin et portee ne peuvent pas être négatifs
     *
     * @throws IllegalArgumentException si l'une des conditions n'est pas respectée
     */
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
