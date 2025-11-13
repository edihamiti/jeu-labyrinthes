package modele.Cellules;

import javafx.scene.image.Image;

import java.util.Objects;

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

    @Override
    public Image getTexture() {
        return new Image(Objects.requireNonNull(getClass().getResourceAsStream("/img/chemin.png")));
    }
}
