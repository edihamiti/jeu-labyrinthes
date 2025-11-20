package modele.Cellules;

import javafx.scene.image.Image;

/**
 * Classe représentant une cellule de type mur dans un labyrinthe.
 */
public class Mur extends Cellule {
    static Image imageCache;
    static Image imageCacheBlocked;
    String imagePath = "";

    public Mur() {
        imageCache = new Image(getClass().getResourceAsStream(imagePath));
        imageCacheBlocked = new Image(getClass().getResourceAsStream(imagePath));
    }

    public Mur(int x, int y) {
        this.setX(x);
        this.setY(y);
    }

    public Image getTexture() {
        return imageCache;
    }

    public Image getTextureBlocked() {
        return imageCacheBlocked;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
        imageCache = new Image(getClass().getResourceAsStream(imagePath));
        String[] split = imagePath.split("\\.");
        imageCacheBlocked = new Image(getClass().getResourceAsStream(split[0] + "_blocked.png"));
    }

    @Override
    public boolean estMur() {
        return true;
    }
}
