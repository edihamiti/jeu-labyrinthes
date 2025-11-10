package modele.items;

import modele.Joueur;

public abstract class Item {
    private String nom;
    private String imageFilePath;
    private int price;

    public Item(String nom, String imageFilePath, int price) {
        this.nom = nom;
        this.imageFilePath = imageFilePath;
        this.price = price;
    }

    public Item(String nom, String imageFilePath) {
        this(nom, imageFilePath, 0);
    }

    public Item(String nom) {
        this(nom, null, 0);
        try {
            this.imageFilePath = getClass().getResource("img/items/" + nom).toString();
        } catch (NullPointerException e) {
            this.imageFilePath = null;
        }
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getImageFilePath() {
        return imageFilePath;
    }

    public void setImageFilePath(String imageFilePath) {
        this.imageFilePath = imageFilePath;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public abstract String toString();

    public abstract void utiliser(Joueur joueur); // Je suis pas trop sûr de ça les gars
}
