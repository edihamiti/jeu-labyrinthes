package modele.items;

import modele.Joueur;


/**
 * Classe abstraite représentant un objet (item) pouvant être possédé et utilisé par un joueur.
 * Un item possède un nom, un chemin vers son image et un prix.
 */
public abstract class Item {
    private String nom;
    private String imageFilePath;
    private int price;


    /**
     * Construit un item avec un nom, un chemin d'image et un prix.
     *
     * @param nom           le nom de l'item
     * @param imageFilePath le chemin vers l'image représentant l'item
     * @param price         le prix de l'item
     */
    public Item(String nom, String imageFilePath, int price) {
        this.nom = nom;
        this.imageFilePath = imageFilePath;
        this.price = price;
    }


    /**
     * Construit un item avec un nom et un chemin d'image, le prix par défaut est 0.
     *
     * @param nom           le nom de l'item
     * @param imageFilePath le chemin vers l'image représentant l'item
     */
    public Item(String nom, String imageFilePath) {
        this(nom, imageFilePath, 0);
    }


    /**
     * Construit un item avec seulement un nom.
     * Le chemin d'image est tenté automatiquement depuis "img/items/{nom}".
     * Si l'image n'existe pas, le chemin est null.
     *
     * @param nom le nom de l'item
     */
    public Item(String nom) {
        this(nom, null, 0);
        try {
            this.imageFilePath = getClass().getResource("img/items/" + nom).toString();
        } catch (NullPointerException e) {
            this.imageFilePath = null;
        }
    }


    /**
     * Retourne le nom de l'item.
     *
     * @return le nom de l'item
     */
    public String getNom() {
        return nom;
    }


    /**
     * Modifie le nom de l'item.
     *
     * @param nom le nouveau nom
     */
    public void setNom(String nom) {
        this.nom = nom;
    }


    /**
     * Retourne le chemin de l'image associée à l'item.
     *
     * @return le chemin de l'image ou null si non défini
     */
    public String getImageFilePath() {
        return imageFilePath;
    }


    /**
     * Définit le chemin de l'image associée à l'item.
     *
     * @param imageFilePath le chemin de l'image
     */
    public void setImageFilePath(String imageFilePath) {
        this.imageFilePath = imageFilePath;
    }


    /**
     * Retourne le prix de l'item.
     *
     * @return le prix de l'item
     */
    public int getPrice() {
        return price;
    }


    /**
     * Définit le prix de l'item.
     *
     * @param price le prix à définir
     */
    public void setPrice(int price) {
        this.price = price;
    }


    /**
     * Retourne une représentation textuelle de l'item.
     *
     * @return une chaîne décrivant l'item
     */
    public abstract String toString();


    /**
     * Action à exécuter lorsque l'item est utilisé par un joueur.
     *
     * @param joueur le joueur qui utilise l'item
     */
    public abstract void utiliser(Joueur joueur); // Je suis pas trop sûr de ça les gars
}
