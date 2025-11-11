package vue;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import modele.Cellules.Cellule;
import modele.Labyrinthe;

public class LimiteeRendu implements Rendu {

    private final Image imgMur = new Image(getClass().getResourceAsStream("/img/mur.png"));
    private final Image imgChemin = new Image(getClass().getResourceAsStream("/img/chemin.png"));
    private final Image imgSortie = new Image(getClass().getResourceAsStream("/img/sortie.png"));
    private final Image imgJoueur = new Image(getClass().getResourceAsStream("/img/joueur.png"));
    private final Image imgRedWall = new Image(getClass().getResourceAsStream("/img/redWall.png"));
    private Labyrinthe labyrinthe;
    private VBox contientLabyrinthe;
    private int lastBlockedX = -1;
    private int lastBlockedY = -1;

    /**
     * Constructeur de la classe LabyrintheRendu.
     *
     * @param labyrinthe        Le labyrinthe à rendre.
     * @param contienLabyrinthe Le conteneur VBox pour afficher le labyrinthe.
     */
    public LimiteeRendu(Labyrinthe labyrinthe, VBox contienLabyrinthe) {
        this.labyrinthe = labyrinthe;
        this.contientLabyrinthe = contienLabyrinthe;
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
        contientLabyrinthe.getChildren().clear();
        contientLabyrinthe.getChildren().add(creerCanvasLimited(this.labyrinthe.getCellules()));
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
        int porteeVueLocale = 2;
        Cellule[][] cellules = this.labyrinthe.getCellules();
        int largeurMax = this.labyrinthe.getLargeurMax();
        int hauteurMax = this.labyrinthe.getHauteurMax();
        double heightVBox = contientLabyrinthe.getHeight();
        double widthVBox = contientLabyrinthe.getWidth();

        if (heightVBox == 0 && contientLabyrinthe.getScene() != null) {
            heightVBox = contientLabyrinthe.getScene().getHeight() - 200;
            widthVBox = contientLabyrinthe.getScene().getWidth() * 0.5;
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
                    gc.setFill(javafx.scene.paint.Color.DARKGRAY);
                    gc.fillRect(x, y, tailleCellule, tailleCellule);

                    gc.setStroke(javafx.scene.paint.Color.BLACK);
                    gc.strokeRect(x, y, tailleCellule, tailleCellule);
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

    public void setContientLabyrinthe(VBox contientLabyrinthe) {
        this.contientLabyrinthe = contientLabyrinthe;
    }
}
