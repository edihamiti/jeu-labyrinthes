package modele;

import modele.Cellules.Cellule;

public class Labyrinthe {

    private final int largeur;
    private final int hauteur;
    private final double pourcentageMurs;
    private final int distanceMin;
    private Cellule[][] cellules;

    public Labyrinthe(int largeur, int hauteur, double pourcentageMurs, int distanceMin) {
        this.largeur = largeur;
        this.hauteur = hauteur;
        this.pourcentageMurs = pourcentageMurs;
        this.distanceMin = distanceMin;
    }

    public void generer() {
        //TODO
    }

    public void calculePlusCourtChemin() {
        //TODO
        //this.distanceMin = ;
    }
}
