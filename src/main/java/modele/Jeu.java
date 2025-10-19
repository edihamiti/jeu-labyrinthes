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

    public ModeJeu getModeJeu() {return modeJeu;}
    public void setModeJeu(ModeJeu modeJeu) {this.modeJeu = modeJeu;}
    public Joueur getJoueur() {return joueur;}
    public void setJoueur(Joueur joueur) {this.joueur = joueur;}
    public Labyrinthe getLabyrinthe() {return labyrinthe;}
    public void setLabyrinthe(Labyrinthe labyrinthe) {this.labyrinthe = labyrinthe;}
    public void addObservateur(LabyrinthesObservateur o){observateur.add(o);}
    public void removeObservateur(LabyrinthesObservateur o){observateur.remove(o);}

}
