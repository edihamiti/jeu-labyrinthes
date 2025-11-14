package modele.Cellules;

/**
 * Classe représentant une cellule de type piege dans un labyrinthe.
 */
public class Piege extends Cellule {

    public Piege(int x, int y) {
        this.setX(x);
        this.setY(y);
    }

    @Override
    public boolean estPiege() {
        return true;
    }

}
