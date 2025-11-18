package modele.Cellules;

import javafx.scene.image.Image;

/**
 * Classe représentant une cellule de type mur dans un labyrinthe.
 */
public class Mur extends Cellule {
    static Image imageCache;
    String imagePath = "/img/mur.png";

    public Mur(int x, int y) {
        this.setX(x);
        this.setY(y);
    }

    public Image getTexture() {
        if (imageCache == null) {
            imageCache = new Image(getClass().getResourceAsStream(imagePath));
        }
        return imageCache;
    }

    @Override
    public boolean estMur() {
        return true;
    }
}
