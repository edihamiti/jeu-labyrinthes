package modele.generateurs;

import modele.Cellules.*;
import modele.Labyrinthe;

import java.util.*;

public class GenerateurParfait extends GenerateurLabyrinthe {
    double pourcentageMurs;

    public GenerateurParfait(int largeur, int hauteur, double pourcentageMurs) {
        super(largeur, hauteur);
        this.pourcentageMurs = pourcentageMurs;
    }

    /**
     * Génère le labyrinthe en utilisant l'algorithme de génération de labyrinthe.
     */
    public void generer(Labyrinthe lab) {
        Cellule[][] cellules = new Cellule[largeurMax][hauteurMax];
        for (int i = 0; i < largeurMax; i++) {
            for (int j = 0; j < hauteurMax; j++) {
                cellules[i][j] = new Mur(i, j);
            }
        }

        Random rand = new Random();
        int nbChemins = 0;

        int entreeX = rand.nextInt(largeur);
        int entreeY = 1 + rand.nextInt(largeur);
        if (entreeY >= hauteurMax) entreeY = hauteur;
        cellules[entreeX][entreeY] = new Entree(entreeX, entreeY);
        nbChemins++;

        lab.setJoueurX(entreeX);
        lab.setJoueurY(entreeY);

        if (entreeX + 1 < largeurMax) {
            cellules[entreeX + 1][entreeY] = new Chemin(entreeX + 1, entreeY);
            nbChemins++;
        }

        Stack<int[]> pile = new Stack<>();
        boolean[][] visite = new boolean[largeurMax][hauteurMax];
        pile.push(new int[]{entreeX + 1, entreeY});
        visite[entreeX + 1][entreeY] = true;

        int[][] directions = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}}; // droite, bas, gauche, haut

        while (!pile.empty()) {
            int[] pos = pile.peek();
            int x = pos[0], y = pos[1];

            List<Integer> dirs = new ArrayList<>();
            for (int i = 0; i < directions.length; i++) {
                int nx = x + 2 * directions[i][0];
                int ny = y + 2 * directions[i][1];
                if (nx > 0 && nx < largeurMax - 1 && ny > 0 && ny < hauteurMax - 1 && !visite[nx][ny]) {
                    dirs.add(i);
                }
            }

            if (!dirs.isEmpty()) {
                int dir = dirs.get(rand.nextInt(dirs.size()));
                int nx = x + 2 * directions[dir][0];
                int ny = y + 2 * directions[dir][1];

                cellules[x + directions[dir][0]][y + directions[dir][1]] = new Chemin(x + directions[dir][0], y + directions[dir][1]);
                cellules[nx][ny] = new Chemin(nx, ny);
                nbChemins += 2;

                visite[nx][ny] = true;
                pile.push(new int[]{nx, ny});
            } else {
                pile.pop();
            }
        }

        int maxX = -1, maxY = -1, maxDist = -1;
        int[][] dist = new int[largeurMax][hauteurMax];
        for (int i = 0; i < largeurMax; i++) {
            for (int j = 0; j < hauteurMax; j++) {
                dist[i][j] = -1;
            }
        }

        Queue<int[]> queue = new LinkedList<>();
        queue.add(new int[]{entreeX + 1, entreeY});
        dist[entreeX + 1][entreeY] = 0;
        visite = new boolean[largeurMax][hauteurMax];
        visite[entreeX + 1][entreeY] = true;

        while (!queue.isEmpty()) {
            int[] pos = queue.poll();
            int x = pos[0], y = pos[1];

            if (dist[x][y] > maxDist) {
                maxDist = dist[x][y];
                maxX = x;
                maxY = y;
            }

            for (int[] dir : directions) {
                int nx = x + dir[0];
                int ny = y + dir[1];
                if (nx > 0 && nx < largeurMax - 1 && ny > 0 && ny < hauteurMax - 1
                        && !visite[nx][ny] && !(cellules[nx][ny] instanceof Mur)) {
                    visite[nx][ny] = true;
                    dist[nx][ny] = dist[x][y] + 1;
                    queue.add(new int[]{nx, ny});
                }
            }
        }

        if (maxX != -1 && maxY != -1) {
            cellules[maxX][maxY] = new Sortie(maxX, maxY);
        } else {
            int sx = largeur, sy = hauteur;
            while (cellules[sx][sy] instanceof Mur) {
                sx = 1 + rand.nextInt(largeur);
                sy = 1 + rand.nextInt(hauteur);
            }
            cellules[sx][sy] = new Sortie(sx, sy);
        }

        if (pourcentageMurs < 100.0) {
            int totalCellules = (largeur) * (hauteur);
            double pourcentageCheminsVoulu = 100.0 - pourcentageMurs;
            int cheminsSouhaites = (int) ((pourcentageCheminsVoulu / 100.0) * totalCellules);

            while (nbChemins < cheminsSouhaites) {
                int x = 1 + rand.nextInt(largeur);
                int y = 1 + rand.nextInt(hauteur);

                if (cellules[x][y] instanceof Mur &&
                        !(cellules[x][y].estEntree()) &&
                        !(cellules[x][y].estSortie())) {

                    int cheminsAdjacents = 0;
                    for (int[] dir : directions) {
                        int nx = x + dir[0];
                        int ny = y + dir[1];
                        if (nx >= 0 && nx < largeurMax && ny >= 0 && ny < hauteurMax &&
                                !(cellules[nx][ny] instanceof Mur)) {
                            cheminsAdjacents++;
                        }
                    }

                    if (cheminsAdjacents >= 1) {
                        cellules[x][y] = new Chemin(x, y);
                        nbChemins++;
                    }
                }
            }
        }

        lab.setCellules(cellules);
    }
}
