package modele;

/**
 * Enumération représentant les différents défis disponibles.
 * Chaque défi a une largeur, une hauteur, un pourcentage de murs et des points.
 */
public enum Defi {
    FACILE1(6, 6, 25.0, 5, 1, Vision.VUE_LIBRE, 1, TypeLabyrinthe.ALEATOIRE, 0),
    MOYEN1(6, 6, 50.0, 15, 1, Vision.VUE_LIBRE, 1, TypeLabyrinthe.ALEATOIRE, 0),
    DIFFICILE1(6, 6, 75.0, 45, 1, Vision.VUE_LIBRE, 1, TypeLabyrinthe.ALEATOIRE, 0),

    FACILE2(15, 15, 25.0, 10, 2, Vision.VUE_LIBRE, 1, TypeLabyrinthe.ALEATOIRE, 0),
    MOYEN2(15, 15, 50.0, 20, 2, Vision.VUE_LIBRE, 1, TypeLabyrinthe.ALEATOIRE, 0),
    DIFFICILE2(15, 15, 75.0, 50, 2, Vision.VUE_LIBRE, 1, TypeLabyrinthe.ALEATOIRE, 0),

    FACILE3(30, 30, 75.0, 15, 3, Vision.VUE_LOCAL, 1, TypeLabyrinthe.ALEATOIRE, 0),
    MOYEN3(30, 30, 50.0, 25, 3, Vision.VUE_LOCAL, 1, TypeLabyrinthe.ALEATOIRE, 0),
    DIFFICILE3(30, 30, 25.0, 55, 3, Vision.VUE_LOCAL, 1, TypeLabyrinthe.ALEATOIRE, 0),

    FACILE4(7, 9, 100, 15, 4, Vision.VUE_LIBRE, 25, TypeLabyrinthe.PARFAIT, 0),
    MOYEN4(11, 13, 100, 25, 4, Vision.VUE_LIBRE, 55, TypeLabyrinthe.PARFAIT, 0),
    DIFFICILE4(13, 15, 100, 55, 4, Vision.VUE_LIBRE, 94, TypeLabyrinthe.PARFAIT, 0),

    FACILE5(17, 21, 100, 15, 5, Vision.VUE_LIMITEE, 160, TypeLabyrinthe.PARFAIT, 4),
    MOYEN5(21, 23, 100, 25, 5, Vision.VUE_LIMITEE, 220, TypeLabyrinthe.PARFAIT, 2),
    DIFFICILE5(23, 27, 100, 55, 5, Vision.VUE_LIMITEE, 288, TypeLabyrinthe.PARFAIT, 1),

    FACILE6(17, 19, 100, 15, 6, Vision.VUE_CARTE, 160, TypeLabyrinthe.PARFAIT, 4),
    MOYEN6(21, 23, 100, 25, 6, Vision.VUE_CARTE, 220, TypeLabyrinthe.PARFAIT, 2),
    DIFFICILE6(23, 27, 100, 55, 6, Vision.VUE_CARTE, 288, TypeLabyrinthe.PARFAIT, 1);

    private final int largeur;
    private final int hauteur;
    private final double pourcentageMurs;
    private final int points;
    private final Vision vision;
    private final int distanceMin;
    private TypeLabyrinthe typeLabyrinthe;
    private final int portee;
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
     * @param etape           étape du défi (1-6)
     * @param vision          mode de vision lors du jeu
     * @param distanceMin     distance minimale entre le joueur et la sortie du labyrinthe
     * @param typeLabyrinthe  type de labyrinthe
     * @param portee          portée de vision pour les modes VUE_LIMITEE et VUE_CARTE
     */
    private Defi(int largeur, int hauteur, double pourcentageMurs, int points, int etape, Vision vision, int distanceMin, TypeLabyrinthe typeLabyrinthe, int portee) {
        this.largeur = largeur;
        this.hauteur = hauteur;
        this.pourcentageMurs = pourcentageMurs;
        this.points = points;
        this.vision = vision;
        this.etape = etape;
        this.distanceMin = distanceMin;
        this.typeLabyrinthe = typeLabyrinthe;
        this.portee = portee;
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

    public int getPortee() {
        return portee;
    }
}
