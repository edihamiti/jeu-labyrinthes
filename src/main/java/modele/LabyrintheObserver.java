package modele;

/**
 * Interface pour un observateur de labyrinthe.
 * Permet de notifier les changements dans le labyrinthe.
 */
public interface LabyrintheObserver {

    /**
     * Méthode appelée lorsqu'une mise à jour du labyrinthe est nécessaire.
     */
    void update();
}
