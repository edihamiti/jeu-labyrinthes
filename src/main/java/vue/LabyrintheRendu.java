package vue;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.VBox;
import modele.Cellules.Cellule;
import modele.Labyrinthe;

/**
 * Classe responsable du rendu graphique du labyrinthe.
 */
public class LabyrintheRendu implements Rendu {
    private Labyrinthe labyrinthe;
    private VBox contienLabyrinthe;
    private int lastBlockedX = -1;
    private int lastBlockedY = -1;

    /**
     * Constructeur de la classe LabyrintheRendu.
     *
     * @param labyrinthe        Le labyrinthe à rendre.
     * @param contienLabyrinthe Le conteneur VBox pour afficher le labyrinthe.
     */
    public LabyrintheRendu(Labyrinthe labyrinthe, VBox contienLabyrinthe) {
        this.labyrinthe = labyrinthe;
        this.contienLabyrinthe = contienLabyrinthe;
    }

    /**
     * Rend le labyrinthe sous forme de Canvas.
     *
     * @param labyrinthe Le labyrinthe à rendre.
     * @return Le Canvas représentant le labyrinthe.
     */
    public Canvas rendu(Labyrinthe labyrinthe) {
        this.labyrinthe = labyrinthe;
        return creerCanvasLabyrinthe(labyrinthe.getCellules());
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
        contienLabyrinthe.getChildren().clear();
        contienLabyrinthe.getChildren().add(creerCanvasLabyrinthe(this.labyrinthe.getCellules()));
    }

    /**
     * Crée un Canvas représentant le labyrinthe.
     *
     * @param labyrinthe Le labyrinthe sous forme de matrice de cellules.
     * @return Le Canvas représentant le labyrinthe.
     */
    private Canvas creerCanvasLabyrinthe(Cellule[][] labyrinthe) {
        double heightVBox = contienLabyrinthe.getHeight();
        double widthVBox = contienLabyrinthe.getWidth();

        if (heightVBox == 0 && contienLabyrinthe.getScene() != null) {
            heightVBox = contienLabyrinthe.getScene().getHeight() - 200;
            widthVBox = contienLabyrinthe.getScene().getWidth() * 0.5;
        }

        if (heightVBox == 0) {
            heightVBox = 600;
            widthVBox = 600;
        }

        int largeurMax = this.labyrinthe.getLargeurMax();
        int hauteurMax = this.labyrinthe.getHauteurMax();

        int tailleCelluleH = (int) (heightVBox / largeurMax);
        int tailleCelluleW = (int) (widthVBox / hauteurMax);
        int tailleCellule = Math.min(tailleCelluleH, tailleCelluleW);

        if (tailleCellule < 10) {
            tailleCellule = 10;
        }

        Canvas canvas = new Canvas(hauteurMax * tailleCellule, largeurMax * tailleCellule);
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();

        int overlap = Math.max(2, tailleCellule / 4);
        double halfOverlap = overlap / 2.0;

        for (int i = 0; i < largeurMax; i++) {
            for (int j = 0; j < hauteurMax; j++) {
                double x = j * tailleCellule;
                double y = i * tailleCellule;

                if (labyrinthe[i][j].estChemin() || labyrinthe[i][j].estEntree()) {
                    graphicsContext.drawImage(imgChemin, x, y, tailleCellule, tailleCellule);
                } else if (labyrinthe[i][j].estSortie()) {
                    graphicsContext.drawImage(imgSortie, x, y, tailleCellule, tailleCellule);
                } else if (labyrinthe[i][j].estMur()) {
                    graphicsContext.drawImage(imgChemin, x, y, tailleCellule, tailleCellule);
                } else {
                    graphicsContext.clearRect(x, y, tailleCellule, tailleCellule);
                }
            }
        }

        for (int i = 0; i < largeurMax; i++) {
            for (int j = 0; j < hauteurMax; j++) {
                if (labyrinthe[i][j].estMur()) {
                    double x = j * tailleCellule - halfOverlap;
                    double y = i * tailleCellule - halfOverlap;
                    double w = tailleCellule + overlap;
                    double h = tailleCellule + overlap;

                    if (i == lastBlockedX && j == lastBlockedY) {
                        graphicsContext.drawImage(imgRedWall, x, y, w, h);
                    } else {
                        graphicsContext.drawImage(imgMur, x, y, w, h);
                    }
                }
            }
        }

        int px = this.labyrinthe.getJoueurX();
        int py = this.labyrinthe.getJoueurY();
        if (px >= 0 && py >= 0) {
            double x = py * tailleCellule;
            double y = px * tailleCellule;
            graphicsContext.drawImage(imgJoueur, x, y, tailleCellule, tailleCellule);
        }

        lastBlockedX = -1;
        lastBlockedY = -1;

        return canvas;
    }

    public void setLabyrinthe(Labyrinthe labyrinthe) {
        this.labyrinthe = labyrinthe;
    }

    public void setContienLabyrinthe(VBox contienLabyrinthe) {
        this.contienLabyrinthe = contienLabyrinthe;
    }
}
