package modele.Cellules;

import javafx.scene.image.Image;

/**
 * Classe représentant une cellule de type chemin dans un labyrinthe.
 */
public class Chemin extends Cellule {
    static Image imageCache;
    String imagePath = "";

    public Chemin(int x, int y) {
        this.setX(x);
        this.setY(y);
    }

    public Chemin() {
        imageCache = new Image(getClass().getResourceAsStream(imagePath));
    }

    public Image getTexture() {
        return imageCache;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
        imageCache = new Image(getClass().getResourceAsStream(imagePath));
    }

    @Override
    public boolean estChemin() {
        return true;
    }

}
