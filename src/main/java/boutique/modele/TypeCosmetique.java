package boutique.modele;

/**
 * Énumération des différents types de cosmétiques disponibles dans la boutique.
 * Chaque type correspond à un élément visuel personnalisable du jeu.
 */
public enum TypeCosmetique {

    /** Texture pour les murs du labyrinthe */
    TEXTURE_MUR("Mur"),

    /** Texture pour le personnage du joueur */
    TEXTURE_JOUEUR("Joueur"),

    /** Texture pour les chemins praticables */
    TEXTURE_CHEMIN("Chemin"),

    /** Texture pour la sortie du labyrinthe */
    TEXTURE_SORTIE("Sortie");

    private final String nomAffichage;

    /**
     * Construit un type de cosmétique avec son nom d'affichage.
     *
     * @param nomAffichage le nom à afficher dans l'interface
     */
    TypeCosmetique(String nomAffichage) {
        this.nomAffichage = nomAffichage;
    }

    /**
     * @return le nom d'affichage du type de cosmétique
     */
    public String getNomAffichage() {
        return nomAffichage;
    }

}
