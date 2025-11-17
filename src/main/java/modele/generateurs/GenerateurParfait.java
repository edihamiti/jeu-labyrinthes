package modele.generateurs;

import modele.Cellules.*;
import modele.Labyrinthe;

import java.util.*;

public class GenerateurParfait extends GenerateurLabyrinthe {
    int distanceMin;

    public GenerateurParfait(int largeur, int hauteur, int distanceMin) {
        super(largeur, hauteur);
        this.distanceMin = distanceMin;
    }

    /**
     * Génère le labyrinthe en utilisant l'algorithme de génération de labyrinthe.
     */
    public void generer(Labyrinthe lab) {
        System.out.println("=== DEBUT GENERATION PARFAIT ===");
        System.out.println("Distance minimale requise: " + distanceMin);

        Cellule[][] cellules = new Cellule[largeurMax][hauteurMax];
        for (int i = 0; i < largeurMax; i++) {
            for (int j = 0; j < hauteurMax; j++) {
                cellules[i][j] = new Mur(i, j);
            }
        }

        Random rand = new Random();

        int entreeX = 0;
        int entreeY = 1 + rand.nextInt(Math.max(1, hauteur - 2));
        cellules[entreeX][entreeY] = new Entree(entreeX, entreeY);
        System.out.println("Entrée placée à: (" + entreeX + ", " + entreeY + ")");

        lab.setJoueurX(entreeX);
        lab.setJoueurY(entreeY);

        int startX = entreeX + 1;
        int startY = entreeY;

        if (startX < largeurMax - 1) {
            cellules[startX][startY] = new Chemin(startX, startY);
        }

        Stack<int[]> pile = new Stack<>();
        boolean[][] visite = new boolean[largeurMax][hauteurMax];
        pile.push(new int[]{startX, startY});
        visite[startX][startY] = true;

        int[][] directions = {{0, 2}, {2, 0}, {0, -2}, {-2, 0}}; // droite, bas, gauche, haut (saut de 2)

        while (!pile.empty()) {
            int[] pos = pile.peek();
            int x = pos[0], y = pos[1];

            List<Integer> dirs = new ArrayList<>();
            for (int i = 0; i < directions.length; i++) {
                int nx = x + directions[i][0];
                int ny = y + directions[i][1];
                if (nx > 0 && nx < largeurMax - 1 && ny > 0 && ny < hauteurMax - 1 && !visite[nx][ny]) {
                    dirs.add(i);
                }
            }

            if (!dirs.isEmpty()) {
                int dir = dirs.get(rand.nextInt(dirs.size()));
                int nx = x + directions[dir][0];
                int ny = y + directions[dir][1];

                cellules[x + directions[dir][0]/2][y + directions[dir][1]/2] = new Chemin(x + directions[dir][0]/2, y + directions[dir][1]/2);
                cellules[nx][ny] = new Chemin(nx, ny);

                visite[nx][ny] = true;
                pile.push(new int[]{nx, ny});
            } else {
                pile.pop();
            }
        }

        placerSortieAvecDistance(cellules, entreeX, entreeY);

        lab.setCellules(cellules);
        System.out.println("=== FIN GENERATION PARFAIT ===");
    }

    /**
     * Place la sortie en respectant la distance minimale et maximale (distanceMin + 5)
     */
    private void placerSortieAvecDistance(Cellule[][] cellules, int entreeX, int entreeY) {
        int[][] distances = calculerDistances(cellules, entreeX, entreeY);

        int distanceMax = distanceMin + 5;
        List<int[]> candidats = new ArrayList<>();

        for (int i = 0; i < largeurMax; i++) {
            for (int j = 0; j < hauteurMax; j++) {
                if (cellules[i][j].estChemin() &&
                    distances[i][j] >= distanceMin &&
                    distances[i][j] <= distanceMax) {
                    candidats.add(new int[]{i, j, distances[i][j]});
                }
            }
        }

        System.out.println("Distance min requise: " + distanceMin);
        System.out.println("Distance max autorisée: " + distanceMax);
        System.out.println("Candidats dans la plage: " + candidats.size());

        if (!candidats.isEmpty()) {
            Random rand = new Random();
            int[] choisi = candidats.get(rand.nextInt(candidats.size()));
            int sortieX = choisi[0];
            int sortieY = choisi[1];
            int distanceSortie = choisi[2];

            cellules[sortieX][sortieY] = new Sortie(sortieX, sortieY);
            System.out.println("Sortie placée à (" + sortieX + ", " + sortieY + ") avec distance: " + distanceSortie);
        } else {
            System.out.println("Aucun candidat dans la plage, recherche du meilleur compromis");
            int meilleureX = -1, meilleureY = -1, meilleureDiff = Integer.MAX_VALUE;

            for (int i = 0; i < largeurMax; i++) {
                for (int j = 0; j < hauteurMax; j++) {
                    if (cellules[i][j].estChemin() && distances[i][j] > 0) {
                        int diff = Math.abs(distances[i][j] - distanceMin);
                        if (diff < meilleureDiff) {
                            meilleureDiff = diff;
                            meilleureX = i;
                            meilleureY = j;
                        }
                    }
                }
            }

            if (meilleureX != -1 && meilleureY != -1) {
                cellules[meilleureX][meilleureY] = new Sortie(meilleureX, meilleureY);
                System.out.println("Sortie placée (fallback) à (" + meilleureX + ", " + meilleureY + ") avec distance: " + distances[meilleureX][meilleureY]);
            }
        }
    }

    /**
     * Calcule les distances depuis l'entrée en utilisant BFS
     */
    private int[][] calculerDistances(Cellule[][] cellules, int entreeX, int entreeY) {
        int[][] distances = new int[largeurMax][hauteurMax];
        boolean[][] visite = new boolean[largeurMax][hauteurMax];

        for (int i = 0; i < largeurMax; i++) {
            for (int j = 0; j < hauteurMax; j++) {
                distances[i][j] = -1;
            }
        }

        Queue<int[]> queue = new LinkedList<>();
        queue.add(new int[]{entreeX, entreeY});
        distances[entreeX][entreeY] = 0;
        visite[entreeX][entreeY] = true;

        int[][] directions = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
        int cellulesParcourues = 0;

        while (!queue.isEmpty()) {
            int[] pos = queue.poll();
            int x = pos[0];
            int y = pos[1];
            cellulesParcourues++;

            for (int[] dir : directions) {
                int nx = x + dir[0];
                int ny = y + dir[1];

                if (nx >= 0 && nx < largeurMax && ny >= 0 && ny < hauteurMax
                        && !visite[nx][ny]) {

                    Cellule cellule = cellules[nx][ny];
                    if (cellule.estChemin() || cellule.estEntree()) {
                        visite[nx][ny] = true;
                        distances[nx][ny] = distances[x][y] + 1;
                        queue.add(new int[]{nx, ny});
                    }
                }
            }
        }

        System.out.println("BFS: " + cellulesParcourues + " cellules parcourues");
        return distances;
    }
}
