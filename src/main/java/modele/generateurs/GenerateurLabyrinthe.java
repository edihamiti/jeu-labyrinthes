package modele.generateurs;

import modele.Labyrinthe;


/**
 * Classe abstraite représentant un générateur de labyrinthe.
 *
 * Toutes les classes de générateurs de labyrinthes doivent hériter de cette classe
 * et implémenter la méthode `generer` qui construit le labyrinthe dans un objet
 * {@link Labyrinthe}.
 *
 * La classe gère :
 * - la largeur et la hauteur souhaitées du labyrinthe
 * - la largeur et la hauteur réelles (largeur + 2, hauteur + 2) pour inclure
 *   les murs périphériques
 */
public abstract class GenerateurLabyrinthe {
    int largeur;
    int hauteur;
    int largeurMax;
    int hauteurMax;


    /**
     * Constructeur du générateur.
     *
     * @param largeur largeur souhaitée du labyrinthe
     * @param hauteur hauteur souhaitée du labyrinthe
     */
    public GenerateurLabyrinthe(int largeur, int hauteur) {
        this.largeur = largeur;
        this.hauteur = hauteur;
        this.largeurMax = largeur + 2;
        this.hauteurMax = hauteur + 2;
    }


    /**
     * Méthode abstraite devant être implémentée par chaque générateur concret.
     * Cette méthode doit construire le labyrinthe et le stocker dans l'objet
     * {@link Labyrinthe} fourni.
     *
     * @param lab labyrinthe à remplir avec les cellules générées
     */
    public abstract void generer(Labyrinthe lab);
}
