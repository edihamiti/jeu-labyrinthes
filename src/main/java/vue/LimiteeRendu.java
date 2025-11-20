package vue;

import boutique.modele.TypeCosmetique;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import modele.Cellules.Cellule;
import modele.Cellules.Chemin;
import modele.Cellules.Mur;
import modele.Cellules.Sortie;
import modele.Jeu;
import modele.Labyrinthe;

public class LimiteeRendu implements Rendu {

    private final Image imgBrouillard = new Image(getClass().getResourceAsStream("/img/brouillard.png"));
    private Labyrinthe labyrinthe;
    private VBox conteneurLabyrinthe;
    private int porteeVision;
    private int lastBlockedX = -1;
    private int lastBlockedY = -1;
    private Jeu jeu;

    private Mur mur = new Mur();
    private Chemin chemin = new Chemin();
    private Sortie sortie = new Sortie();
    private Image imageJoueur;

    /**
     * Constructeur de la classe LimiteeRendu.
     *
     * @param labyrinthe          Le labyrinthe à rendre.
     * @param conteneurLabyrinthe Le conteneur VBox pour afficher le labyrinthe.
     * @param porteeVision        La portée de vision du joueur.
     * @param jeu                 Le jeu contenant les informations sur les textures.
     */
    public LimiteeRendu(Labyrinthe labyrinthe, VBox conteneurLabyrinthe, int porteeVision, Jeu jeu) {
        this.labyrinthe = labyrinthe;
        this.conteneurLabyrinthe = conteneurLabyrinthe;
        this.porteeVision = porteeVision;
        this.jeu = jeu;
        initTextureEquipe();
    }

    /**
     * Constructeur avec portée de vision par défaut (2).
     *
     * @param labyrinthe          Le labyrinthe à rendre.
     * @param conteneurLabyrinthe Le conteneur VBox pour afficher le labyrinthe.
     * @param jeu                 Le jeu contenant les informations sur les textures.
     */
    public LimiteeRendu(Labyrinthe labyrinthe, VBox conteneurLabyrinthe, Jeu jeu) {
        this(labyrinthe, conteneurLabyrinthe, 2, jeu);
    }

    /**
     * Initialise les textures équipées par le joueur.
     */
    private void initTextureEquipe() {
        mur.setImagePath(jeu.getBoutique().obtenirTextureEquipee(jeu.getJoueur().getPseudo(), TypeCosmetique.TEXTURE_MUR));
        chemin.setImagePath(jeu.getBoutique().obtenirTextureEquipee(jeu.getJoueur().getPseudo(), TypeCosmetique.TEXTURE_CHEMIN));
        sortie.setImagePath(jeu.getBoutique().obtenirTextureEquipee(jeu.getJoueur().getPseudo(), TypeCosmetique.TEXTURE_SORTIE));
        imageJoueur = new Image(getClass().getResourceAsStream(jeu.getBoutique().obtenirTextureEquipee(jeu.getJoueur().getPseudo(), TypeCosmetique.TEXTURE_JOUEUR)));
    }

    /**
     * Rend le labyrinthe sous forme de Canvas.
     *
     * @param labyrinthe Le labyrinthe à rendre.
     * @return Le Canvas représentant le labyrinthe.
     */
    public Canvas rendu(Labyrinthe labyrinthe) {
        this.labyrinthe = labyrinthe;
        return creerCanvasLimited(labyrinthe.getCellules());
    }

    /**
     * Définit le mur bloqué à une position spécifique.
     *
     * @param x La coordonnée X de la cellule du mur bloqué.
     * @param y La coordonnée Y de la cellule du mur bloqué.
     */
    public void setBlockedWall(int x, int y) {
        this.lastBlockedX = x;
        this.lastBlockedY = y;
        afficherLabyrinthe();
    }

    /**
     * Affiche le labyrinthe dans le conteneur VBox.
     */
    public void afficherLabyrinthe() {
        conteneurLabyrinthe.getChildren().clear();
        conteneurLabyrinthe.getChildren().add(creerCanvasLimited(this.labyrinthe.getCellules()));
    }

    /**
     * Crée un Canvas représentant le labyrinthe.
     *
     * @param labyrinthe Le labyrinthe sous forme de matrice de cellules.
     * @return Le Canvas représentant le labyrinthe.
     */
    private Canvas creerCanvasLimited(Cellule[][] labyrinthe) {
        int joueurX = this.labyrinthe.getJoueurX();
        int joueurY = this.labyrinthe.getJoueurY();

        int porteeVueLocale = this.porteeVision;

        Cellule[][] cellules = this.labyrinthe.getCellules();
        int largeurMax = this.labyrinthe.getLargeurMax();
        int hauteurMax = this.labyrinthe.getHauteurMax();

        double heightVBox = conteneurLabyrinthe.getHeight();
        double widthVBox = conteneurLabyrinthe.getWidth();

        if (heightVBox == 0 && conteneurLabyrinthe.getScene() != null) {
            heightVBox = conteneurLabyrinthe.getScene().getHeight() - 200;
            widthVBox = conteneurLabyrinthe.getScene().getWidth() * 0.5;
        }

        if (heightVBox == 0) {
            heightVBox = 600;
            widthVBox = 600;
        }

        int tailleCelluleH = (int) (heightVBox / largeurMax);
        int tailleCelluleW = (int) (widthVBox / hauteurMax);
        int tailleCellule = Math.min(tailleCelluleH, tailleCelluleW);

        if (tailleCellule < 10) {
            tailleCellule = 10;
        }

        Canvas canvas = new Canvas(hauteurMax * tailleCellule, largeurMax * tailleCellule);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        int overlap = Math.max(2, tailleCellule / 4);
        double halfOverlap = overlap / 2.0;

        for (int i = 0; i < largeurMax; i++) {
            for (int j = 0; j < hauteurMax; j++) {
                double x = j * tailleCellule;
                double y = i * tailleCellule;

                int distanceX = Math.abs(i - joueurX);
                int distanceY = Math.abs(j - joueurY);
                boolean dansPorteeVision = (distanceX <= porteeVueLocale && distanceY <= porteeVueLocale);

                if (dansPorteeVision) {
                    if (cellules[i][j].estChemin() || cellules[i][j].estEntree()) {
                        gc.drawImage(chemin.getTexture(), x, y, tailleCellule, tailleCellule);
                    } else if (cellules[i][j].estSortie()) {
                        gc.drawImage(sortie.getTexture(), x, y, tailleCellule, tailleCellule);
                    } else if (cellules[i][j].estMur()) {
                        gc.drawImage(mur.getTexture(), x, y, tailleCellule, tailleCellule);
                    } else {
                        gc.clearRect(x, y, tailleCellule, tailleCellule);
                    }
                } else {
                    gc.drawImage(imgBrouillard, x, y, tailleCellule, tailleCellule);
                }
            }
        }

        if (joueurX >= 0 && joueurY >= 0) {
            double x = joueurY * tailleCellule;
            double y = joueurX * tailleCellule;
            gc.drawImage(imageJoueur, x, y, tailleCellule, tailleCellule);
        }

        for (int i = 0; i < largeurMax; i++) {
            for (int j = 0; j < hauteurMax; j++) {
                int distanceX = Math.abs(i - joueurX);
                int distanceY = Math.abs(j - joueurY);
                boolean dansPorteeVision = (distanceX <= porteeVueLocale && distanceY <= porteeVueLocale);

                if (dansPorteeVision && cellules[i][j].estMur()) {
                    double x = j * tailleCellule - halfOverlap;
                    double y = i * tailleCellule - halfOverlap;
                    double w = tailleCellule + overlap;
                    double h = tailleCellule + overlap;

                    if (i == lastBlockedX && j == lastBlockedY) {
                        gc.drawImage(mur.getTextureBlocked(), x, y, w, h);
                    } else {
                        gc.drawImage(mur.getTexture(), x, y, w, h);
                    }
                }
            }
        }

        lastBlockedX = -1;
        lastBlockedY = -1;

        return canvas;
    }

    public void setLabyrinthe(Labyrinthe labyrinthe) {
        this.labyrinthe = labyrinthe;
    }

    public void setConteneurLabyrinthe(VBox conteneurLabyrinthe) {
        this.conteneurLabyrinthe = conteneurLabyrinthe;
    }
}
