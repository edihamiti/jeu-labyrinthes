package modele;

import modele.generateurs.GenerateurLabyrinthe;
import modele.generateurs.GenerateurAleatoire;
import modele.generateurs.GenerateurParfait;

public enum TypeLabyrinthe {
    PARFAIT("Parfait") {
        @Override
        public GenerateurLabyrinthe creerGenerateur(int largeur, int hauteur, double pourcentageMurs, int distanceMin) {
            return new GenerateurParfait(largeur, hauteur, distanceMin);
        }
    },
    ALEATOIRE("Aléatoire") {
        @Override
        public GenerateurLabyrinthe creerGenerateur(int largeur, int hauteur, double pourcentageMurs, int distanceMin) {
            return new GenerateurAleatoire(largeur, hauteur, pourcentageMurs);
        }
    };

    private String nom;

    TypeLabyrinthe(String nom) {
        this.nom = nom;
    }

    public String getNom() {
        return nom;
    }

    public abstract GenerateurLabyrinthe creerGenerateur(int largeur, int hauteur, double pourcentageMurs, int distanceMin);
}