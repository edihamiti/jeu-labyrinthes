import Cellules.Cellule;
import Cellules.Entree;
import Cellules.Mur;

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

    public void generer(int largeur, int hauteur, double pourcentageMurs) {
        int largeurMax = largeur+2;
        int hauteurMax = hauteur+2;
        Cellule[][] sol = new Cellule[largeurMax][hauteurMax];
        int nombreMursTotal = (int)((largeur*hauteur)*pourcentageMurs);
        int nombreMurs = nombreMursTotal;
        for(int i = 0; i<=largeurMax; i++){
            sol[i][0]= new Mur(i,0);
        }
        for(int i = 0; i<=largeurMax; i++){
            sol[i][hauteurMax]= new Mur(i,0);
        }
        for(int i = largeur; i<=hauteurMax; i++){
            sol[largeurMax][i]= new Mur(i,0);
        }
        for(int i = 0; i<=hauteurMax; i++){
            sol[0][i]= new Mur(i,0);
        }
        sol[1][0]= new Entree(1,0);
        //TODO
    }

    public void calculePlusCourtChemin() {
        //TODO
        //this.distanceMin = ;
    }
}
