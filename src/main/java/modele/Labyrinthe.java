package modele;

import modele.defi.Defi;
import modele.Cellules.Cellule;
import modele.defi.Defi;

import java.util.ArrayList;
import java.util.List;


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
    private int joueurX;
    private int joueurY;
    private boolean jeuEnCours;
    private boolean cleObtenue;
    private int cleX;
    private int cleY;
    private int sortieX;
    private int sortieY;
    private final List<LabyrintheObserver> observers;


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
        this.joueurX = 0;
        this.joueurY = 1;
        this.jeuEnCours = true;
        this.observers = new ArrayList<>();
        this.cleObtenue = false;
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
        notifyObservers();
    }

    public void setJoueurX(int joueurX) {
        this.joueurX =  joueurX;
        notifyObservers();
    }

    public boolean getCleObtenue(){
        return cleObtenue;
    }

    public void setCleObtenue(){
        this.cleObtenue = true;
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

        if (cellules[x][y].estSortie() && cellules[x][y] instanceof modele.Cellules.Sortie sortie) {
            if (sortie.estVerrouillee() && !cleObtenue) {
                return false;
            }
        }

        return !cellules[x][y].estMur();
    }

    public boolean deplacer(int x, int y) {
        if (!peutDeplacer(x, y)) return false;

        setJoueurX(x);
        setJoueurY(y);
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

    public void setCellules(Cellule[][] cellules) {
        this.cellules = cellules;
    }

    public void addObserver(LabyrintheObserver observer) {
        observers.add(observer);
    }

    public void notifyObservers() {
        observers.forEach(LabyrintheObserver::update);
    }

    public boolean isCleObtenue() {
        return cleObtenue;
    }


    public void setPositionCle(int x, int y) {
        this.cleX = x;
        this.cleY = y;
    }

    public void setPositionSortie(int x, int y) {
        this.sortieX = x;
        this.sortieY = y;
    }

    public int getSortieX() {
        return sortieX;
    }

    public int getSortieY() {
        return sortieY;
    }

    public void resetCleObtenue() {
        this.cleObtenue = false;
    }
}