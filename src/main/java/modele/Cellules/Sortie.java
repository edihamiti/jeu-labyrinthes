package modele.Cellules;

import javafx.scene.image.Image;


/**
 * Classe représentant une cellule de type sortie dans un labyrinthe.
 *
 * La sortie est la cellule que le joueur doit atteindre pour terminer le labyrinthe.
 * Elle peut être verrouillée ou déverrouillée. Cette classe hérite de {@link Cellule}
 * et redéfinit {@link #estSortie()}.
 *
 * La texture est partagée entre toutes les instances de Sortie.
 */
public class Sortie extends Cellule {
    static Image imageCache;
    String imagePath = "";
    private boolean verrouillee = false;


    /**
     * Constructeur par défaut.
     * La sortie n'a pas de coordonnées initiales et est déverrouillée.
     */
    public Sortie() {
    }


    /**
     * Constructeur avec coordonnées.
     * La sortie est déverrouillée par défaut.
     *
     * @param x coordonnée X de la cellule
     * @param y coordonnée Y de la cellule
     */
    public Sortie(int x, int y) {
        this.setX(x);
        this.setY(y);
    }


    /**
     * Constructeur avec coordonnées et état verrouillé.
     *
     * @param x coordonnée X de la cellule
     * @param y coordonnée Y de la cellule
     * @param verrouillee true si la sortie doit être verrouillée
     */
    public Sortie(int x, int y, boolean verrouillee) {
        this.setX(x);
        this.setY(y);
        this.verrouillee =  verrouillee;
    }


    /**
     * Indique si la sortie est verrouillée.
     *
     * @return true si verrouillée, false sinon
     */
    public boolean estVerrouillee(){
        return verrouillee;
    }


    /**
     * Déverrouille la sortie.
     */
    public void deverrouillee(){
        this.verrouillee = false;
    }


    /**
     * Retourne la texture (image) associée à cette cellule.
     *
     * @return image de la sortie
     */
    public Image getTexture() {
        return imageCache;
    }


    /**
     * Définit le chemin vers l'image de la sortie et recharge la texture.
     *
     * @param imagePath chemin relatif vers le fichier image
     */
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
        imageCache = new Image(getClass().getResourceAsStream(imagePath));
    }


    /**
     * Indique que cette cellule est une sortie.
     *
     * @return toujours true
     */
    public boolean estSortie() {
        return true;
    }
}
