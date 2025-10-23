package modele;

import java.util.Scanner;

/**
 * Classe représentant une partie de jeu.
 */
public class Jeu {

    private static Jeu instance;
    Sauvegarde sauvegarde = new Sauvegarde();
    private Joueur joueur;
    private Labyrinthe labyrinthe;

    private ModeJeu modeJeu;
    private Defi defiEnCours;

    /**
     * @param modeJeu     un mode de jeu
     * @param joueur      un joueur
     * @param labyrinthe  un labyrinthe
     * @param defiEnCours un défi en cours
     */
    public Jeu(ModeJeu modeJeu, Joueur joueur, Labyrinthe labyrinthe, Defi defiEnCours) {
        this.modeJeu = modeJeu;
        this.joueur = joueur;
        this.labyrinthe = labyrinthe;
        this.defiEnCours = defiEnCours;
    }

    public Jeu() {
        
    }


    public static Jeu getInstance() {
        if (instance == null) {
            instance = new Jeu();
        }
        return instance;
    }

    public static void setInstance(Jeu newInstance) {
        instance = newInstance;
    }

    public ModeJeu getModeJeu() {
        return modeJeu;
    }

    public void setModeJeu(ModeJeu modeJeu) {
        this.modeJeu = modeJeu;
    }

    public Sauvegarde getSauvegarde() {
        return sauvegarde;
    }

    public Joueur getJoueur() {
        return joueur;
    }

    public void setJoueur(Joueur joueur) {
        this.joueur = joueur;
    }

    public void setJoueur(String pseudo) {
        sauvegarde.chargerJoueurs();
        setJoueur(sauvegarde.getJoueurParPseudo(pseudo));
    }

    public Labyrinthe getLabyrinthe() {
        return labyrinthe;
    }

    public void setLabyrinthe(Labyrinthe labyrinthe) {
        this.labyrinthe = labyrinthe;
    }

    public void setLabyrinthe(int largeur, int hauteur, double pourcentageMurs) {
        setLabyrinthe(new Labyrinthe(largeur, hauteur, pourcentageMurs));
    }

    public void setLabyrinthe(Defi defi) {
        setLabyrinthe(new Labyrinthe(defi));
    }

    public Defi getDefiEnCours() {
        return defiEnCours;
    }

    public void setDefiEnCours(Defi defiEnCours) {
        this.defiEnCours = defiEnCours;
    }

    /**
     * Initialise une nouvelle partie.
     *
     * @param largeur         largeur du labyrinthe
     * @param hauteur         hauteur du labyrinthe
     * @param pourcentageMurs pourcentage de murs dans le labyrinthe
     * @throws PseudoException si le pseudo du joueur est invalide
     */
    public void initialiser(int largeur, int hauteur, double pourcentageMurs) throws PseudoException {
        Scanner scanner = new Scanner(System.in);
        this.labyrinthe = new Labyrinthe(largeur, hauteur, pourcentageMurs);
        this.labyrinthe.generer();

        System.out.print("Entrez le pseudo du joueur : ");
        this.joueur = new Joueur(scanner.nextLine());

        this.labyrinthe.setJoueurX(0);
        this.labyrinthe.setJoueurY(1);
        this.labyrinthe.setJeuEnCours(true);
    }

    /**
     * Déplace le joueur dans le labyrinthe.
     *
     * @param dx déplacement gauche, droite
     * @param dy déplacement haut, bas
     * @return true si le déplacement est réussi, false sinon
     */
    public boolean deplacerJoueur(int dx, int dy) {
        if (this.labyrinthe == null || !this.labyrinthe.isJeuEnCours()) {
            return false;
        }

        int nouveauX = this.labyrinthe.getJoueurX() + dx;
        int nouveauY = this.labyrinthe.getJoueurY() + dy;

        if (this.labyrinthe.peutDeplacer(nouveauX, nouveauY)) {
            this.labyrinthe.setJoueurX(nouveauX);
            this.labyrinthe.setJoueurY(nouveauY);

            if (this.labyrinthe.estSurSortie(nouveauX, nouveauY)) {
                this.labyrinthe.setJeuEnCours(false);
            }

            return true;
        }

        return false;
    }

    /**
     * Vérifie si le joueur a atteint la sortie du labyrinthe.
     *
     * @return true si le joueur a gagné, false sinon
     */
    public boolean estVictoire() {
        if (this.labyrinthe == null) {
            return false;
        }

        if (!this.labyrinthe.isJeuEnCours()) {
            int joueurX = this.labyrinthe.getJoueurX();
            int joueurY = this.labyrinthe.getJoueurY();
            return this.labyrinthe.estSurSortie(joueurX, joueurY);
        }

        return false;
    }

    /**
     * Termine la partie en cours.
     *
     * @param victoire true si le joueur a gagné, false sinon
     * @return message de fin de partie
     */
    public String terminerPartie(boolean victoire) {
        this.labyrinthe.setJeuEnCours(false);

        StringBuilder resultat = new StringBuilder();
        if (victoire && this.joueur != null && this.defiEnCours != null) {
            this.joueur.ajouterScore(this.defiEnCours);
            this.sauvegarde.sauvegardeJoueurs();
            resultat.append("Vous avez trouvé la sortie et terminé le labyrinthe !\n");
            resultat.append("Score : ").append(this.joueur.getScore());

        } else {
            resultat.append("Partie terminée.\n");
        }

        return resultat.toString();
    }
}
