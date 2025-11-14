package modele.Cellules;

import javafx.scene.image.Image;

/**
 * Classe représentant une cellule de type piege dans un labyrinthe.
 */
public class Piege extends Cellule {
    static Image imageCache;
    String imagePath = "/img/piege.png";

    public Piege(int x, int y) {
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
    public boolean estPiege() {
        return true;
    }

}
