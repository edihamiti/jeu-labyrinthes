package modele.Cellules;

import javafx.scene.image.Image;

/**
 * Classe représentant une cellule de type chemin dans un labyrinthe.
 *
 * Un chemin est une cellule traversable par le joueur. Cette classe hérite
 * de {@link Cellule} et redéfinit la méthode {@link #estChemin()} pour indiquer
 * qu'il s'agit d'un chemin.
 *
 * Elle gère également une image de texture partagée pour toutes les instances.
 */
public class Chemin extends Cellule {
    static Image imageCache;
    String imagePath = "";


    /**
     * Constructeur principal avec coordonnées.
     *
     * @param x coordonnée X de la cellule
     * @param y coordonnée Y de la cellule
     */
    public Chemin(int x, int y) {
        this.setX(x);
        this.setY(y);
    }


    /**
     * Constructeur par défaut.
     * Charge l'image à partir du chemin défini dans {@link #imagePath}.
     */
    public Chemin() {
        imageCache = new Image(getClass().getResourceAsStream(imagePath));
    }


    /**
     * Retourne la texture (image) associée à cette cellule.
     *
     * @return image de la cellule
     */
    public Image getTexture() {
        return imageCache;
    }


    /**
     * Définit le chemin vers l'image à utiliser pour la texture.
     * Recharge l'image pour toutes les instances de Chemin.
     *
     * @param imagePath chemin relatif vers le fichier image
     */
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
        imageCache = new Image(getClass().getResourceAsStream(imagePath));
    }

    /**
     * Indique que cette cellule est bien un chemin.
     *
     * @return toujours true
     */
    public boolean estChemin() {
        return true;
    }

}
