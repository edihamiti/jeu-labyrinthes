package modele;

import boutique.GestionnaireBoutique;
import modele.defi.Defi;
import modele.joueursRepositories.JSONRepository;
import modele.joueursRepositories.JoueurRepository;

import java.time.Duration;
import java.util.Scanner;

/**
 * Classe représentant une partie de jeu.
 */
public class Jeu {

    JoueurRepository sauvegarde = new JSONRepository();
    private Joueur joueur;
    private Labyrinthe labyrinthe;

    private ModeJeu modeJeu;
    private Vision vision = Vision.VUE_LIBRE;
    private Defi defiEnCours;
    private int nombreDeplacements;
    private GameTimer gameTimer;
    private GestionnaireBoutique boutique;

    /**
     * @param modeJeu     un mode de jeu
     * @param joueur      un joueur
     * @param labyrinthe  un labyrinthe
     * @param defiEnCours un défi en cours
     * @param boutique    un gestionnaire de boutique
     */
    public Jeu(ModeJeu modeJeu, Joueur joueur, Labyrinthe labyrinthe, Defi defiEnCours, GestionnaireBoutique boutique) {
        this.modeJeu = modeJeu;
        this.joueur = joueur;
        this.labyrinthe = labyrinthe;
        this.defiEnCours = defiEnCours;
        this.gameTimer = new GameTimer();
    }

    public Jeu() {
        this.boutique = new GestionnaireBoutique();
    }

    public Vision getVision() {
        return vision;
    }

    public void setVision(Vision vision) {
        this.vision = vision;
    }

    public ModeJeu getModeJeu() {
        return modeJeu;
    }

    public void setModeJeu(ModeJeu modeJeu) {
        this.modeJeu = modeJeu;
    }

    public JoueurRepository getSauvegarde() {
        return sauvegarde;
    }

    public Joueur getJoueur() {
        return joueur;
    }

    public void setJoueur(Joueur joueur) {
        this.joueur = joueur;
    }

    public void setJoueur(String pseudo) throws PseudoException {
        sauvegarde.chargerJoueurs();
        setJoueur(sauvegarde.getJoueurParPseudo(pseudo));
    }

    public Labyrinthe getLabyrinthe() {
        return labyrinthe;
    }

    public void setLabyrinthe(Labyrinthe labyrinthe) {
        this.labyrinthe = labyrinthe;
        resetTimer();
    }

    public void setLabyrinthe(Defi defi) {
        setLabyrinthe(new Labyrinthe(defi));
    }

    public void setLabyrinthe(int largeur, int hauteur, double pourcentageMurs) {
        setLabyrinthe(new Labyrinthe(largeur, hauteur, pourcentageMurs));
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
     * @param distanceMin     distance minimale entre le joueur et la sortie du labyrinthe
     * @param typeLab         type de labyrinthe
     * @throws PseudoException si le pseudo du joueur est invalide
     */
    public void initialiser(int largeur, int hauteur, double pourcentageMurs, int distanceMin, TypeLabyrinthe typeLab) throws PseudoException {
        Scanner scanner = new Scanner(System.in);
        this.labyrinthe = new Labyrinthe(largeur, hauteur, pourcentageMurs);

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

        gameTimer.startTimer(); // TODO: REVÉRIFIER TOUT ÇA JE SUIS PAS SÛR DE CE QUE J'AI FAIT

        int nouveauX = this.labyrinthe.getJoueurX() + dx;
        int nouveauY = this.labyrinthe.getJoueurY() + dy;

        if (this.labyrinthe.peutDeplacer(nouveauX, nouveauY)) {
            this.labyrinthe.setJoueurX(nouveauX);
            this.labyrinthe.setJoueurY(nouveauY);
            nombreDeplacements++;

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

        if (victoire) {
            resultat.append("Vous avez trouvé la sortie et terminé le labyrinthe !\n");
        } else {
            resultat.append("Partie terminée.\n");
        }

        this.endTimer();
        Duration duration = gameTimer.getDuration();
        long minutes = duration.toMinutes();
        long seconds = duration.toSecondsPart();
        resultat.append("Temps écoulé : ").append(minutes).append(" min ").append(seconds).append(" sec\n");

        resultat.append("Nombre de déplacements : ").append(nombreDeplacements).append("\n");

        if (victoire && this.joueur != null && this.defiEnCours != null) {
            int scoreObtenu = CalculateurScore.calculerScore(defiEnCours, nombreDeplacements);
            this.joueur.ajouterScore(scoreObtenu, defiEnCours);
            this.sauvegarde.sauvegarder();
            resultat.append("Points gagnés : ").append(scoreObtenu);
        }

        System.out.println(resultat);
        return resultat.toString();
    }

    public void endTimer() {
        this.gameTimer.endTimer();
    }

    public void startTimer() {
        this.gameTimer.startTimer();
    }

    public Duration getDuration() {
        return this.gameTimer.getDuration();
    }

    public boolean isRunning() {
        return this.gameTimer.isRunning();
    }

    public int getNombreDeplacements() {
        return nombreDeplacements;
    }

    public void setNombreDeplacements(int nombreDeplacements) {
        this.nombreDeplacements = nombreDeplacements;
    }

    /**
     * Remet à zéro le timer pour une nouvelle partie
     */
    public void resetTimer() {
        this.gameTimer = new GameTimer();
        this.nombreDeplacements = 0;
    }

    public GestionnaireBoutique getBoutique() {
        return boutique;
    }

    public void setBoutique(GestionnaireBoutique boutique) {
        this.boutique = boutique;
    }
}
