package modele;

/**
 * Enumération représentant les différents défis disponibles.
 * Chaque défi a une largeur, une hauteur, un pourcentage de murs et des points.
 */
public enum Defi {
    FACILE1(6, 6, 25.0, 5, 1, Vision.VUE_LIBRE, 1, TypeLabyrinthe.ALEATOIRE),
    MOYEN1(6, 6, 50.0, 15, 1, Vision.VUE_LIBRE, 1, TypeLabyrinthe.ALEATOIRE),
    DIFFICILE1(6, 6, 75.0, 45, 1, Vision.VUE_LIBRE, 1, TypeLabyrinthe.ALEATOIRE),

    FACILE2(15, 15, 25.0, 10, 2, Vision.VUE_LIBRE, 1, TypeLabyrinthe.ALEATOIRE),
    MOYEN2(15, 15, 50.0, 20, 2, Vision.VUE_LIBRE, 1, TypeLabyrinthe.ALEATOIRE),
    DIFFICILE2(15, 15, 75.0, 50, 2, Vision.VUE_LIBRE, 1, TypeLabyrinthe.ALEATOIRE),

    FACILE3(30, 30, 75.0, 15, 3, Vision.VUE_LOCAL, 1, TypeLabyrinthe.ALEATOIRE),
    MOYEN3(30, 30, 50.0, 25, 3, Vision.VUE_LOCAL, 1, TypeLabyrinthe.ALEATOIRE),
    DIFFICILE3(30, 30, 25.0, 55, 3, Vision.VUE_LOCAL, 1, TypeLabyrinthe.ALEATOIRE),

    FACILE4(7, 10, 100, 15, 4, Vision.VUE_LIBRE, 4, TypeLabyrinthe.PARFAIT),
    MOYEN4(10, 13, 100, 25, 4, Vision.VUE_LIBRE, 6, TypeLabyrinthe.PARFAIT),
    DIFFICILE4(13, 16, 100, 55, 4, Vision.VUE_LIBRE, 9, TypeLabyrinthe.PARFAIT),

    FACILE5(17, 20, 100, 15, 5, Vision.VUE_LIMITEE, 4, TypeLabyrinthe.PARFAIT),
    MOYEN5(20, 23, 100, 25, 5, Vision.VUE_LIMITEE, 6, TypeLabyrinthe.PARFAIT),
    DIFFICILE5(23, 26, 100, 55, 5, Vision.VUE_LIMITEE, 9, TypeLabyrinthe.PARFAIT);

    private final int largeur;
    private final int hauteur;
    private final double pourcentageMurs;
    private final int points;
    private final Vision vision;
    private final int distanceMin;
    private TypeLabyrinthe typeLabyrinthe;
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
     * @param vision          mode de vision lors du jeu
     * @param distanceMin     distance minimale entre le joueur et la sortie du labyrinthe
     * @param typeLabyrinthe  type de labyrinthe
     */
    private Defi(int largeur, int hauteur, double pourcentageMurs, int points, int etape, Vision vision, int distanceMin, TypeLabyrinthe typeLabyrinthe) {
        this.largeur = largeur;
        this.hauteur = hauteur;
        this.pourcentageMurs = pourcentageMurs;
        this.points = points;
        this.vision = vision;
        this.etape = etape;
        this.distanceMin = distanceMin;
        this.typeLabyrinthe = typeLabyrinthe;
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

    public Vision getVision() {
        return vision;
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

    public int getDistanceMin() {
        return distanceMin;
    }

    public TypeLabyrinthe getTypeLabyrinthe() {
        return typeLabyrinthe;
    }

}
