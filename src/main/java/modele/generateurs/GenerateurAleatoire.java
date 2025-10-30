package modele.generateurs;

import java.util.LinkedList;
import modele.Cellules.*;
import modele.Labyrinthe;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GenerateurAleatoire extends GenerateurLabyrinthe {
    double pourcentageMurs;
    private int nbChemins = 0;
    Cellule[][] cellules;

    public GenerateurAleatoire(int largeur, int hauteur, double pourcentageMurs) {
        super(largeur, hauteur);
        this.pourcentageMurs = pourcentageMurs;
    }

    public void generer(Labyrinthe lab) {
        nbChemins = 0;

        cellules = new Cellule[largeurMax][hauteurMax];
        for (int i = 0; i < largeurMax; i++) {
            for (int j = 0; j < hauteurMax; j++) {
                cellules[i][j] = new Mur(i, j);
            }
        }

        int entreeX = 0;
        int entreeY = 1;
        cellules[entreeX][entreeY] = new Entree(entreeX, entreeY);
        faireChemin(cellules, entreeX, entreeY);

        for (int i = 1; i < largeurMax - 1; i++) {
            for (int j = 1; j < hauteurMax - 1; j++) {
                if (cellules[i][j] instanceof Chemin) {
                    if (i % 10 == 0) {
                        faireCheminAlternatif(cellules, i, j);
                    }
                }
            }
        }

        int totalCellules = (largeurMax - 2) * (hauteurMax - 2);
        double pourcentageCheminsVoulu = 100.0 - pourcentageMurs;
        int nbCheminsVoulu = (int) (totalCellules * pourcentageCheminsVoulu / 100.0);

        Random random = new Random();

        while (nbChemins < nbCheminsVoulu) {
            int i = random.nextInt(largeurMax - 2) + 1;
            int j = random.nextInt(hauteurMax - 2) + 1;

            if (cellules[i][j] instanceof Mur) {
                cellules[i][j] = new Chemin(i, j);
                nbChemins++;
            }
        }

        lab.setCellules(cellules);
        lab.setJoueurX(entreeX);
        lab.setJoueurY(entreeY);
        lab.setJeuEnCours(true);
    }

    public void faireChemin(Cellule[][] cellules, int startX, int startY) {
        Random random = new Random();
        LinkedList<int[]> pile = new LinkedList<>();
        boolean[][] visite = new boolean[largeurMax][hauteurMax];

        int x = startX;
        int y = startY;

        cellules[x][y] = new Entree(x, y);
        visite[x][y] = true;
        pile.push(new int[]{x, y});

        int[][] directions = {{0, -1}, {0, 1}, {-1, 0}, {1, 0}}; // haut, bas, gauche, droite

        int[] derniereVisite = null;

        while (!pile.isEmpty()) {
            int[] courant = pile.peek();
            x = courant[0];
            y = courant[1];

            // Directions possibles
            List<int[]> voisins = new ArrayList<>();
            for (int[] d : directions) {
                int nx = x + d[0];
                int ny = y + d[1];

                if (nx > 0 && ny > 0 && nx < largeurMax - 1 && ny < hauteurMax - 1 && !visite[nx][ny]) {
                    voisins.add(new int[]{nx, ny});
                }
            }

            if (!voisins.isEmpty()) {
                int[] suivant = voisins.get(random.nextInt(voisins.size()));
                int nx = suivant[0];
                int ny = suivant[1];

                cellules[nx][ny] = new Chemin(nx, ny);
                nbChemins++;
                derniereVisite = new int[]{nx, ny};
                visite[nx][ny] = true;
                pile.push(new int[]{nx, ny});
            } else {
                int[] popped = pile.pop();
                if (derniereVisite == null) {
                    derniereVisite = popped;
                }
            }

            if (pile.size() > (largeur + hauteur) * 2) {
                break;
            }
        }

        if (!pile.isEmpty()) {
            int[] derniere = pile.peek();
            cellules[derniere[0]][derniere[1]] = new Sortie(derniere[0], derniere[1]);
        } else if (derniereVisite != null) {
            cellules[derniereVisite[0]][derniereVisite[1]] = new Sortie(derniereVisite[0], derniereVisite[1]);
        } else {
            int ex = startX;
            int ey = startY + 1;
            if (ey > 0 && ey < hauteurMax - 1) {
                cellules[ex][ey] = new Sortie(ex, ey);
            }
        }
    }

    public void faireCheminAlternatif(Cellule[][] cellules, int startX, int startY) {
        Random random = new Random();
        int[][] directions = {{0, -1}, {0, 1}, {-1, 0}, {1, 0}};

        int x = startX;
        int y = startY;
        int longueur = (largeur + hauteur) / 2;

        for (int i = 0; i < longueur; i++) {
            List<int[]> possibles = new ArrayList<>();
            for (int[] d : directions) {
                int nx = x + d[0];
                int ny = y + d[1];
                if (nx > 0 && ny > 0 && nx < largeurMax - 1 && ny < hauteurMax - 1) {
                    if (cellules[nx][ny] instanceof Mur) {
                        possibles.add(new int[]{nx, ny});
                    }
                }
            }

            if (possibles.isEmpty()) break;

            int[] suivant = possibles.get(random.nextInt(possibles.size()));
            int nx = suivant[0];
            int ny = suivant[1];

            cellules[nx][ny] = new Chemin(nx, ny);
            nbChemins++;

            x = nx;
            y = ny;
        }
    }

}
