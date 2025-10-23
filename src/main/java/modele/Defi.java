package modele;

/**
 * Enumération représentant les différents défis disponibles.
 * Chaque défi a une largeur, une hauteur, un pourcentage de murs et des points.
 */
public enum Defi {
    FACILE1(10, 10, 25.0, 5),
    FACILE2(10, 10, 25.0, 10),
    FACILE3(10, 10, 25.0, 15),
    MOYEN1(15, 15, 50.0, 15),
    MOYEN2(15, 15, 50.0, 20),
    MOYEN3(15, 15, 50.0, 25),
    DIFFICILE1(20, 20, 75.0, 45),
    DIFFICILE2(20, 20, 75.0, 50),
    DIFFICILE3(20, 20, 75.0, 55);

    private final int largeur;
    private final int hauteur;
    private final double pourcentageMurs;
    private final int points;

    /**
     * Constructeur pour un défi.
     *
     * @param largeur         largeur en cases
     * @param hauteur         hauteur en cases
     * @param pourcentageMurs pourcentage de murs (0-100)
     * @param points          points attribués
     */
    private Defi(int largeur, int hauteur, double pourcentageMurs, int points) {
        this.largeur = largeur;
        this.hauteur = hauteur;
        this.pourcentageMurs = pourcentageMurs;
        this.points = points;
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

}
