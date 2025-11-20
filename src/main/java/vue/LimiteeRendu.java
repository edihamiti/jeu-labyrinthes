package vue;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import modele.Cellules.Cellule;
import modele.Labyrinthe;

public class LimiteeRendu implements Rendu {

    private final Image imgMur = new Image(getClass().getResourceAsStream("/textures/default/texture_mur.png"));
    private final Image imgChemin = new Image(getClass().getResourceAsStream("/textures/default/texture_chemin.png"));
    private final Image imgSortie = new Image(getClass().getResourceAsStream("/textures/default/texture_sortie.png"));
    private final Image imgJoueur = new Image(getClass().getResourceAsStream("/textures/default/texture_joueur.png"));
    private final Image imgRedWall = new Image(getClass().getResourceAsStream("/textures/default/texture_mur_blocked.png"));
    private final Image imgBrouillard = new Image(getClass().getResourceAsStream("/img/brouillard.png"));
    private Labyrinthe labyrinthe;
    private VBox conteneurLabyrinthe;
    private int porteeVision;
    private int lastBlockedX = -1;
    private int lastBlockedY = -1;

    /**
     * Constructeur de la classe LimiteeRendu.
     *
     * @param labyrinthe          Le labyrinthe à rendre.
     * @param conteneurLabyrinthe Le conteneur VBox pour afficher le labyrinthe.
     * @param porteeVision        La portée de vision du joueur.
     */
    public LimiteeRendu(Labyrinthe labyrinthe, VBox conteneurLabyrinthe, int porteeVision) {
        this.labyrinthe = labyrinthe;
        this.conteneurLabyrinthe = conteneurLabyrinthe;
        this.porteeVision = porteeVision;
    }

    /**
     * Constructeur avec portée de vision par défaut (2).
     *
     * @param labyrinthe          Le labyrinthe à rendre.
     * @param conteneurLabyrinthe Le conteneur VBox pour afficher le labyrinthe.
     */
    public LimiteeRendu(Labyrinthe labyrinthe, VBox conteneurLabyrinthe) {
        this(labyrinthe, conteneurLabyrinthe, 2);
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

        for (int i = 0; i < largeurMax; i++) {
            for (int j = 0; j < hauteurMax; j++) {
                double x = j * tailleCellule;
                double y = i * tailleCellule;

                int distanceX = Math.abs(i - joueurX);
                int distanceY = Math.abs(j - joueurY);
                boolean dansPorteeVision = (distanceX <= porteeVueLocale && distanceY <= porteeVueLocale);
                if (i == joueurX && j == joueurY) {
                    gc.drawImage(imgJoueur, x, y, tailleCellule, tailleCellule);
                } else if (dansPorteeVision) {
                    if (cellules[i][j].estMur()) {
                        if (i == lastBlockedX && j == lastBlockedY) {
                            gc.drawImage(imgRedWall, x, y, tailleCellule, tailleCellule);
                        } else {
                            gc.drawImage(imgMur, x, y, tailleCellule, tailleCellule);
                        }
                    } else if (cellules[i][j].estChemin() || cellules[i][j].estEntree()) {
                        gc.drawImage(imgChemin, x, y, tailleCellule, tailleCellule);
                    } else if (cellules[i][j].estSortie()) {
                        gc.drawImage(imgSortie, x, y, tailleCellule, tailleCellule);
                    }
                } else {
                    gc.drawImage(imgBrouillard, x, y, tailleCellule, tailleCellule);
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
