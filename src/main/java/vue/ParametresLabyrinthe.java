package vue;

import modele.TypeLabyrinthe;

/**
 * Classe pour passer des paramètres de labyrinthe via le Router.
 */
public class ParametresLabyrinthe {
    private final int largeur;
    private final int hauteur;
    private final double pourcentageMurs;
    private final int distanceMin;
    private final TypeLabyrinthe typeLabyrinthe;

    public ParametresLabyrinthe(int largeur, int hauteur, double pourcentageMurs, int distanceMin, TypeLabyrinthe typeLabyrinthe) {
        this.largeur = largeur;
        this.hauteur = hauteur;
        this.pourcentageMurs = pourcentageMurs;
        this.distanceMin = distanceMin;
        this.typeLabyrinthe = typeLabyrinthe;
    }

    public int getLargeur() {
        return largeur;
    }

    public int getHauteur() {
        return hauteur;
    }

    public double getPourcentageMurs() {
        return pourcentageMurs;
    }

    public int getDistanceMin() {
        return distanceMin;
    }

    public TypeLabyrinthe getTypeLabyrinthe() {
        return typeLabyrinthe;
    }
}

