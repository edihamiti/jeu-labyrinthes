package modele.Cellules;

import javafx.scene.image.Image;


/**
 * Classe représentant une cellule de type mur dans un labyrinthe.
 *
 * Un mur est une cellule non traversable par le joueur. Cette classe
 * hérite de {@link Cellule} et redéfinit {@link #estMur()}.
 *
 * Elle gère deux textures :
 * - imageCache : l'image normale du mur
 * - imageCacheBlocked : l'image du mur bloqué (optionnelle)
 *
 * Les textures sont partagées entre toutes les instances de Mur.
 */
public class Mur extends Cellule {
    static Image imageCache;
    static Image imageCacheBlocked;
    String imagePath = "";


    /**
     * Constructeur par défaut.
     * Initialise les textures du mur et du mur bloqué à partir de imagePath.
     */
    public Mur() {
        imageCache = new Image(getClass().getResourceAsStream(imagePath));
        imageCacheBlocked = new Image(getClass().getResourceAsStream(imagePath));
    }


    /**
     * Constructeur avec coordonnées.
     *
     * @param x coordonnée X de la cellule
     * @param y coordonnée Y de la cellule
     */
    public Mur(int x, int y) {
        this.setX(x);
        this.setY(y);
    }


    /**
     * Retourne la texture normale du mur.
     *
     * @return image du mur
     */
    public Image getTexture() {
        return imageCache;
    }


    /**
     * Retourne la texture du mur bloqué.
     *
     * @return image du mur bloqué
     */
    public Image getTextureBlocked() {
        return imageCacheBlocked;
    }


    /**
     * Définit le chemin vers l'image du mur et recharge les textures.
     * La texture du mur bloqué est automatiquement déduite en ajoutant "_blocked"
     * avant l'extension du fichier.
     *
     * @param imagePath chemin relatif vers le fichier image
     */
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
        imageCache = new Image(getClass().getResourceAsStream(imagePath));
        String[] split = imagePath.split("\\.");
        imageCacheBlocked = new Image(getClass().getResourceAsStream(split[0] + "_blocked.png"));
    }

    /**
     * Indique que cette cellule est un mur.
     *
     * @return toujours true
     */
    public boolean estMur() {
        return true;
    }
}
