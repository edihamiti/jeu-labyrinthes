package modele;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import modele.Cellules.*;

import java.util.LinkedList;


/**
 * Classe représentant un labyrinthe.
 */
public class Labyrinthe {

    private final int largeur;
    private final int hauteur;
    private final double pourcentageMurs;
    private final int distanceMin;
    private final int largeurMax;
    private final int hauteurMax;
    private Cellule[][] cellules;
    private int nbChemins;
    private final IntegerProperty joueurX;
    private final IntegerProperty joueurY;
    private final BooleanProperty jeuEnCours;

    /**
     * Constructeur pour un labyrinthe.
     *
     * @param largeur         largeur en cases
     * @param hauteur         hauteur en cases
     * @param pourcentageMurs pourcentage de murs (0-100)
     */
    public Labyrinthe(int largeur, int hauteur, double pourcentageMurs) {
        this.largeur = largeur;
        this.hauteur = hauteur;
        this.pourcentageMurs = pourcentageMurs;
        this.distanceMin = 1;
        this.largeurMax = largeur + 2;
        this.hauteurMax = hauteur + 2;
        this.nbChemins = 0;
        this.joueurX = new SimpleIntegerProperty(0);
        this.joueurY = new SimpleIntegerProperty(1);
        this.jeuEnCours = new SimpleBooleanProperty(true);
    }

    /**
     * Constructeur pour un labyrinthe à partir d'un défi.
     *
     * @param defi le défi à utiliser pour créer le labyrinthe
     */
    public Labyrinthe(Defi defi) {
        this(defi.getLargeur(), defi.getHauteur(), defi.getPourcentageMurs());
    }

    /**
     * Génère le labyrinthe en utilisant l'algorithme de génération de labyrinthe.
     */
    public void generer() {
        cellules = new Cellule[largeurMax][hauteurMax];
        for (int i = 0; i < largeurMax; i++) {
            for (int j = 0; j < hauteurMax; j++) {
                cellules[i][j] = new Mur(i, j);
            }
        }

        java.util.Random rand = new java.util.Random();
        nbChemins = 0;

        int entreeX = 0;
        int entreeY = 1;
        if (entreeY >= hauteurMax) entreeY = 1;
        cellules[entreeX][entreeY] = new Entree(entreeX, entreeY);
        nbChemins++;

        if (entreeX + 1 < largeurMax) {
            cellules[entreeX + 1][entreeY] = new Chemin(entreeX + 1, entreeY);
            nbChemins++;
        }

        java.util.Stack<int[]> pile = new java.util.Stack<>();
        boolean[][] visite = new boolean[largeurMax][hauteurMax];
        pile.push(new int[]{entreeX + 1, entreeY});
        visite[entreeX + 1][entreeY] = true;

        int[][] directions = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}}; // droite, bas, gauche, haut

        while (!pile.empty()) {
            int[] pos = pile.peek();
            int x = pos[0], y = pos[1];

            java.util.List<Integer> dirs = new java.util.ArrayList<>();
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

        java.util.Queue<int[]> queue = new java.util.LinkedList<>();
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
            int sx = largeurMax - 2, sy = hauteurMax - 2;
            while (cellules[sx][sy] instanceof Mur) {
                sx = 1 + rand.nextInt(largeurMax - 2);
                sy = 1 + rand.nextInt(hauteurMax - 2);
            }
            cellules[sx][sy] = new Sortie(sx, sy);
        }

        if (pourcentageMurs < 100.0) {
            int totalCellules = (largeurMax - 2) * (hauteurMax - 2);
            double pourcentageCheminsVoulu = 100.0 - pourcentageMurs;
            int cheminsSouhaites = (int) ((pourcentageCheminsVoulu / 100.0) * totalCellules);

            while (nbChemins < cheminsSouhaites) {
                int x = 1 + rand.nextInt(largeurMax - 2);
                int y = 1 + rand.nextInt(hauteurMax - 2);

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
    }

    /* Calcule le plus court chemin entre l'entrée et la sortie du labyrinthe (Dijkstra).
     *
     * @return la longueur du plus court chemin
     */
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
        return jeuEnCours.get();
    }

    public int getJoueurY() {
        return joueurY.get();
    }

    public int getJoueurX() {
        return joueurX.get();
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
        this.jeuEnCours.set(jeuEnCours);
    }

    public void setJoueurY(int joueurY) {
        this.joueurY.set(joueurY);
    }

    public void setJoueurX(int joueurX) {
        this.joueurX.set(joueurX);
    }

    public void setCellules(Cellule[][] cellules) {
        this.cellules = cellules;
    }

    /**
     * Vérifie si le joueur peut se déplacer vers une cellule donnée.
     *
     * @param x la coordonnée x de la cellule
     * @param y la coordonnée y de la cellule
     * @return true si le déplacement est possible, false sinon
     */
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

    public IntegerProperty joueurXProperty() {
        return joueurX;
    }

    public IntegerProperty joueurYProperty() {
        return joueurY;
    }

    public BooleanProperty jeuEnCoursProperty() {
        return jeuEnCours;
    }
}
