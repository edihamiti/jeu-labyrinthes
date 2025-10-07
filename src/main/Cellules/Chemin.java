package main.Cellules;

public class Chemin extends Cellule {

    public Chemin(int x, int y) {
        this.setX(x);
        this.setY(y);
    }

    @Override
    public boolean estChemin() {
        return true;
    }

}
