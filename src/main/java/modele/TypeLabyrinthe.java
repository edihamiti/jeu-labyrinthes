package modele;

public enum TypeLabyrinthe {
    PARFAIT("Parfait"),
    ALEATOIRE("Aléatoire");

    private String nom;

    TypeLabyrinthe(String nom) {
        this.nom = nom;
    }

    public String getNom() {
        return nom;
    }
}
