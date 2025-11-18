package modele;

import modele.Cellules.Cellule;

import java.util.LinkedList;

/**
 * Classe responsable du calcul de chemins dans un labyrinthe.
 * Utilise l'algorithme BFS (Breadth-First Search) pour trouver le plus court chemin.
 */
public class Pathfinder {

    /**
     * Calcule la longueur du plus court chemin entre l'entrée et la sortie du labyrinthe.
     * Utilise un algorithme BFS (parcours en largeur) qui garantit de trouver le chemin le plus court
     * dans un graphe non pondéré.
     *
     * @param cellules la grille de cellules du labyrinthe
     * @param largeurMax la largeur maximale de la grille
     * @param hauteurMax la hauteur maximale de la grille
     * @return la longueur du plus court chemin, ou 0 si aucun chemin n'existe
     */
    public int findShortestPath(Cellule[][] cellules, int largeurMax, int hauteurMax) {
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
            return 0;
        }
        return dist[endX][endY];
    }
}
