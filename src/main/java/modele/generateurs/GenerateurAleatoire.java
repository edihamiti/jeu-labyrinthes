package modele.generateurs;

import modele.Labyrinthe;

public class GenerateurAleatoire extends GenerateurLabyrinthe {
    double pourcentageMurs;

    public GenerateurAleatoire(int largeur, int hauteur, double pourcentageMurs) {
        super(largeur, hauteur);
        this.pourcentageMurs = pourcentageMurs;
    }

    public void generer(Labyrinthe lab) {
        throw new UnsupportedOperationException("La génération aléatoire de labyrinthe n'est pas encore implémentée");
    }
}
