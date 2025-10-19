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

    public Labyrinthe(int largeur, int hauteur, double pourcentageMurs, int distanceMin) {
        this.largeur = largeur;
        this.hauteur = hauteur;
        this.pourcentageMurs = pourcentageMurs;
        this.distanceMin = distanceMin;
    }

    public void generer() {
        cellules = new Cellule[largeur+2][hauteur+2];
        for (int i = 0; i < largeur+2; i++) {
            for (int j = 0; j < hauteur+2; j++) {
                cellules[i][j] = new Mur(i,j);
            }
        }
        cellules[0][1]= new Entree(0,1);
        faireChemin(cellules);
    }


    public void faireChemin(Cellule[][] cellules) {

    }

    public void calculePlusCourtChemin() {
        //TODO
        //this.distanceMin = ;
    }
}
