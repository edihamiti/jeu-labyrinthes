package modele.Cellules;

import javafx.scene.image.Image;

/**
 * Classe représentant une cellule de type sortie dans un labyrinthe.
 */
public class Sortie extends Cellule {
    static Image imageCache;
    String imagePath = "";

    public Sortie(int x, int y) {
        this.setX(x);
        this.setY(y);
    }

    public Image getTexture() {
        return imageCache;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
        imageCache = new Image(getClass().getResourceAsStream(imagePath));
    }

    @Override
    public boolean estSortie() {
        return true;
    }
}
