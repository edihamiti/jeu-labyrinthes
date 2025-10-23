package modele.Cellules;

/**
 * Classe représentant une cellule de type entrée dans un labyrinthe.
 */
public class Entree extends Cellule {

    public Entree(int x, int y) {
        this.setX(x);
        this.setY(y);
    }

    @Override
    public boolean estEntree() {
        return true;
    }
}
