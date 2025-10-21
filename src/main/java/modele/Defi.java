package modele;

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

    public double getPourcentageMurs() {
        return pourcentageMurs;
    }

    public double getTauxMurs() {
        return pourcentageMurs / 100;
    }


}
