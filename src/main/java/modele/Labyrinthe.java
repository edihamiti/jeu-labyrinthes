package modele;

import modele.Cellules.*;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Random;

public class Labyrinthe {

    private final int largeur;
    private final int hauteur;
    private final double pourcentageMurs;
    private final int distanceMin;
    private final int largeurMax;
    private final int hauteurMax;
    private Cellule[][] cellules;
    private int joueurX;
    private int joueurY;
    private boolean jeuEnCours;

    public Labyrinthe(int largeur, int hauteur, double pourcentageMurs) {
        this.largeur = largeur;
        this.hauteur = hauteur;
        this.pourcentageMurs = pourcentageMurs;
        this.distanceMin = 1;
        this.largeurMax = largeur + 2;
        this.hauteurMax = hauteur + 2;
        this.joueurX = 0;
        this.joueurY = 1;
        this.jeuEnCours = true;
    }

    public Labyrinthe(Defi defi) {
        this(defi.getLargeur(), defi.getHauteur(), defi.getPourcentageMurs());
    }

    public void generer() {
        cellules = new Cellule[largeurMax][hauteurMax];
        for (int i = 0; i < largeurMax; i++) {
            for (int j = 0; j < hauteurMax; j++) {
                cellules[i][j] = new Mur(i, j);
            }
        }
        int x = 0;
        int y = 1;
        cellules[0][1] = new Entree(x, y);
        faireChemin(cellules, x, y);

        for (int i = 1; i < largeurMax-1; i++) {
            for (int j = 1; j < hauteurMax-1; j++) {
                if ((!(cellules[i][j] instanceof Entree) && !(cellules[i][j] instanceof Sortie)) && (Math.random() < (1 - (pourcentageMurs / 100)))) {
                    cellules[i][j] = new Chemin(i, j);
                }
            }
        }
    }


    public void faireChemin(Cellule[][] cellules, int x, int y) {
        int nbCaseChemin = (int) ((largeur * hauteur) /*** ((100 - pourcentageMurs) / 100)**/);

        int cheminx = x;
        int cheminy = y;
        if (x == 0) {
            cheminx += 1;
        } else if (y == 0) {
            cheminy += 1;
        }

        cellules[cheminx][cheminy] = new Chemin(cheminx, cheminy);

        boolean nouveauchemin;
        for (int i = 1; i <= nbCaseChemin; i++) {
            nouveauchemin = false;
            Random random = new Random();
            int sens = random.nextInt(4);

            while (!nouveauchemin) {
                switch (sens) {
                    case 0: //Pour aller vers le haut
                        if (cheminy - 1 != 0) {
                            cheminy--;
                            nouveauchemin = true;
                        } else {
                            nouveauchemin = false;
                            sens += random.nextInt(3) + 1;
                        }
                        break;
                    case 1: //Pour aller vers la droite
                        if (cheminx + 1 != largeurMax) {
                            cheminx++;
                            nouveauchemin = true;
                        } else {
                            nouveauchemin = false;
                            int choix = random.nextInt(2);
                            if (choix == 0) sens = 0;
                            else sens += random.nextInt(2) + 2;
                        }
                        break;
                    case 2: //Pour aller vers le bas
                        if (cheminy + 1 != hauteurMax) {
                            cheminy++;
                            nouveauchemin = true;
                        } else {
                            nouveauchemin = false;
                            int choix = random.nextInt(2);
                            if (choix == 0) sens -= random.nextInt(2) + 1;
                            else sens = 3;
                        }
                        break;
                    case 3: //Pour aller vers la gauche
                        if (cheminx - 1 != 0) {
                            cheminx--;
                            nouveauchemin = true;
                        } else {
                            nouveauchemin = false;
                            sens -= random.nextInt(3) + 1;
                        }
                        break;
                }
            }

            if (i == nbCaseChemin) {
                cellules[cheminx][cheminy] = new Sortie(x, y);
            } else {
                cellules[cheminx][cheminy] = new Chemin(cheminx, cheminy);
            }
        }
    }

    public int calculePlusCourtChemin() {
        int startX = -1;
        int startY = -1;
        int endX = -1;
        int endY = -1;

        for (int i = 0; i < largeurMax; i++) {
            for (int j = 0; j < hauteurMax; j++) {
                if (cellules[i][j] != null && cellules[i][j].estEntree()) {
                    startX = i;
                    startY = j;
                }
                if (cellules[i][j] != null && cellules[i][j].estSortie()) {
                    endX = i;
                    endY = j;
                }
            }
        }

        if (startX == -1 || endX == -1) {
            // Pas d'entrée ou sortie
            return 0;
        }

        boolean[][] visited = new boolean[largeurMax][hauteurMax];
        int[][] prevX = new int[largeurMax][hauteurMax];
        int[][] prevY = new int[largeurMax][hauteurMax];
        int[][] dist = new int[largeurMax][hauteurMax];
        for (int i = 0; i < largeurMax; i++) {
            for (int j = 0; j < hauteurMax; j++) {
                prevX[i][j] = -1;
                prevY[i][j] = -1;
                dist[i][j] = -1;
            }
        }

        LinkedList<int[]> queue = new LinkedList<>();
        queue.add(new int[]{startX, startY});
        visited[startX][startY] = true;
        dist[startX][startY] = 0;

        int[][] direction = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        while (!queue.isEmpty()) {
            int[] courant = queue.removeFirst();
            int cx = courant[0];
            int cy = courant[1];

            if (cx == endX && cy == endY) {
                break;
            }

            for (int[] d : direction) {
                int nx = cx + d[0];
                int ny = cy + d[1];

                if (nx < 0 || nx >= largeurMax || ny < 0 || ny >= hauteurMax) continue;
                if (visited[nx][ny]) continue;
                if (cellules[nx][ny] == null) continue;
                if (cellules[nx][ny].estMur()) continue;

                visited[nx][ny] = true;
                prevX[nx][ny] = cx;
                prevY[nx][ny] = cy;
                dist[nx][ny] = dist[cx][cy] + 1;
                queue.addLast(new int[]{nx, ny});
            }
        }

        if (dist[endX][endY] == -1) {
            // Pas de chemin trouvé
            return 0;
        }
        return dist[endX][endY];
    }

    public double getPourcentageMurs() {
        return pourcentageMurs;
    }

    public boolean isJeuEnCours() {
        return jeuEnCours;
    }

    public int getJoueurY() {
        return joueurY;
    }

    public int getJoueurX() {
        return joueurX;
    }

    public int getDistanceMin() {
        return distanceMin;
    }

    public int getHauteur() {
        return hauteur;
    }

    public int getLargeur() {
        return largeur;
    }

    public void setJeuEnCours(boolean jeuEnCours) {
        this.jeuEnCours = jeuEnCours;
    }

    public void setJoueurY(int joueurY) {
        this.joueurY = joueurY;
    }

    public void setJoueurX(int joueurX) {
        this.joueurX = joueurX;
    }

    public void setCellules(Cellule[][] cellules) {
        this.cellules = cellules;
    }

    public boolean peutDeplacer(int x, int y) {
        if (x < 0 || x >= largeurMax || y < 0 || y >= hauteurMax) {
            return false;
        } else if (cellules[x][y] == null || cellules[x][y].estMur()) {
            return false;
        }
        return !cellules[x][y].estMur();
    }

    public boolean estSurSortie(int x, int y) {
        return cellules[x][y].estSortie();
    }
    
    public int getLargeurMax() {
        return largeurMax;
    }

    public int getHauteurMax() {
        return hauteurMax;
    }

    public Cellule[][] getCellules() {
        return cellules;
    }
}
