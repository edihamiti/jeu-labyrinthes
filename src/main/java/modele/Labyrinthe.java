package modele;

import modele.Cellules.*;

import java.util.Random;

public class Labyrinthe {

    private final int largeur;
    private final int hauteur;
    private final double pourcentageMurs;
    private final int distanceMin;
    private Cellule[][] cellules;

    private final int largeurMax;
    private final int hauteurMax;
    private final double nbCaseChemin;

    public Labyrinthe(int largeur, int hauteur, double pourcentageMurs, int distanceMin) {
        this.largeur = largeur;
        this.hauteur = hauteur;
        this.pourcentageMurs = pourcentageMurs;
        this.distanceMin = distanceMin;
        this.largeurMax = largeur+2;
        this.hauteurMax = hauteur+2;
        this.nbCaseChemin =(int) ((largeur*hauteur)*((100-pourcentageMurs)/100));
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

        for (int i = 1; i < largeur; i++) {
            for (int j = 1; j < hauteur; j++) {
                if ((!(cellules[i][j] instanceof Entree) && !(cellules[i][j] instanceof Sortie)) && (Math.random() < (1-(pourcentageMurs/100)))) {
                    cellules[i][j] = new Chemin(x,y);
                }
            }
        }
    }


    public void faireChemin(Cellule[][] cellules, int x, int y) {
        int cheminx = x;
        int cheminy = y;
        if(x == 0){
            cheminx += 1;
        } else if (y == 0){
            cheminy += 1;
        }

        cellules[cheminx][cheminy] = new Chemin(cheminx,cheminy);

        boolean nouveauchemin;
        for (int i = 1; i <= nbCaseChemin; i++) {
        nouveauchemin = false;
            Random random = new Random();
            int sens = random.nextInt(4);

            while(!nouveauchemin){
                switch (sens){
                    case 0: //Pour aller vers le haut
                    if(cheminy-1 != 0){
                        cheminy--;
                        nouveauchemin = true;
                    } else {
                        nouveauchemin = false;
                        sens += random.nextInt(3)+1;
                    }
                    break;
                    case 1: //Pour aller vers la droite
                    if(cheminx+1 != largeurMax){
                        cheminx++;
                        nouveauchemin = true;
                    } else {
                        nouveauchemin = false;
                        int choix = random.nextInt(2);
                        if(choix == 0)sens = 0;
                        else sens += random.nextInt(2)+2;
                    }
                    break;
                    case 2: //Pour aller vers le bas
                    if(cheminy+1 != hauteurMax){
                        cheminy++;
                        nouveauchemin = true;
                    } else {
                        nouveauchemin = false;
                        int choix = random.nextInt(2);
                        if(choix == 0)sens -=  random.nextInt(2)+1;
                        else sens = 3;
                    }
                    break;
                    case 3: //Pour aller vers la gauche
                    if(cheminx-1 != 0){
                        cheminx--;
                        nouveauchemin = true;
                    } else {
                        nouveauchemin = false;
                        sens -= random.nextInt(3)+1;
                    }
                    break;
                }
            }

            if(i == nbCaseChemin){
                cellules[cheminx][cheminy] = new Sortie(x,y);
            } else {
                cellules[cheminx][cheminy] = new Chemin(cheminx, cheminy);
            }
        }
    }

    public void calculePlusCourtChemin() {
        //TODO
        //this.distanceMin = ;
    }
}
