package modele.Cellules;

import javafx.scene.image.Image;

/**
 * Classe représentant une cellule de type sortie dans un labyrinthe.
 */
public class Sortie extends Cellule {
    static Image imageCache;
    String imagePath = "/img/sortie.png";

    public Sortie(int x, int y) {
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
    public boolean estSortie() {
        return true;
    }
}
