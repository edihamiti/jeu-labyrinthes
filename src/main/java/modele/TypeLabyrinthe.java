package modele;

import modele.generateurs.GenerateurLabyrinthe;
import modele.generateurs.GenerateurAleatoire;
import modele.generateurs.GenerateurParfait;

/**
 * Enumération des types de labyrinthe disponibles.
 * Chaque type permet de créer un générateur spécifique.
 */
public enum TypeLabyrinthe {

    /**
     * Labyrinthe parfait (sans cycles).
     * Utilise un générateur de type GenerateurParfait.
     */
    PARFAIT("Parfait") {
        @Override
        public GenerateurLabyrinthe creerGenerateur(int largeur, int hauteur, double pourcentageMurs, int distanceMin) {
            return new GenerateurParfait(largeur, hauteur, distanceMin);
        }
    },

    /**
     * Labyrinthe aléatoire.
     * Utilise un générateur de type GenerateurAleatoire.
     */
    ALEATOIRE("Aléatoire") {
        @Override
        public GenerateurLabyrinthe creerGenerateur(int largeur, int hauteur, double pourcentageMurs, int distanceMin) {
            return new GenerateurAleatoire(largeur, hauteur, pourcentageMurs);
        }
    };

    private String nom;

    /**
     * Construit un type de labyrinthe avec un nom lisible.
     *
     * @param nom le nom du type de labyrinthe
     */
    TypeLabyrinthe(String nom) {
        this.nom = nom;
    }


    /**
     * Retourne le nom lisible du type de labyrinthe.
     *
     * @return le nom du type de labyrinthe
     */
    public String getNom() {
        return nom;
    }


    /**
     * Crée un générateur de labyrinthe correspondant à ce type.
     *
     * @param largeur largeur du labyrinthe
     * @param hauteur hauteur du labyrinthe
     * @param pourcentageMurs pourcentage de murs (utilisé par certains générateurs)
     * @param distanceMin distance minimale entre l'entrée et la sortie (pour certains générateurs)
     * @return un générateur de labyrinthe adapté au type
     */
    public abstract GenerateurLabyrinthe creerGenerateur(int largeur, int hauteur, double pourcentageMurs, int distanceMin);
}