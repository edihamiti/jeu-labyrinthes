package modele.Cellules;

import javafx.scene.image.Image;

public class Cle extends Cellule {
    static Image imageCache;
    String imagePath = "/img/cle.png";

    public Cle(int x, int y) {
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
    public boolean estCle() {
        return true;
    }
}
