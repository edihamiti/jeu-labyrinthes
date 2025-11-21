package modele.Cellules;

import javafx.scene.image.Image;

/**
 * Classe abstraite représentant une cellule dans un labyrinthe.
 */
public abstract class Cellule {

    private int x;
    private int y;
    private Image textureMur;
    
    public abstract Image getTexture();

    public boolean estPiege() {
        return false;
    }

    public boolean estChemin() {
        return false;
    }

    public boolean estMur() {
        return false;
    }

    public boolean estEntree() {
        return false;
    }

    public boolean estSortie() {
        return false;
    }

    public boolean estCle(){ return false;}

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
