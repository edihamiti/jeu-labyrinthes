package controleur;

import modele.Jeu;

public abstract class Controleur {
    protected Jeu jeu;

    public void setJeu(Jeu jeu) {
        this.jeu = jeu;
    }

    public Jeu getJeu() {
        return this.jeu;
    }
}
