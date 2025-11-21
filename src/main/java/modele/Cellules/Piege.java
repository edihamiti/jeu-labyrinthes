package modele.Cellules;

import javafx.scene.image.Image;


/**
 * Classe représentant une cellule de type piège dans un labyrinthe.
 *
 * Un piège est une cellule qui peut infliger un effet négatif au joueur
 * lorsqu'il la traverse. Cette classe hérite de {@link Cellule} et
 * redéfinit {@link #estPiege()}.
 *
 * La texture est partagée entre toutes les instances de Piege.
 */
public class Piege extends Cellule {
    static Image imageCache;
    String imagePath = "/img/piege.png";


    /**
     * Constructeur avec coordonnées.
     *
     * @param x coordonnée X de la cellule
     * @param y coordonnée Y de la cellule
     */
    public Piege(int x, int y) {
        this.setX(x);
        this.setY(y);
    }


    /**
     * Retourne la texture (image) associée à cette cellule.
     * Si l'image n'a pas encore été chargée, elle est chargée depuis le chemin défini.
     *
     * @return image du piège
     */
    public Image getTexture() {
        if (imageCache == null) {
            imageCache = new Image(getClass().getResourceAsStream(imagePath));
        }
        return imageCache;
    }


    /**
     * Indique que cette cellule est un piège.
     *
     * @return toujours true
     */
    public boolean estPiege() {
        return true;
    }

}
