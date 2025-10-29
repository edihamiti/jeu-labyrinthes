package modele.generateurs;

import modele.Labyrinthe;

public abstract class GenerateurLabyrinthe {
    int largeur;
    int hauteur;
    int largeurMax;
    int hauteurMax;

    public GenerateurLabyrinthe(int largeur, int hauteur) {
        this.largeur = largeur;
        this.hauteur = hauteur;
        this.largeurMax = largeur + 2;
        this.hauteurMax = hauteur + 2;
    }

    public abstract void generer(Labyrinthe lab);
}
