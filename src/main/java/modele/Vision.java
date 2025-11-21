package modele;

/**
 * Enumération des différents types de vision disponibles pour un joueur dans le labyrinthe.
 * Chaque type de vision détermine la quantité d'information visible par le joueur.
 */
public enum Vision {
    VUE_LIBRE,
    VUE_LOCALE,
    VUE_CARTE,
    VUE_LIMITEE,
    VUE_AVEUGLE,
    VUE_CLE,
}
