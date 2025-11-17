package modele;

import defi.modele.Defi;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import modele.Cellules.Cellule;


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
        this(defi.largeur(), defi.hauteur(), defi.pourcentageMurs());
    }

    /**
     * Calcule le plus court chemin entre l'entrée et la sortie du labyrinthe.
     * Délègue le calcul à la classe Pathfinder qui utilise l'algorithme BFS.
     *
     * @return la longueur du plus court chemin
     */
    public int calculePlusCourtChemin() {
        Pathfinder pathfinder = new Pathfinder();
        return pathfinder.findShortestPath(cellules, largeurMax, hauteurMax);
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
