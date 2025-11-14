package modele.Cellules;

import javafx.scene.image.Image;

/**
 * Classe représentant une cellule de type entrée dans un labyrinthe.
 */
public class Entree extends Cellule {
    static Image imageCache;
    String imagePath = "/img/chemin.png";

    public Entree(int x, int y) {
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
    public boolean estEntree() {
        return true;
    }
}
