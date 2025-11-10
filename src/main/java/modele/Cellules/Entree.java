package modele.Cellules;

import javafx.scene.image.Image;

import java.util.Objects;

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

    @Override
    public Image getTexture() {
        return new Image(Objects.requireNonNull(getClass().getResourceAsStream("/img/chemin.png")));
    }
}
