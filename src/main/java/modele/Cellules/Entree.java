package modele.Cellules;

import javafx.scene.image.Image;

/**
 * Classe représentant une cellule de type entrée dans un labyrinthe.
 *
 * L'entrée est la position de départ du joueur dans le labyrinthe.
 * Cette classe hérite de {@link Cellule} et redéfinit {@link #estEntree()}.
 *
 * La texture est partagée entre toutes les instances de Entree.
 */
public class Entree extends Cellule {
    static Image imageCache;
    String imagePath = "/img/chemin.png";


    /**
     * Constructeur avec coordonnées.
     *
     * @param x coordonnée X de l'entrée
     * @param y coordonnée Y de l'entrée
     */
    public Entree(int x, int y) {
        this.setX(x);
        this.setY(y);
    }


    /**
     * Retourne la texture (image) associée à cette cellule.
     * Si l'image n'a pas encore été chargée, elle est chargée depuis le chemin défini.
     *
     * @return image de la cellule d'entrée
     */
    public Image getTexture() {
        if (imageCache == null) {
            imageCache = new Image(getClass().getResourceAsStream(imagePath));
        }
        return imageCache;
    }


    /**
     * Indique que cette cellule est une entrée.
     *
     * @return toujours true
     */
    public boolean estEntree() {
        return true;
    }
}
