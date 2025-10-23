package modele.Cellules;

/**
 * Classe représentant une cellule de type chemin dans un labyrinthe.
 */
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
