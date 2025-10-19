package modele;

import modele.Cellules.Cellule;
import modele.Cellules.Entree;
import modele.Cellules.Mur;

public class Labyrinthe {

    private final int largeur;
    private final int hauteur;
    private final double pourcentageMurs;
    private final int distanceMin;
    private Cellule[][] cellules;

    private final int largeurMax;
    private final int hauteurMax;

    public Labyrinthe(int largeur, int hauteur, double pourcentageMurs, int distanceMin) {
        this.largeur = largeur;
        this.hauteur = hauteur;
        this.pourcentageMurs = pourcentageMurs;
        this.distanceMin = distanceMin;
        this.largeurMax = largeur+2;
        this.hauteurMax = hauteur+2;
    }

    public void generer() {
        cellules = new Cellule[largeurMax][hauteurMax];
        for (int i = 0; i < largeurMax; i++) {
            for (int j = 0; j < hauteurMax; j++) {
                cellules[i][j] = new Mur(i,j);
            }
        }
        int x = 0;
        int y = 1;
        cellules[0][1]= new Entree(x,y);
        faireChemin(cellules,x,y);
    }


    public void faireChemin(Cellule[][] cellules, int x, int y) {

    }

    public void calculePlusCourtChemin() {
        //TODO
        //this.distanceMin = ;
    }
}
