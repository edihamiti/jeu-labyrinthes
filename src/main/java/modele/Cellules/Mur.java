package modele.Cellules;

/**
 * Classe représentant une cellule de type mur dans un labyrinthe.
 */
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
