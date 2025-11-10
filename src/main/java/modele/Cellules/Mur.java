package modele.Cellules;

import javafx.scene.image.Image;

import java.util.Objects;

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

    @Override
    public Image getTexture() {
        return new Image(Objects.requireNonNull(getClass().getResourceAsStream("/img/mur.png")));
    }
}
