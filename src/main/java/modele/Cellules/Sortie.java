package modele.Cellules;

import javafx.scene.image.Image;

import java.util.Objects;

/**
 * Classe représentant une cellule de type sortie dans un labyrinthe.
 */
public class Sortie extends Cellule {

    public Sortie(int x, int y) {
        this.setX(x);
        this.setY(y);
    }

    @Override
    public boolean estSortie() {
        return true;
    }

    @Override
    public Image getTexture() {
        return new Image(Objects.requireNonNull(getClass().getResourceAsStream("/img/sortie.png")));
    }
}
