package modele;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import modele.Cellules.*;

import java.util.*;


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
    private final IntegerProperty joueurX;
    private final IntegerProperty joueurY;
    private final BooleanProperty jeuEnCours;

    /**
     * Constructeur pour un labyrinthe.
     *
     * @param largeur         largeur en cases
     * @param hauteur         hauteur en cases
     * @param pourcentageMurs pourcentage de murs (0-100)
     * @param distanceMin     distance minimale entre l'entrée et la sortie
     */
    public Labyrinthe(int largeur, int hauteur, double pourcentageMurs, int distanceMin) {
        this.largeur = largeur;
        this.hauteur = hauteur;
        this.pourcentageMurs = pourcentageMurs;
        this.distanceMin = distanceMin;
        this.largeurMax = largeur + 2;
        this.hauteurMax = hauteur + 2;
        this.joueurX = new SimpleIntegerProperty(0);
        this.joueurY = new SimpleIntegerProperty(1);
        this.jeuEnCours = new SimpleBooleanProperty(true);
    }

    /**
     * Constructeur pour un labyrinthe.
     *
     * @param largeur         largeur en cases
     * @param hauteur         hauteur en cases
     * @param pourcentageMurs pourcentage de murs (0-100)
     */
    public Labyrinthe(int largeur, int hauteur, double pourcentageMurs) {
        this(largeur, hauteur, pourcentageMurs, 1);
    }

    /**
     * Constructeur pour un labyrinthe à partir d'un défi.
     *
     * @param defi le défi à utiliser pour créer le labyrinthe
     */
    public Labyrinthe(Defi defi) {
        this(defi.getLargeur(), defi.getHauteur(), defi.getPourcentageMurs());
    }

    /** Calcule le plus court chemin entre l'entrée et la sortie du labyrinthe (Dijkstra).
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

    public boolean deplacer(int x, int y) {
        if (!peutDeplacer(x, y)) return false;

        joueurX.set(x);
        joueurY.set(y);
        return true;
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
