package modele;

public class Jeu {
    private ModeJeu modeJeu;
    private  Joueur joueur;
    private Labyrinthe labyrinthe;

    public Jeu(ModeJeu modeJeu, Joueur joueur, Labyrinthe labyrinthe) {
        this.modeJeu = modeJeu;
        this.joueur = joueur;
        this.labyrinthe = labyrinthe;
    }

    public Jeu(Labyrinthe labyrinthe, Joueur joueur) {
        this.joueur = joueur;
        this.labyrinthe = labyrinthe;
    }

    /**
     * Initialise une nouvelle partie en générant le labyrinthe
     */
    public void initialiser() {
        labyrinthe.generer();
    }

    /**
     * Tente de déplacer le joueur vers une nouvelle position
     * @param nouveauX nouvelle position X
     * @param nouveauY nouvelle position Y
     * @return true si le déplacement est effectué, false sinon
     */
    public boolean deplacerJoueur(int nouveauX, int nouveauY) {
        if (labyrinthe.peutDeplacer(nouveauX, nouveauY)) {
            labyrinthe.setJoueurX(nouveauX);
            labyrinthe.setJoueurY(nouveauY);
            return true;
        }
        return false;
    }

    /**
     * Vérifie si le joueur a atteint la sortie
     * @return true si le joueur est sur la sortie
     */
    public boolean estVictoire() {
        return labyrinthe.estSurSortie(labyrinthe.getJoueurX(), labyrinthe.getJoueurY());
    }

    /**
     * Termine la partie
     */
    public void terminerPartie() {
        labyrinthe.setJeuEnCours(false);
    }

    public ModeJeu getModeJeu() {
        return modeJeu;
    }

    public void setModeJeu(ModeJeu modeJeu) {
        this.modeJeu = modeJeu;
    }

    public Joueur getJoueur() {
        return joueur;
    }

    public void setJoueur(Joueur joueur) {
        this.joueur = joueur;
    }

    public Labyrinthe getLabyrinthe() {
        return labyrinthe;
    }

    public void setLabyrinthe(Labyrinthe labyrinthe) {
        this.labyrinthe = labyrinthe;
    }
}
