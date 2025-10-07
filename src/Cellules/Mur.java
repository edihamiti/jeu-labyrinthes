package Cellules;

public class Mur extends Cellule {

    public Mur(int x, int y) {
        this.setX(x);
        this.setY(y);
    }

    @Override
    public boolean estMur() {
        return true;
    }
}
