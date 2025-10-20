package modele;

public enum Defi {
    FACILE1(10, 10, 25.0),
    FACILE2(10, 10, 25.0),
    FACILE3(10, 10, 25.0),
    MOYEN1(15, 15, 50.0),
    MOYEN2(15, 15, 50.0),
    MOYEN3(15, 15, 50.0),
    DIFFICILE1(20, 20, 75.0),
    DIFFICILE2(20, 20, 75.0),
    DIFFICILE3(20, 20, 75.0);

    private final int largeur;
    private final int hauteur;
    private final double pourcentageMurs;

    private Defi(int largeur, int hauteur, double pourcentageMurs) {
        this.largeur = largeur;
        this.hauteur = hauteur;
        this.pourcentageMurs = pourcentageMurs;
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

    public double getTauxMurs(){
        return pourcentageMurs/100;
    }


}
