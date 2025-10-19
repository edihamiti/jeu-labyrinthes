package modele.Cellules;

public class Sortie extends Cellule {

    public Sortie(int x, int y) {
        this.setX(x);
        this.setY(y);
    }

    @Override
    public boolean estSortie() {
        return true;
    }
}
