package modele;

import java.util.ArrayList;

public class Jeu {
    private ModeJeu modeJeu;
    private Joueur joueur;
    private Labyrinthe labyrinthe;

    public Jeu(ModeJeu modeJeu, Joueur joueur, Labyrinthe labyrinthe) {
        this.modeJeu = modeJeu;
        this.joueur = joueur;
        this.labyrinthe = labyrinthe;
    }

    public ModeJeu getModeJeu() {return modeJeu;}
    public void setModeJeu(ModeJeu modeJeu) {this.modeJeu = modeJeu;}
    public Joueur getJoueur() {return joueur;}
    public void setJoueur(Joueur joueur) {this.joueur = joueur;}
    public Labyrinthe getLabyrinthe() {return labyrinthe;}
    public void setLabyrinthe(Labyrinthe labyrinthe) {this.labyrinthe = labyrinthe;}
}
