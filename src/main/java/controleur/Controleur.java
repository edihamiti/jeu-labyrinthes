package controleur;

import modele.Jeu;

public abstract class Controleur {
    protected Jeu jeu;
    protected AppControleur appControleur;

    public void setJeu(Jeu jeu) {
        this.jeu = jeu;
    }

    public Jeu getJeu() {
        return this.jeu;
    }

    public void setAppControleur(AppControleur appControleur) {
        this.appControleur = appControleur;
    }

    public AppControleur getAppControleur() {
        return this.appControleur;
    }
}
