package modele;

import vue.LabyrinthesObservateur;

import java.util.ArrayList;

public class Jeu {
    private ModeJeu modeJeu;
    private Joueur joueur;
    private Labyrinthe labyrinthe;
    private ArrayList<LabyrinthesObservateur> observateur;

    public Jeu(ModeJeu modeJeu, Joueur joueur, Labyrinthe labyrinthe) {
        this.modeJeu = modeJeu;
        this.joueur = joueur;
        this.labyrinthe = labyrinthe;
    }
}
