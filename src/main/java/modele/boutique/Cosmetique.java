package modele.boutique;

/**
 * Représente un cosmétique disponible dans la boutique.
 * Un cosmétique peut être acheté par un joueur et équipé pour personnaliser son interface de jeu.
 */
public record Cosmetique(String id, String nom, TypeCosmetique type, int prix, String cheminTexture) {

    /**
     * Construit un nouveau cosmétique.
     *
     * @param id            l'identifiant unique du cosmétique
     * @param nom           le nom d'affichage du cosmétique
     * @param type          le type de cosmétique (mur, joueur, chemin, sortie)
     * @param prix          le prix en points du cosmétique
     * @param cheminTexture le chemin vers le fichier de texture
     */
    public Cosmetique {
    }

    /**
     * @return l'identifiant unique du cosmétique
     */
    @Override
    public String id() {
        return id;
    }

    /**
     * @return le nom d'affichage du cosmétique
     */
    @Override
    public String nom() {
        return nom;
    }

    /**
     * @return le type de cosmétique
     */
    @Override
    public TypeCosmetique type() {
        return type;
    }

    /**
     * @return le prix en points du cosmétique
     */
    @Override
    public int prix() {
        return prix;
    }

    /**
     * @return le chemin vers le fichier de texture
     */
    @Override
    public String cheminTexture() {
        return cheminTexture;
    }

    /**
     * @return nom et prix du cosmétique sous la forme "Nom (XX pts)"
     */
    @Override
    public String toString() {
        return nom + " (" + prix + " pts)";
    }

}
