package modele.Cellules;

import javafx.scene.image.Image;

/**
 * Classe abstraite représentant une cellule dans un labyrinthe.
 *
 * Chaque cellule a :
 * - une position (x, y)
 * - une texture associée (image)
 *
 * Cette classe définit également des méthodes pour connaître le type
 * de cellule (mur, chemin, entrée, sortie, clé ou piège).
 *
 * Les classes concrètes (Mur, Chemin, Entree, Sortie, Cle, Piege, etc.)
 * doivent hériter de cette classe et redéfinir la méthode {@link #getTexture()}
 * ainsi que les méthodes de type si nécessaire.
 */

public abstract class Cellule {

    private int x;
    private int y;
    private Image textureMur;


    /**
     * Retourne la texture (image) associée à la cellule.
     * Chaque type de cellule doit implémenter cette méthode.
     *
     * @return image de la cellule
     */
    public abstract Image getTexture();


    /**
     * Indique si la cellule est un piège.
     * Par défaut, retourne false.
     *
     * @return true si la cellule est un piège, false sinon
     */
    public boolean estPiege() {
        return false;
    }


    /**
     * Indique si la cellule est un chemin.
     * Par défaut, retourne false.
     *
     * @return true si la cellule est un chemin, false sinon
     */
    public boolean estChemin() {
        return false;
    }


    /**
     * Indique si la cellule est un mur.
     * Par défaut, retourne false.
     *
     * @return true si la cellule est un mur, false sinon
     */
    public boolean estMur() {
        return false;
    }


    /**
     * Indique si la cellule est l'entrée du labyrinthe.
     * Par défaut, retourne false.
     *
     * @return true si la cellule est une entrée, false sinon
     */
    public boolean estEntree() {
        return false;
    }


    /**
     * Indique si la cellule est la sortie du labyrinthe.
     * Par défaut, retourne false.
     *
     * @return true si la cellule est une sortie, false sinon
     */
    public boolean estSortie() {
        return false;
    }


    /**
     * Indique si la cellule contient une clé.
     * Par défaut, retourne false.
     *
     * @return true si la cellule contient une clé, false sinon
     */
    public boolean estCle(){ return false;}


    /**
     * Retourne la coordonnée X de la cellule.
     *
     * @return coordonnée X
     */
    public int getX() {
        return x;
    }


    /**
     * Définit la coordonnée X de la cellule.
     *
     * @param x nouvelle coordonnée X
     */
    public void setX(int x) {
        this.x = x;
    }


    /**
     * Retourne la coordonnée Y de la cellule.
     *
     * @return coordonnée Y
     */
    public int getY() {
        return y;
    }


    /**
     * Définit la coordonnée Y de la cellule.
     *
     * @param y nouvelle coordonnée Y
     */
    public void setY(int y) {
        this.y = y;
    }
}
