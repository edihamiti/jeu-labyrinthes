package modele.Cellules;

import javafx.scene.image.Image;


/**
 * Classe représentant une cellule contenant une clé dans un labyrinthe.
 *
 * La clé est un objet que le joueur doit récupérer pour pouvoir accéder
 * à certaines fonctionnalités du labyrinthe, par exemple débloquer la sortie.
 * Cette classe hérite de {@link Cellule} et redéfinit {@link #estCle()}.
 *
 * La texture est partagée entre toutes les instances de Cle.
 */
public class Cle extends Cellule {
    static Image imageCache;
    String imagePath = "/img/cle.png";


    /**
     * Constructeur avec coordonnées.
     *
     * @param x coordonnée X de la cellule
     * @param y coordonnée Y de la cellule
     */
    public Cle(int x, int y) {
        this.setX(x);
        this.setY(y);
    }


    /**
     * Retourne la texture (image) associée à cette cellule.
     * Si l'image n'a pas encore été chargée, elle est chargée depuis le chemin défini.
     *
     * @return image de la clé
     */
    public Image getTexture() {
        if (imageCache == null) {
            imageCache = new Image(getClass().getResourceAsStream(imagePath));
        }
        return imageCache;
    }


    /**
     * Indique que cette cellule contient une clé.
     *
     * @return toujours true
     */
    public boolean estCle() {
        return true;
    }
}
