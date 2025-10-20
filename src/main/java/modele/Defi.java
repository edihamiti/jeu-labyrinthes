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

    private final int x;
    private final int y;
    private final double pourcentageMurs;

    private Defi(int x, int y, double pourcentageMurs) {
        this.x = x;
        this.y = y;
        this.pourcentageMurs = pourcentageMurs;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public double getPourcentageMurs() {
        return pourcentageMurs;
    }

    public double getTauxMurs(){
        return pourcentageMurs/100;
    }


}
