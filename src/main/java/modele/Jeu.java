package modele;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

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


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Configuration du labyrinthe :");
        System.out.print("Largeur (min 5, max 30) : ");
        int largeur = getIntegerInput(scanner, 5, 30);

        System.out.print("Hauteur (min 5, max 30) : ");
        int hauteur = getIntegerInput(scanner, 5, 30);

        System.out.print("Pourcentage de murs (min 10, max 50) : ");
        int pourcentage = getIntegerInput(scanner, 10, 50);

        Labyrinthe lab = new Labyrinthe(largeur, hauteur, pourcentage);

        Joueur joueur;
        try {
            System.out.print("Entrez le pseudo du joueur : ");
            joueur = new Joueur(scanner.nextLine());
        } catch (PseudoException e) {
            throw new RuntimeException(e);
        }

        Jeu jeu = new Jeu(lab, joueur);
        jeu.jouer();
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

    public void jouer() {
        labyrinthe.generer();
        System.out.println("Génération d'un labyrinthe " + labyrinthe.getLargeurMax() + "x" + labyrinthe.getHauteurMax() + " avec " + labyrinthe.getPourcentageMurs() + "% de murs...");

        while (labyrinthe.isJeuEnCours()) {
            System.out.flush();

            labyrinthe.afficherAvecJoueur();

            try {
                char touche = labyrinthe.lireTouche();
                switch (touche) {
                    case 'q': // Gauche
                        if (labyrinthe.peutDeplacer(labyrinthe.getJoueurX() - 1, labyrinthe.getJoueurY())) {
                            labyrinthe.setJoueurX(labyrinthe.getJoueurX() - 1);
                        } else System.out.println("Impossible");
                        break;

                    case 'd': // Droite
                        if (labyrinthe.peutDeplacer(labyrinthe.getJoueurX() + 1, labyrinthe.getJoueurY())) {
                            labyrinthe.setJoueurX(labyrinthe.getJoueurX() + 1);
                        } else System.out.println("Impossible");
                        break;

                    case 'z': // Haut
                        if (labyrinthe.peutDeplacer(labyrinthe.getJoueurX(), labyrinthe.getJoueurY() - 1)) {
                            labyrinthe.setJoueurY(labyrinthe.getJoueurY() - 1);
                        } else System.out.println("Impossible");
                        break;

                    case 's': // Bas
                        if (labyrinthe.peutDeplacer(labyrinthe.getJoueurX(), labyrinthe.getJoueurY() + 1)) {
                            labyrinthe.setJoueurY(labyrinthe.getJoueurY() + 1);
                        } else System.out.println("Impossible");
                        break;
                    case 'x': // Quitter
                        labyrinthe.setJeuEnCours(false);
                        break;
                }

                if (labyrinthe.estSurSortie(labyrinthe.getJoueurX(), labyrinthe.getJoueurY())) {
                    System.out.println("\nBravo ! Vous avez atteint la sortie !");
                    labyrinthe.setJeuEnCours(false);
                    Sauvegarde sauvegarde = new Sauvegarde();
                    sauvegarde.chargerJoueurs();
                    sauvegarde.addJoueur(joueur);
                    sauvegarde.sauvegardeJoueurs();
                }

            } catch (IOException e) {
                e.printStackTrace();
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
}
