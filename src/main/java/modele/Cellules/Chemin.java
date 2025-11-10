package modele.Cellules;

import javafx.scene.image.Image;

import java.util.Objects;

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

    @Override
    public Image getTexture() {
        return new Image(Objects.requireNonNull(getClass().getResourceAsStream("/img/chemin.png")));
    }

}
