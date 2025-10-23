package modele;

/**
 * Enumération représentant les différents défis disponibles.
 * Chaque défi a une largeur, une hauteur, un pourcentage de murs et des points.
 */
public enum Defi {
    FACILE1(6, 6, 25.0, 5, 1),
    MOYEN1(6, 6, 50.0, 15, 1),
    DIFFICILE1(6, 6, 75.0, 45, 1),

    FACILE2(12, 12, 25.0, 10, 2),
    MOYEN2(12, 12, 50.0, 20, 2),
    DIFFICILE2(12, 12, 75.0, 50, 2),

    FACILE3(18, 18, 25.0, 15, 3),
    MOYEN3(18, 18, 50.0, 25, 3),
    DIFFICILE3(18, 18, 75.0, 55, 3);

    private final int largeur;
    private final int hauteur;
    private final double pourcentageMurs;
    private final int points;
    /*
    L'étape associée au défi (Pour rappel : chaque étape a 3 défis).
     */
    private final int etape;

    /**
     * Constructeur pour un défi.
     *
     * @param largeur         largeur en cases
     * @param hauteur         hauteur en cases
     * @param pourcentageMurs pourcentage de murs (0-100)
     * @param points          points attribués
     */
    private Defi(int largeur, int hauteur, double pourcentageMurs, int points, int etape) {
        this.largeur = largeur;
        this.hauteur = hauteur;
        this.pourcentageMurs = pourcentageMurs;
        this.points = points;
        this.etape = etape;
    }

    public int getPoints() {
        return points;
    }

    public int getLargeur() {
        return largeur;
    }

    public int getHauteur() {
        return hauteur;
    }

    /**
     * Retourne le pourcentage de murs pour ce défi.
     *
     * @return pourcentage de murs (0-100)
     */
    public double getPourcentageMurs() {
        return pourcentageMurs;
    }

    /* Utilisation ???
    public double getTauxMurs() {
        return pourcentageMurs / 100;
    }
    */

    public int getEtape() {
        return etape;
    }

}
