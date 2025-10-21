package modele;

import java.util.Scanner;

public class Jeu {
    private ModeJeu modeJeu;
    private Joueur joueur;
    private Labyrinthe labyrinthe;
    private Defi defiEnCours;

    public Jeu(ModeJeu modeJeu, Joueur joueur, Labyrinthe labyrinthe, Defi defiEnCours) {
        this.modeJeu = modeJeu;
        this.joueur = joueur;
        this.labyrinthe = labyrinthe;
        this.defiEnCours = defiEnCours;
    }

    public Jeu(Labyrinthe labyrinthe, Joueur joueur) {
        this.joueur = joueur;
        this.labyrinthe = labyrinthe;
    }

    // constructeur vide
    public Jeu() {
    }

    private static int getIntegerInput(Scanner scanner, int min, int max) {
        int value;
        while (true) {
            try {
                value = Integer.parseInt(scanner.nextLine());
                if (value >= min && value <= max) {
                    return value;
                }
                System.out.print("Veuillez entrer une valeur entre " + min + " et " + max + " : ");
            } catch (NumberFormatException e) {
                System.out.print("Veuillez entrer un nombre valide : ");
            }
        }
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

    public Defi getDefiEnCours() {
        return defiEnCours;
    }

    public void setDefiEnCours(Defi defiEnCours) {
        this.defiEnCours = defiEnCours;
    }

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

    public String terminerPartie(boolean victoire) {

        this.labyrinthe.setJeuEnCours(false);

        int distanceOptimale = this.labyrinthe.calculePlusCourtChemin();

        StringBuilder resultat = new StringBuilder();
        if (victoire) {
            if (this.joueur != null) {
                // Ajout score
                this.joueur.ajouterScore(this.defiEnCours);


                if (this.defiEnCours != null) {
                    this.joueur.ajouterScore(this.defiEnCours);
                }
            }

            resultat.append("Félicitations ! Vous avez terminé le labyrinthe !\n");
            resultat.append("Score : ").append(this.joueur.getScore());

        } else {
            resultat.append("Partie terminée.\n");
        }

        return resultat.toString();
    }
}
